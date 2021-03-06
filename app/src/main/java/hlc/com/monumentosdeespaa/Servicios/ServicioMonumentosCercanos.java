package hlc.com.monumentosdeespaa.Servicios;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import hlc.com.monumentosdeespaa.Activitys.MonumentosCercanos;
import hlc.com.monumentosdeespaa.Activitys.SplashActivity;
import hlc.com.monumentosdeespaa.Datos.Constantes;
import hlc.com.monumentosdeespaa.Datos.Monumentos;
import hlc.com.monumentosdeespaa.Datos.VolleySingleton;
import hlc.com.monumentosdeespaa.R;

/**
 * Created by Dani on 19/02/2018.
 */

public class ServicioMonumentosCercanos extends Service {

    private Location location;
    private final int NOTIFICATION_ID=1;
    private Timer timer = new Timer();

    //Preferencias
    private SharedPreferences pref;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(getApplicationContext());

                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    //Comprobamos que el gps esta activo
                    LocationManager manager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

                    if(manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                        client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                try{
                                    //Construccion del JSON con los datos para enviar al servidor
                                    JSONObject data = new JSONObject();
                                    try {
                                        data.put("latitud",location.getLatitude());
                                        data.put("longitud",location.getLongitude());
                                        data.put("radio", radioPreferencias());
                                    } catch (JSONException e) {
                                        //e.printStackTrace();
                                    }

                                    //Peticion al servidor
                                    VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(
                                            new JsonObjectRequest(Request.Method.POST,
                                                    Constantes.GET_MONUMENTOS_CERCANOS,
                                                    data,
                                                    new Response.Listener<JSONObject>() {
                                                        @Override
                                                        public void onResponse(JSONObject jsonObject) {
                                                            procesarRespuesta(jsonObject);
                                                        }
                                                    }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError volleyError) {
                                                    Log.e("Error", volleyError.getMessage());
                                                }
                                            })
                                    );
                                }catch(NullPointerException nfe){

                                }

                            }
                        });

                    }else{
                        Toast.makeText(getApplicationContext(),getString(R.string.errorGPS),Toast.LENGTH_LONG).show();
                    }


                }

            }
        };

        //Se repetira cada 8 horas
        timer.schedule(timerTask,28800000,  28800000);
        return START_STICKY;
    }

    /**
     * Procesar la respuesta obtenida del servidor
     * @param jsonObject json recogido del servidor
     */
    private void procesarRespuesta(JSONObject jsonObject){
        try {
            //Procesar JSON
            String estado = jsonObject.getString("estado");
            if(estado.equals("1")){

                //Creacion de la notificacion
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this,null)
                        .setAutoCancel(true)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(getString(R.string.monumentosCercanos))
                        .setSmallIcon(android.R.drawable.arrow_up_float)
                        .setVibrate(new long[]{100,300,500,300,100});

                //Recuperamos el array de monumentos
                JSONArray datosMonumentos = jsonObject.getJSONArray("monumentos_cercanos");

                Gson gson = new Gson();
                Monumentos[] monumentos =  gson.fromJson(datosMonumentos.toString(), Monumentos[].class);

                //PendingIntent
                Intent intentCercanos = new Intent(this, SplashActivity.class);
                intentCercanos.putExtra("monumentos_cercanos", monumentos);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0, intentCercanos,0);

                //Añadir pending a notificacion
                builder.setContentIntent(pendingIntent);

                //Lanzar la notificacion
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                manager.notify(NOTIFICATION_ID, builder.build());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * Método que devuelve el valor del radio introducido en las preferencias.
     *
     * @return Float con el radio elegido.
     */
    private float radioPreferencias(){

        //Leemos las preferencias guardadas.
        pref = PreferenceManager.getDefaultSharedPreferences(this);

        String radio=pref.getString("prefRadio", "");

        //Si el valor es nulo, retorna 0.
        if(radio==null || radio==""){
            return 0f;
            //Si no, retorna el valor introducido en la preferencia.
        }else
            return Float.parseFloat(radio);

    }

}

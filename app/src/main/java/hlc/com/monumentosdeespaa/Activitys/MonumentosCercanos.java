package hlc.com.monumentosdeespaa.Activitys;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
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

import java.util.ArrayList;
import java.util.Collection;

import hlc.com.monumentosdeespaa.Adaptadores.AdaptadorMonumentosCercanos;
import hlc.com.monumentosdeespaa.Datos.Constantes;
import hlc.com.monumentosdeespaa.Datos.Monumentos;
import hlc.com.monumentosdeespaa.Datos.VolleySingleton;
import hlc.com.monumentosdeespaa.R;

public class MonumentosCercanos extends AppCompatActivity {

    private ArrayList<Monumentos> listaMonumentosCercanos;
    private AdaptadorMonumentosCercanos adaptador;
    private RecyclerView recyclerView;

    //Preferencias
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monumentos_cercanos);

        //flecha de volver atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(getIntent().hasExtra("monumentos_cercanos")){

            //Recuperamos los datos devueltos por el servicio
            Object[] monumentos = (Object[]) getIntent().getParcelableArrayExtra("monumentos_cercanos");

            listaMonumentosCercanos = new ArrayList<>();

            for(Object objeto: monumentos) {
                Monumentos m = (Monumentos) objeto;
                listaMonumentosCercanos.add(m);
            }

            //adaptar el recylerView
            adaptarRecyclerView(listaMonumentosCercanos);

        }else{

            //Rellenar la lista de monumentos cercanos y adaptar el recyclerView
            rellenarListaMonumentosCercanos();

        }
    }

    private void rellenarListaMonumentosCercanos(){
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(getApplicationContext());

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            client.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {

                    //Construccion del JSON con los datos para enviar al servidor

                    JSONObject data = new JSONObject();
                    try {
                        data.put("latitud", location.getLatitude());
                        data.put("longitud", location.getLongitude());
                        data.put("radio", radioPreferencias());
                    } catch (JSONException e) {
                        e.printStackTrace();
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
                                            //adaptar el recyclerView
                                            adaptarRecyclerView(listaMonumentosCercanos);
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    Log.e("Error", volleyError.getMessage());
                                    Toast.makeText(getApplicationContext(), getString(R.string.ErrorServidor), Toast.LENGTH_LONG).show();
                                }
                            })
                    );
                }
            });
        }
    }

    /**
     * Procesar la respuesta del servidor
     * @param jsonObject json devuelto por el servidor
     */
    private void procesarRespuesta(JSONObject jsonObject){
        try {
            //Procesar JSON
            String estado = jsonObject.getString("estado");
            if(estado.equals("1")){

                //Recuperamos el array de monumentos
                JSONArray datosMonumentos = jsonObject.getJSONArray("monumentos_cercanos");

                //Recogemos los datos
                Gson gson = new Gson();
                Monumentos[] monumentos =  gson.fromJson(datosMonumentos.toString(), Monumentos[].class);

                listaMonumentosCercanos = new ArrayList<Monumentos>();

                for(Monumentos m : monumentos)
                    listaMonumentosCercanos.add(m);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adaptar el recyclerView del activity
     * @param listaMonumentosCercanos lista de monumentos cercanos
     */
    private void adaptarRecyclerView(ArrayList<Monumentos> listaMonumentosCercanos){

        adaptador = new AdaptadorMonumentosCercanos(this, listaMonumentosCercanos);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_monumentos_cercanos);

        recyclerView.setHasFixedSize(true);

        //establecemos el tipo de presentacion del recycler
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));

        //añadimos el adaptador
        recyclerView.setAdapter(adaptador);
    }

    /**
     * Pulsando en el ojo de los distintos monumentos se cierra el activity y se mueve la camara al punto donde esta el monumento
     */
    public void cerrarActivity(int pos){
        Intent intent = new Intent();
        intent.putExtra("posicion",listaMonumentosCercanos.get(pos).getLatLng());
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //opcion de la flecha de volver atras
        if(item.getItemId()==android.R.id.home){
            finish();
            return true;
        }

        return false;
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

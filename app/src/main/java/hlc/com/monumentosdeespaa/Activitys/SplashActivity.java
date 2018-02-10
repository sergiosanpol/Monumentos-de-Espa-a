package hlc.com.monumentosdeespaa.Activitys;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import hlc.com.monumentosdeespaa.Datos.Constantes;
import hlc.com.monumentosdeespaa.Datos.Monumentos;
import hlc.com.monumentosdeespaa.Datos.VolleySingleton;
import hlc.com.monumentosdeespaa.R;

public class SplashActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        cargarDatos();

    }

    private void cargarDatos(){
        VolleySingleton.getInstance(this).addToRequestQueue(
                new JsonObjectRequest(Request.Method.GET,
                        Constantes.GET_TODOS_MONUMENTOS,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                procesarRespuesta(jsonObject);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                Toast.makeText(getApplicationContext(), getString(R.string.ErrorServidor),Toast.LENGTH_LONG).show();
                                SystemClock.sleep(1500);
                                Log.d("ERROR","Error en cargar datos");

                                //cerrar la aplicacion
                                finish();
                            }
                        })
        );
    }

    private void procesarRespuesta(JSONObject jsonObject){

        try {
            String estado = jsonObject.getString("estado");
            Log.d("ESTADO",estado);
            Monumentos[] monumentos;
            switch (estado){
                case "1":
                    JSONArray datosMonumentos = jsonObject.getJSONArray("monumentos");
                    monumentos = new Monumentos[datosMonumentos.length()];

                    for(int i=0; i<monumentos.length; i++){
                        monumentos[i] = new Monumentos();
                        monumentos[i].setId_monumentos(datosMonumentos.getJSONObject(i).getInt("id_monumentos"));
                        monumentos[i].setNombre(datosMonumentos.getJSONObject(i).getString("nombre"));

                        try {
                            monumentos[i].setAnno(new SimpleDateFormat("yyyy-MM-dd")
                                    .parse(datosMonumentos.getJSONObject(i).getString("anno")));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        monumentos[i].setLocalidad(datosMonumentos.getJSONObject(i).getString("localidad"));
                        monumentos[i].setProvincia(datosMonumentos.getJSONObject(i).getString("provincia"));
                        monumentos[i].setComunidad(datosMonumentos.getJSONObject(i).getString("comunidad"));
                        monumentos[i].setLatLng(new LatLng(
                                datosMonumentos.getJSONObject(i).getDouble("latitud"),
                                datosMonumentos.getJSONObject(i).getDouble("longitud")));

                        Log.d("Monumento",monumentos[i].getNombre());
                    }

                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("monumentos",monumentos);
                    startActivity(intent);
                    finish();
                    break;
                case "2":
                    Toast.makeText(getApplicationContext(), getString(R.string.ErrorServidor),Toast.LENGTH_LONG).show();
                    SystemClock.sleep(1500);
                    finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}

package hlc.com.monumentosdeespaa.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import hlc.com.monumentosdeespaa.Datos.Constantes;
import hlc.com.monumentosdeespaa.Datos.Monumentos;
import hlc.com.monumentosdeespaa.Datos.VolleySingleton;
import hlc.com.monumentosdeespaa.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //iniciar animacion
        animacionCargando();

        //Llamamos al método para cargar datos
        cargarDatos();
    }

    /**
     * Animacion del icono de carga
     */
    private void animacionCargando(){

        ImageView imagen = (ImageView) findViewById(R.id.img_cargando);

        Animation rotar = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.rotar);

        imagen.startAnimation(rotar);
    }

    /**
     * Método que recupera los monumentos de la base de datos con JSON
     */
    private void cargarDatos(){
        //Hacemos la petición web con GET. Crea un hilo.
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
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                finish();
                            }
                        })
        );
    }

    /**
     * Método para gestionar la respuesta positiva del servidor. Transformamos el JSON en un array de monumentos
     * y lo enviamos a la siguiente actividad
     * @param jsonObject
     */
    private void procesarRespuesta(JSONObject jsonObject){

        try {
            //Se recupera el estado de la respuesta del servidor
            String estado = jsonObject.getString("estado");
            Log.d("ESTADO",estado);
            switch (estado){
                //Se han obtenido los datos
                case "1":
                    //Cogemos los datos de los monumentos
                    JSONArray datosMonumentos = jsonObject.getJSONArray("monumentos");

                    //Parseamos y transformamos el JSON a un array de la clase Monumentos
                    Gson gson = new Gson();
                    Monumentos[] monumentos =  gson.fromJson(datosMonumentos.toString(), Monumentos[].class);

                    /*
                    OTRA FORMA DE HACERLO

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
                    */

                    //Pasamos hacia la ventana del mapa y finalizamos la activity
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.putExtra("monumentos",monumentos);
                    startActivity(intent);
                    finish();
                    break;
                case "2":
                    Log.i("Ya aqui", "Joder");
                    Toast.makeText(getApplicationContext(), getString(R.string.ErrorServidor),Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}

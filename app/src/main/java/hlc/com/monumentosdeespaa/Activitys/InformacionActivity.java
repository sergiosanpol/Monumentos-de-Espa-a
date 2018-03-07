package hlc.com.monumentosdeespaa.Activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import hlc.com.monumentosdeespaa.BBDDSQLite.ConsultasSQLite;
import hlc.com.monumentosdeespaa.Datos.Constantes;
import hlc.com.monumentosdeespaa.Datos.Monumentos;
import hlc.com.monumentosdeespaa.R;

public class InformacionActivity extends AppCompatActivity {

    private Monumentos monumento;
    private TextView nombre, comunidad, provincia, localidad,calle,datado;
    private ImageView imagenMonumento;
    private Address address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion);

        //Flecha de volver atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //recogemos el monumento
        monumento = (Monumentos) getIntent().getParcelableExtra("monumento");

        nombre = (TextView) findViewById(R.id.nombre_monumento);
        calle = (TextView) findViewById(R.id.calle);
        comunidad = (TextView) findViewById(R.id.comunidad);
        provincia = (TextView) findViewById(R.id.provincia);
        localidad = (TextView) findViewById(R.id.localidad);
        datado = (TextView) findViewById(R.id.anno_construccion);
        imagenMonumento = (ImageView) findViewById(R.id.imagenMonumento);

        //informacion para buscar la calle del monumento
        Geocoder geocoder = new Geocoder(this);

        List<Address> direccion = null;

        try {
            direccion = geocoder.getFromLocationName(monumento.getNombre()+","+monumento.getLocalidad()+","+monumento.getProvincia(),1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Mostramos la informacion
        if(direccion!=null) {
            if (direccion.size() != 0) {
                address = direccion.get(0);
                calle.setText(address.getThoroughfare());
            }else{
                calle.setText(getString(R.string.noDisponible));
            }
        }

        nombre.setText(monumento.getNombre());
        comunidad.setText(monumento.getComunidad());
        provincia.setText(monumento.getProvincia());
        localidad.setText(monumento.getLocalidad());

        obtenerImagen();
    }

    /**
     * Cargamos el menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_informacion, menu);
        return true;
    }

    /**
     * Funcionalidad de los elementos del menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.ver_google_maps){
           Intent intent =  new Intent(Intent.ACTION_VIEW,
                   Uri.parse("geo:"+address.getLatitude()+","+address.getLongitude()
                           +"?q="+address.getLatitude()+","+address.getLongitude()+
                           "("+monumento.getNombre()+")"));
           startActivity(intent);
           return true;
        }else if(item.getItemId()==R.id.add_visitar_futuro){
            ConsultasSQLite.insertarVisitaFurtura(getApplicationContext(),monumento);
            return  true;
        }
        return false;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    /**
     * Método que recupera las imagenes del servidor relacionadas con un monumento
     */
    private void obtenerImagen(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //Creamos los objetos de conexión, donde recuperamos la imagen y la ruta del fichero
                HttpURLConnection urlConnection = null;
                final Bitmap bitmap;
                String path = Constantes.IMAGENES + procesarString();

                try{
                    //Abrimos la conexión
                    urlConnection = (HttpURLConnection) new URL(path).openConnection();
                    urlConnection.connect();
                    //Recuperamos la imagen devuelta mediante flujos
                    bitmap = BitmapFactory.decodeStream(urlConnection.getInputStream());
                    //Actualizamos la imagen utilizando el método que permite editar una vista desde el
                    //hilo principal de la aplicación
                    imagenMonumento.post(new Runnable() {
                        @Override
                        public void run() {
                            imagenMonumento.setImageBitmap(bitmap);
                        }
                    });
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    if(urlConnection != null){
                        urlConnection.disconnect();
                    }
                }
            }
        }).start();
    }

    /**
     * Método que devuelve un String sin espacios, sin caracteres extraños y en minúsculas
     * @return
     */
    private String procesarString(){
        String stringFinal = monumento.getNombre().replace(" ", "+").toLowerCase();
        stringFinal = stringFinal.replace("á", "a");
        stringFinal = stringFinal.replace("é", "e");
        stringFinal = stringFinal.replace("í", "i");
        stringFinal = stringFinal.replace("ó", "o");
        stringFinal = stringFinal.replace("ú", "u");
        return stringFinal;
    }

}

package hlc.com.monumentosdeespaa.Activitys;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import hlc.com.monumentosdeespaa.BBDDSQLite.BBDD_Helper;
import hlc.com.monumentosdeespaa.BBDDSQLite.ConsultasSQLite;
import hlc.com.monumentosdeespaa.Datos.Monumentos;
import hlc.com.monumentosdeespaa.R;

public class InformacionActivity extends AppCompatActivity {

    private Monumentos monumento;
    private TextView nombre, comunidad, provincia, localidad,calle,datado;
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

        //informacion para buscar la calle del monumento
        Geocoder geocoder = new Geocoder(this);

        List<Address> direccion = null;

        try {
            direccion = geocoder.getFromLocationName(monumento.getNombre()+","+monumento.getLocalidad()+","+monumento.getProvincia(),1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Mostramos la informacion
        if(direccion!=null){
            address = direccion.get(0);

            nombre.setText(monumento.getNombre());
            calle.setText(address.getThoroughfare());
            comunidad.setText(monumento.getComunidad());
            provincia.setText(monumento.getProvincia());
            localidad.setText(monumento.getLocalidad());
        }else
            Toast.makeText(this,getString(R.string.ErrorServidor),Toast.LENGTH_LONG).show();

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

}

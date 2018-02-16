package hlc.com.monumentosdeespaa.Activitys;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import hlc.com.monumentosdeespaa.Adaptadores.AdaptadorFuturasVisitas;
import hlc.com.monumentosdeespaa.BBDDSQLite.ConsultasSQLite;
import hlc.com.monumentosdeespaa.BBDDSQLite.EstructuraBBDD;
import hlc.com.monumentosdeespaa.Datos.Monumentos;
import hlc.com.monumentosdeespaa.R;

public class FuturasVisitasActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Monumentos> listaMonumentos;
    private AdaptadorFuturasVisitas adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_futuras_visitas);

        //flecha de volver atras
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Recogemos los datos de la consulta
        recogerListaFuturasVisitas();

        //adaptador del recyclerView
        adaptador = new AdaptadorFuturasVisitas(listaMonumentos);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_futuras_visitas);

        recyclerView.setHasFixedSize(true);

        //establecemos el tipo de presentacion del recycler
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));

        //animacion del recycler
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //añadimos el adaptador
        recyclerView.setAdapter(adaptador);
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

    //Rellenamos el arrayList con los datos de la
    private void recogerListaFuturasVisitas(){
        //Realizamos la consulta
        Cursor cursor = ConsultasSQLite.consultarFuturasVisitas(getApplicationContext());

        listaMonumentos = new ArrayList<>();

        //Si el curso tiene datos los insertamos en el ArrayList
        if(cursor.moveToFirst())
            do{
                Monumentos monumento = new Monumentos(
                        cursor.getInt(cursor.getColumnIndex(EstructuraBBDD.ID_MONUMENTO)),
                        cursor.getString(cursor.getColumnIndex(EstructuraBBDD.NOMBRE_MONUMENTO)),
                        null,
                        cursor.getString(cursor.getColumnIndex(EstructuraBBDD.LOCALIDAD_MONUMENTO)),
                        cursor.getString(cursor.getColumnIndex(EstructuraBBDD.PROVINCIA_MONUMENTO)),
                        cursor.getString(cursor.getColumnIndex(EstructuraBBDD.COMUNIDAD_MONUMENTO)),
                        cursor.getDouble(cursor.getColumnIndex(EstructuraBBDD.LATITUD_MONUMENTO)),
                        cursor.getDouble(cursor.getColumnIndex(EstructuraBBDD.LONGITUD_MONUMENTO))
                );

                listaMonumentos.add(monumento);
            }while (cursor.moveToNext());

    }


}

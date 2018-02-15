package hlc.com.monumentosdeespaa.Activitys;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import hlc.com.monumentosdeespaa.Adaptadores.AdaptadorFuturasVisitas;
import hlc.com.monumentosdeespaa.BBDDSQLite.ConsultasSQLite;
import hlc.com.monumentosdeespaa.BBDDSQLite.EstructuraBBDD;
import hlc.com.monumentosdeespaa.Datos.Monumentos;
import hlc.com.monumentosdeespaa.R;

public class FuturasVisitasActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Monumentos> listaMonumentos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_futuras_visitas);

        //Recogemos los datos de la consulta
        recogerListaFuturasVisitas();

        //añadimos el adaptador
        recyclerView = (RecyclerView) findViewById(R.id.recycler_futuras_visitas);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(new AdaptadorFuturasVisitas(listaMonumentos));


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

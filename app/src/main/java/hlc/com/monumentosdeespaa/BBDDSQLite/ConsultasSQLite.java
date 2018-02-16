package hlc.com.monumentosdeespaa.BBDDSQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import hlc.com.monumentosdeespaa.Datos.Monumentos;
import hlc.com.monumentosdeespaa.R;

/**
 * Created by Dani on 14/02/2018.
 */

public class ConsultasSQLite {

    /**
     * Inserta el monumento en la tabla VisitaFuturas
     */
    public static void insertarVisitaFurtura(Context context, Monumentos monumento){

        SQLiteDatabase db = new BBDD_Helper(context).getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(EstructuraBBDD.ID_MONUMENTO, monumento.getId_monumentos());
        values.put(EstructuraBBDD.NOMBRE_MONUMENTO, monumento.getNombre());
        values.put(EstructuraBBDD.COMUNIDAD_MONUMENTO, monumento.getComunidad());
        values.put(EstructuraBBDD.PROVINCIA_MONUMENTO, monumento.getProvincia());
        values.put(EstructuraBBDD.LOCALIDAD_MONUMENTO, monumento.getLocalidad());
       // values.put(EstructuraBBDD.ANNO_MONUMENTO, monumento.getAnno());
        values.put(EstructuraBBDD.LATITUD_MONUMENTO, monumento.getLatitud());
        values.put(EstructuraBBDD.LONGITUD_MONUMENTO, monumento.getLongitud());

        long num_col = db.insert(EstructuraBBDD.TABLE_VISITAS_FUTURAS,null,values);

        if(num_col>0)
            Toast.makeText(context, context.getString(R.string.insertado), Toast.LENGTH_LONG).show();
        else
            Toast.makeText(context, context.getString(R.string.yaExiste), Toast.LENGTH_LONG).show();
    }

    /**
     *
     * Consulta de todos los monumentos que se guardan en la tabla VisitasFuturas
     * @return devuelve el cursor con todos los monumentos que se han añadido a futuras visitas
     */
    public static Cursor consultarFuturasVisitas(Context context){

        SQLiteDatabase db = new BBDD_Helper(context).getReadableDatabase();

        //Array con todos los datos de la tabla monumentos equivale al select
        String[] projecction = {
                EstructuraBBDD.ID_MONUMENTO,
                EstructuraBBDD.NOMBRE_MONUMENTO,
                EstructuraBBDD.COMUNIDAD_MONUMENTO,
                EstructuraBBDD.PROVINCIA_MONUMENTO,
                EstructuraBBDD.LOCALIDAD_MONUMENTO,
                EstructuraBBDD.ANNO_MONUMENTO,
                EstructuraBBDD.LATITUD_MONUMENTO,
                EstructuraBBDD.LONGITUD_MONUMENTO
        };

        //Consulta
        Cursor c = db.query(
                EstructuraBBDD.TABLE_VISITAS_FUTURAS,
                projecction,
                null,
                null,
                null,
                null,
                null);

        return c;
    }

    /**
     * Borramos un monumento de la tabla de VisitasFuturas
     */
    public static void borrarFuturasVisitas(Context context, int id){

        SQLiteDatabase db = new BBDD_Helper(context).getWritableDatabase();

        //where del delete
        String seleccion = EstructuraBBDD.ID_MONUMENTO +" = ?";

        //argumentos de la consulta parametrizada
        String[] seleccionArgs = {String.valueOf(id)};

        db.delete(EstructuraBBDD.TABLE_VISITAS_FUTURAS,seleccion,seleccionArgs);

    }

}

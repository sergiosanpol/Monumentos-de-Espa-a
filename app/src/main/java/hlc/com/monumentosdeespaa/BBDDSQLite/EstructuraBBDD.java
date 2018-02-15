package hlc.com.monumentosdeespaa.BBDDSQLite;

/**
 * Created by Dani on 14/02/2018.
 */

public class EstructuraBBDD {
    public static final String TABLE_VISITAS_FUTURAS = "VisitasFuturas";
    public static final String ID_MONUMENTO = "id";
    public static final String NOMBRE_MONUMENTO = "nombre";
    public static final String COMUNIDAD_MONUMENTO  = "comunidad";
    public static final String PROVINCIA_MONUMENTO  = "provincia";
    public static final String LOCALIDAD_MONUMENTO  = "localidad";
    public static final String ANNO_MONUMENTO  = "anno";
    public static final String LATITUD_MONUMENTO  = "latitud";
    public static final String LONGITUD_MONUMENTO  = "longitud";




    public static final String SQL_CREATE_VISITAS_FUTURAS =
            "CREATE TABLE " + EstructuraBBDD.TABLE_VISITAS_FUTURAS + " (" +
                    EstructuraBBDD.ID_MONUMENTO + " INTEGER PRIMARY KEY," +
                    EstructuraBBDD.NOMBRE_MONUMENTO + " TEXT,"+
                    EstructuraBBDD.COMUNIDAD_MONUMENTO +" TEXT,"+
                    EstructuraBBDD.PROVINCIA_MONUMENTO+ " TEXT,"+
                    EstructuraBBDD.LOCALIDAD_MONUMENTO+" TEXT," +
                    EstructuraBBDD.ANNO_MONUMENTO+" INTEGER," +
                    EstructuraBBDD.LATITUD_MONUMENTO+" REAL," +
                    EstructuraBBDD.LONGITUD_MONUMENTO+" REAL )";

    public static final String SQL_DELETE_VISITAS_FUTURAS =
            "DROP TABLE IF EXISTS " + EstructuraBBDD.TABLE_VISITAS_FUTURAS;
}

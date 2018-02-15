package hlc.com.monumentosdeespaa.BBDDSQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Dani on 14/02/2018.
 */

public class BBDD_Helper extends SQLiteOpenHelper {

    private static int version = 1;
    private static final String nombreBD="monumentos_sqlite.db";

    public BBDD_Helper(Context context) {
        super(context, nombreBD, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(EstructuraBBDD.SQL_CREATE_VISITAS_FUTURAS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(EstructuraBBDD.SQL_DELETE_VISITAS_FUTURAS);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}

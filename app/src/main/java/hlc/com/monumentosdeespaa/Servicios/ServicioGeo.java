package hlc.com.monumentosdeespaa.Servicios;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.support.annotation.Nullable;

/**
 * Clase para crear el servicio
 * Created by Sergio on 11/02/2018.
 */

public class ServicioGeo extends IntentService {

    private LocationManager locationManager;

    public ServicioGeo(){
        super("ServicioGeo");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

}

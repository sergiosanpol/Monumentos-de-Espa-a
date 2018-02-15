package hlc.com.monumentosdeespaa.Servicios;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Clase para crear el servicio
 * Created by Sergio on 11/02/2018.
 */

public class ServicioGeo extends IntentService implements LocationListener{

    private LocationManager locationManager;

    public ServicioGeo(){
        super("ServicioGeo");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

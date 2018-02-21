package hlc.com.monumentosdeespaa.Activitys;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import hlc.com.monumentosdeespaa.Datos.Monumentos;
import hlc.com.monumentosdeespaa.Fragments.MapsFragment;
import hlc.com.monumentosdeespaa.R;
import hlc.com.monumentosdeespaa.Servicios.ServicioGeo;
import hlc.com.monumentosdeespaa.Servicios.ServicioMonumentosCercanos;

public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener, GoogleMap.OnInfoWindowClickListener {

    private static final int LOCATION_REQUEST_CODE = 1;
    private static final int ACTUALIZAR_GOOGLE_PLAY_SERVICES = 2;
    private static final int ACTIVITY_BUSCADOR = 3;
    private MapsFragment mapsFragment;
    private DrawerLayout drawerLayout;
    private Object[] monumentos;
    private GoogleMap mapa;
    private final LatLng espanna = new LatLng(40.46366700000001, -3.7492200000000366);

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

       /* startService(new Intent(this, ServicioGeo.class));
        IntentFilter filter = new IntentFilter();
        BroadCastGeo broadCastGeo = new BroadCastGeo();
        registerReceiver(broadCastGeo, filter);*/

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //Codigo del menu lateral.
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.abrir_navegacion_lateral, R.string.cerrar_navegacion_lateral);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Comprobar si recibe información de los monumentos
        if(getIntent().hasExtra("monumentos")) {
            monumentos = (Object[]) getIntent().getParcelableArrayExtra("monumentos");
        }

        //Comprobar versión de Google Play Services. En caso de que tengamos la ultima versión mostrará el mapa
        //Si no tenemos la última versión nos pide actualizar
        GoogleApiAvailability servicioPlay = GoogleApiAvailability.getInstance();
        int estado = servicioPlay.isGooglePlayServicesAvailable(this);
        if(estado == ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED){
            servicioPlay.getErrorDialog(this, estado, ACTUALIZAR_GOOGLE_PLAY_SERVICES).show();
        }else{
            //Cargar el fragmento en la activity
            cargarFragmentMap();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        startService(new Intent(getApplicationContext(),ServicioMonumentosCercanos.class));
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;

        //Comprobar si tenemos permiso de geolocalización para habilitar el botón de mi ubicación
        if(comprobarPermisoLocalizacion()){
            googleMap.setMyLocationEnabled(true);
            //Comprobamos que tengamos la geolocalización aproximada y mostramos en el mapa la última ubicación conocida del usuario
            //En caso de no poder acceder a esto nos muestra una visión de la peninsula
            if(comprobarPermisosLocalizacionAproximada()){
                FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
                client.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        moverCamara(new LatLng(location.getLatitude(), location.getLongitude()), 18f);
                    }
                });
            }else{
                //posicionamiento de la camara en españa
                moverCamara(espanna, 5.5f);
            }
        }else{
            //posicionamiento de la camara en españa
            moverCamara(espanna, 5.5f);
        }

        //Comprobar que haya monumentos para cargarlos
        if(monumentos != null) {
            for (Object object : monumentos) {
                Monumentos m = (Monumentos) object;
                mapa.addMarker(new MarkerOptions()
                        .position(m.getLatLng())
                        .title(m.getNombre()));
            }
        }

        mapa.setOnInfoWindowClickListener(this);

    }

    /**
     * Método para mover la camara del mapa
     * @param latLng
     * @param zoom
     */
    private void moverCamara(LatLng latLng, float zoom){
        CameraPosition cameraPosition =
                CameraPosition.builder()
                        .target(latLng)
                        .zoom(zoom).build();

        mapa.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    /**
     * Método que comprueba si tenemos persmisos de localización sobre el dispositivo
     * @return
     */
    private boolean comprobarPermisoLocalizacion(){
        //Comprobar si tenemos persmisos sobre localización
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED){
            // Solicitar permiso en caso de que no lo haya
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            return false;
        }
        return true;
    }

    private boolean comprobarPermisosLocalizacionAproximada(){
        //Comprobar si tenemos persmisos sobre localización
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED){
            // Solicitar permiso en caso de que no lo haya
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 3);
            return false;
        }
        return true;
    }

    /**
     * Método que carga el fragment del mapa
     */
    private void cargarFragmentMap(){
        mapsFragment = MapsFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.layout_principal,mapsFragment).commit();
        mapsFragment.getMapAsync(this);
    }

    /**
     * Método para gestionar las respuestas de otras activitys
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //En caso de que no esté actualizado Google Play Services y se actualice al volver de actualizar carga el mapa
        if(requestCode == ACTUALIZAR_GOOGLE_PLAY_SERVICES){
            cargarFragmentMap();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //abrir el activity de futuras visitas
        if(item.getItemId()==R.id.nav_futuras_visitas){
            startActivity(new Intent(this, FuturasVisitasActivity.class));
            return true;
        }else if(item.getItemId() == R.id.nav_busquedaFiltrada) {
            Intent buscador = new Intent(this, BuscadorActivity.class);
            buscador.putExtra("monumentos", monumentos);
            startActivity(buscador);
        //Abre las preferencias de la aplicación.
        }else if(item.getItemId() == R.id.nav_preferencias) {
            Intent preferencias = new Intent(this, Preferencias.class);
            startActivity(preferencias);
        //Para salir de la aplicación
        }else if(item.getItemId() == R.id.nav_salir){
            finish();
        }
        return false;
    }

    /**
     * Método para realizar las acciones del toolbar de la aplicación. Solo realiza la acción de abrir o cerrar el
     * menú lateral
     * @return
     */
    @Override
    public boolean onSupportNavigateUp() {
        //Si el menú está abierto
        if(drawerLayout.isDrawerOpen(Gravity.LEFT)){
            //Cerrará el menú
            drawerLayout.closeDrawer(Gravity.LEFT);
        }else{
            //Abrirá el menu
            drawerLayout.openDrawer(Gravity.LEFT);
        }
        return super.onSupportNavigateUp();
    }

    /**
     * Abrir el activity de informacion con el monumento pulsado
     * @param marker marcador pulsado
     */
    @Override
    public void onInfoWindowClick(Marker marker) {

        LatLng position = marker.getPosition();
        Monumentos seleccionado = null;

        //Busca el objeto del monumento que hemos pulsado
        for(Object o : monumentos){
            Monumentos m = (Monumentos) o;
            if(m.getLatLng().equals(position)){
                seleccionado = m;
                break;
            }
        }

        //iniciamos el activity pasandole el monumento seleccionado
        Intent intent = new Intent(this, InformacionActivity.class);
        intent.putExtra("monumento",seleccionado);
        startActivity(intent);
    }

    public class BroadCastGeo extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            context.startService(new Intent(context, ServicioGeo.class));
        }
    }

}
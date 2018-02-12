package hlc.com.monumentosdeespaa.Activitys;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import hlc.com.monumentosdeespaa.Datos.Monumentos;
import hlc.com.monumentosdeespaa.Fragments.MapsFragment;
import hlc.com.monumentosdeespaa.R;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    private static final int LOCATION_REQUEST_CODE = 1;
    private static final int ACTUALIZAR_GOOGLE_PLAY_SERVICES = 2;
    private MapsFragment mapsFragment;
    private DrawerLayout drawerLayout;
    private Object[] monumentos;
    private final LatLng espanna = new LatLng(40.46366700000001,-3.7492200000000366);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

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

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {

        //Comprobar si tenemos permiso de geolocalización para habilitar el botón de mi ubicación
        if(comprobarPermisoLocalizacion()){
            googleMap.setMyLocationEnabled(true);
        }

        //posicionamiento de la camara en españa
        CameraPosition cameraPosition =
                CameraPosition.builder()
                .target(espanna)
                .zoom(5.5f).build();

        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        //Comprobar que haya monumentos para cargarlos
        if(monumentos != null) {
            for (Object object : monumentos) {
                Monumentos m = (Monumentos) object;
                googleMap.addMarker(new MarkerOptions().position(m.getLatLng()).title(m.getNombre()));
            }
        }

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

}
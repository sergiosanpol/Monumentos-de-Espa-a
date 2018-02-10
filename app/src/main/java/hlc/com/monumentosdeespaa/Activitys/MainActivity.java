package hlc.com.monumentosdeespaa.Activitys;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import hlc.com.monumentosdeespaa.Datos.Monumentos;
import hlc.com.monumentosdeespaa.Fragments.MapsFragment;
import hlc.com.monumentosdeespaa.R;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener{

    private MapsFragment mapsFragment;
    private Object[] monumentos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);

        //Añadir el menu lateral del la app
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        monumentos = (Object[]) getIntent().getParcelableArrayExtra("monumentos");
=======
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Codigo del menu lateral. Comentado
        //Preparando el menu lateral

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.abrir_navegacion_lateral, R.string.cerrar_navegacion_lateral);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

>>>>>>> Stashed changes

        mapsFragment = MapsFragment.newInstance();

        getSupportFragmentManager().beginTransaction().add(R.id.layout_principal,mapsFragment).commit();

        mapsFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
<<<<<<< Updated upstream
=======
        map = googleMap;

        new InsertarMonumentos().execute();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }


    private class InsertarMonumentos extends AsyncTask<Void, Void, ArrayList<LatLng>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            LatLng espanna = new LatLng(40.4479921355443,-3.8294219970703125);

            CameraPosition cameraPosition = CameraPosition.builder().target(espanna).zoom(5.5f).build();

            map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


>>>>>>> Stashed changes

        for (Object object :  monumentos){
            Monumentos m = (Monumentos) object;
            googleMap.addMarker(new MarkerOptions().position(m.getLatLng()).title(m.getNombre()));
        }

    }




}
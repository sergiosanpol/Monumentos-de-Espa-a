package hlc.com.monumentosdeespaa.Activitys;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MarkerOptions;

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

        //Añadimos la barra superior
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Codigo del menu lateral.
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.abrir_navegacion_lateral, R.string.cerrar_navegacion_lateral);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Comprobar si recibe información de los monumentos
        if(getIntent().hasExtra("monumentos")) {
            monumentos = (Object[]) getIntent().getParcelableArrayExtra("monumentos");
        }

        //Cargar el fragmento en la activity
        mapsFragment = MapsFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.layout_principal,mapsFragment).commit();
        mapsFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        //Comprobar que haya monumentos para cargarlos
        if(monumentos != null) {
            for (Object object : monumentos) {
                Monumentos m = (Monumentos) object;
                googleMap.addMarker(new MarkerOptions().position(m.getLatLng()).title(m.getNombre()));
            }
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
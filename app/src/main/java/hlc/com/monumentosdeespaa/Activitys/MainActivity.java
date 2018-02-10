package hlc.com.monumentosdeespaa.Activitys;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import hlc.com.monumentosdeespaa.Datos.Monumentos;
import hlc.com.monumentosdeespaa.Fragments.MapsFragment;
import hlc.com.monumentosdeespaa.R;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{

    private MapsFragment mapsFragment;
    private Object[] monumentos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        monumentos = (Object[]) getIntent().getParcelableArrayExtra("monumentos");

        mapsFragment = MapsFragment.newInstance();

        getSupportFragmentManager().beginTransaction().add(R.id.layout_principal,mapsFragment).commit();

        mapsFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        for (Object object :  monumentos){
            Monumentos m = (Monumentos) object;
            googleMap.addMarker(new MarkerOptions().position(m.getLatLng()).title(m.getNombre()));
        }

    }




}
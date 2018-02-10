package hlc.com.monumentosdeespaa.Activitys;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import hlc.com.monumentosdeespaa.Fragments.MapsFragment;
import hlc.com.monumentosdeespaa.R;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{

    private MapsFragment mapsFragment;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        mapsFragment = MapsFragment.newInstance();

        getSupportFragmentManager().beginTransaction().add(R.id.layout_principal,mapsFragment).commit();

        mapsFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        new InsertarMonumentos().execute();
    }


    private class InsertarMonumentos extends AsyncTask<Void, Void, ArrayList<LatLng>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            LatLng espanna = new LatLng(40.4479921355443,-3.8294219970703125);

            CameraPosition cameraPosition = CameraPosition.builder().target(espanna).zoom(5.5f).build();

            map.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));



        }

        @Override
        protected ArrayList doInBackground(Void...v) {

            ArrayList<LatLng> arrayListPrueba = new ArrayList<>();

            LatLng l1 = new LatLng(36.834047,-2.4637136000000055);
            LatLng l2 = new LatLng(40.4167754,-3.7037901999999576);
            LatLng l3 = new LatLng(41.3850639,2.1734034999999494);
            LatLng l4 = new LatLng(43.3619145,-5.849388699999963);

            arrayListPrueba.add(l1);
            arrayListPrueba.add(l2);
            arrayListPrueba.add(l3);
            arrayListPrueba.add(l4);


            return arrayListPrueba;
        }

        @Override
        protected void onPostExecute(ArrayList<LatLng> latLngs) {
            super.onPostExecute(latLngs);

            for(LatLng l : latLngs){
                map.addMarker(new MarkerOptions().position(l));
            }
        }
    }
}
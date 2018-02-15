package hlc.com.monumentosdeespaa.Activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import hlc.com.monumentosdeespaa.Datos.Monumentos;
import hlc.com.monumentosdeespaa.R;

/**
 * Clase para el buscador
 */
public class BuscadorActivity extends AppCompatActivity {

    private Object[] monumentos;
    private ArrayList<Monumentos> listMonumentos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscador);

        //Reciba los monumentos
        if(getIntent().hasExtra("monumentos")){
            monumentos = getIntent().getParcelableArrayExtra("monumentos");
        }

        if(monumentos != null){
            listMonumentos = new ArrayList<>();
            for(int i = 0; i < monumentos.length; i++){
                listMonumentos.add((Monumentos) monumentos[i]);
            }
        }
    }
}

package hlc.com.monumentosdeespaa.Activitys;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

import hlc.com.monumentosdeespaa.Adaptadores.AdaptadorBuscador;
import hlc.com.monumentosdeespaa.Datos.Monumentos;
import hlc.com.monumentosdeespaa.R;

/**
 * Clase para el buscador
 */
public class BuscadorActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private Object[] monumentos;
    private ArrayList<Monumentos> listMonumentos;
    private SearchView buscador;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscador);

        //Obtenemos el buscador
        buscador = (SearchView) findViewById(R.id.buscador);
        //Le agregamos un escuchador para cuando se escriba algo
        buscador.setOnQueryTextListener(this);
        //Obtener el recycler
        recyclerView = (RecyclerView) findViewById(R.id.adaptador_buscador);
        recyclerView.setHasFixedSize(true);

        //Usamos el administardor
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Reciba los monumentos
        if(getIntent().hasExtra("monumentos")){
            monumentos = getIntent().getParcelableArrayExtra("monumentos");
        }

        //Pasamos los monumento de Object a Monumentos en una ArrayList
        if(monumentos != null){
            listMonumentos = new ArrayList<>();
            for(int i = 0; i < monumentos.length; i++){
                listMonumentos.add((Monumentos) monumentos[i]);
            }
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    /**
     * Método para detectar los cambios en el buscador. Con esto se adapta los items del buscador
     * a la busqueda que estemos realizando en todo momento
     * @param newText
     * @return
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        cargarAdaptador(newText);
        return false;
    }

    /**
     * Método que carga el adaptador conforme a la busqueda que estemos realizando. Comprueba que haya coincidencias en el
     * nombre del monumento, localidad, provincia y/o comunidad.
     * @param busqueda
     */
    private void cargarAdaptador(String busqueda){
        //Comprobamos que hayamos recibido monumentos
        if(listMonumentos != null) {
            List<Monumentos> monBuscados = new ArrayList<>();

            //Realizamos una iteración buscando coincidencias
            for (int i = 0; i < listMonumentos.size(); i++) {
                if (listMonumentos.get(i).getComunidad().contains(busqueda) ||
                        listMonumentos.get(i).getLocalidad().contains(busqueda) ||
                        listMonumentos.get(i).getNombre().contains(busqueda) ||
                        listMonumentos.get(i).getProvincia().contains(busqueda)) {
                    //Si coincide se añade a la lista que pasaremos para crear los items
                    monBuscados.add(listMonumentos.get(i));
                }
            }

            //Inicializamos el adaptador
            adapter = new AdaptadorBuscador(monBuscados);
            recyclerView.setAdapter(adapter);
        }
    }
}

package hlc.com.monumentosdeespaa.Adaptadores;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import hlc.com.monumentosdeespaa.Activitys.MonumentosCercanos;
import hlc.com.monumentosdeespaa.BBDDSQLite.ConsultasSQLite;
import hlc.com.monumentosdeespaa.Datos.Monumentos;
import hlc.com.monumentosdeespaa.R;

/**
 * Created by Dani on 26/02/2018.
 */

public class AdaptadorMonumentosCercanos extends RecyclerView.Adapter<AdaptadorMonumentosCercanos.MonumentoCercanosViewHolder>{

    private MonumentosCercanos monumentosCercanos;
    private ArrayList<Monumentos> listaMonumentosCercanos;

    public AdaptadorMonumentosCercanos(MonumentosCercanos monumentosCercanos, ArrayList<Monumentos> listaMonumentos){
        this.monumentosCercanos = monumentosCercanos;
        this.listaMonumentosCercanos = listaMonumentos;
    }

    /**
     * Inflamos la vista monumentos_cercanos_fila
     */
    @Override
    public MonumentoCercanosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.monumentos_cercanos_fila, parent,false);

        MonumentoCercanosViewHolder monumentoCercanosViewHolder = new MonumentoCercanosViewHolder(itemView);

        return monumentoCercanosViewHolder;
    }

    /**
     * Insertamos los datos en la vista
     */
    @Override
    public void onBindViewHolder(MonumentoCercanosViewHolder holder, int position) {

        final int posicionArray = position;
        holder.bindHolder(listaMonumentosCercanos.get(position));

        holder.botonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Insertamos el monumento cercano a futuras visitas
                ConsultasSQLite.insertarVisitaFurtura(view.getContext(), listaMonumentosCercanos.get(posicionArray));
            }
        });

        holder.botonVerMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                monumentosCercanos.cerrarActivity(posicionArray);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaMonumentosCercanos.size();
    }

    /**
     * Clase que carga los datos en la vista
     */
    public static class MonumentoCercanosViewHolder extends RecyclerView.ViewHolder{

        public ImageView botonAdd,botonVerMapa;
        private TextView nombreMonumento, ubicacionMonumento;

        public MonumentoCercanosViewHolder(View itemView) {
            super(itemView);

            nombreMonumento = (TextView) itemView.findViewById(R.id.nombre_monumento);
            ubicacionMonumento = (TextView) itemView.findViewById(R.id.ubicacion_monumento);
            botonAdd = (ImageView) itemView.findViewById(R.id.boton_add_futuras);
            botonVerMapa = (ImageView) itemView.findViewById(R.id.boton_ver_mapa);
        }

        public void bindHolder(Monumentos monumento){
            nombreMonumento.setText(monumento.getNombre());
            ubicacionMonumento.setText(monumento.getLocalidad()+", "+monumento.getProvincia());
        }
    }
}

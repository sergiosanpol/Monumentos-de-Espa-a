package hlc.com.monumentosdeespaa.Activitys;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import hlc.com.monumentosdeespaa.Datos.Monumentos;
import hlc.com.monumentosdeespaa.R;

/**
 * Created by Dani on 15/02/2018.
 */

public class AdaptadorFuturasVisitas extends RecyclerView.Adapter<AdaptadorFuturasVisitas.MonumentoViewHolder>{

    private ArrayList<Monumentos> listaMonumentos;

    public AdaptadorFuturasVisitas(ArrayList<Monumentos> lista){
        this.listaMonumentos = lista;
    }

    @Override
    public MonumentoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.futuras_visitas_fila,parent,false);

        MonumentoViewHolder monumentoViewHolder = new MonumentoViewHolder(itemView);

        return monumentoViewHolder;
    }

    @Override
    public void onBindViewHolder(MonumentoViewHolder holder, int position) {
        holder.bindHolder(listaMonumentos.get(position));
    }

    @Override
    public int getItemCount() {
        return listaMonumentos.size();
    }

    public static class MonumentoViewHolder extends RecyclerView.ViewHolder{

        private ImageView botonInfo, botonBorrar;
        private TextView nombreMonumento, ubicacionMonumento;

        public MonumentoViewHolder(View itemView) {
            super(itemView);

            nombreMonumento = (TextView) itemView.findViewById(R.id.nombre_monumento);
            ubicacionMonumento = (TextView) itemView.findViewById(R.id.ubicacion_monumento);
            botonInfo = (ImageView) itemView.findViewById(R.id.boton_info);
            botonBorrar = (ImageView) itemView.findViewById(R.id.boton_borrar);
        }

        public void bindHolder(Monumentos monumento){
            Log.d("PRUEBA",monumento==null?"NULO":monumento.getNombre());
            nombreMonumento.setText(monumento.getNombre());
            ubicacionMonumento.setText(monumento.getLocalidad()+", "+monumento.getProvincia());
        }
    }

}

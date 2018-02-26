package hlc.com.monumentosdeespaa.Adaptadores;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import hlc.com.monumentosdeespaa.Activitys.FuturasVisitasActivity;
import hlc.com.monumentosdeespaa.Activitys.InformacionActivity;
import hlc.com.monumentosdeespaa.BBDDSQLite.ConsultasSQLite;
import hlc.com.monumentosdeespaa.Datos.Monumentos;
import hlc.com.monumentosdeespaa.R;

/**
 * Created by Dani on 15/02/2018.
 */

public class AdaptadorFuturasVisitas extends RecyclerView.Adapter<AdaptadorFuturasVisitas.MonumentoViewHolder>{

    private ArrayList<Monumentos> listaMonumentos;
    private FuturasVisitasActivity futurasVisitas;


    public AdaptadorFuturasVisitas(FuturasVisitasActivity futurasVisitas, ArrayList<Monumentos> lista){
        this.futurasVisitas=futurasVisitas;
        this.listaMonumentos = lista;
    }

    /**
     * Inflamos la vista fila_futuras_visitas
     */
    @Override
    public MonumentoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.futuras_visitas_fila,parent,false);

        MonumentoViewHolder monumentoViewHolder = new MonumentoViewHolder(itemView);

        return monumentoViewHolder;
    }

    /**
     * Insertamos los datos en la vista
     */
    @Override
    public void onBindViewHolder(MonumentoViewHolder holder, int position) {

        final int posicionArray = position;
        holder.bindHolder(listaMonumentos.get(position));

        //Evento del boton de informacion
        holder.botonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), InformacionActivity.class);
                intent.putExtra("monumento",listaMonumentos.get(posicionArray));
                view.getContext().startActivity(intent);
            }
        });

        //Evento del boton de borrar
        holder.botonBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConsultasSQLite.borrarFuturasVisitas(view.getContext(),
                        listaMonumentos.get(posicionArray).getId_monumentos());
                Toast.makeText(view.getContext(),
                        listaMonumentos.get(posicionArray).getNombre()+view.getContext().getString(R.string.quitarFuturasVisitas),
                        Toast.LENGTH_LONG).show();
                listaMonumentos.remove(listaMonumentos.get(posicionArray));
                notifyItemRemoved(posicionArray);
            }
        });

        holder.botonVerMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                futurasVisitas.cerrarActivity(posicionArray);
            }
        });


    }

    @Override
    public int getItemCount() {
        return listaMonumentos.size();
    }

    /**
     * Clase que carga los datos en la vista
     */
    public static class MonumentoViewHolder extends RecyclerView.ViewHolder{

        public ImageView botonInfo, botonBorrar, botonVerMapa;
        private TextView nombreMonumento, ubicacionMonumento;

        public MonumentoViewHolder(View itemView) {
            super(itemView);

            nombreMonumento = (TextView) itemView.findViewById(R.id.nombre_monumento);
            ubicacionMonumento = (TextView) itemView.findViewById(R.id.ubicacion_monumento);
            botonInfo = (ImageView) itemView.findViewById(R.id.boton_info);
            botonBorrar = (ImageView) itemView.findViewById(R.id.boton_borrar);
            botonVerMapa = (ImageView) itemView.findViewById(R.id.boton_ver_mapa);
        }

        public void bindHolder(Monumentos monumento){
            nombreMonumento.setText(monumento.getNombre());
            ubicacionMonumento.setText(monumento.getLocalidad()+", "+monumento.getProvincia());
        }
    }

}

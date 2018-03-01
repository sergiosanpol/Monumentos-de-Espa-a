package hlc.com.monumentosdeespaa.Adaptadores;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import hlc.com.monumentosdeespaa.Activitys.InformacionActivity;
import hlc.com.monumentosdeespaa.Datos.Monumentos;
import hlc.com.monumentosdeespaa.R;

/**
 * Clase para adaptar las busquedas del buscador de lugares
 * Created by Sergio on 15/02/2018.
 */

public class AdaptadorBuscador extends RecyclerView.Adapter<AdaptadorBuscador.AdaptadorBuscadorHolder> {

    private List<Monumentos> items;
    private Activity activity;

    public AdaptadorBuscador(List<Monumentos> items, Activity activity){
        this.items = items;
        this.activity = activity;
    }

    /**
     * Clase que crea los items
     */
    public static class AdaptadorBuscadorHolder extends RecyclerView.ViewHolder {

        private TextView nombre;

        public AdaptadorBuscadorHolder(View itemView) {
            super(itemView);
            nombre = (TextView) itemView.findViewById(R.id.buscador_fila);
        }

    }

    @Override
    public AdaptadorBuscadorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.buscador_fila, parent, false);
        return new AdaptadorBuscadorHolder(view);
    }

    @Override
    public void onBindViewHolder(final AdaptadorBuscadorHolder holder, int position) {
        //Obtenemos el monumento a rellenar
        Monumentos monumentos = items.get(position);
        //Ponemos el nombre y la localidad del monumento
        holder.nombre.setText(monumentos.getNombre() + ", " + monumentos.getLocalidad());
        //Cuando pulsemos sobre este item nos llevará a la activity de la información
        holder.nombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), InformacionActivity.class);
                intent.putExtra("monumento_buscado", items.get(holder.getAdapterPosition()));
                activity.setResult(Activity.RESULT_OK, intent);
                activity.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}

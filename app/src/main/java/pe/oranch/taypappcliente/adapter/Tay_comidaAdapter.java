package pe.oranch.taypappcliente.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import pe.oranch.taypappcliente.R;
import pe.oranch.taypappcliente.entidades.Tay_comida;

import static pe.oranch.taypappcliente.Config.APP_API_URL;

/**
 * Created by Daniel on 15/11/2017.
 */

public class Tay_comidaAdapter extends RecyclerView.Adapter<Tay_comidaAdapter.Tay_comidaHolder> {
    List<Tay_comida> listaComida;
    private Context mContext;

    public Tay_comidaAdapter(Context context, List<Tay_comida> listaComida) {
        this.listaComida = listaComida;
        this.mContext= context;
    }

    @Override
    public Tay_comidaAdapter.Tay_comidaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_descubrir,parent,false);
        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        return new Tay_comidaAdapter.Tay_comidaHolder(vista);
    }

    @Override
    public void onBindViewHolder(Tay_comidaAdapter.Tay_comidaHolder holder, int position) {
        holder.item_titulo.setText(listaComida.get(position).getTay_tipocomida_nombre().toString());


        for (int i=0;i<listaComida.size();i++){
            String urlimagen;
            urlimagen = APP_API_URL + (listaComida.get(position).getTay_tipocomida_url().toString());
            Picasso.with(mContext).load(urlimagen).into(holder.imagenmenu);
        }


    }

    @Override
    public int getItemCount() {
        return listaComida.size();
    }

    public class Tay_comidaHolder extends RecyclerView.ViewHolder{

        TextView item_titulo;
        ImageView imagenmenu;

        public Tay_comidaHolder(View itemView) {
            super(itemView);
            item_titulo = (TextView) itemView.findViewById(R.id.item_Titulo);
            imagenmenu = (ImageView) itemView.findViewById(R.id.imagenMenu);
        }
    }
}

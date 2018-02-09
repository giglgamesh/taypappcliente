package pe.oranch.taypappcliente.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import pe.oranch.taypappcliente.R;
import pe.oranch.taypappcliente.models.PCartaData;
import pe.oranch.taypappcliente.models.PComentarioData;

import static pe.oranch.taypappcliente.Config.APP_API_CARTA;

/**
 * Created by Daniel on 15/11/2017.
 */

public class Tay_comentariosAdapter extends RecyclerView.Adapter<Tay_comentariosAdapter.Tay_comentariosHolder> {
    List<PComentarioData> pComentarioDataList;
    private Context mContext;

    public Tay_comentariosAdapter(Context context, List<PComentarioData> comentarios) {
        this.pComentarioDataList = comentarios;
        this.mContext= context;
    }

    @Override
    public Tay_comentariosAdapter.Tay_comentariosHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_comentario,parent,false);
        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        return new Tay_comentariosAdapter.Tay_comentariosHolder(vista);
    }

    @Override
    public void onBindViewHolder(Tay_comentariosAdapter.Tay_comentariosHolder holder, final int position) {
        final PComentarioData comentario = pComentarioDataList.get(position);

        holder.titulocomentario.setText(comentario.tay_cliente_nombre);
        holder.textocomentario.setText(comentario.tay_comentario_descripcion);

        //String urlimagen;
        //urlimagen = APP_API_CARTA + (carta.tay_carta_ruta_imagen);
        //Picasso.with(mContext).load(urlimagen).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.imagencarta);


    }

    @Override
    public int getItemCount() {
        if(pComentarioDataList != null) {
            return pComentarioDataList.size();
        }
        return 0;
        //return listaComida.size();
    }

    public class Tay_comentariosHolder extends RecyclerView.ViewHolder{

        TextView titulocomentario, textocomentario;
        //ImageView imagencarta;
        public Tay_comentariosHolder(View itemView) {
            super(itemView);
            titulocomentario = (TextView) itemView.findViewById(R.id.TituloComentario);
            textocomentario = (TextView) itemView.findViewById(R.id.TextoComentario);
            //imagencarta = (ImageView) itemView.findViewById(R.id.ImagenCarta);
        }
    }
}

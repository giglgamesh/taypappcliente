package pe.oranch.taypappcliente.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import pe.oranch.taypappcliente.R;
import pe.oranch.taypappcliente.activities.DescubrirComidaActivity;
import pe.oranch.taypappcliente.entidades.Tay_comida;
import pe.oranch.taypappcliente.models.PComidaData;

import static pe.oranch.taypappcliente.Config.APP_API_ANTIGUO;
import static pe.oranch.taypappcliente.Config.APP_API_URL;

/**
 * Created by Daniel on 15/11/2017.
 */

public class Tay_comidaAdapter extends RecyclerView.Adapter<Tay_comidaAdapter.Tay_comidaHolder> {
    //List<Tay_comida> listaComida;
    List<PComidaData> pComidaDataList;
    private Context mContext;

    public Tay_comidaAdapter(Context context, List<PComidaData> comidas) {
        //this.listaComida = listaComida;
        this.pComidaDataList = comidas;
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
    public void onBindViewHolder(Tay_comidaAdapter.Tay_comidaHolder holder, final int position) {
        final PComidaData comida = pComidaDataList.get(position);

        //holder.item_titulo.setText(listaComida.get(position).getTay_tipocomida_nombre().toString());
        holder.item_titulo.setText(comida.tay_tipocomida_nombre);

        //for (int i=0;i<listaComida.size();i++){
        //    String urlimagen;
        //    urlimagen = APP_API_ANTIGUO + (listaComida.get(position).getTay_tipocomida_url().toString());
        //    Picasso.with(mContext).load(urlimagen).into(holder.imagenmenu);
        //}
        for (int i=0;i<pComidaDataList.size();i++){
            String urlimagen;
            urlimagen = APP_API_ANTIGUO + (comida.tay_tipocomida_url);
            Picasso.with(mContext).load(urlimagen).into(holder.imagenmenu);
        }


        //holder.tipocomida.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        Intent intentReg = new Intent(mContext,DescubrirComidaActivity.class);
        //        intentReg.putExtra("tay_tipocomida_id", listaComida.get(position).getTay_tipocomida_id().toString());
        //        intentReg.putExtra("tay_tipocomida_nombre", listaComida.get(position).getTay_tipocomida_nombre().toString());
        //        mContext.startActivity(intentReg);
        //    }
        //});
    }

    @Override
    public int getItemCount() {
        if(pComidaDataList != null) {
            return pComidaDataList.size();
        }
        return 0;
        //return listaComida.size();
    }

    public class Tay_comidaHolder extends RecyclerView.ViewHolder{

        TextView item_titulo;
        ImageView imagenmenu;
        RelativeLayout tipocomida;
        public Tay_comidaHolder(View itemView) {
            super(itemView);
            item_titulo = (TextView) itemView.findViewById(R.id.item_Titulo);
            imagenmenu = (ImageView) itemView.findViewById(R.id.imagenMenu);
            tipocomida = (RelativeLayout) itemView.findViewById(R.id.TipoComida);
        }
    }
}

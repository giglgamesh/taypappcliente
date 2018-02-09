package pe.oranch.taypappcliente.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import pe.oranch.taypappcliente.R;
import pe.oranch.taypappcliente.models.PCartaData;
import pe.oranch.taypappcliente.models.PComidaData;

import static pe.oranch.taypappcliente.Config.APP_API_ANTIGUO;
import static pe.oranch.taypappcliente.Config.APP_API_CARTA;

/**
 * Created by Daniel on 15/11/2017.
 */

public class Tay_cartaAdapter extends RecyclerView.Adapter<Tay_cartaAdapter.Tay_cartaHolder> {
    List<PCartaData> pCartaDataList;
    private Context mContext;

    public Tay_cartaAdapter(Context context, List<PCartaData> cartas) {
        this.pCartaDataList = cartas;
        this.mContext= context;
    }

    @Override
    public Tay_cartaAdapter.Tay_cartaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_carta,parent,false);
        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        return new Tay_cartaAdapter.Tay_cartaHolder(vista);
    }

    @Override
    public void onBindViewHolder(Tay_cartaAdapter.Tay_cartaHolder holder, final int position) {
        final PCartaData carta = pCartaDataList.get(position);

        holder.nombreplato.setText(carta.tay_carta_nombre);
        holder.precioplato.setText("Costo: S/"+String.valueOf(carta.tay_carta_precio));

        String urlimagen;
        urlimagen = APP_API_CARTA + (carta.tay_carta_ruta_imagen);
        Picasso.with(mContext).load(urlimagen).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).into(holder.imagencarta);


    }

    @Override
    public int getItemCount() {
        if(pCartaDataList != null) {
            return pCartaDataList.size();
        }
        return 0;
        //return listaComida.size();
    }

    public class Tay_cartaHolder extends RecyclerView.ViewHolder{

        TextView nombreplato, precioplato;
        ImageView imagencarta;
        public Tay_cartaHolder(View itemView) {
            super(itemView);
            nombreplato = (TextView) itemView.findViewById(R.id.NombrePlato);
            precioplato = (TextView) itemView.findViewById(R.id.PrecioPlato);
            imagencarta = (ImageView) itemView.findViewById(R.id.ImagenCarta);
        }
    }
}

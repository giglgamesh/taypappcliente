package pe.oranch.taypappcliente.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import pe.oranch.taypappcliente.R;
import pe.oranch.taypappcliente.activities.DescubrirComidaActivity;
import pe.oranch.taypappcliente.entidades.Tay_comida;
import pe.oranch.taypappcliente.entidades.Tay_restaurantesjoin;

import static pe.oranch.taypappcliente.Config.APP_API_URL;

/**
 * Created by Daniel on 15/11/2017.
 */

public class Tay_restaurantesAdapter extends RecyclerView.Adapter<Tay_restaurantesAdapter.Tay_restaurantesHolder> {
    List<Tay_restaurantesjoin> listaRestaurantes;
    private Context mContext;

    public Tay_restaurantesAdapter(Context context, List<Tay_restaurantesjoin> listaRestaurantes) {
        this.listaRestaurantes = listaRestaurantes;
        this.mContext= context;
    }

    @Override
    public Tay_restaurantesAdapter.Tay_restaurantesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_descubrir_comida,parent,false);
        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        return new Tay_restaurantesAdapter.Tay_restaurantesHolder(vista);
    }

    @Override
    public void onBindViewHolder(Tay_restaurantesAdapter.Tay_restaurantesHolder holder, final int position) {
        holder.nombrerestaurante.setText(listaRestaurantes.get(position).getTay_empresa_nombre().toString());
        holder.tipocomida.setText(listaRestaurantes.get(position).getTay_tipocomida_nombre().toString());
        holder.direccionrestaurante.setText(listaRestaurantes.get(position).getTay_empresa_direccion().toString());
        //holder.ratingrestaurante.setRating(listaRestaurantes.get(position).getTay_calificacion_calificacion());

        for (int i=0;i<listaRestaurantes.size();i++){
            //String urlimagen;
            //urlimagen = APP_API_URL + (listaRestaurantes.get(position).getTay_tipocomida_url().toString());
            //Picasso.with(mContext).load(urlimagen).into(holder.imagenmenu);
        }

        holder.tipocomida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intentReg = new Intent(mContext,DescubrirComidaActivity.class);
                //intentReg.putExtra("tay_tipocomida_id", listaRestaurantes.get(position).getTay_tipocomida_id().toString());
                //intentReg.putExtra("tay_tipocomida_nombre", listaRestaurantes.get(position).getTay_tipocomida_nombre().toString());
                //mContext.startActivity(intentReg);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaRestaurantes.size();
    }

    public class Tay_restaurantesHolder extends RecyclerView.ViewHolder{

        TextView nombrerestaurante, tipocomida, direccionrestaurante;
        RatingBar ratingrestaurante;
        ImageView imagenrestaurante;
        LinearLayout layoutrestaurante;
        public Tay_restaurantesHolder(View itemView) {
            super(itemView);
            nombrerestaurante = (TextView) itemView.findViewById(R.id.NombreRestaurante);
            tipocomida = (TextView) itemView.findViewById(R.id.TipoComida);
            direccionrestaurante = (TextView) itemView.findViewById(R.id.DireccionRestaurante);
            ratingrestaurante = (RatingBar) itemView.findViewById(R.id.RatingRestaurante);
            imagenrestaurante = (ImageView) itemView.findViewById(R.id.ImagenRestaurante);
            layoutrestaurante = (LinearLayout) itemView.findViewById(R.id.LayoutRestaurante);
        }
    }
}

package pe.oranch.taypappcliente.adapter;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import pe.oranch.taypappcliente.R;
import pe.oranch.taypappcliente.activities.CartaActivity;
import pe.oranch.taypappcliente.activities.RestauranteActivity;
import pe.oranch.taypappcliente.entidades.Tay_restaurantesjoin;
import pe.oranch.taypappcliente.fragments.DialogMenuFotoFragment;
import pe.oranch.taypappcliente.fragments.DialogMenuFragment;
import pe.oranch.taypappcliente.fragments.DialogOfertaFragment;
import pe.oranch.taypappcliente.models.RestauranteRowData;

/**
 * Created by Daniel on 15/11/2017.
 */

public class Tay_restaurantesantiguosAdapter extends RecyclerView.Adapter<Tay_restaurantesantiguosAdapter.Tay_restaurantesHolder> {
    List<Tay_restaurantesjoin> listaRestaurantes;
    private Context mContext;

    public Tay_restaurantesantiguosAdapter(Context context, List<Tay_restaurantesjoin> listaRestaurantes) {
        this.listaRestaurantes = listaRestaurantes;
        this.mContext= context;
    }

    @Override
    public Tay_restaurantesantiguosAdapter.Tay_restaurantesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_descubrir_comida,parent,false);
        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        return new Tay_restaurantesantiguosAdapter.Tay_restaurantesHolder(vista);
    }

    @Override
    public void onBindViewHolder(Tay_restaurantesantiguosAdapter.Tay_restaurantesHolder holder, final int position) {
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

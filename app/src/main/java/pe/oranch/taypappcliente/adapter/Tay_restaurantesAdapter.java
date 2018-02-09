package pe.oranch.taypappcliente.adapter;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import pe.oranch.taypappcliente.activities.InicioActivity;
import pe.oranch.taypappcliente.activities.RestauranteActivity;
import pe.oranch.taypappcliente.fragments.DialogMenuFotoFragment;
import pe.oranch.taypappcliente.fragments.DialogMenuFragment;
import pe.oranch.taypappcliente.fragments.DialogOfertaFragment;
import pe.oranch.taypappcliente.models.RestauranteRowData;

/**
 * Created by Daniel on 15/11/2017.
 */

public class Tay_restaurantesAdapter extends RecyclerView.Adapter<Tay_restaurantesAdapter.Tay_restaurantesHolder> {
    //List<Tay_restaurantesjoin> listaRestaurantes;
    List<RestauranteRowData> listaRestaurantes;
    private Context mContext;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private int visibleThreshold = 2;
    private OnLoadMoreListener onLoadMoreListener = null;
    private String nombreTipoComida;
    private String ratingrest;

    public Tay_restaurantesAdapter(Context context, final List<RestauranteRowData> listaRestaurantes, RecyclerView recyclerView) {
        this.listaRestaurantes = listaRestaurantes;
        this.mContext= context;
        if(recyclerView.getLayoutManager()instanceof LinearLayoutManager) {

            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        // End has been reached
                        // Do something
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }else if(recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager)recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = gridLayoutManager.getItemCount();
                    lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();
                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        // End has been reached
                        // Do something
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });

        }else if(recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {

            final StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager)recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    if(listaRestaurantes != null) {
                        if (listaRestaurantes.size() > 0) {

                            totalItemCount = staggeredGridLayoutManager.getItemCount();

                            // for staggeredGridLayoutManager
                            int[] arr = new int[totalItemCount];
                            int[] lastVisibleItem2 = staggeredGridLayoutManager.findLastVisibleItemPositions(arr);
                            String string = "";
                            int greatestItem = 0;
                            for (int i = 0; i < lastVisibleItem2.length; i++) {
                                if (lastVisibleItem2[i] > greatestItem) {
                                    greatestItem = lastVisibleItem2[i];
                                }
                                string += " = " + lastVisibleItem2[i];
                            }
                            if (!loading && totalItemCount <= (greatestItem + visibleThreshold)) {
                                // End has been reached
                                // Do something
                                if (onLoadMoreListener != null) {
                                    onLoadMoreListener.onLoadMore();
                                }
                                loading = true;
                            }
                        }
                    }

                }
            });
        }
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
        holder.tipocomida.setText(listaRestaurantes.get(position).getRestaurante_comida_nombre().toString());
        if (listaRestaurantes.get(position).getRestaurante_rating()==null){
            ratingrest = "0";
        }else{
            ratingrest = listaRestaurantes.get(position).getRestaurante_rating().toString();
        }
        holder.ratingrestaurante.setRating(Float.parseFloat(ratingrest));
        holder.direccionrestaurante.setText(listaRestaurantes.get(position).getTay_empresa_direccion().toString());

        if (listaRestaurantes.get(position).getPrecio_desde_carta()==null){
            holder.botoncarta.setVisibility(View.INVISIBLE);
            holder.textocarta.setVisibility(View.INVISIBLE);
        }
        else{
            holder.textocarta.setText("Desde: S/."+listaRestaurantes.get(position).getPrecio_desde_carta().toString());
        }

        if (listaRestaurantes.get(position).getPrecio_desde_menu()==null){
            holder.botonmenu.setVisibility(View.INVISIBLE);
            holder.textomenu.setVisibility(View.INVISIBLE);
        }
        else{
            holder.textomenu.setText("Desde: S/."+listaRestaurantes.get(position).getPrecio_desde_menu().toString());
        }

        if (listaRestaurantes.get(position).getRestaurante_oferta()==null){
            holder.botonoferta.setVisibility(View.INVISIBLE);
        }
        else{
            if (listaRestaurantes.get(position).getRestaurante_oferta().equals("false")){
                holder.botonoferta.setVisibility(View.INVISIBLE);
            }
        }


        holder.botonoferta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = ((AppCompatActivity)mContext).getFragmentManager();
                DialogOfertaFragment newFragment = DialogOfertaFragment.newInstance();
                DialogOfertaFragment frag = new DialogOfertaFragment();
                Bundle args = new Bundle();
                args.putString("id_empresa", (String.valueOf(listaRestaurantes.get(position).getTay_empresa_id())));
                newFragment.setArguments(args);
                newFragment.show(fm, "Title");
            }
        });

        holder.linearlayoutrestaurante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent;
                intent = new Intent(mContext,RestauranteActivity.class);
                //PARA EL PREFERENCE
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
                SharedPreferences.Editor editor = prefs.edit();
                //FIN PREFERENCE
                intent.putExtra("restaurante_nombre", listaRestaurantes.get(position).getTay_empresa_nombre() );
                intent.putExtra("restaurante_direccion", listaRestaurantes.get(position).getTay_empresa_direccion());
                if (listaRestaurantes.get(position).getTay_empresa_telefono()==null){
                    intent.putExtra("restaurante_telefono", "N/D");
                    editor.putString("restaurante_telefono", "N/D");
                }
                else{
                    intent.putExtra("restaurante_telefono", listaRestaurantes.get(position).getTay_empresa_telefono());
                    editor.putString("restaurante_telefono", listaRestaurantes.get(position).getTay_empresa_telefono());
                }
                if (listaRestaurantes.get(position).getTay_empresa_horainicial()==null){
                    intent.putExtra("restaurante_inicio", "N/D");
                    editor.putString("restaurante_inicio", "N/D");
                }
                else{
                    intent.putExtra("restaurante_inicio", listaRestaurantes.get(position).getTay_empresa_horainicial());
                    editor.putString("restaurante_inicio", listaRestaurantes.get(position).getTay_empresa_horainicial());
                }
                if (listaRestaurantes.get(position).getTay_empresa_horafin()==null){
                    intent.putExtra("restaurante_fin", "N/D");
                    editor.putString("restaurante_fin", "N/D");
                }
                else{
                    intent.putExtra("restaurante_fin", listaRestaurantes.get(position).getTay_empresa_horafin());
                    editor.putString("restaurante_fin", listaRestaurantes.get(position).getTay_empresa_horafin());
                }
                if (listaRestaurantes.get(position).getTay_empresa_latitud()==null){
                    intent.putExtra("restaurante_latitud", "0");
                    editor.putString("restaurante_latitud", "0");
                }
                else{
                    intent.putExtra("restaurante_latitud", listaRestaurantes.get(position).getTay_empresa_latitud());
                    editor.putString("restaurante_latitud", listaRestaurantes.get(position).getTay_empresa_latitud());
                }
                if (listaRestaurantes.get(position).getTay_empresa_longitud()==null){
                    intent.putExtra("restaurante_longitud", "0");
                    editor.putString("restaurante_longitud", "0");
                }
                else{
                    intent.putExtra("restaurante_longitud", listaRestaurantes.get(position).getTay_empresa_longitud());
                    editor.putString("restaurante_longitud", listaRestaurantes.get(position).getTay_empresa_longitud());
                }

                intent.putExtra("restaurante_comida", listaRestaurantes.get(position).getRestaurante_comida_nombre());

                //OTROS VALORES DEL PREF
                editor.putString("restaurante_id", String.valueOf(listaRestaurantes.get(position).getTay_empresa_id()));
                editor.putString("restaurante_nombre", listaRestaurantes.get(position).getTay_empresa_nombre());
                editor.putString("restaurante_direccion", listaRestaurantes.get(position).getTay_empresa_direccion());
                editor.putString("restaurante_comida", listaRestaurantes.get(position).getRestaurante_comida_nombre());
                //FIN VALORES

                editor.apply();
                mContext.startActivity(intent);
            }
        });

        holder.botoncarta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent;
                intent = new Intent(mContext,CartaActivity.class);
                intent.putExtra("restaurante_id", String.valueOf(listaRestaurantes.get(position).getTay_empresa_id()));
                mContext.startActivity(intent);
            }
        });
        //holder.ratingrestaurante.setRating(listaRestaurantes.get(position).getTay_calificacion_calificacion());

        holder.botonmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listaRestaurantes.get(position).getMenufoto().equals("true")){
                    FragmentManager fm = ((AppCompatActivity)mContext).getFragmentManager();
                    DialogMenuFotoFragment newFragment = DialogMenuFotoFragment.newInstance();
                    Bundle args = new Bundle();
                    args.putString("id_empresa", (String.valueOf(listaRestaurantes.get(position).getTay_empresa_id())));
                    newFragment.setArguments(args);
                    newFragment.show(fm, "Title");
                }
                else{
                    FragmentManager fm = ((AppCompatActivity)mContext).getFragmentManager();
                    DialogMenuFragment newFragment = DialogMenuFragment.newInstance();
                    Bundle args = new Bundle();
                    args.putString("id_empresa", (String.valueOf(listaRestaurantes.get(position).getTay_empresa_id())));
                    newFragment.setArguments(args);
                    newFragment.show(fm, "Title");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(listaRestaurantes != null) {
            return listaRestaurantes.size();
        }
        return 0;
    }

    public interface OnLoadMoreListener{
        void onLoadMore();
    }

    public class Tay_restaurantesHolder extends RecyclerView.ViewHolder{

        TextView nombrerestaurante, tipocomida, direccionrestaurante,textocarta,textomenu;
        Button botonoferta, botonmenu, botoncarta;
        ImageView imagenrestaurante;
        LinearLayout layoutrestaurante, linearlayoutrestaurante;
        RatingBar ratingrestaurante;
        public Tay_restaurantesHolder(View itemView) {
            super(itemView);
            nombrerestaurante = (TextView) itemView.findViewById(R.id.NombreRestaurante);
            tipocomida = (TextView) itemView.findViewById(R.id.TipoComida);
            direccionrestaurante = (TextView) itemView.findViewById(R.id.DireccionRestaurante);
            imagenrestaurante = (ImageView) itemView.findViewById(R.id.ImagenRestaurante);
            layoutrestaurante = (LinearLayout) itemView.findViewById(R.id.LayoutRestaurante);
            ratingrestaurante = (RatingBar) itemView.findViewById(R.id.RatingRestaurante);
            textocarta = (TextView) itemView.findViewById(R.id.TextoCarta);
            textomenu = (TextView) itemView.findViewById(R.id.TextoMenu);
            botonoferta = (Button) itemView.findViewById(R.id.BotonOferta);
            botonmenu = (Button) itemView.findViewById(R.id.BotonMenu);
            botoncarta = (Button) itemView.findViewById(R.id.BotonCarta);
            linearlayoutrestaurante = (LinearLayout) itemView.findViewById(R.id.LinearLayoutRestaurante);
        }
    }
}

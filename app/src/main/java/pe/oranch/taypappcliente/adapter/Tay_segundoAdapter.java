package pe.oranch.taypappcliente.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import pe.oranch.taypappcliente.R;
import pe.oranch.taypappcliente.models.PMenuData;

/**
 * Created by Daniel on 15/11/2017.
 */

public class Tay_segundoAdapter extends RecyclerView.Adapter<Tay_segundoAdapter.Tay_entradaHolder> {
    //List<Tay_comida> listaComida;
    List<PMenuData> pMenuDataList;
    private Context mContext;

    public Tay_segundoAdapter(Context context, List<PMenuData> menus) {
        //this.listaComida = listaComida;
        this.pMenuDataList = menus;
        this.mContext= context;
    }

    @Override
    public Tay_segundoAdapter.Tay_entradaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista= LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_menu,parent,false);
        RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        vista.setLayoutParams(layoutParams);
        return new Tay_segundoAdapter.Tay_entradaHolder(vista);
    }

    @Override
    public void onBindViewHolder(Tay_segundoAdapter.Tay_entradaHolder holder, final int position) {
        final PMenuData menu = pMenuDataList.get(position);
        holder.nombreplato.setText("- "+menu.tay_menu_nombre);
    }

    @Override
    public int getItemCount() {
        if(pMenuDataList != null) {
            return pMenuDataList.size();
        }
        return 0;
        //return listaComida.size();
    }

    public class Tay_entradaHolder extends RecyclerView.ViewHolder{

        TextView nombreplato;
        public Tay_entradaHolder(View itemView) {
            super(itemView);
            nombreplato = (TextView) itemView.findViewById(R.id.NombrePlato);
        }
    }
}

package pe.oranch.taypappcliente.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pe.oranch.taypappcliente.R;

/**
 * Created by Daniel on 22/01/2018.
 */

public class RestauranteDatosFragment  extends Fragment {

    public RestauranteDatosFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_restaurante, container, false);


        return v;

    }
}

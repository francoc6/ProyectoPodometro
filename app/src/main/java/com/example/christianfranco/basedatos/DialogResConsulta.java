package com.example.christianfranco.basedatos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DialogResConsulta extends DialogFragment {

    private TextView Var;
    private ListView resultado;

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_resconsulta, container, false);
        Var = (TextView) view.findViewById(R.id.VAR);
        resultado = (ListView) view.findViewById(R.id.listview);
        Var.setText(Consulta.var);
        ArrayAdapter<String> pantalla = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_expandable_list_item_1, Consulta.datos);
        resultado.setAdapter(pantalla);
        return view;
    }
}

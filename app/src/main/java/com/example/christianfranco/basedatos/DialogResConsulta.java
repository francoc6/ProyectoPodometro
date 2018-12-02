package com.example.christianfranco.basedatos;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DialogResConsulta extends DialogFragment {

    private TextView Var;

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_resconsulta, container, false);
      Var=(TextView)view.findViewById(R.id.VAR);
      Var.setText(Consulta.var);




        return view;
    }


}

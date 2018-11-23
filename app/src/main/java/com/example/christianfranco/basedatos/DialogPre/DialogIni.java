package com.example.christianfranco.basedatos.DialogPre;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import com.example.christianfranco.basedatos.R;


public class DialogIni extends DialogFragment {
    //widgets
    private RadioButton p11, p12, p13, p14, p15;
    private RadioButton p21, p22, p23, p24, p25;
    private RadioButton p31, p32, p33, p34, p35;
    private RadioButton p41, p42, p43, p44, p45;
    private Button Aceptar;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_formulario_ini, container, false);


        Aceptar = view.findViewById(R.id.iniAceptar);
        Aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                getDialog().dismiss();//salir de la pantalla
            }
        });

        return view;
    }

    public int[] resultados() {//se lo podria hacer con un case, pero seria casi lo mismo obteniendo los id
        int res[]={};

        //Pregunta 1
        if (p11.isChecked()) { res[0]=1; }
        else if (p12.isChecked()) { res[0]=2; }
        else if(p13.isChecked()){ res[0]=3; }
        else if(p14.isChecked()){ res[0]=4; }
        else if(p15.isChecked()){ res[0]=5; }
        //Pregunta 2
        if (p21.isChecked()) { res[1]=1; }
        else if (p22.isChecked()) { res[1]=2; }
        else if(p23.isChecked()){ res[1]=3; }
        else if(p24.isChecked()){ res[1]=4; }
        else if(p25.isChecked()){ res[1]=5; }
        //Pregunta 3
        if (p31.isChecked()) { res[2]=1; }
        else if (p32.isChecked()) { res[2]=2; }
        else if(p33.isChecked()){ res[2]=3; }
        else if(p34.isChecked()){ res[2]=4; }
        else if(p35.isChecked()){ res[2]=5; }
        //Pregunta 4
        if (p41.isChecked()) { res[3]=1; }
        else if (p42.isChecked()) { res[3]=2; }
        else if(p43.isChecked()){ res[3]=3; }
        else if(p44.isChecked()){ res[3]=4; }
        else if(p45.isChecked()){ res[3]=5; }
        return res;
    }

}

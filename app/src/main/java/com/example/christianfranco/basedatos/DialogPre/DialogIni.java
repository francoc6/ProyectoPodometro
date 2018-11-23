package com.example.christianfranco.basedatos.DialogPre;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.christianfranco.basedatos.Conectar;
import com.example.christianfranco.basedatos.R;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


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
        p11 = (RadioButton) view.findViewById(R.id.i11);
        p12 = (RadioButton) view.findViewById(R.id.i12);
        p13 = (RadioButton) view.findViewById(R.id.i13);
        p14 = (RadioButton) view.findViewById(R.id.i14);
        p15 = (RadioButton) view.findViewById(R.id.i15);

        p21 = (RadioButton) view.findViewById(R.id.i21);
        p22 = (RadioButton) view.findViewById(R.id.i22);
        p23 = (RadioButton) view.findViewById(R.id.i23);
        p24 = (RadioButton) view.findViewById(R.id.i24);
        p25 = (RadioButton) view.findViewById(R.id.i25);

        p31 = (RadioButton) view.findViewById(R.id.i31);
        p32 = (RadioButton) view.findViewById(R.id.i32);
        p33 = (RadioButton) view.findViewById(R.id.i33);
        p34 = (RadioButton) view.findViewById(R.id.i34);
        p35 = (RadioButton) view.findViewById(R.id.i35);

        p41 = (RadioButton) view.findViewById(R.id.i41);
        p42 = (RadioButton) view.findViewById(R.id.i42);
        p43 = (RadioButton) view.findViewById(R.id.i43);
        p44 = (RadioButton) view.findViewById(R.id.i44);
        p45 = (RadioButton) view.findViewById(R.id.i45);

        Aceptar = view.findViewById(R.id.iniAceptar);
        Aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarformularioini(resultados());//envio los resultado a la  base de ddatos
                getDialog().dismiss();//salir de la pantalla
            }
        });

        return view;
    }

    public String resultados() {//se lo podria hacer con un case, pero seria casi lo mismo obteniendo los id
        String res="";//creo un string que se concatenara con las respuestas
        //Pregunta 1
        if (p11.isChecked()) {
            res+="1 ";
        } else if (p12.isChecked()) {
            res+="2 ";
        } else if (p13.isChecked()) {
            res+="3 ";
        } else if (p14.isChecked()) {
            res+="4 ";
        } else if (p15.isChecked()) {
            res+="5 ";
        }
        //Pregunta 2
        if (p21.isChecked()) {
            res+="1 ";
        } else if (p22.isChecked()) {
            res+="2 ";
        } else if (p23.isChecked()) {
            res+="3 ";
        } else if (p24.isChecked()) {
            res+="4 ";
        } else if (p25.isChecked()) {
            res+="5 ";
        }
        //Pregunta 3
        if (p31.isChecked()) {
            res+="1 ";
        } else if (p32.isChecked()) {
            res+="2 ";
        } else if (p33.isChecked()) {
            res+="3 ";
        } else if (p34.isChecked()) {
            res+="4 ";
        } else if (p35.isChecked()) {
            res+="5 ";
        }
        //Pregunta 4
        if (p41.isChecked()) {
            res+="1 ";
        } else if (p42.isChecked()) {
            res+="2 ";
        } else if (p43.isChecked()) {
            res+="3 ";
        } else if (p44.isChecked()) {
            res+="4 ";
        } else if (p45.isChecked()) {
            res+="5 ";
        }
        return res;
    }

    Conectar contacto = new Conectar();//agregar los datos a la base

    public void agregarformularioini(String resultados) {
        String[] parts = resultados.split(" ");//separo el string por espacios

        try {
            PreparedStatement pedir = contacto.conectarabase().prepareStatement("insert into FormularioInicio values(?,?,?,?)");
            pedir.setString(1, (parts[0]));
            pedir.setString(2, (parts[1]));
            pedir.setString(3, (parts[2]));
            pedir.setString(4, (parts[3]));
            pedir.executeUpdate();
            Toast.makeText(getContext(), "Formulario agregado CORRECTAMENTE", Toast.LENGTH_SHORT).show();

        } catch (SQLException e) {
            Toast.makeText(getContext(), "Revisa tu conexion", Toast.LENGTH_SHORT).show();
        }
    }
}
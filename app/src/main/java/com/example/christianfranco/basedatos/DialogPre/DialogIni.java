package com.example.christianfranco.basedatos.DialogPre;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.christianfranco.basedatos.Actividad;
import com.example.christianfranco.basedatos.Conectar;
import com.example.christianfranco.basedatos.R;
import com.example.christianfranco.basedatos.Usuario;

import java.sql.PreparedStatement;
import java.sql.SQLException;



public class DialogIni extends DialogFragment {
    //widgets
    private RadioButton p11, p12, p13, p14, p15;
    private RadioButton p21, p22, p23, p24, p25;
    private RadioButton p31, p32, p33, p34, p35;
    private RadioButton p41, p42, p43, p44, p45;
    private Button Aceptar;
    public static TextView Preg1,Preg2,Preg3,Preg4;
    public static String tabla="",toast="";

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

        Preg1=(TextView)view.findViewById(R.id.Pregunta1);
        Preg2=(TextView)view.findViewById(R.id.Pregunta2);
        Preg3=(TextView)view.findViewById(R.id.Pregunta3);
        Preg4=(TextView)view.findViewById(R.id.Pregunta4);

        Aceptar = view.findViewById(R.id.iniAceptar);



        if(!Usuario.banderaformulario){
            preguntasiniciales();
            Usuario.banderaformulario=true;
        }else{
            preguntasfinales();
            Usuario.banderaformulario=false;
        }

        Aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarformularioini(resultados());//envio los resultado a la  base de ddatos
                Actividad.yasehizo=true;//cuando se lo hace por primera vez, ya no interfiere cuando se presiona el boton para pausar cronometro
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
        String orden="insert into "+tabla+" values(?,?,?,?)";//obtengo la base a usar
        try {
            PreparedStatement pedir = contacto.conectarabase().prepareStatement(orden);
            pedir.setString(1, parts[0]);
            pedir.setString(2, parts[1]);
            pedir.setString(3, parts[2]);
            pedir.setString(4, parts[3]);
            pedir.executeUpdate();
            Toast.makeText(getContext(),toast, Toast.LENGTH_SHORT).show();

        } catch (SQLException e) {
            Toast.makeText(getContext(),"Hubo un problema", Toast.LENGTH_SHORT).show();
        }
    }

    public void preguntasiniciales() {
        Preg1.setText("Pregunta Inicial 1");
        Preg2.setText("Pregunta Inicial 2");
        Preg3.setText("Pregunta Inicial 3");
        Preg4.setText("Pregunta Inicial 4");
        tabla = "FormularioInicio";
        toast = "Formulario Inicial Agregado Correctamente";
    }

    public void preguntasfinales() {
        Preg1.setText("Pregunta Final 1");
        Preg2.setText("Pregunta Final 2");
        Preg3.setText("Pregunta Final 3");
        Preg4.setText("Pregunta Final 4");
        tabla = "FormularioFinal";
        toast = "Formulario Final Agregado Correctamente";
    }
}
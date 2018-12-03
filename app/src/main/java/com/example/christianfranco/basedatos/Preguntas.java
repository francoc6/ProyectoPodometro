package com.example.christianfranco.basedatos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;


public class Preguntas extends AppCompatActivity {

    //widgets
    private RadioButton p11, p12, p13, p14, p15;
    private RadioButton p21, p22, p23, p24, p25;
    private RadioButton p31, p32, p33, p34, p35;
    private RadioButton p41, p42, p43, p44, p45;
    private Button Aceptar;
    public static TextView Preg1, Preg2, Preg3, Preg4;
    SharedPreferences usuariognr;//lo uso para obtener el usuario almacenado

    public String PF;

    boolean confirmar=false;

    Calendar calendarNow = new GregorianCalendar(TimeZone.getTimeZone("America/Guayaquil"));
    int dia =calendarNow.get(Calendar.DAY_OF_MONTH);
    int mes =1+calendarNow.get(Calendar.MONTH);
    int anio =calendarNow.get(Calendar.YEAR);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preguntas);
        p11 = (RadioButton) findViewById(R.id.i11);p12 = (RadioButton) findViewById(R.id.i12);p13 = (RadioButton) findViewById(R.id.i13);p14 = (RadioButton) findViewById(R.id.i14);p15 = (RadioButton) findViewById(R.id.i15);
        p21 = (RadioButton) findViewById(R.id.i21);p22 = (RadioButton) findViewById(R.id.i22);p23 = (RadioButton) findViewById(R.id.i23);p24 = (RadioButton) findViewById(R.id.i24);p25 = (RadioButton) findViewById(R.id.i25);
        p31 = (RadioButton) findViewById(R.id.i31);p32 = (RadioButton) findViewById(R.id.i32);p33 = (RadioButton) findViewById(R.id.i33);p34 = (RadioButton) findViewById(R.id.i34);p35 = (RadioButton) findViewById(R.id.i35);
        p41 = (RadioButton) findViewById(R.id.i41);p42 = (RadioButton) findViewById(R.id.i42);p43 = (RadioButton) findViewById(R.id.i43);p44 = (RadioButton) findViewById(R.id.i44);p45 = (RadioButton) findViewById(R.id.i45);
        Preg1 = (TextView) findViewById(R.id.Pregunta1);Preg2 = (TextView) findViewById(R.id.Pregunta2);Preg3 = (TextView) findViewById(R.id.Pregunta3);Preg4 = (TextView) findViewById(R.id.Pregunta4);
        Aceptar = findViewById(R.id.iniAceptar);
        usuariognr = getSharedPreferences("Guardarusuario", MODE_PRIVATE);//instancio el objeto para obtener usuario
        final String usuario =usuariognr.getString("usuario","vacio");
        //valido si es el inicio o final para mostrar las preguntas correctas
        if (!Usuario.banderaformulario) {
            Actividad.yasehizo = true;//cuando se lo hace por primera vez, ya no presenta el formulario interfiere cuando se presiona el boton para pausar cronometro
            Usuario.banderaformulario = true;//para ver si se presentan las preguntas iniciales o finales
        } else {
            Actividad.yasehizo = false;
            Usuario.banderaformulario = false;
        }

        //cargo las preguntas correspondientes a presentar
        obtenerPreguntas();

        Aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultados();
                if (confirmar==true){
                    if(!Usuario.banderaformulario){
                        agregaractividad(usuario, String.valueOf(Actividad.obtenertiempo(Actividad.tiempofinal)),dia+"/"+mes+"/"+anio,String.valueOf(Actividad.pasos),Actividad.Preguntas_I,PF);
                    }
                    Intent salir = new Intent(Preguntas.this, Actividad.class);
                    startActivity(salir);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Por favor responda el formulario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //obtengo las respuestas de las preguntas en un string
    public void resultados() {//se lo podria hacer con un case, pero seria casi lo mismo obteniendo los id
        String res = "";//creo un string que se concatenara con las respuestas
        //Pregunta 1
        if (p11.isChecked()) {
            res += "1 ";
        } else if (p12.isChecked()) {
            res += "2 ";
        } else if (p13.isChecked()) {
            res += "3 ";
        } else if (p14.isChecked()) {
            res += "4 ";
        } else if (p15.isChecked()) {
            res += "5 ";
        }
        //Pregunta 2
        if (p21.isChecked()) {
            res += "1 ";
        } else if (p22.isChecked()) {
            res += "2 ";
        } else if (p23.isChecked()) {
            res += "3 ";
        } else if (p24.isChecked()) {
            res += "4 ";
        } else if (p25.isChecked()) {
            res += "5 ";
        }
        //Pregunta 3
        if (p31.isChecked()) {
            res += "1 ";
        } else if (p32.isChecked()) {
            res += "2 ";
        } else if (p33.isChecked()) {
            res += "3 ";
        } else if (p34.isChecked()) {
            res += "4 ";
        } else if (p35.isChecked()) {
            res += "5 ";
        }
        //Pregunta 4
        if (p41.isChecked()) {
            res += "1 ";
        } else if (p42.isChecked()) {
            res += "2 ";
        } else if (p43.isChecked()) {
            res += "3 ";
        } else if (p44.isChecked()) {
            res += "4 ";
        } else if (p45.isChecked()) {
            res += "5 ";
        }

        if(res.length()==8){
            confirmar =true;
            if (Usuario.banderaformulario)
                Actividad.Preguntas_I = res;//agrego a la variable correspondiente
            else {
                PF=res;
                //Actividad.Preguntas_F = res;
            }
        }
    }

    //obtengo las preguntas a presentar desde la base de datos
    Conectar conectar = new Conectar();
    public void obtenerPreguntas() {
        List<String> preg = new ArrayList<>();
        try {
            String tipo;
            if (Usuario.banderaformulario) {
                tipo = "Inicio";
            } else {
                tipo = "Fin";
            }
            Statement pedir = conectar.conectarabase().createStatement();
            String orden = "SELECT Texto FROM Preguntas_db WHERE TIPO='" + tipo + "'";
            ResultSet res = null;
            res = pedir.executeQuery(orden);
            // res.next();
            while (res.next()) {
                preg.add(res.getString("Texto"));
            }
            res.close();
        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        Preg1.setText(preg.get(0));
        Preg2.setText(preg.get(1));
        Preg3.setText(preg.get(2));
        Preg4.setText(preg.get(3));
    }

//metodo para agregar datos a la base
    public void agregaractividad(String u,String t,String f, String p,String PI,String PF){
        String orden ="insert into ACTIVIDAD values(?,?,?,?,?,?)";
        try {
            PreparedStatement pedir = conectar.conectarabase().prepareStatement(orden);
            pedir.setString(1,u);
            pedir.setString(2,t);
            pedir.setString(3,f);
            pedir.setString(4,p);
            pedir.setString(5,PI);
            pedir.setString(6,PF);
            pedir.executeUpdate();
            Toast.makeText(getApplicationContext(), "Se agrego a la base", Toast.LENGTH_SHORT).show();
            pedir.close();//cierro la conexion
        }catch (SQLException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}

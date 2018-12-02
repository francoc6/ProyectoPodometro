package com.example.christianfranco.basedatos;

//se van a agregar los datos con id segun el tipo: Azucar 1, Sangre 2,Peso 3,  ...


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;


public class AgregarDato extends AppCompatActivity {
    Button agregar, cancelar;
    Spinner opciones;
    EditText dato;

    Calendar calendarNow = new GregorianCalendar(TimeZone.getTimeZone("America/Guayaquil"));
    int dia =calendarNow.get(Calendar.DAY_OF_MONTH);
    int mes = calendarNow.get(Calendar.MONTH);
    int anio = calendarNow.get(Calendar.YEAR);
    String fecha=dia+"/"+mes+"/"+anio;

    SharedPreferences usuariognr;//lo uso para obtener el usuario almacenado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_dato);
        agregar = (Button) findViewById(R.id.AgregarBtn);
        cancelar = (Button) findViewById(R.id.Cancelaradd);
        dato=(EditText)findViewById(R.id.adddato);
        opciones = (Spinner) findViewById(R.id.opcionspinner);
        String[] variables = {"Azucar (unidad):", "Peso (lb):", "Sangre (ml):", "algo :", "algoo :"};
        opciones.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, variables));

        usuariognr = getSharedPreferences("Guardarusuario",MODE_PRIVATE);//instancio el objeto para obtener usuario
        final String usuario =usuariognr.getString("usuario","vacio");


        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selec=opciones.getSelectedItem().toString();
                String[] parts = selec.split(" ");//para tomar solo la palabra y no la unidad lo llamo con parts[0]
                if(dato.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(), "Ingrese un valor", Toast.LENGTH_SHORT).show();
                }else{
                    agregardato(obtenerindice(parts[0]),usuario,dato.getText().toString(),fecha);
                    dato.setText("");
                }
            }
        });
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menu = new Intent(AgregarDato.this, Menu.class);
                startActivity(menu);
                finish();
            }
        });

    }

    Conectar contacto = new Conectar();
    public void agregardato(Integer i,String u,String valor, String f){
        String orden ="insert into Variables_db values(?,?,?,?)";
        try {
            PreparedStatement pedir = contacto.conectarabase().prepareStatement(orden);
            pedir.setInt(1,i);
            pedir.setString(2,u);
            pedir.setString(3,valor);
            pedir.setString(4,f);
            pedir.executeUpdate();
            Toast.makeText(getApplicationContext(), "Dato agregado correctamente", Toast.LENGTH_SHORT).show();
            pedir.close();//cierro la conexion
        }catch (SQLException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public static int obtenerindice(String d){
        int r=0;
        if(d.equals("Azucar")){
            r=1;
        }
        if(d.equals("Sangre")){
            r=2;
        }
        if(d.equals("Peso")){
            r=3;
        }
        return r;
    }


}

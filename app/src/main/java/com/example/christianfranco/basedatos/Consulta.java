package com.example.christianfranco.basedatos;


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Consulta extends AppCompatActivity {
    Spinner datoaconsultar;
    Button consultar,grafico;
    static String var;
    SharedPreferences usuariognr;//lo uso para obtener el usuario almacenado
    static ArrayList<String> datos;
    Boolean ban=false;//bandera para desplegar el fragment, si es que hay conexion a la base

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);

        usuariognr = getSharedPreferences("Guardarusuario", MODE_PRIVATE);//instancio el objeto para obtener usuario
        final String usuario = usuariognr.getString("usuario", "vacio");

        final DialogResConsulta dialog = new DialogResConsulta();

        consultar=(Button)findViewById(R.id.consultar);
        grafico=(Button)findViewById(R.id.Grafico);
        datoaconsultar = (Spinner) findViewById(R.id.spinnerconsulta);
        String[] variables = {"Glucosa", "Hemoglobina","Colesterol","Trigliceridos","Colesterol HDL","Colesterol LDL","Peso","Circunfernecia cintura","Circunferencia cadera","P Arterial Sistolica/Diastolica" };
        datoaconsultar.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, variables));

        consultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //veo la seleccion del usuario
                String selec=datoaconsultar.getSelectedItem().toString();
                datos=respuesta( AgregarDato.obtenerindice(selec),usuario);//obtengo el indice con una funcion preexistente, hago la busqueda en la base
                if(ban==false) {
                    if (datos.isEmpty()) {//si no hay elementos que presentar, no se muestra el dialogo
                        Toast.makeText(getApplicationContext(), "No hay datos por presentar", Toast.LENGTH_SHORT).show();
                    } else {//por el contrario, se muestra el dialogo con todos los resultados
                        dialog.show(getSupportFragmentManager(), "dialogo");
                    }
                }
            }
        });
        grafico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //veo la seleccion del usuario
                String selec=datoaconsultar.getSelectedItem().toString();
                datos=respuesta( AgregarDato.obtenerindice(selec),usuario);
                if(ban==false) {
                    if (datos.isEmpty()) {//si no hay elementos que presentar, no se muestra el dialogo
                        Toast.makeText(getApplicationContext(), "No hay datos por presentar", Toast.LENGTH_SHORT).show();
                    } else {//por el contrario, se muestra el dialogo con todos los resultados
                        Intent go = new Intent(Consulta.this, Grafica.class);
                        startActivity(go);
                    }
                }

            }
        });
    }

//descargo los datos requeridos por el usuario desded la base de datos y devuelvo un arraylist
    Conectar contacto = new Conectar();
    public ArrayList<String> respuesta(Integer i, String u){
        //conexion y descarga de datos
        String orden ="select * from Variables_db WHERE Tipo='"+i+"' AND "+"Usuario='"+u+"'";
        ArrayList<String> ans = new ArrayList<>();
        try {
            Statement pedir = contacto.conectarabase().createStatement();
            ResultSet res=null;
            res = pedir.executeQuery(orden);
            // res.next();
            while (res.next()) {
                ans.add(res.getString("Fecha")+"     "+res.getString("Valor"));
            }
            res.close();
        }catch (Exception e) {
            Toast.makeText(this,"Problema de Red. Intentalo luego.", Toast.LENGTH_SHORT).show();
            ban=true;
        }
        return ans;
    }

    //boton fisico
    @Override
    public void onBackPressed() {//al presionarlo regresa al menu principal, solo si no esta contando pasos, obligando que utilicen el btn de  la app regresar
            Intent menu = new Intent(Consulta.this,Menu.class);
            startActivity(menu);
            finish();
    }
}

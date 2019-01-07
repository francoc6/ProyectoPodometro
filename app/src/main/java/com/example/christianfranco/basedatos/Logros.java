package com.example.christianfranco.basedatos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Logros extends AppCompatActivity {

    private TextView mLoadingText, posicion;
    public static ImageView copa1, copa2, copa3, copa4;

    private int pasos = 0;

    private Handler mHandler = new Handler();

    SharedPreferences usuariognr;//lo uso para obtener el usuario almacenado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logros);

        usuariognr = getSharedPreferences("Guardarusuario", MODE_PRIVATE);//instancio el objeto para obtener usuario
        final String usuario = usuariognr.getString("usuario", "vacio");

        posicion = (TextView) findViewById(R.id.posicion);
        copa1 = (ImageView) findViewById(R.id.copa1);
        copa2 = (ImageView) findViewById(R.id.copa2);
        copa3 = (ImageView) findViewById(R.id.copa3);
        copa4 = (ImageView) findViewById(R.id.copa4);

        posicion.setText("Usted esta en el puesto: ");

        pasos = obtenerpasos(usuario);//obtengo todos los pasos que ha realizado el usuario
        int mayor = compara(usuario);
        if (pasos == mayor) {
            posicion.setText("Estas en primer lugar. No te descuides!");
        } else {
            posicion.setText("Te faltan " + (mayor - pasos) + " pasos para estar en primer  lugar");
        }


        Retos.retos(pasos);//funcion para encender los trofeos
    }

    //metodo para obtener los pasos totales realizados ingresados a la base de datos
    Conectar contacto = new Conectar();

    public Integer obtenerpasos(String u) {
        //conexion y descarga de datos
        String orden = "select Pasos from TotalPasos_db WHERE Usuario='" + u + "'";
        int ans = 0;
        try {
            Statement pedir = contacto.conectarabase().createStatement();
            ResultSet res = null;
            res = pedir.executeQuery(orden);
            res.next();
            ans = Integer.valueOf(res.getString("Pasos"));
            res.close();
        } catch (Exception e) {
            Toast.makeText(this, "Error en la red.", Toast.LENGTH_SHORT).show();
            Intent go = new Intent(Logros.this, Menu.class);
            startActivity(go);
            finish();
        }
        return ans;
    }



    //boton fisico
    @Override
    public void onBackPressed() {//al presionarlo regresa al menu principal, solo si no esta contando pasos, obligando que utilicen el btn de  la app regresar
        Intent menu = new Intent(Logros.this, Menu.class);
        startActivity(menu);
        finish();
    }


    //obtengo todos los pasos de la tabla
    public ArrayList<Integer> vtotal() {
        //conexion y descarga de datos
        String orden = "select Pasos from TotalPasos_db";
        ArrayList<Integer> ans = new ArrayList<>();
        try {
            Statement pedir = contacto.conectarabase().createStatement();
            ResultSet res = null;
            res = pedir.executeQuery(orden);
            // res.next();
            while (res.next()) {
                ans.add(Integer.valueOf(res.getString("Pasos")));
            }
            res.close();
        } catch (Exception e) {
            Toast.makeText(this, "Problema de Red. Intentalo luego.", Toast.LENGTH_SHORT).show();
        }
        return ans;
    }


    public Integer compara(String u) {
        ArrayList<Integer> t = vtotal();
        int temp = 0;
        for (int y = 0; y < t.size(); y++) {
            if (t.get(y) >= temp) {
                temp = t.get(y);
            }
        }
        return temp;
    }
}

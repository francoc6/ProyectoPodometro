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

    private ProgressBar mProgressBar1,mProgressBar2;
    private TextView mLoadingText;
    public static ImageView copa1,copa2,copa3,copa4;

    private int mProgressStatus = 0;
    private int pasos=0;

    private Handler mHandler = new Handler();

    SharedPreferences usuariognr;//lo uso para obtener el usuario almacenado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logros);

        usuariognr = getSharedPreferences("Guardarusuario",MODE_PRIVATE);//instancio el objeto para obtener usuario
        final String usuario =usuariognr.getString("usuario","vacio");

        mProgressBar1 = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        copa1=(ImageView) findViewById(R.id.copa1);
        copa2=(ImageView)findViewById(R.id.copa2);
        copa3=(ImageView)findViewById(R.id.copa3);
        copa4=(ImageView)findViewById(R.id.copa4);

    pasos=obtenerpasos(usuario);//obtengo todos los pasos que ha realizado el usuario
        Retos.retos(pasos);//funcion para encender los trofeos
    }

    //metodo para obtener los pasos totales realizados ingresados a la base de datos
    Conectar contacto = new Conectar();
    public  Integer obtenerpasos(String u){
        Integer total=0;
        //conexion y descarga de datos
        String orden ="select Pasos from ACTIVIDAD WHERE Usuario='"+u+"'";
        ArrayList<String> ans = new ArrayList<>();
        try {
            Statement pedir = contacto.conectarabase().createStatement();
            ResultSet res=null;
            res = pedir.executeQuery(orden);
            // res.next();
            while (res.next()) {
                ans.add(res.getString("Pasos"));
            }
            res.close();
        }catch (Exception e) {
            Toast.makeText(this, "Error en la red.", Toast.LENGTH_SHORT).show();
            Intent go = new Intent(Logros.this,Menu.class);
            startActivity(go);
            finish();
        }
        ans.size();

        for(int i=0;i<ans.size();i++){
            total+=Integer.valueOf(ans.get(i));
        }
        return total;
    }

    //boton fisico
    @Override
    public void onBackPressed() {//al presionarlo regresa al menu principal, solo si no esta contando pasos, obligando que utilicen el btn de  la app regresar
        Intent menu = new Intent(Logros.this,Menu.class);
        startActivity(menu);
        finish();
    }
}

package com.example.christianfranco.basedatos;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Status extends AppCompatActivity {
    TextView Psangre,Pazucar,Ppresion,Tsangre,Tazucar,Tpresion;
    SharedPreferences usuariognr;//lo uso para obtener el usuario almacenado


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        usuariognr = getSharedPreferences("Guardarusuario", MODE_PRIVATE);//instancio el objeto para obtener usuario
        final String usuario = usuariognr.getString("usuario", "vacio");

        Psangre=(TextView)findViewById(R.id.Psangre);
        Ppresion=(TextView)findViewById(R.id.Ppresion);
        Pazucar=(TextView)findViewById(R.id.Pazucar);

        Tsangre=(TextView)findViewById(R.id.Tsangre);
        Tpresion=(TextView)findViewById(R.id.Tpresion);
        Tazucar=(TextView)findViewById(R.id.Tazucar);
        obtenerdatos(usuario);
    }


//obtengo los ultimos valores de las variables del usuario
    Conectar conectar = new Conectar();
    public void obtenerdatos(String u){
        ArrayList<String> resul=new ArrayList<>();
        ArrayList<String> temp=new ArrayList<>();
        try {
            Statement pedir = conectar.conectarabase().createStatement();
            ResultSet res = null;
            for (int x=1;x<4;x++){
                res = pedir.executeQuery("select Valor from Variables_db where Usuario='"+u+"' AND Id='"+x+"'");
                while (res.next()) {
                    temp.add(res.getString("Valor"));
                }
                if(temp.size()==0){
                    resul.add("No hay Entrada");
                }else{
                   // resul.add(res.getString(res.getFetchSize()));
                    resul.add(temp.get(temp.size()-1));
                }
                temp.clear();
            }
            res.close();
            Psangre.setText(resul.get(0));
            Ppresion.setText(resul.get(1));
            Pazucar.setText(resul.get(2));
        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}

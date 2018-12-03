package com.example.christianfranco.basedatos;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.christianfranco.basedatos.DialogPre.DialogIni;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class Status extends AppCompatActivity {
    TextView PGR,PG,Ppresion,Ppeso,TGR,TG,Tpresion,Tpeso;
    SharedPreferences usuariognr;//lo uso para obtener el usuario almacenado

    ArrayList<String> res = new ArrayList<String>();

    public static String respuestaG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        usuariognr = getSharedPreferences("Guardarusuario", MODE_PRIVATE);//instancio el objeto para obtener usuario
        final String usuario = usuariognr.getString("usuario", "vacio");

        final DialogIni dialog = new DialogIni();

        PGR=(TextView)findViewById(R.id.PGR);
        Ppresion=(TextView)findViewById(R.id.Ppresion);
        PG=(TextView)findViewById(R.id.PG);
        Ppeso=(TextView)findViewById(R.id.Ppeso);

        TGR=(TextView)findViewById(R.id.TGR);
        Tpresion=(TextView)findViewById(R.id.Tpresion);
        TG=(TextView)findViewById(R.id.TG);
        Tpeso=(TextView)findViewById(R.id.Tpeso);

        res=obtenerdatos(usuario);

        if (!(res.get(0).equals("Sin Registro"))){
            if (Integer.valueOf(res.get(0)) > 200) {//si es mayor a 200 es DIABETES
                Integer.valueOf(res.get(0));
                TG.setBackgroundResource(R.color.ROJO);
                respuestaG="Diabetes";
            } else if (144 < Integer.valueOf(res.get(0)) && Integer.valueOf(res.get(0)) < 149) {//entre 144 y 149 es PREDIABETES
                TG.setBackgroundResource(R.color.AMARILLO);
                respuestaG="PRE-DIABETES";
            } else {//Normal
                TG.setBackgroundResource(R.color.VERDE);
                respuestaG="Normal";
            }
        }

        TG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show(getSupportFragmentManager(), "dialogo");
            }
        });


    }


//obtengo los ultimos valores de las variables del usuario
    Conectar conectar = new Conectar();
    public  ArrayList <String> obtenerdatos(String u){
        ArrayList<String> resul=new ArrayList<>();
        ArrayList<String> temp=new ArrayList<>();
        try {
            Statement pedir = conectar.conectarabase().createStatement();
            ResultSet res = null;
            for (int x=1;x<5;x++){
                res = pedir.executeQuery("select Valor from Variables_db where Usuario='"+u+"' AND Id='"+x+"'");
                while (res.next()) {
                    temp.add(res.getString("Valor"));
                }
                if(temp.size()==0){
                    resul.add("Sin Registro");
                }else{
                   // resul.add(res.getString(res.getFetchSize()));
                    resul.add(temp.get(temp.size()-1));
                }
                temp.clear();
            }
            res.close();
            PG.setText(resul.get(0));
            PGR.setText(resul.get(1));
            Ppresion.setText(resul.get(2));
            Ppeso.setText(resul.get(3));
        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return resul;
    }
    public void referencias (){

    }
}

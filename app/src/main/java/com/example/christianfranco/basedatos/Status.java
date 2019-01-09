package com.example.christianfranco.basedatos;

import android.app.Dialog;
import android.content.Intent;
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
    //TextView PGR,PG,Ppresion,Ppeso,TGR,TG,Tpresion,Tpeso;
    TextView GT,HT,CT,TT,COLT,COLLT,PEST,CIRT,CIRCT,PAT,IMT;
    TextView GR,HR,CR,TR,COLR,COLLR,PESR,CIRR,CIRCR,PAR,IMR;

    SharedPreferences usuariognr;//lo uso para obtener el usuario almacenado
    Boolean ban=false;

    ArrayList<String> res = new ArrayList<String>();

    public static String respuestaG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        usuariognr = getSharedPreferences("Guardarusuario", MODE_PRIVATE);//instancio el objeto para obtener usuario
        final String usuario = usuariognr.getString("usuario", "vacio");

        final DialogIni dialog = new DialogIni();


        GT=(TextView)findViewById(R.id.GT);
        HT=(TextView)findViewById(R.id.HT);
        CT=(TextView)findViewById(R.id.CT);
        TT=(TextView)findViewById(R.id.TT);
        COLT=(TextView)findViewById(R.id.COLT);
        COLLT=(TextView)findViewById(R.id.COLLT);
        PEST=(TextView)findViewById(R.id.PEST);
        CIRT=(TextView)findViewById(R.id.CIRT);
        CIRCT=(TextView)findViewById(R.id.CIRCT);
        PAT=(TextView)findViewById(R.id.PAT);
        IMT=(TextView)findViewById(R.id.IMT);

        GR=(TextView)findViewById(R.id.GR);
        HR=(TextView)findViewById(R.id.HR);
        CR=(TextView)findViewById(R.id.CR);
        TR=(TextView)findViewById(R.id.TR);
        COLR=(TextView)findViewById(R.id.COLR);
        COLLR=(TextView)findViewById(R.id.COLLR);
        PESR=(TextView)findViewById(R.id.PESR);
        CIRR=(TextView)findViewById(R.id.CIRR);
        CIRCR=(TextView)findViewById(R.id.CIRCR);
        PAR=(TextView)findViewById(R.id.PAR);
        IMR=(TextView)findViewById(R.id.IMR);

        res=obtenerdatos(usuario);
        if(ban==false) {
            color();
        }

        GT.setOnClickListener(new View.OnClickListener() {
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
            for (int x=1;x<11;x++){//ya que hay 10 variables
                res = pedir.executeQuery("select Valor from Variables_db where Usuario='"+u+"' AND Tipo='"+x+"'");
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
            GR.setText(resul.get(0));
            HR.setText(resul.get(1));
            CR.setText(resul.get(2));
            TR.setText(resul.get(3));
            COLR.setText(resul.get(4));
            COLLR.setText(resul.get(5));
            PESR.setText(resul.get(6));
            CIRR.setText(resul.get(7));
            CIRCR.setText(resul.get(8));
            PAR.setText(resul.get(9));

            res=pedir.executeQuery("select Talla from RegistroUsuarios_db where Usuario='"+u+"'");
            res.next();
            Float talla=Float.valueOf(res.getString("Talla"));
            res.close();
            Float peso=Float.valueOf(resul.get(6));
            Float re=peso/(talla*talla);
            if(resul.get(6).equals("Sin registro")){
                IMR.setText("No se ha ingresado peso");
            }else{
                IMR.setText(re.toString());
            }

        } catch (Exception e) {
            ban=true;
            Toast.makeText(getApplicationContext(),"Error de red.", Toast.LENGTH_SHORT).show();
            Intent go= new Intent(Status.this, Menu.class);
            startActivity(go);
            finish();
        }
        return resul;
    }

    //comparar los datos del usuario con referencias       FALTAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    public void referencias (){

    }


//seteo el color del background y el mensaje del dialogo
    public void color (){
        if (!(res.get(0).equals("Sin Registro"))){
            if (Float.valueOf(res.get(0)) > 200) {//si es mayor a 200 es DIABETES
                GT.setBackgroundResource(R.color.ROJO);
                respuestaG="Diabetes";
            } else if (144 < Float.valueOf(res.get(0)) && Integer.valueOf(res.get(0)) < 149) {//entre 144 y 149 es PREDIABETES
                GT.setBackgroundResource(R.color.AMARILLO);
                respuestaG="PRE-DIABETES";
            } else {//Normal
                GT.setBackgroundResource(R.color.VERDE);
                respuestaG="Normal";
            }
        }
    }


    //boton fisico
    @Override
    public void onBackPressed() {//al presionarlo regresa al menu principal, solo si no esta contando pasos, obligando que utilicen el btn de  la app regresar
        Intent menu = new Intent(Status.this,Menu.class);
        startActivity(menu);
        finish();
    }
}

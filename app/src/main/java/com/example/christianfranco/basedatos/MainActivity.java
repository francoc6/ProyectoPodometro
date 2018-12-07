package com.example.christianfranco.basedatos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    EditText editUSR, editPSW;
    TextView addusr;
    Button btnIN;

    SharedPreferences sp,keepusr;//mantener logeado, y guardar usuario
    SharedPreferences usuariognr;
    Context context =this;
    String usr="";

    java.util.Calendar calendarNow = new GregorianCalendar(TimeZone.getTimeZone("America/Guayaquil"));
    String hora= calendarNow.getTime().toString();
    String[] parts = hora.split(" ");//Thu Dec 06 21:06:21 GMT-05:00 2018


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usuariognr = getSharedPreferences("Guardarusuario", MODE_PRIVATE);//instancio el objeto para obtener usuario
        final String usuario = usuariognr.getString("usuario", "vacio");

        editUSR = (EditText) findViewById(R.id.edtUSR);
        editPSW = (EditText) findViewById(R.id.editPSW);
        btnIN = (Button) findViewById(R.id.btnIN);
        addusr = (TextView) findViewById(R.id.registarse);
        sp = getSharedPreferences("logged",MODE_PRIVATE);//varia para mantenerse logeado

        if (sp.getBoolean("logged", false)) {//este metodo revisa si ya esta logeado
            registrodeentrada(usuario, parts[2]+"/"+parts[1]+"/"+parts[5],parts[3]);
            iraprincipal();
            finish();
            Toast.makeText(getApplicationContext(), hora, Toast.LENGTH_SHORT).show();
        }

        btnIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (ingreso(editUSR.getText().toString(), editPSW.getText().toString())) {
                        usr=editUSR.getText().toString();
                        sp.edit().putBoolean("logged", true).apply();//cambia el valor a que ya esta logueado
                        registrodeentrada(editUSR.getText().toString(), parts[2]+"/"+parts[1]+"/"+parts[5],parts[3]);
                        iraprincipal();
                        guardarusr();//solo se guarda cundo se inicia sesion
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "CREDENCIALES INCORRECTAS", Toast.LENGTH_SHORT).show();
                        editUSR.setText("");
                        editPSW.setText("");
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "REVISA TU CONEXION", Toast.LENGTH_SHORT).show();
                }
            }
        });
        addusr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newusr = new Intent(MainActivity.this, Registro.class);
                startActivity(newusr);
                finish();//evito que regresen al presionar el boton del celular
            }
        });
    }

    //ir a menu principal
    public void iraprincipal() {

       // Intent menuprin = new Intent(MainActivity.this, MenuuPrincipal.class); //menu solo con botones
        Intent menuprin = new Intent(MainActivity.this, Menu.class);//nuevo menu con imagenes
        startActivity(menuprin);

    }

    Conectar contacto = new Conectar();
    //descargar lista para comprobar usuarios y contraseña
    public boolean ingreso(String u, String p) {
        List<String> userpass= new ArrayList<>();
        try {
            Statement pedir = contacto.conectarabase().createStatement();
            ResultSet res = pedir.executeQuery("select * from RegistroUsuarios_db");
            while (res.next()) {
                userpass.add(res.getString("Usuario")+" "+res.getString("Pass"));
            }
            res.close();
        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return comprobar(u, p, userpass);
    }

    //agrego a la base de datos el ingreso
    public void registrodeentrada(String u, String f,String h){
        String orden ="insert into Ingreso_DB values(?,?,?)";
        try {
            PreparedStatement pedir = contacto.conectarabase().prepareStatement(orden);
            pedir.setString(1,u);
            pedir.setString(2,f);
            pedir.setString(3,h);
            pedir.executeUpdate();
            Toast.makeText(getApplicationContext(), "Se ha registrado ingreso a la base", Toast.LENGTH_SHORT).show();
            pedir.close();//cierro la conexion
        }catch (SQLException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //metodo para guardar usuario
    public void guardarusr(){
        keepusr = getSharedPreferences("Guardarusuario",context.MODE_PRIVATE);
        SharedPreferences.Editor editor = keepusr.edit();
        editor.putString("usuario",usr);//guardo el usuario con el que estoy trabajando
        editor.commit();
    }

    //compruebo las credenciales con las de la base
    public boolean comprobar (String u,String p, List<String> usuarios){//funcion que realiza el logeo
        for (int i=0; i<usuarios.size(); ++i){
            String[] parts =usuarios.get(i).split(" ");//para tomar solo la palabra y no la unidad lo llamo con parts[0]
            if (parts[0].equals(u)&&parts[1].equals(p)){
                return true;
            }
        }
        return false;
    }

}
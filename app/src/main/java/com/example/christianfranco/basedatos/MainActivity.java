package com.example.christianfranco.basedatos;

import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText editUSR, editPSW;
    TextView addusr;
    Button btnIN;
    SharedPreferences sp;//mantener logeado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editUSR = (EditText) findViewById(R.id.edtUSR);
        editPSW = (EditText) findViewById(R.id.editPSW);
        btnIN = (Button) findViewById(R.id.btnIN);
        addusr = (TextView) findViewById(R.id.registarse);
        sp = getSharedPreferences("login",MODE_PRIVATE);

        if(sp.getBoolean("login",false)){//este metodo revisa si ya esta logueado
            iraprincipal();
            finish();
        }

        btnIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ingreso(editUSR.getText().toString(),editPSW.getText().toString())){
                    sp.edit().putBoolean("login",true).apply();
                    iraprincipal();
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "CREDENCIALES INCORRECTAS", Toast.LENGTH_SHORT).show();
                    editUSR.setText("");
                    editPSW.setText("");
                }
            }
        });
        addusr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newusr = new Intent(MainActivity.this, Registro.class);
                startActivity(newusr);
                finish();//evito que regresen al presionar el boto del celular
            }
        });
    }


    //ir a menu principal
    public void iraprincipal(){
        Intent menuprin = new Intent(MainActivity.this, MenuPrincipal.class);
        startActivity(menuprin);
    }


    //metodo para hacer la conexion a la base de datos
    Conectar contacto = new Conectar();

//descargar lista para comprobar usuarios y contraseña
    public boolean ingreso(String u, String p) {
        List<Usuario> usuarios = new ArrayList<>();
        Usuario test = new Usuario();
        try {
            Statement pedir = contacto.conectarabase().createStatement();
            ResultSet res = pedir.executeQuery("select * from DatosPersonales");

            while (res.next()) {
                usuarios.add(new Usuario(res.getString("Usuario"), res.getString("Password")));
            }

        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return test.comprobar(u,p, usuarios);
    }
}
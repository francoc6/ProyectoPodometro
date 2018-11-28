package com.example.christianfranco.basedatos;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Informacion extends AppCompatActivity {
    TextView resNombre, resUsuario, resEdad, resCorreo,resGenero;
    EditText contraAnteior,contraNueva;
    Button Cambiar;
    SharedPreferences usuariognr;//lo uso para obtener el usuario almacenado
    Context context =this;
    String contravieja;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion);
        resNombre=(TextView)findViewById(R.id.resNombre);
        resUsuario=(TextView)findViewById(R.id.resUsuario);
        resCorreo=(TextView)findViewById(R.id.resCorreo);
        resEdad=(TextView)findViewById(R.id.resEdad);
        resGenero=(TextView)findViewById(R.id.resGenero);
        contraAnteior=(EditText)findViewById(R.id.contraAnterior);
        contraNueva=(EditText)findViewById(R.id.contraNueva);
        Cambiar=(Button)findViewById(R.id.btnCambiar);
        usuariognr = getSharedPreferences("Guardarusuario",context.MODE_PRIVATE);//instancio el objeto para obtener usuario
        obtenerdatos(usuariognr.getString("usuario","vacio"));//usu el usuario para buscar los datos a mostrar

        Toast.makeText(getApplicationContext(), "Usuario: "+usuariognr.getString("usuario","vacio")+" mostrado", Toast.LENGTH_SHORT).show();


        Cambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!contraAnteior.getText().toString().equals("")|!contraNueva.getText().toString().equals("")){
                    cambiarcontra(contravieja,usuariognr.getString("usuario","vacio"));
                    contraAnteior.setText("");
                    contraNueva.setText("");
                }else
                Toast.makeText(getApplicationContext(), "No deje ningun campo vacio", Toast.LENGTH_SHORT).show();
            }
        });
    }

    Conectar contacto = new Conectar();
    public void obtenerdatos(String usuario) {
        try {
            Statement pedir = contacto.conectarabase().createStatement();
            String orden ="select * from DatosPersonales WHERE  Usuario='"+usuario+"'";
            ResultSet res=null;
            res = pedir.executeQuery(orden);
            res.next();
            resNombre.setText(res.getString("Nombre"));
            resUsuario.setText(res.getString("Usuario"));
            resCorreo.setText(res.getString("Correo"));
            resEdad.setText(res.getString("Edad"));
            resGenero.setText(res.getString("Genero"));
            contravieja=res.getString("Password");
        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void cambiarcontra(String vieja,String usuario){
        if(!vieja.equals(contraAnteior.getText().toString())){
            Toast.makeText(getApplicationContext(), "La contraseña anterior no es la correcta", Toast.LENGTH_SHORT).show();
            contraAnteior.setText("");
            contraNueva.setText("");
        } else {
            String orden ="UPDATE DatosPersonales SET Password='"+contraNueva.getText().toString()+"' WHERE Usuario='"+usuario+"'";
            try {
                PreparedStatement pedir = contacto.conectarabase().prepareStatement(orden);
                pedir.executeUpdate();
                Toast.makeText(getApplicationContext(), "Se han realizado los cambios", Toast.LENGTH_SHORT).show();
            } catch (SQLException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}

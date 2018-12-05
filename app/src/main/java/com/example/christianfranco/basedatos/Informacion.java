package com.example.christianfranco.basedatos;

import android.content.Context;
import android.content.Intent;
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
    TextView resNombre,resApellido, resUsuario, resEdad, resCorreo,resGenero,resPasos;
    EditText contraAnteior,contraNueva;
    Button Cambiar;
    String contravieja;

    SharedPreferences usuariognr;//lo uso para obtener el usuario almacenado
    Context context =this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion);
        resNombre=(TextView)findViewById(R.id.resNombre);
        resApellido=(TextView)findViewById(R.id.resApellido);
        resUsuario=(TextView)findViewById(R.id.resUsuario);
        resCorreo=(TextView)findViewById(R.id.resCorreo);
        resEdad=(TextView)findViewById(R.id.resEdad);
        resGenero=(TextView)findViewById(R.id.resGenero);
        resPasos=(TextView)findViewById(R.id.resPasos);
        contraAnteior=(EditText)findViewById(R.id.contraAnterior);
        contraNueva=(EditText)findViewById(R.id.contraNueva);
        Cambiar=(Button)findViewById(R.id.btnCambiar);

        usuariognr = getSharedPreferences("Guardarusuario",context.MODE_PRIVATE);//instancio el objeto para obtener usuario
        String usuario=usuariognr.getString("usuario","vacio");
        obtenerdatos(usuario);//uso el usuario para buscar los datos a mostrar

        resPasos.setText(obtenerpasos(usuario).toString());


        Cambiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!contraAnteior.getText().toString().equals("")){
                    if(!contraNueva.getText().toString().equals("")){
                        cambiarcontra(contravieja,usuariognr.getString("usuario","vacio"));
                        contraAnteior.setText("");
                        contraNueva.setText("");
                    }else {
                        Toast.makeText(getApplicationContext(), "No ha  ingresado una contraseña nueva", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Ingrese la contraseña anterior", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    Conectar contacto = new Conectar();
    public void obtenerdatos(String usuario) {
        try {
            Statement pedir = contacto.conectarabase().createStatement();
            String orden ="select * from RegistroUsuarios_db WHERE  Usuario='"+usuario+"'";
            ResultSet res=null;
            res = pedir.executeQuery(orden);
            res.next();
            resNombre.setText(res.getString("Nombre"));
            resApellido.setText(res.getString("Apellido"));
            resUsuario.setText(res.getString("Usuario"));
            resCorreo.setText(res.getString("Correo"));
            resEdad.setText(res.getString("Edad"));
            resGenero.setText(res.getString("Genero"));
            contravieja=res.getString("Pass");
            res.close();
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
            contravieja=contraNueva.getText().toString();
            String orden ="UPDATE DatosPersonales SET Pass='"+contraNueva.getText().toString()+"' WHERE Usuario='"+usuario+"'";
            try {
                PreparedStatement pedir = contacto.conectarabase().prepareStatement(orden);
                pedir.executeUpdate();
                pedir.close();
                Toast.makeText(getApplicationContext(), "Se han realizado los cambios", Toast.LENGTH_SHORT).show();
            } catch (SQLException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

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
        }catch (SQLException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

        Intent menu = new Intent(Informacion.this,Menu.class);
        startActivity(menu);
        finish();

    }
}

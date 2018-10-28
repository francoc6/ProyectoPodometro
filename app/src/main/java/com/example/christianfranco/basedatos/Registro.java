package com.example.christianfranco.basedatos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Registro extends AppCompatActivity {



    EditText Nombre, Usuario, Correo, Edad,Pass;
    Spinner Genero;
    Button Cancelar, Registrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        Nombre = (EditText) findViewById(R.id.Nombre);
        Usuario = (EditText) findViewById(R.id.Usuario);
        Correo = (EditText) findViewById(R.id.Correo);
        Edad = (EditText) findViewById(R.id.Edad);
        Pass=(EditText)findViewById(R.id.pass);
        Genero = (Spinner) findViewById(R.id.spinnerGenero);
        Cancelar = (Button) findViewById(R.id.BotonCancelar);
        Registrar = (Button) findViewById(R.id.BotonAgregar);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.genero, android.R.layout.simple_spinner_item);
        Genero.setAdapter(adapter);

        Registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregarusuario();
            }
        });


        Cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cancelar = new Intent(Registro.this,MainActivity.class);
                startActivity(cancelar);
            }
        });

    }
    //String genselected = Genero.getSelectedItem().toString();

    Conectar contacto = new Conectar();

//valido que el usuario no este ya registrado
    public boolean apusr(String u){
        List<Usuario> usuarios = new ArrayList<>();
        Usuario test = new Usuario();
        try{
            Statement pedir =contacto.conectar().createStatement();
            ResultSet res= pedir.executeQuery("select * from DatosPersonales");

            while (res.next()){
                usuarios.add(new Usuario(res.getString("Nombre"),res.getString("Correo")));
            }

        }catch (SQLException e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return test.yaexiste(u,usuarios);
    }

    //metodo para agregar usuario
    public void agregarusuario(){
        try {
            if (apusr(Usuario.getText().toString())){
                Toast.makeText(getApplicationContext(), "EL USUARIO YA EXISTE", Toast.LENGTH_SHORT).show();
            }else{
                Statement pedir = contacto.conectar().prepareStatement("insert into DatosPersonales values(?,?,?,?,?)");
                ((PreparedStatement) pedir).setString(1,Nombre.getText().toString());
                ((PreparedStatement) pedir).setString(2,Usuario.getText().toString());
                ((PreparedStatement) pedir).setString(3,Pass.getText().toString());
                ((PreparedStatement) pedir).setString(4,Correo.getText().toString());
                ((PreparedStatement) pedir).setString(5,Edad.getText().toString());
                //((PreparedStatement) pedir).setString(1,genselected);
                ((PreparedStatement) pedir).executeUpdate();
                Toast.makeText(getApplicationContext(), "USUARIO AGREGADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
            }
        }catch (SQLException e ){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }




}

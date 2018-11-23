package com.example.christianfranco.basedatos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Registro extends AppCompatActivity {


    RadioButton radioH, radioM;
    EditText Nombre, Usuario, Correo, Edad, Pass;
    Button Cancelar, Registrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        Nombre = (EditText) findViewById(R.id.Nombre);
        Usuario = (EditText) findViewById(R.id.Usuario);
        Correo = (EditText) findViewById(R.id.Correo);
        Edad = (EditText) findViewById(R.id.Edad);
        Pass = (EditText) findViewById(R.id.pass);
        radioH = (RadioButton) findViewById(R.id.i22);
        radioM = (RadioButton) findViewById(R.id.radioM);
        Cancelar = (Button) findViewById(R.id.BotonCancelar);
        Registrar = (Button) findViewById(R.id.BotonAgregar);

        Registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //compruebo que radioboton fue seleccionado para agregarlo
                String gen = "";
                if (radioH.isChecked() == true) {
                    gen = "Hombre";
                } else if (radioM.isChecked() == true) {
                    gen = "Mujer";
                }
                agregarusuario(gen);
                if (aprobo) {
                    Intent regresar = new Intent(Registro.this, MainActivity.class);
                    startActivity(regresar);
                    finish();
                }
            }
        });
        Cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cancelar = new Intent(Registro.this, MainActivity.class);
                startActivity(cancelar);
                finish();
            }
        });
    }

    //metodo para agregar usuario
    Boolean aprobo = false;
    public void agregarusuario(String gen) {
        try {
            String res =verificarcampos(Nombre.getText().toString(),Usuario.getText().toString(),Correo.getText().toString(),Edad.getText().toString(),Pass.getText().toString());
            if (res=="") {//si los campos no estan vacios, siue a  verificar que el usuario no exista
                if (apusr(Usuario.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "EL USUARIO YA EXISTE", Toast.LENGTH_SHORT).show();
                } else {//si el usuario no existe se comunica a la base para el registro
                    aprobo = true;//para regresar al inicio si se agrego usuario
                    PreparedStatement pedir = contacto.conectarabase().prepareStatement("insert into DatosPersonales values(?,?,?,?,?,?)");
                    pedir.setString(1, Nombre.getText().toString());
                    pedir.setString(2, Usuario.getText().toString());
                    pedir.setString(3, Pass.getText().toString());
                    pedir.setString(4, Correo.getText().toString());
                    pedir.setString(5, Edad.getText().toString());
                    pedir.setString(6, gen);
                    pedir.executeUpdate();
                    Toast.makeText(getApplicationContext(), "USUARIO AGREGADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(getApplicationContext(), "Los siguientes campos estan vacios "+res+"Por favor completarlos", Toast.LENGTH_SHORT).show();
            }
        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), "Revisa tu conexion", Toast.LENGTH_SHORT).show();
        }
    }

    //metodo paravalidar que el usuario no este ya registrado
    Conectar contacto = new Conectar();


    public boolean apusr(String u) {
        List<Usuario> usuarios = new ArrayList<>();
        Usuario test = new Usuario();
        try {
            Statement pedir = contacto.conectarabase().createStatement();
            ResultSet res = pedir.executeQuery("select * from DatosPersonales");
            while (res.next()) {
                usuarios.add(new Usuario(res.getString("Usuario"), res.getString("Correo")));
            }
        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return test.yaexiste(u, usuarios);
    }
    //metodo para verificar campos vacios
    public String verificarcampos(String Nombre, String U, String Correo, String Edad, String Pass) {
        String resultado = "";
        if (Nombre.isEmpty()) { resultado += "Nombre "; }
        if (U.isEmpty()) { resultado += "Usuario "; }
        if (Correo.isEmpty()) { resultado += "Correo "; }
        if (Edad.isEmpty()) { resultado += "Edad "; }
        if (Pass.isEmpty()) { resultado += "Contraseña "; }
        return resultado;
    }

}
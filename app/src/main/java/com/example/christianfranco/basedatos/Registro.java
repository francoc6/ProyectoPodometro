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
    EditText Nombre, Apellido, Usuario, Correo, Edad, Pass, Talla;
    Button Cancelar, Registrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        Nombre = (EditText) findViewById(R.id.Nombre);
        Apellido = (EditText) findViewById(R.id.Apellido);
        Usuario = (EditText) findViewById(R.id.Usuario);
        Correo = (EditText) findViewById(R.id.Correo);
        Edad = (EditText) findViewById(R.id.Edad);
        Pass = (EditText) findViewById(R.id.pass);
        Talla= (EditText)findViewById(R.id.talla);
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
        String res = verificarcampos(Nombre.getText().toString(), Apellido.getText().toString(), Usuario.getText().toString(), Correo.getText().toString(), Edad.getText().toString(), Pass.getText().toString());
        if (res == "") {//si los campos no estan vacios, siue a  verificar que el usuario no exista
            String campos = tamanio(Nombre.getText().toString(), Apellido.getText().toString(), Usuario.getText().toString(), Correo.getText().toString(), Edad.getText().toString(), Pass.getText().toString());
            if (campos == "") {
                if (Usuario.getText().toString().contains(" ")) {
                    Toast.makeText(getApplicationContext(), "Usuario no acepta espacios", Toast.LENGTH_SHORT).show();
                } else if (apusr(Usuario.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "EL USUARIO YA EXISTE", Toast.LENGTH_SHORT).show();
                } else {//si el usuario no existe se comunica a la base para el registro
                    try {
                        aprobo = true;//para regresar al inicio si se agrego usuario
                        PreparedStatement pedir = contacto.conectarabase().prepareStatement("insert into RegistroUsuarios_db values(?,?,?,?,?,?,?)");
                        pedir.setString(1, Nombre.getText().toString());
                        pedir.setString(2, Apellido.getText().toString());
                        pedir.setString(3, Usuario.getText().toString());
                        pedir.setString(4, Pass.getText().toString());
                        pedir.setString(5, Correo.getText().toString());
                        pedir.setString(6, Edad.getText().toString());
                        pedir.setString(7, gen);
                        pedir.executeUpdate();
                        pedir.close();
                        Toast.makeText(getApplicationContext(), "USUARIO AGREGADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Revisa tu conexion", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(getApplicationContext(), campos, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Los siguientes campos estan vacios " + res + "Por favor completarlos", Toast.LENGTH_SHORT).show();
        }
        // } catch (SQLException e) {
        // Toast.makeText(getApplicationContext(), "Revisa tu conexion", Toast.LENGTH_SHORT).show();
        //  }
    }

    //metodo paravalidar que el usuario no este ya registrado
    Conectar contacto = new Conectar();
    public boolean apusr(String u) {
        List<String> usr = new ArrayList<>();
        try {
            Statement pedir = contacto.conectarabase().createStatement();
            ResultSet res = pedir.executeQuery("select * from RegistroUsuarios_db");
            while (res.next()) {
                usr.add(res.getString("Usuario"));
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Revisa tu conexion", Toast.LENGTH_SHORT).show();
        }
        // } catch (SQLException e) {
        //     Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        //  }
        return yaexiste(u, usr);
    }

    //metodo para verificar campos vacios
    public String verificarcampos(String Nombre, String A, String U, String Correo, String Edad, String Pass) {
        String resultado = "";
        if (Nombre.isEmpty()) {
            resultado += "Nombre ";
        }
        if (A.isEmpty()) {
            resultado += "Apellido ";
        }
        if (U.isEmpty()) {
            resultado += "Usuario ";
        }
        if (Correo.isEmpty()) {
            resultado += "Correo ";
        }
        if (Edad.isEmpty()) {
            resultado += "Edad ";
        }
        if (Pass.isEmpty()) {
            resultado += "Contraseña ";
        }
        return resultado;
    }

    //metodo para verficar tamaño y edad
    public String tamanio(String Nombre, String A, String U, String Correo, String Edad, String Pass) {
        String resultado = "";
        if (Nombre.length() > 40) {
            resultado += "Ingrese su nombre correctamente.-";
        }
        if (A.length() > 40) {
            resultado += "Ingrese su apellido correctamente.-";
        }
        if (U.length() > 20) {
            resultado += "Elija un usuario mas corto.-";
        }
        if (Correo.length() > 35) {
            resultado += "Ingrese su correo correctamente.-";
        }
        if (Integer.valueOf(Edad) < 10 | Integer.valueOf(Edad) > 90) {
            resultado += "Ingrese edad entre 10 y 90.-";
        }
        if (Pass.length() > 15) {
            resultado += "Ingrese una contraseña mas corta.-";
        }
        return resultado;
    }

    //boton fisico
    @Override
    public void onBackPressed() {//al presionarlo regresa al menu principal, solo si no esta contando pasos, obligando que utilicen el btn de  la app regresar
        Intent go = new Intent(Registro.this, MainActivity.class);
        startActivity(go);
        finish();
    }

    //metodo para comprobar si el usuario existe
    public boolean yaexiste(String u, List<String> usuarios) {
        for (int i = 0; i < usuarios.size(); ++i) {
            if (usuarios.get(i).equals(u)) {
                return true;
            }
        }
        return false;
    }


}
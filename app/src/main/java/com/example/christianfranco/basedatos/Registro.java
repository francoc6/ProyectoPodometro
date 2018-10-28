package com.example.christianfranco.basedatos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class Registro extends AppCompatActivity {



    EditText Nombre, Usuario, Correo, Edad;
    Spinner Genero;
    Button Cancelar, Registrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        Nombre = (EditText)findViewById(R.id.Nombre);
        Usuario = (EditText)findViewById(R.id.Usuario);
        Correo = (EditText)findViewById(R.id.Correo);
        Edad = (EditText)findViewById(R.id.Edad);
        Genero=(Spinner)findViewById(R.id.spinnerGenero);
        Cancelar =(Button)findViewById(R.id.BotonCancelar);
        Registrar=(Button)findViewById(R.id.BotonAgregar);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.genero, android.R.layout.simple_spinner_item);
        Genero.setAdapter(adapter);


    }
}

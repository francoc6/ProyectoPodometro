package com.example.christianfranco.basedatos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class AgregarDato extends AppCompatActivity {
    Button agregar, cancelar;
    Spinner opciones;
    TextView dato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_dato);
        agregar = (Button) findViewById(R.id.AgregarBtn);
        cancelar = (Button) findViewById(R.id.Cancelaradd);
        opciones = (Spinner) findViewById(R.id.opcionspinner);
        String[] variables = {"Azucar(unidad):", "Peso(lb):", "Sangre(ml):", "algo", "algoo"};
        opciones.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, variables));

    }
}

package com.example.christianfranco.basedatos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class Consulta extends AppCompatActivity {

    Spinner datoaconsultar;
    Button consultar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);

        datoaconsultar = (Spinner) findViewById(R.id.spinnerconsulta);
        String[] variables = {"Azucar:", "Peso:", "Sangre:", "algo: ", "algoo: "};
        datoaconsultar.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, variables));


        consultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent res= new Intent(Consulta.this,DialogResConsulta.class);
                startActivity(res);
            }
        });

    }
}

package com.example.christianfranco.basedatos;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.example.christianfranco.basedatos.ContadordePasos.IntSerBack;

public class MenuuPrincipal extends AppCompatActivity {
    Button btnagregar;
    Button btnConsulta;
    Button btnPasos;
    Button btnLogOut;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menuu_principal);

        //inicio el servicio de background
        Intent intentservice = new Intent(this, IntSerBack.class);//inicio el servicio
        startService(intentservice);

        sp = getSharedPreferences("logged", MODE_PRIVATE);
        btnLogOut = (Button) findViewById(R.id.btnLO);
        btnPasos = (Button) findViewById(R.id.btnPasos);
        btnConsulta = (Button) findViewById(R.id.btnConsulta);
        btnagregar = (Button) findViewById(R.id.btnagregar);



        btnagregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add = new Intent(MenuuPrincipal.this,AgregarDato.class);
                startActivity(add);
            }
        });

        btnConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ver = new Intent(MenuuPrincipal.this,Consulta.class);
                startActivity(ver);
            }
        });


        btnPasos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent verdatos = new Intent(MenuuPrincipal.this, Actividad.class);
                startActivity(verdatos);
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.edit().putBoolean("logged", false).apply();
                Intent salir = new Intent(MenuuPrincipal.this, MainActivity.class);
                startActivity(salir);
                finish();//evito que regresen al presionar el boto del celular
            }
        });
    }
}
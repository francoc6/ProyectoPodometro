package com.example.christianfranco.basedatos;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import com.example.christianfranco.basedatos.ContadordePasos.IntSerBack;

public class Actividad extends AppCompatActivity {

    public static TextView TvSteps;
    private static final String TEXT_NUM_STEPS = "Numero de pasos  realizados: ";
    Button BtnStart, BtnStop;
    Chronometer simpleChronometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad);
        TvSteps = (TextView) findViewById(R.id.tv_steps);
        BtnStart = (Button) findViewById(R.id.btn_start);
        BtnStop = (Button) findViewById(R.id.btn_stop);
        TvSteps.setText(TEXT_NUM_STEPS + IntSerBack.getNumSteps());//obtengo los pasos dados, para que aparezca al iniciar
        simpleChronometer = (Chronometer) findViewById(R.id.simpleChronometer);
        final Intent intentservice = new Intent(this, IntSerBack.class);//inicio el servicio
        BtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                simpleChronometer.start();
                simpleChronometer.setFormat("%s");
                simpleChronometer.setBase(SystemClock.elapsedRealtime());
                IntSerBack.start();
                startService(intentservice);
            }
        });
        BtnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                simpleChronometer.stop();
                simpleChronometer.setBase(SystemClock.elapsedRealtime());
                IntSerBack.detener();
                stopService(intentservice);
            }
        });
    }
}

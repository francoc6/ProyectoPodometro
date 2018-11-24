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
import com.example.christianfranco.basedatos.DialogPre.DialogIni;

public class Actividad extends AppCompatActivity {
    public static TextView TvSteps;
    private static final String TEXT_NUM_STEPS = "Numero de pasos  realizados: ";
    Button BtnStart, BtnPausa, BtnStop;
    Chronometer simpleChronometer;

    public static boolean banderapausa;
    private long tiempopausa;
    public static boolean yasehizo = false, pausado=false;//para ejecutar formulario-el pausado es para desactivar botones

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad);
        TvSteps = (TextView) findViewById(R.id.tv_steps);
        BtnStart = (Button) findViewById(R.id.btn_start);
        BtnPausa = (Button) findViewById(R.id.Pausa);
        BtnStop = (Button) findViewById(R.id.btn_stop);
        TvSteps.setText(TEXT_NUM_STEPS + IntSerBack.getNumSteps());//obtengo los pasos dados, para que aparezca al iniciar
        simpleChronometer = (Chronometer) findViewById(R.id.simpleChronometer);
        final Intent intentservice = new Intent(this, IntSerBack.class);//inicio el servicio

        BtnPausa.setEnabled(false);//para que solo se pueda presionar empezar
        BtnStop.setEnabled(false);

        final DialogIni dialog = new DialogIni();

        BtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //formulario inicial
                if (yasehizo == false) {
                    dialog.show(getSupportFragmentManager(), "Mi dialogo");
                }
                //servicio y cronometro
                IntSerBack.start();
                startService(intentservice);
                simpleChronometer.setFormat("%s");
                start();
                //botones
                BtnStart.setEnabled(false);
                BtnPausa.setEnabled(true);
                BtnStop.setEnabled(true);
            }
        });

        BtnPausa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pausa();
                //botones
                BtnPausa.setEnabled(false);//desactivo boton
                BtnStart.setEnabled(true);
            }
        });

        BtnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                detener();
                IntSerBack.detener();
                stopService(intentservice);
                dialog.show(getSupportFragmentManager(), "Mi dialogo");
                //botones
                BtnPausa.setEnabled(false);
                BtnStop.setEnabled(false);
                BtnStart.setEnabled(true);
            }
        });
    }

    public void start() {
        if (!banderapausa) {
            simpleChronometer.setBase(SystemClock.elapsedRealtime() - tiempopausa);
            simpleChronometer.start();
            banderapausa = true;
        }
    }
    public void pausa() {
        if (banderapausa) {
            simpleChronometer.stop();
            banderapausa = false;
            tiempopausa = SystemClock.elapsedRealtime() - simpleChronometer.getBase();
        }
    }
    public void detener() {
        simpleChronometer.stop();
        tiempopausa = 0;
        simpleChronometer.setBase(SystemClock.elapsedRealtime());
        banderapausa = false;
    }
}

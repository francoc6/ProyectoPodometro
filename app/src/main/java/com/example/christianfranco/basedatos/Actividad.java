package com.example.christianfranco.basedatos;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.christianfranco.basedatos.ContadordePasos.IntSerBack;
import com.example.christianfranco.basedatos.DialogPre.DialogIni;

public class Actividad extends AppCompatActivity {
    public static TextView TvSteps;
    private static final String TEXT_NUM_STEPS = "Numero de pasos  realizados: ",Datoaenviar="";
    Button BtnStart, BtnPausa, BtnStop,BtnRegresar;
    Chronometer simpleChronometer;
    private long tiempopausa,tiempofinal;
    public static boolean yasehizo = false, banderapausa;//para ejecutar formulario

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad);
        TvSteps = (TextView) findViewById(R.id.tv_steps);
        BtnStart = (Button) findViewById(R.id.btn_start);
        BtnPausa = (Button) findViewById(R.id.Pausa);
        BtnStop = (Button) findViewById(R.id.btn_stop);
        BtnRegresar=(Button)findViewById(R.id.btnRegresar);
        TvSteps.setText(TEXT_NUM_STEPS + IntSerBack.getNumSteps());//obtengo los pasos dados, para que aparezca al iniciar
        simpleChronometer = (Chronometer) findViewById(R.id.simpleChronometer);

        final DialogIni dialog = new DialogIni();
        final Intent intentservice = new Intent(this, IntSerBack.class);//inicio el servicio

        BtnPausa.setEnabled(false);//para que solo se pueda presionar empezar
        BtnStop.setEnabled(false);

        BtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //formulario inicial
                if (yasehizo == false) { dialog.show(getSupportFragmentManager(), "Mi dialogo"); }
                //servicio
                IntSerBack.start();startService(intentservice);
                //cronometro
                simpleChronometer.setFormat("%s");start();
                //botones
                BtnStart.setEnabled(false);BtnPausa.setEnabled(true);BtnStop.setEnabled(true);BtnRegresar.setEnabled(false);
            }
        });

        BtnPausa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pausa();//cronometro
                //botones
                BtnPausa.setEnabled(false);BtnStart.setEnabled(true);BtnRegresar.setEnabled(false);
            }
        });

        BtnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                detener();//detener el reloj
                IntSerBack.detener();//detener el servicio
                stopService(intentservice);
                dialog.show(getSupportFragmentManager(), "Mi dialogo");//formulario final
                //botones
                BtnPausa.setEnabled(false);BtnStop.setEnabled(false);BtnStart.setEnabled(true);BtnRegresar.setEnabled(true);
                Toast.makeText(getApplicationContext(),obtenerhora(tiempofinal), Toast.LENGTH_SHORT).show();
            }
        });

        BtnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BtnStart.isEnabled()&&!BtnPausa.isEnabled()&&!BtnStop.isEnabled()){
                    Intent menu = new Intent(Actividad.this,Menu.class);
                    startActivity(menu);
                    finish();
                }
            }
        });

    }

    //boton fisico
    @Override
    public void onBackPressed() {//al presionarlo regresa al menu principal, solo si no esta contando pasos, obligando que utilicen el btn de  la app regresar
        if(BtnStart.isEnabled()&&!BtnPausa.isEnabled()&&!BtnStop.isEnabled()){
            Intent menu = new Intent(Actividad.this,Menu.class);
            startActivity(menu);
            finish();
        }
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
        tiempofinal=SystemClock.elapsedRealtime()-simpleChronometer.getBase();
        tiempopausa = 0;
        simpleChronometer.setBase(SystemClock.elapsedRealtime());
        banderapausa = false;
    }


    public String obtenerhora(long t){
        int resmili,resseg,resmin,reshora;
        long x;
        String res;
        if(t<3600000){//minutos, sin horas
            resmili=(int)(t%1000);
            x=t/1000;
            resseg=(int)(x%100);
            x=x/100;
            resmin=(int)(x%100);
            res="00:"+String.valueOf(resmin)+":"+String.valueOf(resseg)+":"+String.valueOf(resmili);
        }else{//ya hay horas
            resmili=(int)(t%1000);
            x=t-resmili;
            resseg=(int)(x%100);
            x=x-resseg;
            resmin=(int)(x%100);
            x=x-resmin;
            reshora=(int)(x%100);
            res=String.valueOf(reshora)+":"+String.valueOf(resmin)+":"+String.valueOf(resseg)+":"+String.valueOf(resmili);
        }
        return res;
    }


}

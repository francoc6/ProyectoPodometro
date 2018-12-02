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

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class Actividad extends AppCompatActivity {
    public static TextView TvSteps;
    private static final String TEXT_NUM_STEPS = "Numero de pasos  realizados: ",Datoaenviar="";
    Button BtnStart, BtnPausa, BtnStop,BtnRegresar;
    Chronometer simpleChronometer;
    private long tiempopausa,tiempofinal;
    public static boolean yasehizo = false, banderapausa;//para ejecutar formulario



    public static String Preguntas_I,Preguntas_F;

    Calendar calendarNow = new GregorianCalendar(TimeZone.getTimeZone("America/Guayaquil"));
    int dia =calendarNow.get(Calendar.DAY_OF_MONTH);
    int mes = calendarNow.get(Calendar.MONTH);

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
                if (yasehizo == false) {

                        dialog.show(getSupportFragmentManager(), "Mi dialogo");

                }
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
                dialog.show(getSupportFragmentManager(), "Mi dialogo");//formulario final
                Toast.makeText(getApplicationContext(),"Mes: "+ mes+"Dia: "+dia+"Tiempo: "+obtenerhora(tiempofinal)+" Pasos: "+String.valueOf(IntSerBack.getNumSteps())+" Datos: ", Toast.LENGTH_SHORT).show();
                IntSerBack.detener();//detener el servicio
                stopService(intentservice);

                //botones
                BtnPausa.setEnabled(false);BtnStop.setEnabled(false);BtnStart.setEnabled(true);BtnRegresar.setEnabled(true);

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
        String mili,seg,min,hora;
        long x;
        String res="";
        if(t<3600000){//minutos, sin horas
            resmili=(int)(t%1000);
            mili=String.valueOf(resmili);
            x=t/1000;
            resseg=(int)(x%100);
            seg=String.valueOf(resseg);
            if(resseg<10){
                seg="0"+String.valueOf(resseg);
            }
            x=x/100;
            resmin=(int)(x%100);
            min=String.valueOf(resmin);
            if(resmin<10){
                min="0"+String.valueOf(resmin);
            }
            res="00:"+min+":"+seg+":"+mili;
        }else{//ya hay horas
            resmili=(int)(t%1000);
            mili=String.valueOf(resmili);
            x=t/1000;
            resseg=(int)(x%100);
            seg=String.valueOf(resseg);
            if(resseg<10){
                seg="0"+String.valueOf(resseg);
            }
            x=x/100;
            resmin=(int)(x%100);
            min=String.valueOf(resmin);
            if(resmin<10){
                min="0"+String.valueOf(resmin);
            }
            x=x/100;
            reshora=(int)(x%100);
            hora=String.valueOf(reshora);
            if(reshora<10){
                hora="0"+String.valueOf(reshora);
            }
            res=hora+":"+min+":"+seg+":"+mili;
        }
        return res;
    }


}

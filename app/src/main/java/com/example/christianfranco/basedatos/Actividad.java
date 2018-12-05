package com.example.christianfranco.basedatos;

import android.content.Intent;
import android.content.SharedPreferences;
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
    Button BtnStart, BtnPausa, BtnStop,BtnRegresar;
    Chronometer simpleChronometer;

    private static final String TEXT_NUM_STEPS = "Numero de pasos  realizados: ";

    public static long tiempopausa,tiempofinal;

    public static boolean yasehizo = false, banderapausa;//para ejecutar formulario

    public static String Preguntas_I;


    public static int pasos;

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
        simpleChronometer.setFormat("%s");

        final Intent intentservice = new Intent(this, IntSerBack.class);//inicio el servicio

        if(yasehizo){
            BtnStart.setEnabled(false);BtnPausa.setEnabled(true);BtnStop.setEnabled(true);BtnRegresar.setEnabled(false);
            simpleChronometer.start();
            IntSerBack.start();startService(intentservice);
        }else {
            detener();//empieza detenido el reloj de pantalla
            //para que solo se pueda presionar empezar
            BtnPausa.setEnabled(false);
            BtnStop.setEnabled(false);
            IntSerBack.detener();//detener el servicio
            stopService(intentservice);
        }

        BtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //cronometro
                start();
                if (yasehizo == false) {

                    Intent preg = new Intent(Actividad.this,Preguntas.class);
                    startActivity(preg);
                    finish();
                    pasos=0;
                }else{
                    BtnPausa.setEnabled(true);
                    BtnStart.setEnabled(false);
                    //intentservice
                    IntSerBack.start();
                    startService(intentservice);
                }
            }
        });

        BtnPausa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pausa();//cronometro
                //botones
                BtnPausa.setEnabled(false);BtnStart.setEnabled(true);BtnRegresar.setEnabled(false);

                pasos=IntSerBack.getNumSteps();
                //intent service
                IntSerBack.detener();//detener el servicio
                stopService(intentservice);
            }
        });

        BtnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(banderapausa){
                    pasos=IntSerBack.getNumSteps();
                }
                //detener el reloj
                detener();
                //empiezo activity
                Intent preg = new Intent(Actividad.this,Preguntas.class);
                startActivity(preg);

                Toast.makeText(getApplicationContext(),obtenertiempo(tiempofinal)+" Pasos: "+pasos+" Datos: ", Toast.LENGTH_SHORT).show();
                //IntentService
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
        if(BtnStart.isEnabled()&&!BtnPausa.isEnabled()&&!BtnStop.isEnabled()&&BtnRegresar.isEnabled()){
            Intent menu = new Intent(Actividad.this,Menu.class);
            startActivity(menu);
            finish();
        }
    }

    //metodos para el reloj
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
        if(!banderapausa){
            tiempofinal=tiempopausa;
        }else{
            tiempofinal=SystemClock.elapsedRealtime()-simpleChronometer.getBase();//tiempo final
        }
        tiempopausa = 0;
        simpleChronometer.setBase(SystemClock.elapsedRealtime());
        banderapausa = false;
    }

    //lo transformo a un valor H:M:S:MS
    public static String obtenertiempo(long t){
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

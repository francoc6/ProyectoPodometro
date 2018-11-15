package com.example.christianfranco.basedatos.ContadordePasos;


import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.example.christianfranco.basedatos.Actividad;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class IntSerBack extends IntentService implements SensorEventListener, StepListener {

    public IntSerBack() {
        super("IntSerBack");
    }

    //parametros para el sensor
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private static boolean band = true;
    private static final String TEXT_NUM_STEPS = "Numero de pasos realizados: ";
    private static int numSteps;

    @Override
    protected void onHandleIntent(Intent intent) {
//sensorrrrrr de  contar pasossssssssssssss
        // Get an instance of the SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);
        numSteps = 0;
        sensorManager.registerListener(IntSerBack.this, accel, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /////////////////////METODOS DEL SENSOR
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
//nadddddddddddddddddddddddddddd
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    public void step(long timeNs) {
        try {
            if (band) {
                Actividad.TvSteps.setText(TEXT_NUM_STEPS + numSteps);//yo aqui muestro por pantalla los pasos, actualiza autmaticamnete lo presentado por pantalla
                numSteps++;
            } else {
                //MainActivity.TvSteps.setText(TEXT_NUM_STEPS + numSteps);//yo aqui muestro por pantalla los pasos, actualiza autmaticamnete lo presentado por pantalla
                numSteps = 0;
                this.finalize();//finalizo
            }

        } catch (Exception e) {
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public static int getNumSteps() {//accedo al contador desde la pantalla que muestra los numeros
        return numSteps;
    }

    public static void detener() {//accedo al contador desde la pantalla que muestra los numero
        band = false;
    }

    public static void start() {//accedo al contador desde la pantalla que muestra los numero
        band = true;
    }
}


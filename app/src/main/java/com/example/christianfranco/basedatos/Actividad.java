package com.example.christianfranco.basedatos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import cz.msebera.android.httpclient.Header;

import com.example.christianfranco.basedatos.ContadordePasos.IntSerBack;

import org.json.JSONObject;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Actividad extends AppCompatActivity implements LocationListener {
    public static TextView TvSteps;
    Button BtnStart, BtnPausa, BtnStop, BtnRegresar;
    Chronometer simpleChronometer;
    private static final String TEXT_NUM_STEPS = "Numero de pasos  realizados: ";
    public static long tiempopausa, tiempofinal;
    public static boolean yasehizo = false, banderapausa;//para ejecutar formulario
    public static String Preguntas_I;
    public static int pasos;

    //////////////////////////////////////////////ubicacion/////////////////////////////////////////////////////////////////
    public static String tvLongi;
    public static String tvLati;
    LocationManager locationManager;
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////clima////////////////////////////////////////////////////////////////////////
    final String APP_ID = "b13f14be7c0909550f568e788748a9b8";
    final String WEATHER_URL = "http://api.openweathermap.org/data/2.5/weather";
    public String temperatura,ciudad;
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad);
        TvSteps = (TextView) findViewById(R.id.tv_steps);
        BtnStart = (Button) findViewById(R.id.btn_start);
        BtnPausa = (Button) findViewById(R.id.Pausa);
        BtnStop = (Button) findViewById(R.id.btn_stop);
        BtnRegresar = (Button) findViewById(R.id.btnRegresar);

        TvSteps.setText(TEXT_NUM_STEPS + IntSerBack.getNumSteps());//obtengo los pasos dados, para que aparezca al iniciar

        simpleChronometer = (Chronometer) findViewById(R.id.simpleChronometer);
        simpleChronometer.setFormat("%s");

        //posicion
        CheckPermission();

        final Intent intentservice = new Intent(this, IntSerBack.class);//inicio el servicio

        if (yasehizo) {
            BtnStart.setEnabled(false);
            BtnPausa.setEnabled(true);
            BtnStop.setEnabled(true);
            BtnRegresar.setEnabled(false);
            simpleChronometer.start();
            IntSerBack.start();
            startService(intentservice);
        } else {
            detener();//empieza detenido el reloj de pantalla
            //para que solo se pueda presionar empezar
            BtnPausa.setEnabled(false);
            BtnStop.setEnabled(false);
            IntSerBack.detener();//detener el servicio
            stopService(intentservice);
        }
        prueba();//para probar conexion

        BtnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //cronometro
                start();
                if (yasehizo == false) {
                    Intent preg = new Intent(Actividad.this, Preguntas.class);
                    startActivity(preg);
                    finish();
                    pasos = 0;
                } else {
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
                BtnPausa.setEnabled(false);
                BtnStart.setEnabled(true);
                BtnRegresar.setEnabled(false);
                pasos = IntSerBack.getNumSteps();
                //intent service
                IntSerBack.detener();//detener el servicio
                stopService(intentservice);
            }
        });

        BtnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (banderapausa) {
                    pasos = IntSerBack.getNumSteps();
                }
                //detener el reloj
                detener();
                //empiezo activity
                Intent preg = new Intent(Actividad.this, Preguntas.class);
                startActivity(preg);
                //Toast.makeText(getApplicationContext(),obtenertiempo(tiempofinal)+" Pasos: "+pasos+" Datos: ", Toast.LENGTH_SHORT).show();
                //IntentService
                IntSerBack.detener();//detener el servicio
                stopService(intentservice);
                //botones
                BtnPausa.setEnabled(false);
                BtnStop.setEnabled(false);
                BtnStart.setEnabled(true);
                BtnRegresar.setEnabled(true);
            }
        });

        BtnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BtnStart.isEnabled() && !BtnPausa.isEnabled() && !BtnStop.isEnabled()) {
                    Intent menu = new Intent(Actividad.this, Menu.class);
                    startActivity(menu);
                    finish();
                    onPause();//listener del gps
                }
            }
        });
    }

    //boton fisico
    @Override
    public void onBackPressed() {//al presionarlo regresa al menu principal, solo si no esta contando pasos, obligando que utilicen el btn de  la app regresar
        if (BtnStart.isEnabled() && !BtnPausa.isEnabled() && !BtnStop.isEnabled() && BtnRegresar.isEnabled()) {
            Intent menu = new Intent(Actividad.this, Menu.class);
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
        if (!banderapausa) {
            tiempofinal = tiempopausa;
        } else {
            tiempofinal = SystemClock.elapsedRealtime() - simpleChronometer.getBase();//tiempo final
        }
        tiempopausa = 0;
        simpleChronometer.setBase(SystemClock.elapsedRealtime());
        banderapausa = false;
    }

    //lo transformo a un valor H:M:S:MS
    public static String obtenertiempo(long t) {
        int resmili, resseg, resmin, reshora;
        String mili, seg, min, hora;
        long x;
        String res = "";
        if (t < 3600000) {//minutos, sin horas
            resmili = (int) (t % 1000);
            mili = String.valueOf(resmili);
            x = t / 1000;
            resseg = (int) (x % 100);
            seg = String.valueOf(resseg);
            if (resseg < 10) {
                seg = "0" + String.valueOf(resseg);
            }
            x = x / 100;
            resmin = (int) (x % 100);
            min = String.valueOf(resmin);
            if (resmin < 10) {
                min = "0" + String.valueOf(resmin);
            }
            res = "00:" + min + ":" + seg + ":" + mili;
        } else {//ya hay horas
            resmili = (int) (t % 1000);
            mili = String.valueOf(resmili);
            x = t / 1000;
            resseg = (int) (x % 100);
            seg = String.valueOf(resseg);
            if (resseg < 10) {
                seg = "0" + String.valueOf(resseg);
            }
            x = x / 100;
            resmin = (int) (x % 100);
            min = String.valueOf(resmin);
            if (resmin < 10) {
                min = "0" + String.valueOf(resmin);
            }
            x = x / 100;
            reshora = (int) (x % 100);
            hora = String.valueOf(reshora);
            if (reshora < 10) {
                hora = "0" + String.valueOf(reshora);
            }
            res = hora + ":" + min + ":" + seg + ":" + mili;
        }
        return res;
    }

    Conectar conectar = new Conectar();

    public void prueba() {
        List<String> preg = new ArrayList<>();
        try {
            Statement pedir = conectar.conectarabase().createStatement();
            String orden = "SELECT Texto FROM Preguntas_db WHERE TIPO='Inicio'";
            ResultSet res = null;
            res = pedir.executeQuery(orden);
            // res.next();
            while (res.next()) {
                preg.add(res.getString("Texto"));
            }
            res.close();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "No hay conexion.", Toast.LENGTH_SHORT).show();
            Intent go = new Intent(Actividad.this, Menu.class);
            startActivity(go);
            finish();
        }
    }

///////////////////////////////////////////////////////////POSICION///////////////////////////////////////////////////////////////////////////////////////

    //metodo para almacenar datos en memoria del dispositivo
    public void guardatos(String Latitud, String Longitud) {
        SharedPreferences keepdata = getSharedPreferences("Ubicacion", getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = keepdata.edit();
        editor.putString("Latitud", Latitud);
        editor.putString("Longitud", Longitud);
        editor.commit();
    }

    /* Request updates at startup */
    @Override
    public void onResume() {
        super.onResume();
        getLocation();
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    public void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void CheckPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        tvLongi = String.valueOf(location.getLongitude());
        tvLati = String.valueOf(location.getLatitude());
        if(!yasehizo) {//para que solo lo haga al iniciar la pantalla
            if (location.getLongitude() != 0 && location.getLatitude() != 0) {
                guardatos(tvLati, tvLongi);
                Toast.makeText(getApplicationContext(), "Se obtuvo posicion " + tvLati + " " + tvLongi, Toast.LENGTH_SHORT).show();
                getWeatherForCurrentLocation(tvLati, tvLongi);//inicio metodo para obtener clima con la posicion
                //onDestroy();
            }
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(getApplicationContext(), "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider!" + provider, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(this);
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////CLIMA///////////////////////////////////////////////////////////////////////////////////////////////////////////

    //se utiliza la version gratuita de un api de OPENWEATHERMAP.ORG el cual solo permite 60 consultas por minuto
    //Con onLocationChanged de la posicion, se llama al metodo para que obtenga el clima y la ciudad

    //en este metodo se envia la latitud y longitud, con la key para acceder al OPENWEATHER, a letsDoSomeNetworking
    public void getWeatherForCurrentLocation(String latitud, String longitud) {
        String Longitude = longitud;
        String Latitude = latitud;
        RequestParams params = new RequestParams();
        params.put("lat", Latitude);
        params.put("lon", Longitude);
        params.put("appid", APP_ID);
        letsDoSomeNetworking(params);
    }

    //se crea instancia AsyncHttpClient el cual envia los datos a la clase WeatherDataModel y a su vez recibe el resultado de  la misma en ObtenerDatos
    public void letsDoSomeNetworking(final RequestParams params) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(WEATHER_URL, params, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] header, JSONObject response) {
               // Log.d("Clima", "Success" + response.toString());
                WeatherDataModel weatherdata = WeatherDataModel.fromJSON(response);
                ObtenerDatos(weatherdata);
            }

            public void onFailure(int statuscode, Header[] header, Throwable e, JSONObject response) {
                //Log.d("Clima", "Status code" + statuscode);
                //Log.d("Clima", "Fail" + e.toString());
                Toast.makeText(Actividad.this, "Invalid Location", Toast.LENGTH_SHORT).show();
              //  getWeatherForCurrentLocation();
            }

        });
    }

    //los asigna a las variables temperatura y ciudad
    public void ObtenerDatos(WeatherDataModel data){
        temperatura=data.getTemperature();
        ciudad=data.getCity();
        Toast.makeText(getApplicationContext(),temperatura+" y "+ciudad, Toast.LENGTH_SHORT).show();
        SharedPreferences keepdata = getSharedPreferences("Clima", getApplicationContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = keepdata.edit();
        editor.putString("Temperatura", temperatura);
        editor.putString("Ciudad", ciudad);
        editor.commit();


    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}


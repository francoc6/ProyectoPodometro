package com.example.christianfranco.basedatos;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ObtenerClima extends AsyncTask<String,Void,String> {

    @Override
    protected String doInBackground(String... direccion) {
        String result ="";
        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(direccion[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);
            int data = reader.read();
            while (data != -1){
                char current = (char) data;
                result += current;
                data = reader.read();
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONObject clima = new JSONObject(jsonObject.getString("main"));//segun ejemplo como agarra la info del archivo JSON
            double temperatura = Double.parseDouble(clima.getString("temp"));
            //String tempp = clima.getString("temp");

            //cambbiar a centigrados
            int tempCelcius = (int)(temperatura - 273.15);

            //lugar donde se toma temperatura
            String lugar = jsonObject.getString("name");
           Actividades.resLugar.setText(String.valueOf(lugar));//seteo el resultado para mostrar en la otra actividad
            //Actividades.resLugar.setText(lugar);
            Actividades.resTemperatura.setText(String.valueOf(tempCelcius));
            //Actividades.resTemperatura.setText(tempp);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.example.christianfranco.basedatos;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Actividades extends AppCompatActivity {
    static TextView resTemperatura;
    static TextView resLatitud;
    static TextView resLongitud;
    static TextView resLugar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividades);

        resTemperatura= (TextView) findViewById(R.id.resTemperatura);
        resLatitud=(TextView)findViewById(R.id.resLatitud);
        resLongitud=(TextView)findViewById(R.id.resLongitud);
        resLugar=(TextView)findViewById(R.id.resLugar);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(new Criteria(), false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);

        double latitud = location.getLatitude();
        double longitud =location.getLongitude();

        resLatitud.setText(String.valueOf(latitud));
        resLongitud.setText(String.valueOf(longitud));

        ObtenerClima task = new ObtenerClima();
       // task.execute("https://samples.openweathermap.org/data/2.5/weather?lat="+String.valueOf(latitud)+"&lon="+String.valueOf(longitud)+"&appid=b6907d289e10d714a6e88b30761fae22");
        task.execute("https://api.openweathermap.org/data/2.5/weather?lat="+String.valueOf(latitud)+"&lon="+String.valueOf(longitud)+"&appid=dfc9b847f55f036cbc3ea844c1cc9bef");
    }
}

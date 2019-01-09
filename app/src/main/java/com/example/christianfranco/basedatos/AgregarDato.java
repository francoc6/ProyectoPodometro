package com.example.christianfranco.basedatos;

//se van a agregar los datos con id segun el tipo: Glucosa 1, Hemoglobina 2,Colesterol 3,Trigliceridos 4, Colesterol HDL 5
// Colesterol LDL 6, Peso 7, Circunfernecia cintura 8, Circunferencia cadera 9, P. Arteriar  sistolica/diastolica 10

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;


public class AgregarDato extends AppCompatActivity {
    Button agregar, cancelar;
    Spinner opciones;
    EditText dato;
    Calendar calendarNow = new GregorianCalendar(TimeZone.getTimeZone("America/Guayaquil"));
    int dia = calendarNow.get(Calendar.DAY_OF_MONTH);
    int mes = 1 + calendarNow.get(Calendar.MONTH);
    int anio = calendarNow.get(Calendar.YEAR);
    String fecha = dia + "/" + mes + "/" + anio;
    SharedPreferences usuariognr;//lo uso para obtener el usuario almacenado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_dato);
        agregar = (Button) findViewById(R.id.AgregarBtn);
        cancelar = (Button) findViewById(R.id.Cancelaradd);
        dato = (EditText) findViewById(R.id.adddato);
        opciones = (Spinner) findViewById(R.id.opcionspinner);

        String[] variables = {"Glucosa-(mg/dl):", "Hemoglobina-(%):", "Colesterol-(mg/dl):", "Trigliceridos-(mg/dl):", "Colesterol HDL-(mg/dl):", "Colesterol LDL-(mg/dl):", "Peso-(Kg):", "Circunfernecia cintura-(cm):", "Circunferencia cadera-(cm):", "P Arterial Sistolica/Diastolica-(mmHg):"};

        opciones.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, variables));
        usuariognr = getSharedPreferences("Guardarusuario", MODE_PRIVATE);//instancio el objeto para obtener usuario
        final String usuario = usuariognr.getString("usuario", "vacio");

        agregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selec = opciones.getSelectedItem().toString();
                String[] parts = selec.split("-");//para tomar solo la palabra y no la unidad lo llamo con parts[0]
                if (dato.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Ingrese un valor", Toast.LENGTH_SHORT).show();
                } else if (validar(parts[0], dato.getText().toString()) != "") {
                    Toast.makeText(getApplicationContext(), validar(parts[0], dato.getText().toString()), Toast.LENGTH_SHORT).show();
                } else if (parts[0].equals("P Arterial Sistolica/Diastolica")) {
                    if (valpresion(dato.getText().toString()) != "") {
                        Toast.makeText(getApplicationContext(), valpresion(dato.getText().toString()), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    agregardato(obtenerindice(parts[0]), usuario, Float.valueOf(dato.getText().toString()).toString(), fecha);//para facilitar la graficacion
                    dato.setText("");
                }
            }
        });
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent menu = new Intent(AgregarDato.this, Menu.class);
                startActivity(menu);
                finish();
            }
        });
    }

    Conectar contacto = new Conectar();

    public void agregardato(Integer i, String u, String valor, String f) {
        String orden = "insert into Variables_db values(?,?,?,?)";
        try {
            PreparedStatement pedir = contacto.conectarabase().prepareStatement(orden);
            pedir.setInt(1, i);
            pedir.setString(2, u);
            pedir.setString(3, valor);
            pedir.setString(4, f);
            pedir.executeUpdate();
            Toast.makeText(getApplicationContext(), "Dato agregado correctamente", Toast.LENGTH_SHORT).show();
            pedir.close();//cierro la conexion
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Problemas de conexion, intentelo luego.", Toast.LENGTH_SHORT).show();
        }
    }

    //en la base no se almacenan los nombres de las variables sino un numero
    public static int obtenerindice(String d) {
        int r = 0;
        if (d.equals("Glucosa")) {
            r = 1;
        }
        if (d.equals("Hemoglobina")) {
            r = 2;
        }
        if (d.equals("Colesterol")) {
            r = 3;
        }
        if (d.equals("Trigliceridos")) {
            r = 4;
        }
        if (d.equals("Colesterol HDL")) {
            r = 5;
        }
        if (d.equals("Colesterol LDL")) {
            r = 6;
        }
        if (d.equals("Peso")) {
            r = 7;
        }
        if (d.equals("Circunfernecia cintura")) {
            r = 8;
        }
        if (d.equals("Circunferencia cadera")) {
            r = 9;
        }
        if (d.equals("P Arterial Sistolica/Diastolica")) {
            r = 10;
        }
        return r;
    }

    //boton fisico
    @Override
    public void onBackPressed() {//al presionarlo regresa al menu principal, solo si no esta contando pasos, obligando que utilicen el btn de  la app regresar
        Intent menu = new Intent(AgregarDato.this, Menu.class);
        startActivity(menu);
        finish();
    }

    //valida las entradas de acuerdo a valores referenciales proporcionados por las compañeras de licnut
    public String validar(String u, String v) {
        Float d = Float.valueOf(v);
        String res = "";
        if (u.equals("Glucosa") & (d < 10 | d > 3000)) {
            res = "Por favor ingrese un valor entre 10 y 3000";
        }
        if (u.equals("Hemoglobina") & (d < 4 | d > 16)) {
            res = "Por favor ingrese un valor entre 4 y 16";
        }
        if (u.equals("Colesterol") & (d < 10 | d > 1000)) {
            res = "Por favor ingrese un valor entre 10 y 1000";
        }
        if (u.equals("Trigliceridos") & (d < 10 | d > 1000)) {
            res = "Por favor ingrese un valor entre 10 y 1000";
        }
        if (u.equals("Colesterol HDL") & (d < 10 | d > 1000)) {
            res = "Por favor ingrese un valor entre 10 y 1000";
        }
        if (u.equals("Colesterol LDL") & (d < 10 | d > 1000)) {
            res = "Por favor ingrese un valor entre 10 y 1000";
        }
        if (u.equals("Peso") & (d < 20 | d > 500)) {
            res = "Por favor ingrese un valor entre 20 y 500";
        }
        if (u.equals("Circunfernecia cintura") & (d < 10 | d > 500)) {
            res = "Por favor ingrese un valor entre 10 y 500";
        }
        if (u.equals("Circunferencia cadera") & (d < 10 | d > 500)) {
            res = "Por favor ingrese un valor entre 10 y 500";
        }
        return res;
    }

    public String valpresion(String v) {
        String[] parts = v.split(".");
        Float x, y;
        String res = "";

        if (parts.length != 0) {
            x = Float.valueOf(parts[0]);
            y = Float.valueOf(parts[1]);
            if (x < 10 | x > 300) {
                res = "Por favor ingrese Sistolica entre 10 y 300";
            }
            if (y < 10 | y > 300) {
                res = "Por favor ingrese Diastolica entre 10 y 300";
            }
        } else {
            res = "Por favor ingrese ambos valores separados por un punto (.)";
        }
        return res;
    }

}

package com.example.christianfranco.basedatos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class CuestDiab extends AppCompatActivity {

    TextView ima;

    private RadioButton p11, p12, p13, p14;
    private RadioButton p21, p22;
    private RadioButton p31, p32;
    private RadioButton p41, p42;
    private RadioButton p51, p52;
    private RadioButton p61, p62;
    private RadioButton p71, p72, p73, p74;
    private Button Aceptar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuest_diab);

        final ImagenReferencial dialog = new ImagenReferencial();

        ima = (TextView) findViewById(R.id.imagenref);

        p11 = (RadioButton) findViewById(R.id.i11);
        p12 = (RadioButton) findViewById(R.id.i12);
        p13 = (RadioButton) findViewById(R.id.i13);
        p14 = (RadioButton) findViewById(R.id.i14);
        p21 = (RadioButton) findViewById(R.id.i21);
        p22 = (RadioButton) findViewById(R.id.i22);
        p31 = (RadioButton) findViewById(R.id.i31);
        p32 = (RadioButton) findViewById(R.id.i32);
        p41 = (RadioButton) findViewById(R.id.i41);
        p42 = (RadioButton) findViewById(R.id.i42);
        p51 = (RadioButton) findViewById(R.id.i51);
        p52 = (RadioButton) findViewById(R.id.i52);
        p61 = (RadioButton) findViewById(R.id.i61);
        p62 = (RadioButton) findViewById(R.id.i62);
        p71 = (RadioButton) findViewById(R.id.i71);
        p72 = (RadioButton) findViewById(R.id.i72);
        p73 = (RadioButton) findViewById(R.id.i73);
        p74 = (RadioButton) findViewById(R.id.i74);

        Aceptar=(Button)findViewById(R.id.btnAceptar);

        ima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show(getSupportFragmentManager(), "dialogo");
            }
        });

        Aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int res =resultados();
                if (res>5 | res==5){
                    Toast.makeText(getApplicationContext(),"Fregado", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Normal PANA", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //obtengo las respuestas de las preguntas en un string
    public int resultados() {//se lo podria hacer con un case, pero seria casi lo mismo obteniendo los id
        Integer res = 0;//creo un string que se concatenara con las respuestas
        //Pregunta 1
        if (p11.isChecked()) {
            res += 0;
        } else if (p12.isChecked()) {
            res += 1;
        } else if (p13.isChecked()) {
            res += 2;
        } else if (p14.isChecked()) {
            res += 3;
        }
        //Pregunta 2
        if (p21.isChecked()) {
            res += 1;
        } else if (p22.isChecked()) {
            res += 0;
        }
        //Pregunta 3
        if (p31.isChecked()) {
            res += 1;
        } else if (p32.isChecked()) {
            res += 0;
        }
        //Pregunta 4
        if (p41.isChecked()) {
            res += 1;
        } else if (p42.isChecked()) {
            res += 0;
        }
        //Pregunta 5
        if (p51.isChecked()) {
            res += 1;
        } else if (p52.isChecked()) {
            res += 0;
        }
        //Pregunta 6
        if (p61.isChecked()) {
            res += 0;
        } else if (p62.isChecked()) {
            res += 1;
        }
        //Pregunta 7
        if (p71.isChecked()) {
            res += 0;
        } else if (p72.isChecked()) {
            res += 1;
        } else if (p73.isChecked()) {
            res += 2;
        } else if (p74.isChecked()) {
            res += 3;
        }

        return res;
    }

}
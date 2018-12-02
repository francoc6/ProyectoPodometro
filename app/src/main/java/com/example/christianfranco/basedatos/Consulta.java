package com.example.christianfranco.basedatos;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class Consulta extends AppCompatActivity {
    Spinner datoaconsultar;
    Button consultar;

    static String var;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);

        final DialogResConsulta dialog = new DialogResConsulta();

        consultar=(Button)findViewById(R.id.consultar);
        datoaconsultar = (Spinner) findViewById(R.id.spinnerconsulta);
        String[] variables = {"Azucar:", "Peso:", "Sangre:", "algo: ", "algoo: "};
        datoaconsultar.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, variables));


        consultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //se despliega el dialogo con el resultado
                String selec=datoaconsultar.getSelectedItem().toString();
                String[] parts = selec.split(":");//para tomar solo la palabra y no la unidad lo llamo con parts[0]
                var =parts[0];
                dialog.show(getSupportFragmentManager(), "dialogo");
            }
        });
    }
}

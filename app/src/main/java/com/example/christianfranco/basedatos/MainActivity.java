package com.example.christianfranco.basedatos;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText editUSR, editPSW;
    TextView addusr;
    Button btnIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editUSR= (EditText)findViewById(R.id.edtUSR);
        editPSW=(EditText)findViewById(R.id.editPSW);
        btnIN=(Button)findViewById(R.id.btnIN);
        addusr=(TextView)findViewById(R.id.registarse);

        btnIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newusr = new Intent(MainActivity.this,MenuPrincipal.class);

                startActivity(newusr);
               // agregarusuario();
            }
        });
        addusr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newusr = new Intent(MainActivity.this,Registro.class);
                startActivity(newusr);
            }
        });
    }

    //metodo para hacer la conexion a la base de datos
    public Connection conectar(){
        Connection conexion=null;//objeto tipo conection para acceder
        try{
            //registramos el permiso de acceso para la aplicacion
            StrictMode.ThreadPolicy policy =new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            conexion= DriverManager.getConnection("jdbc:jtds:sqlserver://192.168.1.104;port=1433;databaseName=Registro_db;user=sa;password=admin;");
        }catch(Exception d){
            Toast.makeText(getApplicationContext(),d.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return conexion;
    }


    public boolean apusr(String u){
        List<Usuario> usuarios = new ArrayList<>();
        Usuario test = new Usuario();
        try{
            Statement pedir =conectar().createStatement();
            ResultSet res= pedir.executeQuery("select * from TbRegistros");

            while (res.next()){
                usuarios.add(new Usuario(res.getString("Nombre"),res.getString("Correo")));
            }

        }catch (SQLException e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return test.yaexiste(u,usuarios);
    }



    //metodo para agregar usuario
    public void agregarusuario(){
        try {
            if (apusr(editUSR.getText().toString())){
                Toast.makeText(getApplicationContext(), "EL USUARIO YA EXISTE", Toast.LENGTH_SHORT).show();
            }else{
                PreparedStatement pst = conectar().prepareStatement("insert into TbRegistros values(?,?)");
                pst.setString(1, editUSR.getText().toString());
                pst.setString(2, editPSW.getText().toString());
                pst.executeUpdate();
                Toast.makeText(getApplicationContext(), "USUARIO AGREGADO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                editUSR.setText("");
                editPSW.setText("");
            }
        }catch (SQLException e ){
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        }
    }
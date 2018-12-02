package com.example.christianfranco.basedatos;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class DialogResConsulta extends DialogFragment {

    private TextView Var;
    private ListView resultado;

    SharedPreferences usuariognr;//lo uso para obtener el usuario almacenado

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_resconsulta, container, false);
        usuariognr = getActivity().getSharedPreferences("Guardarusuario", MODE_PRIVATE);//instancio el objeto para obtener usuario
        final String usuario = usuariognr.getString("usuario", "vacio");

        Var = (TextView) view.findViewById(R.id.VAR);
        resultado=(ListView)view.findViewById(R.id.listview);

        Var.setText(Consulta.var);

        ArrayList<String> datos=respuesta( AgregarDato.obtenerindice(Consulta.var),usuario);
        ArrayAdapter<String> pantalla= new ArrayAdapter<String>(getActivity(),android.R.layout.simple_expandable_list_item_1,datos);

        resultado.setAdapter(pantalla);
        return view;
    }


    Conectar contacto = new Conectar();
    public ArrayList<String> respuesta(Integer i,String u){

        //conexion y descarga de datos
        String orden ="select * from Variables_db WHERE ID='"+i+"' AND "+"Usuario='"+u+"'";
        ArrayList<String> ans = new ArrayList<>();
        try {
            Statement pedir = contacto.conectarabase().createStatement();
            ResultSet res=null;
            res = pedir.executeQuery(orden);
            // res.next();
            while (res.next()) {
                ans.add(res.getString("Fecha")+"    "+res.getString("Valor"));
            }
            res.close();
        }catch (SQLException e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        return ans;
    }


}

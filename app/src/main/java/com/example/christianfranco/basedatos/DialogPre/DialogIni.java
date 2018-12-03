package com.example.christianfranco.basedatos.DialogPre;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.example.christianfranco.basedatos.Actividad;
import com.example.christianfranco.basedatos.Conectar;
import com.example.christianfranco.basedatos.R;
import com.example.christianfranco.basedatos.Status;
import com.example.christianfranco.basedatos.Usuario;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class DialogIni extends DialogFragment {

    SharedPreferences usuariognr;//lo uso para obtener el usuario almacenado
    TextView RESPUESTA;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_formulario_ini, container, false);

        RESPUESTA=(TextView)view.findViewById(R.id.RESPUESTA);
        RESPUESTA.setText(Status.respuestaG);
        return view;
    }

}
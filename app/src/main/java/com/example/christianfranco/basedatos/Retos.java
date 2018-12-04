package com.example.christianfranco.basedatos;

import android.widget.Toast;

public class Retos {


    //20 pasos
    public static void retos(Integer p){
        if(p>19){
            Logros.copa1.setImageResource(R.drawable.copaon);
        }
        if(p>49){
            Logros.copa2.setImageResource(R.drawable.copaon);
        }
    }


}

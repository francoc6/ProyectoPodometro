package com.example.christianfranco.basedatos;

import android.widget.Toast;

public class Retos {


    //metodo para cambiar copas
    public static void retos(Integer p){
        if(p>19){//20 pasos
            Logros.copa1.setImageResource(R.drawable.copaon);
        }
        if(p>49){//50 pasos
            Logros.copa2.setImageResource(R.drawable.copaon);
        }
        if(p>100){//100 pasos
            Logros.copa3.setImageResource(R.drawable.copaon);
        }
        if(p>500){//500 pasos
            Logros.copa4.setImageResource(R.drawable.copaon);
        }
    }


}

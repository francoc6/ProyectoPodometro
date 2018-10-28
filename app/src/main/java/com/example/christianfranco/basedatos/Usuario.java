package com.example.christianfranco.basedatos;

import java.util.List;

public class Usuario {
    String Nombre, Correo;

    public Usuario(){

    }

    public Usuario(String nombre, String correo) {
        Nombre = nombre;
        Correo = correo;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }
    
    public boolean yaexiste (String u, List<Usuario> usuarios){
        for (int i=0; i<usuarios.size(); ++i){
            if (usuarios.get(i).getNombre().equals(u)){
                return true;
            }
        }
        return false;

    }
}
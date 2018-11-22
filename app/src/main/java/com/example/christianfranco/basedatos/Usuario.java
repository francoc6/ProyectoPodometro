package com.example.christianfranco.basedatos;

import java.util.List;

public class Usuario {
    String Nombre, Correo, Edad, Usuario, Contraseña, Genero;

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
    
    public boolean yaexiste (String u, List<Usuario> usuarios){//funcion que  utiliza  la comprobacion de usuario
        for (int i=0; i<usuarios.size(); ++i){
            if (usuarios.get(i).getNombre().equals(u)){
                return true;
            }
        }
        return false;
    }


    public int verificar (String u, List<Usuario> usuarios){//funcion que  utiliza  la comprobacion de usuario
        for (int i=0; i<usuarios.size(); ++i){
            if (usuarios.get(i).getNombre().equals(u)){
                return 1;
            }
        }
        return 0;
    }








    public boolean comprobar (String u,String p, List<Usuario> usuarios){//funcion que realiza el logeo
        for (int i=0; i<usuarios.size(); ++i){
            if (usuarios.get(i).getNombre().equals(u)&&usuarios.get(i).getCorreo().equals(p)){
                return true;
            }
        }
        return false;
    }
}
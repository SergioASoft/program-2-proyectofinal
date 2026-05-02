package co.edu.uniquindio.poo.finalproject.controller;

import co.edu.uniquindio.poo.finalproject.model.*;

public class AutenticacionUsuarioProxy implements IPlataforma {
    private Usuario usuario;

    public AutenticacionUsuarioProxy(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    private boolean ValidarPermisos(){
        return usuario.getTipoUsuario() == TipoUsuario.ADMIN;
    }

    @Override
    public void concederPermisos() {
        if(ValidarPermisos()){
            Plataforma.getInstance().concederPermisos();
            System.out.println("Tiene permisos de admin");
            return;
        }
        Plataforma.getInstance().negarPermisos();
        System.out.println("Tiene permisos comunes");
    }
}

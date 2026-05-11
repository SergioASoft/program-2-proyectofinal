package co.edu.uniquindio.poo.finalproject.model.factory;

import co.edu.uniquindio.poo.finalproject.model.TipoUsuario;

public class CreacionAdmin implements CreacionUsuario {
    @Override
    public Usuario crear(String id, String nombre, String contrasena, String correo, String numero) {
        return new Admin(id, contrasena, nombre, correo, numero, TipoUsuario.ADMIN);
    }
}

package co.edu.uniquindio.poo.finalproject.model;

public class Cliente extends Usuario{
    public Cliente(String idUsuario, String contrasena, String nombre, String correo, String numero, TipoUsuario tipoUsuario) {
        super(idUsuario, contrasena, nombre, correo, numero, tipoUsuario);
    }
}

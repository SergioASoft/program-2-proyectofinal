package co.edu.uniquindio.poo.finalproject.model.factory;

public interface CreacionUsuario {
    Usuario crear(String id, String nombre, String contrasena, String correo, String numero);
}

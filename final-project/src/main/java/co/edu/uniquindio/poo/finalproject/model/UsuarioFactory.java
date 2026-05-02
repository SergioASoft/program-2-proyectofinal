package co.edu.uniquindio.poo.finalproject.model;

public class UsuarioFactory {

    public Usuario GetUsuario(String id, String nombre, String contrasena, String correo, String numero, TipoUsuario tipoUsuario){
        return switch (tipoUsuario){
            case CLIENTE ->  new Cliente(id,contrasena,nombre,correo,numero, tipoUsuario);
            case ADMIN -> new Admin(id,contrasena,nombre,correo,numero, tipoUsuario);
            default -> throw new RuntimeException("Tipo de usuario no existe");
        };
    }
}

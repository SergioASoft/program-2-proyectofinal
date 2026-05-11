package co.edu.uniquindio.poo.finalproject.model.factory;

import co.edu.uniquindio.poo.finalproject.model.TipoUsuario;

import java.util.HashMap;
import java.util.Map;

public class UsuarioFactory {

    private static final Map<TipoUsuario, CreacionUsuario> usuarios = new HashMap<>();

    static {
        usuarios.put(TipoUsuario.CLIENTE, new CreacionCliente());
        usuarios.put(TipoUsuario.ADMIN, new CreacionAdmin());
    }

    public Usuario GetUsuario(String id, String nombre, String contrasena, String correo, String numero, TipoUsuario tipoUsuario) {
        CreacionUsuario usuario = usuarios.get(tipoUsuario);
        return usuario.crear(id, nombre, contrasena, correo, numero);
    }
}

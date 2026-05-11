package co.edu.uniquindio.poo.finalproject.model.factory;

import co.edu.uniquindio.poo.finalproject.model.TipoPago;
import co.edu.uniquindio.poo.finalproject.model.TipoUsuario;

public class CreacionCliente implements CreacionUsuario{
    @Override
    public Usuario crear(String id, String nombre, String contrasena, String correo, String numero) {
        return new Cliente(id, contrasena, nombre, correo, numero, TipoUsuario.CLIENTE, TipoPago.NINGUNO);
    }
}

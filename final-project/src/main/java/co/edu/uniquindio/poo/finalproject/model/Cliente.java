package co.edu.uniquindio.poo.finalproject.model;

public class Cliente extends Usuario{
    TipoPago tipoPago;
    public Cliente(String idUsuario, String contrasena, String nombre, String correo, String numero, TipoUsuario tipoUsuario, TipoPago tipoPago) {
        super(idUsuario, contrasena, nombre, correo, numero, tipoUsuario);
        this.tipoPago = tipoPago;
    }

    public TipoPago getTipoPago() {
        return tipoPago;
    }
}

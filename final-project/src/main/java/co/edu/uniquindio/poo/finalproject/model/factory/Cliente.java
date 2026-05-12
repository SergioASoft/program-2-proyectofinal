package co.edu.uniquindio.poo.finalproject.model.factory;

import co.edu.uniquindio.poo.finalproject.model.TipoPago;
import co.edu.uniquindio.poo.finalproject.model.TipoUsuario;
import co.edu.uniquindio.poo.finalproject.model.builder.Zona;
import co.edu.uniquindio.poo.finalproject.model.state.ContextoCompra;

import java.util.List;
import java.util.ArrayList;

public class Cliente extends Usuario{
    TipoPago tipoPago;
    List<ContextoCompra> contextoCompras;
    public Cliente(String idUsuario, String contrasena, String nombre, String correo, String numero, TipoUsuario tipoUsuario, TipoPago tipoPago) {
        super(idUsuario, contrasena, nombre, correo, numero, tipoUsuario);
        this.tipoPago = tipoPago;
        this.contextoCompras = new ArrayList<>();
    }


    public TipoPago getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(TipoPago tipoPago) {
        this.tipoPago = tipoPago;
    }

    public List<ContextoCompra> getHistorialCompras() {
        return contextoCompras;
    }
}

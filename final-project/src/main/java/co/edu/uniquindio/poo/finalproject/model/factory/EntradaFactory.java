package co.edu.uniquindio.poo.finalproject.model.factory;

import co.edu.uniquindio.poo.finalproject.model.Asiento;
import co.edu.uniquindio.poo.finalproject.model.Entrada;
import co.edu.uniquindio.poo.finalproject.model.builder.Zona;
import co.edu.uniquindio.poo.finalproject.model.state.ContextoCompra;

import java.util.UUID;

public class EntradaFactory {
    public Entrada crear(ContextoCompra compra, Zona zona, Asiento asiento) {
        String id = "ENT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return new Entrada(id, compra, compra.getEvento(), zona, asiento, asiento.getPrecio());
    }
}

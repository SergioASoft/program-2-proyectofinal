package co.edu.uniquindio.poo.finalproject.model.factory;

import co.edu.uniquindio.poo.finalproject.model.Asiento;
import co.edu.uniquindio.poo.finalproject.model.TipoServicioAdicional;
import co.edu.uniquindio.poo.finalproject.model.builder.Evento;
import co.edu.uniquindio.poo.finalproject.model.state.ContextoCompra;

import java.util.List;
import java.util.UUID;

public class CompraFactory {
    public ContextoCompra crear(Evento evento, List<Asiento> asientos, List<TipoServicioAdicional> servicios) {
        String idCompra = "CMP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return new ContextoCompra(idCompra, evento, asientos, servicios);
    }
}

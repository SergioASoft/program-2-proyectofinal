package co.edu.uniquindio.poo.finalproject.model.factory;

import co.edu.uniquindio.poo.finalproject.model.Incidencia;
import co.edu.uniquindio.poo.finalproject.model.TipoIncidencia;
import co.edu.uniquindio.poo.finalproject.model.builder.Evento;
import co.edu.uniquindio.poo.finalproject.model.state.ContextoCompra;

import java.time.LocalDate;
import java.util.UUID;

public class IncidenciaFactory {
    public Incidencia crear(TipoIncidencia tipo, String descripcion, Evento evento,
                            ContextoCompra compra, Usuario usuario) {
        String id = "INC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String entidad = compra != null ? "Compra " + compra.getIdCompra()
                : evento != null ? "Evento " + evento.getIdEvento()
                : usuario != null ? "Usuario " + usuario.getIdUsuario()
                : "General";
        return new Incidencia(id, tipo, descripcion, LocalDate.now(), entidad, evento, compra, usuario);
    }
}

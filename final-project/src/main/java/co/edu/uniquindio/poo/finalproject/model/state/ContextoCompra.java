package co.edu.uniquindio.poo.finalproject.model.state;

import co.edu.uniquindio.poo.finalproject.model.Asiento;
import co.edu.uniquindio.poo.finalproject.model.builder.Evento;
import co.edu.uniquindio.poo.finalproject.model.builder.Zona;

import java.util.List;

public class ContextoCompra {
    private EstadoCompra estadoActual;
    private Evento evento;
    private List<Asiento> asientos;

    public ContextoCompra(Evento evento, List<Asiento> asientos) {
        this.estadoActual = new EstadoCreada();
        this.evento = evento;
        this.asientos = asientos;
    }

    public void setEstado(EstadoCompra nuevoEstado) {
        this.estadoActual = nuevoEstado;
    }

    public void realizarPago() {
        estadoActual.pagar(this);
    }

    public void realizarCancelacion() {
        estadoActual.cancelar(this);
    }

    public String obtenerNombreEstado() {
        return estadoActual.getNombre();
    }

    public Evento getEvento() {
        return evento;
    }
}

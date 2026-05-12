package co.edu.uniquindio.poo.finalproject.model;

import co.edu.uniquindio.poo.finalproject.model.builder.Evento;
import co.edu.uniquindio.poo.finalproject.model.builder.Zona;
import co.edu.uniquindio.poo.finalproject.model.state.ContextoCompra;

public class Entrada {
    private final String idEntrada;
    private final ContextoCompra compra;
    private final Evento evento;
    private final Zona zona;
    private final Asiento asiento;
    private final int precioFinal;
    private EstadoEntrada estadoEntrada;

    public Entrada(String idEntrada, ContextoCompra compra, Evento evento, Zona zona, Asiento asiento, int precioFinal) {
        this.idEntrada = idEntrada;
        this.compra = compra;
        this.evento = evento;
        this.zona = zona;
        this.asiento = asiento;
        this.precioFinal = precioFinal;
        this.estadoEntrada = EstadoEntrada.ACTIVA;
    }

    public void marcarUsada() {
        if (estadoEntrada == EstadoEntrada.ACTIVA) {
            estadoEntrada = EstadoEntrada.USADA;
        }
    }

    public void anular() {
        estadoEntrada = EstadoEntrada.ANULADA;
    }

    public String getIdEntrada() {
        return idEntrada;
    }

    public ContextoCompra getCompra() {
        return compra;
    }

    public Evento getEvento() {
        return evento;
    }

    public Zona getZona() {
        return zona;
    }

    public Asiento getAsiento() {
        return asiento;
    }

    public int getPrecioFinal() {
        return precioFinal;
    }

    public EstadoEntrada getEstadoEntrada() {
        return estadoEntrada;
    }
}

package co.edu.uniquindio.poo.finalproject.model;

import co.edu.uniquindio.poo.finalproject.model.builder.Evento;
import co.edu.uniquindio.poo.finalproject.model.factory.Usuario;
import co.edu.uniquindio.poo.finalproject.model.state.ContextoCompra;

import java.time.LocalDate;

public class Incidencia {
    private final String idIncidencia;
    private final TipoIncidencia tipo;
    private final String descripcion;
    private final LocalDate fecha;
    private final String entidadAfectada;
    private final Evento evento;
    private final ContextoCompra compra;
    private final Usuario usuario;

    public Incidencia(String idIncidencia, TipoIncidencia tipo, String descripcion, LocalDate fecha,
                      String entidadAfectada, Evento evento, ContextoCompra compra, Usuario usuario) {
        this.idIncidencia = idIncidencia;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.entidadAfectada = entidadAfectada;
        this.evento = evento;
        this.compra = compra;
        this.usuario = usuario;
    }

    public String getIdIncidencia() {
        return idIncidencia;
    }

    public TipoIncidencia getTipo() {
        return tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public String getEntidadAfectada() {
        return entidadAfectada;
    }

    public Evento getEvento() {
        return evento;
    }

    public ContextoCompra getCompra() {
        return compra;
    }

    public Usuario getUsuario() {
        return usuario;
    }
}

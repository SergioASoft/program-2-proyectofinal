package co.edu.uniquindio.poo.finalproject.model;

import java.util.List;

public class Evento {
    String idEvento;
    String nombre;
    TipoEvento tipoEvento;
    String descripcion;
    String ciudad;
    String fecha;
    EstadoEvento estadoEvento;
    TipoPolitica tipoPolitica;
    Recinto recinto;

    public Evento(String idEvento, String nombre, TipoEvento tipoEvento, String descripcion, String ciudad, String fecha, EstadoEvento estadoEvento, TipoPolitica tipoPolitica, Recinto recinto) {
        this.idEvento = idEvento;
        this.nombre = nombre;
        this.tipoEvento = tipoEvento;
        this.descripcion = descripcion;
        this.ciudad = ciudad;
        this.fecha = fecha;
        this.estadoEvento = estadoEvento;
        this.tipoPolitica = tipoPolitica;
        this.recinto = recinto;
    }

    public String getIdEvento() {
        return idEvento;
    }

    public String getNombre() {
        return nombre;
    }
}

package co.edu.uniquindio.poo.finalproject.model.builder;

import co.edu.uniquindio.poo.finalproject.model.EstadoEvento;
import co.edu.uniquindio.poo.finalproject.model.Recinto;
import co.edu.uniquindio.poo.finalproject.model.TipoEvento;
import co.edu.uniquindio.poo.finalproject.model.TipoPolitica;

import java.time.LocalDate;

public class Evento {
    String idEvento;
    String nombre;
    TipoEvento tipoEvento;
    String descripcion;
    String ciudad;
    LocalDate fecha;
    String hora;
    EstadoEvento estadoEvento;
    TipoPolitica tipoPolitica;
    Recinto recinto;

    private Evento(Builder builder) {
        this.idEvento = builder.idEvento;
        this.nombre = builder.nombre;
        this.tipoEvento = builder.tipoEvento;
        this.descripcion = builder.descripcion;
        this.ciudad = builder.ciudad;
        this.fecha = builder.fecha;
        this.hora = builder.hora;
        this.estadoEvento = builder.estadoEvento;
        this.tipoPolitica = builder.tipoPolitica;
        this.recinto = builder.recinto;
    }
    public static class Builder{
        String idEvento;
        String nombre;
        private String descripcion;
        private String ciudad;
        private LocalDate fecha;
        private Recinto recinto;

        private TipoEvento tipoEvento   = TipoEvento.CONCIERTO;
        private EstadoEvento estadoEvento = EstadoEvento.BORRADOR;
        private TipoPolitica tipoPolitica = TipoPolitica.NINGUNA;
        private String hora;

        public Builder(String idEvento, String nombre, String descripcion, String ciudad, LocalDate fecha, Recinto recinto) {
            this.idEvento = idEvento;
            this.nombre   = nombre;
            this.descripcion = descripcion;
            this.ciudad = ciudad;
            this.fecha = fecha;
            this.recinto = recinto;
        }

        public Builder tipoEvento(TipoEvento tipoEvento) {
            this.tipoEvento = tipoEvento;
            return this;
        }

        public Builder estadoEvento(EstadoEvento estadoEvento) {
            this.estadoEvento = estadoEvento;
            return this;
        }

        public Builder tipoPolitica(TipoPolitica tipoPolitica) {
            this.tipoPolitica = tipoPolitica;
            return this;
        }

        public Builder hora(String  hora) {
            this.hora = hora;
            return this;
        }

        public Evento build() {
            return new Evento(this);
        }
    }
    public String getIdEvento() {
        return idEvento;
    }

    public String getNombre() {return nombre;}

    public TipoEvento getTipoEvento() {
        return tipoEvento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public EstadoEvento getEstadoEvento() {
        return estadoEvento;
    }

    public TipoPolitica getTipoPolitica() {
        return tipoPolitica;
    }

    public Recinto getRecinto() {
        return recinto;
    }

    public void setEstadoEvento(EstadoEvento estadoEvento) {
        this.estadoEvento = estadoEvento;
    }
}

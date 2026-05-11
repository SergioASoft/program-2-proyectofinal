package co.edu.uniquindio.poo.finalproject.model.builder;

import co.edu.uniquindio.poo.finalproject.model.Asiento;
import co.edu.uniquindio.poo.finalproject.model.TipoZona;

import java.util.List;

public class Zona {
    String idZona;
    TipoZona tipoZona;
    int capacidad;
    String reglas;
    List<Asiento> asientos;

    private Zona(Builder builder) {
        this.idZona = builder.idZona;
        this.tipoZona = builder.tipoZona;
        this.capacidad = builder.capacidad;
        this.reglas = builder.reglas;
        this.asientos = builder.asientos;
    }
    public static class Builder{
        String idZona;
        TipoZona tipoZona;
        int capacidad;
        String reglas;
        List<Asiento> asientos;

        public Builder (String idZona, TipoZona tipoZona, int capacidad, List<Asiento> asientos){
            this.idZona = idZona;
            this.tipoZona = tipoZona;
            this.capacidad = capacidad;
            this.asientos = asientos;
        }
        public Builder reglas(String reglas){
            this.reglas = reglas;
            return this;
        }
        public Zona build() {
            return new Zona(this);
        }
    }

    public String getIdZona() {
        return idZona;
    }

    public TipoZona getTipoZona() {
        return tipoZona;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public String getReglas() {
        return reglas;
    }

    public List<Asiento> getAsientos() {
        return asientos;
    }
}

package co.edu.uniquindio.poo.finalproject.model;

import co.edu.uniquindio.poo.finalproject.model.builder.Zona;

import java.util.List;

public class Recinto {
    String idRecinto;
    String nombre;
    List<Zona> zonas;

    public String getIdRecinto() {
        return idRecinto;
    }

    public String getNombre() {
        return nombre;
    }

    public List<Zona> getZonas() {
        return zonas;
    }

    public Recinto(String idRecinto, String nombre, List<Zona> zonas) {
        this.idRecinto = idRecinto;
        this.nombre = nombre;
        this.zonas = zonas;
    }

    @Override
    public String toString() {
        return "Id: " + idRecinto + " Nombre: " + nombre;
    }
}

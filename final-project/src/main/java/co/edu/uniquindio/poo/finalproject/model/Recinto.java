package co.edu.uniquindio.poo.finalproject.model;

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
}

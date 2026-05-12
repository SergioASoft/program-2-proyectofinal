package co.edu.uniquindio.poo.finalproject.model;

import co.edu.uniquindio.poo.finalproject.model.builder.Zona;

import java.util.List;

public class Recinto {
    String idRecinto;
    String nombre;
    String ciudad;
    String direccion;
    List<Zona> zonas;


    public String getIdRecinto() {
        return idRecinto;
    }

    public void setIdRecinto(String idRecinto) {
        this.idRecinto = idRecinto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public List<Zona> getZonas() {
        return zonas;
    }

    public void setZonas(List<Zona> zonas) {
        this.zonas = zonas;
    }

    public Recinto(String idRecinto, String nombre, String ciudad, String direccion, List<Zona> zonas) {
        this.idRecinto = idRecinto;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.direccion = direccion;
        this.zonas = zonas;
    }

    @Override
    public String toString() {
        return "Id: " + idRecinto + " Nombre: " + nombre;
    }
}

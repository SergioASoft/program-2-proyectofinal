package co.edu.uniquindio.poo.finalproject.controller.facade;

import co.edu.uniquindio.poo.finalproject.model.EstadoEvento;
import co.edu.uniquindio.poo.finalproject.model.Recinto;
import co.edu.uniquindio.poo.finalproject.model.builder.Evento;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.stream.Collectors;

public class ControladorEventos {
    private static ControladorEventos instance;
    private final ObservableList<Evento> listaEventos = FXCollections.observableArrayList();
    private final ObservableList<Recinto> listaRecintos = FXCollections.observableArrayList();

    private ControladorEventos(){};

    public static ControladorEventos getInstance(){
        if (instance == null){
            instance = new ControladorEventos();
        }
        return instance;
    }
    public void registrarEvento(Evento evento){
        listaEventos.add(evento);
    }
    public void registrarRecinto(Recinto recinto){
        listaRecintos.add(recinto);
    }

    public Evento obtenerEvento(String id) {
        return listaEventos.stream()
                .filter(e -> e.getIdEvento().equals(id))
                .findFirst()
                .orElse(null);
    }

    public ObservableList<Evento> getEventosPublicados() {
        return listaEventos.stream()
                .filter(e -> e.getEstadoEvento() == EstadoEvento.PUBLICADO)
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        FXCollections::observableArrayList
                ));
    }

    public boolean eliminarEvento(Evento evento){
        listaEventos.remove(evento);
        return listaEventos.contains(evento);
    }

    public boolean eliminarRecinto(Recinto recinto){
        listaRecintos.remove(recinto);
        return listaRecintos.contains(recinto);
    }

    public ObservableList<Evento> getListaEventos() {
        return listaEventos;
    }

    public ObservableList<Recinto> getListaRecintos() {
        return listaRecintos;
    }
}

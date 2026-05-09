package co.edu.uniquindio.poo.finalproject.controller;

import co.edu.uniquindio.poo.finalproject.model.EstadoEvento;
import co.edu.uniquindio.poo.finalproject.model.Evento;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.stream.Collectors;

public class ControladorEventos {
    private static ControladorEventos instance;
    private final ObservableList<Evento> listaEventos = FXCollections.observableArrayList();
    private final ObservableList<Evento> listaRecintos = FXCollections.observableArrayList();

    public static ControladorEventos getInstance(){
        if (instance == null){
            instance = new ControladorEventos();
        }
        return instance;
    }
    public void registrarEvento(Evento evento){
        listaEventos.add(evento);
    }
    public Evento obtenerEvento(String id){
        return listaEventos.stream()
                .filter(e -> e.getIdEvento().equals(id))
                .findFirst()
                .orElse(listaEventos.stream()
                        .filter(e -> e.getIdEvento().equals(id))
                        .findFirst()
                        .orElse(null));
    }

    public ObservableList<Evento> getEventosPublicados() {
        return listaEventos.stream()
                .filter(e -> e.getEstadoEvento() == EstadoEvento.PUBLICADO)
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        FXCollections::observableArrayList
                ));
    }

    public ObservableList<Evento> getListaEventos() {
        return listaEventos;
    }
}

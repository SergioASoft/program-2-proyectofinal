package co.edu.uniquindio.poo.finalproject.controller.facade;

import co.edu.uniquindio.poo.finalproject.model.Incidencia;
import co.edu.uniquindio.poo.finalproject.model.TipoIncidencia;
import co.edu.uniquindio.poo.finalproject.model.builder.Evento;
import co.edu.uniquindio.poo.finalproject.model.factory.IncidenciaFactory;
import co.edu.uniquindio.poo.finalproject.model.factory.Usuario;
import co.edu.uniquindio.poo.finalproject.model.state.ContextoCompra;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.util.stream.Collectors;

public class ControladorIncidencias {
    private static ControladorIncidencias instance;
    private final ObservableList<Incidencia> incidencias = FXCollections.observableArrayList();
    private final IncidenciaFactory factory = new IncidenciaFactory();

    private ControladorIncidencias() {
    }

    public static ControladorIncidencias getInstance() {
        if (instance == null) {
            instance = new ControladorIncidencias();
        }
        return instance;
    }

    public Incidencia registrar(TipoIncidencia tipo, String descripcion, Evento evento,
                                ContextoCompra compra, Usuario usuario) {
        Incidencia incidencia = factory.crear(tipo, descripcion, evento, compra, usuario);
        incidencias.add(incidencia);
        return incidencia;
    }

    public ObservableList<Incidencia> getIncidencias() {
        return incidencias;
    }

    public ObservableList<Incidencia> filtrar(LocalDate desde, LocalDate hasta, TipoIncidencia tipo) {
        return incidencias.stream()
                .filter(incidencia -> desde == null || !incidencia.getFecha().isBefore(desde))
                .filter(incidencia -> hasta == null || !incidencia.getFecha().isAfter(hasta))
                .filter(incidencia -> tipo == null || incidencia.getTipo() == tipo)
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        FXCollections::observableArrayList
                ));
    }
}

package co.edu.uniquindio.poo.finalproject.viewController;

import co.edu.uniquindio.poo.finalproject.controller.facade.ControladorFacade;
import co.edu.uniquindio.poo.finalproject.model.*;
import co.edu.uniquindio.poo.finalproject.model.builder.Evento;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class CrearEventoViewController extends ViewController {

    @FXML private TextField txtIdEvento;
    @FXML private TextField txtNombre;
    @FXML private ComboBox<TipoEvento> comboTipo;
    @FXML private TextField txtCiudad;
    @FXML private ComboBox<Recinto> comboRecinto;
    @FXML private DatePicker dateFecha;
    @FXML private TextField txtHora;
    @FXML private TextArea txtDescripcion;
    @FXML private ComboBox<TipoPolitica> comboPoliticaCancelacion;

    ControladorFacade controladorFacade = ControladorFacade.getInstance();

    private Evento eventoEditar;

    @FXML
    public void initialize() {
        comboTipo.getItems().setAll(TipoEvento.values());
        comboPoliticaCancelacion.getItems().setAll(TipoPolitica.values());
        comboRecinto.getItems().setAll(controladorFacade.getTodosLosRecintos());
    }

    public void regresar(ActionEvent event) {
        if(eventoEditar != null){
            eventoEditar = null;
        }
        crearVista("/co/edu/uniquindio/poo/finalproject/AdminEventoView.fxml",
                "Gestion de eventos", event);
    }

    public void rellenarCamposEvento(Evento evento){
        eventoEditar = evento;
        txtIdEvento.setText(evento.getIdEvento());
        txtNombre.setText(evento.getNombre());
        comboTipo.setValue(evento.getTipoEvento());
        txtCiudad.setText(evento.getCiudad());
        comboRecinto.setValue(evento.getRecinto());
        dateFecha.setValue(evento.getFecha());
        txtHora.setText(evento.getHora());
        txtDescripcion.setText(evento.getDescripcion());
        comboPoliticaCancelacion.setValue(evento.getTipoPolitica());
    }

    public void crearEvento(ActionEvent event) {
        if (formularioIncompleto()) {
            mostrarAlerta("Complete todos los datos para crear un evento");
            return;
        }

        if(eventoEditar !=null){
            controladorFacade.eliminarEvento(eventoEditar);
            eventoEditar = null;
        }
        Recinto recinto = comboRecinto.getValue();

        Evento evento = new Evento.Builder(txtIdEvento.getText(), txtNombre.getText(),txtDescripcion.getText(),txtCiudad.getText(),dateFecha.getValue(),
                recinto)
                .tipoEvento(comboTipo.getValue())
                .estadoEvento(EstadoEvento.BORRADOR)
                .tipoPolitica(comboPoliticaCancelacion.getValue())
                .hora(txtHora.getText())
                .build();

        controladorFacade.registrarEvento(evento);

        crearVista("/co/edu/uniquindio/poo/finalproject/AdminEventoView.fxml",
                "Gestion de eventos", event);
    }

    private boolean formularioIncompleto() {
        return txtIdEvento.getText().isEmpty()
                || txtNombre.getText().isEmpty()
                || comboTipo.getValue() == null
                || txtCiudad.getText().isEmpty()
                || comboRecinto.getValue() == null
                || dateFecha.getValue() == null
                || txtHora.getText().isEmpty()
                || txtDescripcion.getText().isEmpty()
                || comboPoliticaCancelacion.getValue() == null;
    }
}

package co.edu.uniquindio.poo.finalproject.viewController;

import co.edu.uniquindio.poo.finalproject.controller.ControladorEventos;
import co.edu.uniquindio.poo.finalproject.model.*;
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
    @FXML private TextField txtRecinto;
    @FXML private DatePicker dateFecha;
    @FXML private TextField txtHora;
    @FXML private TextArea txtDescripcion;
    @FXML private ComboBox<TipoPolitica> comboPoliticaCancelacion;

    @FXML
    public void initialize() {
        comboTipo.getItems().setAll(TipoEvento.values());
        comboPoliticaCancelacion.getItems().setAll(TipoPolitica.values());
    }

    public void regresar(ActionEvent event) {
        crearVista("/co/edu/uniquindio/poo/finalproject/AdminEventoView.fxml","Gestion de eventos",event);
    }

    public void crearEvento(ActionEvent event) {
        if(formularioIncompleto()){
            mostrarAlerta("Complete todos los datos para crear un evento");
            return;
        }
        String idEvento = txtIdEvento.getText();
        String nombre = txtNombre.getText();
        TipoEvento tipoEvento = comboTipo.getValue();
        String descripcion = txtDescripcion.getText();
        String ciudad = txtCiudad.getText();
        String fecha = dateFecha.getValue().toString();
        TipoPolitica tipoPolitica = comboPoliticaCancelacion.getValue();
        Recinto recinto = new Recinto();
        Evento evento = new Evento(idEvento,nombre,tipoEvento,descripcion,ciudad,fecha,EstadoEvento.BORRADOR,tipoPolitica,recinto);
        ControladorEventos.getInstance().registrarEvento(evento);
        crearVista("/co/edu/uniquindio/poo/finalproject/AdminEventoView.fxml","Gestion de eventos",event);
    }
    private boolean formularioIncompleto(){
        return  txtIdEvento.getText().isEmpty() || txtNombre.getText().isEmpty() || comboTipo.getValue() == null ||
                txtCiudad.getText().isEmpty() || txtRecinto.getText().isEmpty() ||
                dateFecha.getValue() == null || txtHora.getText().isEmpty() || txtDescripcion.getText().isEmpty() ||
                comboPoliticaCancelacion.getValue() == null;
    }
}

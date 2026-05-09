package co.edu.uniquindio.poo.finalproject.viewController;

import co.edu.uniquindio.poo.finalproject.model.Evento;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class DetalleEventoViewController {
    @FXML
    private Label lblNombre, lblCiudad, lblLugar, lblFecha, lblDescripcion;
    private Stage stage;

    public void initData(Evento evento, Stage stage) {
        this.stage = stage;
        lblNombre.setText(evento.getNombre());
        lblCiudad.setText(evento.getCiudad());
        lblLugar.setText(evento.getRecinto().getNombre());
        lblFecha.setText(evento.getFecha());
        lblDescripcion.setText(evento.getDescripcion());
    }

    @FXML void regresar() {
        stage.close();
    }

    @FXML void comprar() {
        stage.close();
    }
}

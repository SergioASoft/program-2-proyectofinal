package co.edu.uniquindio.poo.finalproject.viewController;

import co.edu.uniquindio.poo.finalproject.model.builder.Evento;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CartaEventoViewController {
    @FXML private Label lblNombre;
    @FXML private Label lblEstado;
    @FXML private Label lblId;
    @FXML private Label lblLugar;
    @FXML private Label lblFecha;
    @FXML private Label lblCategoria;

    public void actualizarInformacion(Evento evento) {
        lblNombre.setText(evento.getNombre().toUpperCase());
        lblId.setText("ID: " + evento.getIdEvento());
        lblLugar.setText(evento.getRecinto().getNombre() + ", " + evento.getCiudad());
        lblFecha.setText(evento.getFecha().toString());
        lblCategoria.setText(evento.getTipoEvento().toString());
        lblEstado.setText(evento.getEstadoEvento().toString());

        String baseStyle = "-fx-padding: 2 8; -fx-background-radius: 5; -fx-font-weight: bold; -fx-font-size: 10;";

        switch (evento.getEstadoEvento()) {
            case PUBLICADO ->
                    lblEstado.setStyle(baseStyle + "-fx-background-color: #e1f5fe; -fx-text-fill: #0288d1;");

            case CANCELADO ->
                    lblEstado.setStyle(baseStyle + "-fx-background-color: #ffdad9; -fx-text-fill: #cf3737;");

            case BORRADOR ->
                    lblEstado.setStyle(baseStyle + "-fx-background-color: #f5f5f5; -fx-text-fill: #616161;");

            case PAUSADO ->
                    lblEstado.setStyle(baseStyle + "-fx-background-color: #fff9db; -fx-text-fill: #f08c00;");

            case FINALIZADO ->
                    lblEstado.setStyle(baseStyle + "-fx-background-color: #e8f5e9; -fx-text-fill: #2e7d32;");
        }
    }
}

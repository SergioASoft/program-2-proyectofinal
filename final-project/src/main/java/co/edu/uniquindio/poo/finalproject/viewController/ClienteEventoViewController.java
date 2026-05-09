package co.edu.uniquindio.poo.finalproject.viewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.*;
import java.io.IOException;
import java.util.List;

public class ClienteEventoViewController extends ViewController{
    @FXML private FlowPane containerCards;
    @FXML private TextField txtBusqueda;
    @FXML private ComboBox<String> comboCiudad, comboCategoria;
    @FXML private DatePicker dpFecha;
    @FXML private Label lblResultados;

    @FXML
    public void initialize() {
        // Cargar datos iniciales en combos
        comboCiudad.getItems().addAll("Armenia", "Pereira", "Manizales", "Bogotá");
        comboCategoria.getItems().addAll("Concierto", "Teatro", "Deporte", "Cultural");

        // Cargar tarjetas por primera vez
        actualizarCatalogo();
    }

    private void actualizarCatalogo() {
        containerCards.getChildren().clear();
        // Aquí llamarías a tu controlador lógico para obtener la lista
        // List<Evento> eventos = ControladorEvento.getInstance().getEventosPublicados();
        // renderizarTarjetas(eventos);
    }

    private void renderizarTarjetas(List<Object> eventos) {
        for (Object ev : eventos) {
            try {
                // REUTILIZACIÓN DE UI: Cargamos la tarjeta individual
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uniquindio/poo/finalproject/EventoCard.fxml"));
                VBox card = loader.load();

                // Configurar evento de clic para ver detalle (RF-004)
                card.setOnMouseClicked(e -> abrirDetalleEvento(ev));

                containerCards.getChildren().add(card);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void aplicarFiltros() {
        // Lógica para filtrar la lista según los campos de la izquierda
    }

    @FXML
    void abrirDetalleEvento(Object evento) {
        // Lógica para mostrar los detalles completos (Precios, Zonas, etc.)
    }

    @FXML
    void verMisCompras() {
        // Navegación a la vista de entradas compradas
    }

    @FXML
    void regresar() {
        // Volver al login
    }

    public void limpiarFiltros(ActionEvent event) {
    }
}

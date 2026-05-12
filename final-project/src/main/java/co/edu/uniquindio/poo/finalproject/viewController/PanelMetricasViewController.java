package co.edu.uniquindio.poo.finalproject.viewController;

import co.edu.uniquindio.poo.finalproject.controller.facade.ControladorFacade;
import co.edu.uniquindio.poo.finalproject.model.TipoServicioAdicional;
import co.edu.uniquindio.poo.finalproject.model.adapter.ReporteOperativoAdapter;
import co.edu.uniquindio.poo.finalproject.model.strategy.ReporteOperativoCsvStrategy;
import co.edu.uniquindio.poo.finalproject.model.strategy.ReporteOperativoPdfStrategy;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class PanelMetricasViewController extends ViewController {
    @FXML private LineChart<String, Number> chartVentas;
    @FXML private BarChart<String, Number> chartOcupacion, chartTopEventos;
    @FXML private PieChart chartServicios, chartCancelacion;
    @FXML private ComboBox<String> comboReporte;
    @FXML private Label lblTasaCancelacion;

    private final ControladorFacade controladorFacade = ControladorFacade.getInstance();
    private final ReporteOperativoAdapter adapter = new ReporteOperativoAdapter();

    @FXML
    public void initialize() {
        comboReporte.getItems().setAll(
                "Ventas por periodo",
                "Ocupacion por zona",
                "Ingresos por servicios",
                "Tasa de cancelacion",
                "Top eventos"
        );
        comboReporte.getSelectionModel().selectFirst();
        cargarMetricas();
    }

    @FXML
    void refrescar(ActionEvent event) {
        cargarMetricas();
    }

    @FXML
    void exportarCsv(ActionEvent event) {
        exportar(false);
    }

    @FXML
    void exportarPdf(ActionEvent event) {
        exportar(true);
    }

    @FXML
    void regresar(ActionEvent event) {
        crearVista("/co/edu/uniquindio/poo/finalproject/PlataformaView.fxml", "Menu Principal", event);
    }

    private void cargarMetricas() {
        cargarVentas();
        cargarOcupacion();
        cargarServicios();
        cargarCancelacion();
        cargarTopEventos();
    }

    private void cargarVentas() {
        chartVentas.getData().clear();
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Ingresos");
        for (Map.Entry<LocalDate, Integer> entry : controladorFacade.ventasPorPeriodo().entrySet()) {
            serie.getData().add(new XYChart.Data<>(entry.getKey().toString(), entry.getValue()));
        }
        chartVentas.getData().add(serie);
    }

    private void cargarOcupacion() {
        chartOcupacion.getData().clear();
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Ocupacion %");
        controladorFacade.ocupacionPorZona().forEach((zona, porcentaje) ->
                serie.getData().add(new XYChart.Data<>(zona, porcentaje)));
        chartOcupacion.getData().add(serie);
    }

    private void cargarServicios() {
        chartServicios.setData(FXCollections.observableArrayList(
                controladorFacade.ingresosPorServicios().entrySet().stream()
                        .map(entry -> new PieChart.Data(entry.getKey().getNombre(), entry.getValue()))
                        .toList()
        ));
    }

    private void cargarCancelacion() {
        double tasa = controladorFacade.tasaCancelacion();
        lblTasaCancelacion.setText(String.format("Tasa de cancelacion: %.1f%%", tasa));
        chartCancelacion.setData(FXCollections.observableArrayList(
                new PieChart.Data("Canceladas/Reembolsadas", tasa),
                new PieChart.Data("Restantes", Math.max(0, 100 - tasa))
        ));
    }

    private void cargarTopEventos() {
        chartTopEventos.getData().clear();
        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Compras");
        controladorFacade.topEventos().forEach((evento, compras) ->
                serie.getData().add(new XYChart.Data<>(evento, compras)));
        chartTopEventos.getData().add(serie);
    }

    private void exportar(boolean pdf) {
        List<String[]> filas = construirReporteSeleccionado();
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Guardar reporte operativo");
        chooser.setInitialFileName(pdf ? "reporte-operativo.pdf" : "reporte-operativo.csv");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(pdf ? "PDF" : "CSV", pdf ? "*.pdf" : "*.csv"));
        File destino = chooser.showSaveDialog(comboReporte.getScene().getWindow());
        if (destino == null) {
            return;
        }
        try {
            if (pdf) {
                controladorFacade.exportarReporteOperativo(filas, destino, new ReporteOperativoPdfStrategy());
            } else {
                controladorFacade.exportarReporteOperativo(filas, destino, new ReporteOperativoCsvStrategy());
            }
            mostrarAlerta("Reporte exportado en: " + destino.getAbsolutePath());
        } catch (IOException ex) {
            mostrarAlerta("No se pudo exportar el reporte: " + ex.getMessage());
        }
    }

    private List<String[]> construirReporteSeleccionado() {
        String reporte = comboReporte.getValue();
        if ("Ocupacion por zona".equals(reporte)) {
            return adapter.desdeMapa(reporte, controladorFacade.ocupacionPorZona());
        }
        if ("Ingresos por servicios".equals(reporte)) {
            Map<TipoServicioAdicional, Integer> metricas = controladorFacade.ingresosPorServicios();
            return adapter.desdeMapa(reporte, metricas);
        }
        if ("Tasa de cancelacion".equals(reporte)) {
            return adapter.desdeValor(reporte, controladorFacade.tasaCancelacion());
        }
        if ("Top eventos".equals(reporte)) {
            return adapter.desdeMapa(reporte, controladorFacade.topEventos());
        }
        return adapter.desdeMapa("Ventas por periodo", controladorFacade.ventasPorPeriodo());
    }
}

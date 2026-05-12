package co.edu.uniquindio.poo.finalproject;

import co.edu.uniquindio.poo.finalproject.data.DatosPrueba;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //DatosPrueba.inicializar();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("LoginView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        String css = Objects.requireNonNull(getClass().getResource("/co/edu/uniquindio/poo/finalproject/css/style.css")).toExternalForm();
        scene.getStylesheets().add(css);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }
}

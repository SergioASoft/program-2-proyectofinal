module co.edu.uniquindio.poo.finalproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens co.edu.uniquindio.poo.finalproject to javafx.fxml;
    exports co.edu.uniquindio.poo.finalproject;
    exports co.edu.uniquindio.poo.finalproject.viewController;
    opens co.edu.uniquindio.poo.finalproject.viewController to javafx.fxml;
}
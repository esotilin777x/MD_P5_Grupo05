module com.mycompany.md_proyectofinal {
    requires javafx.controls;
    requires java.base;
    requires javafx.fxml;
    opens com.mycompany.md_proyectofinal to javafx.fxml;
    exports com.mycompany.md_proyectofinal;
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.md_proyectofinal;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author LENOVO
 */
public class InicioController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    private Button buttonCode;
    
    @FXML
    private Button buttonDecode;
    
    @FXML
    private ImageView logoImageView;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Cargar la imagen al inicializar
        Image logoImage = new Image(getClass().getResourceAsStream("/com/mycompany/md_proyectofinal/interfaz/logo_espol.png"));
        logoImageView.setImage(logoImage);
    }

    
    @FXML
    private void goToDecode() {
        try {
            // Cargar la vista decode.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/md_proyectofinal/interfaz/decode.fxml"));
            Parent root = loader.load();

            // Obtener la escena actual y establecer el nuevo root
            Stage stage = (Stage) buttonDecode.getScene().getWindow();  // Obtén la ventana actual
            stage.setScene(new Scene(root));  // Establece la nueva escena
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToCode() {
        try {
            // Cargar la vista code.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/md_proyectofinal/interfaz/code.fxml"));
            Parent root = loader.load();

            // Obtener la escena actual y establecer el nuevo root
            Stage stage = (Stage) buttonCode.getScene().getWindow();  // Obtén la ventana actual
            stage.setScene(new Scene(root));  // Establece la nueva escena
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


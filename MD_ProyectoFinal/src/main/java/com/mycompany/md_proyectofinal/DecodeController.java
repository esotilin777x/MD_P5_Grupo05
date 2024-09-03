/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.md_proyectofinal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Popup;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import modelo.HuffmanTree;
import modelo.HuffmanTreeNode;

/**
 * FXML Controller class
 *
 * @author LENOVO
 */
public class DecodeController implements Initializable {

    @FXML
    private AnchorPane AP;
    @FXML
    private TextField BitsTF;
    @FXML
    private TextArea decodedText;
    @FXML
    private ScrollPane huffmanTreeScrollPane;
    
    private List<String> serializedTrees;
    
    @FXML
    private VBox treeListVBox;  // Contenedor dentro del ScrollPane para los árboles
    
    @FXML
    private Button buttonLimpiar;
    
    @FXML
    private Button buttonVolver;

    private HuffmanTreeNode huffmanTreeRoot;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadTreesFromFile();  // Cargar los árboles desde el archivo al iniciar la escena
        populateTreeList();   // Mostrar los árboles en el VBox dentro del ScrollPane
    }
    
    @FXML
    private void volver() {
        try {
            // Cargar la vista decode.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/md_proyectofinal/interfaz/code.fxml"));
            Parent root = loader.load();

            // Obtener la escena actual y establecer el nuevo root
            Stage stage = (Stage) buttonVolver.getScene().getWindow();  // Obtén la ventana actual
            stage.setScene(new Scene(root));  // Establece la nueva escena
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void getBitsTF() {
        String text = BitsTF.getText();
        if (text.isEmpty()) {
            showAlert("Error", "Por favor ingrese un texto para decodificar.");
            return;
        }

        if (huffmanTreeRoot == null) {
            showAlert("Error", "El árbol de Huffman no ha sido generado. Por favor, codifique primero un texto.");
            return;
        }

        // Decodificar el texto
        String decoded = HuffmanTree.decode(text, huffmanTreeRoot);
        decodedText.setText(decoded);
    }

    @FXML
    private void onShowTree() {
        if (huffmanTreeRoot != null) {
            Popup treePopup = new Popup();
            Label treeLabel = new Label("Visualización del Árbol Huffman (implementación gráfica necesaria)");
            treePopup.getContent().add(treeLabel);
            treePopup.setAutoHide(true);

            treePopup.show(AP.getScene().getWindow());
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    // Método para cargar árboles desde el archivo
    public void loadTreesFromFile() {
        serializedTrees = new ArrayList<>();
        File file = new File("archivos/arbolesGenerados.txt");
        if (!file.exists()) {
            System.out.println("El archivo no existe, no se pueden cargar árboles.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                serializedTrees.add(line);  // Guardar cada línea (árbol serializado) en la lista
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    
      // Método para poblar el VBox con los árboles cargados
    public void populateTreeList() {
        treeListVBox.getChildren().clear();  // Limpiar cualquier contenido previo

        for (int i = 0; i < serializedTrees.size(); i++) {
            String serializedTree = serializedTrees.get(i);
            Label treeLabel = new Label("Árbol " + (i + 1));
            Button viewButton = new Button("Ver");
            Button selectButton = new Button("Seleccionar");

            // Configurar acción del botón "Ver"
            viewButton.setOnAction(e -> showTreeView(serializedTree));

            // Configurar acción del botón "Seleccionar"
            selectButton.setOnAction(e -> selectTree(serializedTree));

            // Añadir los elementos al VBox
            VBox treeItemBox = new VBox(5, treeLabel, viewButton, selectButton);
            treeListVBox.getChildren().add(treeItemBox);
        }
    }

    
    @FXML
    private void showTreeView(String serializedTree) {
        try {
            // Deserializar el árbol desde la cadena de texto
            HuffmanTreeNode deserializedRoot = deserializeTree(serializedTree);

            // Cargar el FXML de arbolGenerado.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/md_proyectofinal/interfaz/arbolGenerado.fxml"));
            Parent parentRoot = loader.load();

            // Obtener el controlador y pasarle el árbol de Huffman
            ArbolGeneradoController controller = loader.getController();
            controller.setHuffmanTree(deserializedRoot);

            // Crear un nuevo Stage (ventana) para mostrar la visualización del árbol
            Stage newStage = new Stage();
            newStage.setTitle("Visualización del Árbol Huffman");
            newStage.setScene(new Scene(parentRoot));
            newStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "No se pudo cargar la visualización del árbol.");
        }
    }




    private String getTreeAsText(HuffmanTreeNode root) {
        return generateTreeText(root, "", true);
    }

    private String generateTreeText(HuffmanTreeNode node, String prefix, boolean isTail) {
        StringBuilder builder = new StringBuilder();

        if (node.getRight() != null) {
            builder.append(generateTreeText(node.getRight(), prefix + (isTail ? "│   " : "    "), false));
        }

        builder.append(prefix).append(isTail ? "└── " : "┌── ").append(node.getData()).append(" (").append(node.getFreq()).append(")").append("\n");

        if (node.getLeft() != null) {
            builder.append(generateTreeText(node.getLeft(), prefix + (isTail ? "    " : "│   "), true));
        }

        return builder.toString();
    }


    
     // Seleccionar el árbol para decodificación
    private void selectTree(String serializedTree) {
        huffmanTreeRoot = deserializeTree(serializedTree);
        showAlert("Árbol seleccionado", "Árbol seleccionado para decodificación.");
    }
    
    // Método para deserializar el árbol (debe coincidir con cómo se serializó)
    private HuffmanTreeNode deserializeTree(String serializedTree) {
       Queue<String> nodes = new LinkedList<>(Arrays.asList(serializedTree.split(" ")));
       return deserializeTreeHelper(nodes);
   }

   private HuffmanTreeNode deserializeTreeHelper(Queue<String> nodes) {
       String val = nodes.poll();
       if (val.equals("#")) {
           return null;
       }

       String[] parts = val.split(":");
       char data = parts[0].charAt(0);
       int freq = Integer.parseInt(parts[1]);

       HuffmanTreeNode node = new HuffmanTreeNode(data, freq);
       node.setLeft(deserializeTreeHelper(nodes));
       node.setRight(deserializeTreeHelper(nodes));

       return node;
   }

    
    @FXML
    private void cleanAll() {
        // Limpiar el archivo arbolesGenerados.txt
        File file = new File("archivos/arbolesGenerados.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Sobrescribimos el archivo con un contenido vacío
            writer.write("");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Hubo un error al limpiar el archivo: " + e.getMessage());
        }

        // Limpiar el VBox que contiene la lista de árboles
        treeListVBox.getChildren().clear();

        // Limpiar cualquier otro campo de texto o área de la interfaz
        BitsTF.clear();
        decodedText.clear();

        // Notificar al usuario que la limpieza se realizó correctamente
        showAlert("Éxito", "Todos los árboles han sido eliminados y la interfaz ha sido limpiada.");
    }


}

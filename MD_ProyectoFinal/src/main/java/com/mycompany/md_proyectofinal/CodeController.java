/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.md_proyectofinal;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import modelo.*;
import java.net.URL;
import java.util.*;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Popup;
import javafx.stage.Stage;
import com.mycompany.md_proyectofinal.DecodeController;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author LENOVO
 */
public class CodeController implements Initializable {
    
    @FXML
    private AnchorPane AP;
    @FXML
    private TextField textTF;

    @FXML
    private TextField BitsTF;
    
    @FXML
    private Button buttonVolver;
    
    @FXML
    private Button buttonDecode;
    
    @FXML
    private TextArea encodedText;

    @FXML
    private TextArea decodedText;
    
    @FXML
    private VBox treeListVBox;
    
    private HuffmanTreeNode huffmanTreeRoot;
    private Map<Character, String> huffmanCodeMap;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    private void volver() {
        try {
            // Cargar la vista decode.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/md_proyectofinal/interfaz/inicio.fxml"));
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
    private void getTextTF() {
        String text = textTF.getText();
        if (text.isEmpty()) {
            showAlert("Error", "Por favor ingrese un texto para codificar.");
            return;
        }

        // Generar el mapa de códigos de Huffman
        huffmanCodeMap = generateHuffmanCodeMap(text);
        // Codificar el texto
        String encoded = HuffmanTree.encode(text, huffmanCodeMap);
        encodedText.setText(encoded);
    }

    
    @FXML
    public void saveOptimalTree() {
        if (huffmanTreeRoot != null) {
            try {
                // Crear directorio si no existe
                File directory = new File("archivos");
                if (!directory.exists()) {
                    directory.mkdirs();  // Crea el directorio si no existe
                }

                // Especificar la ruta del archivo con la extensión .txt
                File file = new File("archivos/arbolesGenerados.txt");
                System.out.println("File path: " + file.getAbsolutePath());

                // Guardar el árbol en el archivo en modo append (adición)
                BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));  // 'true' habilita el modo de adición
                String serializedTree = serializeTree(huffmanTreeRoot);
                writer.write(serializedTree);
                writer.newLine();
                writer.flush();  // Asegurar que se escribe todo el contenido
                writer.close();
                System.out.println("Tree written to file: " + serializedTree); // Verifica que se escribió
                showAlert("Éxito", "Árbol de Huffman guardado correctamente.");

                // **ACTUALIZAR LA INTERFAZ**
                // Cargar la vista decode.fxml para actualizar la lista de árboles
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/md_proyectofinal/interfaz/decode.fxml"));
                Parent root = loader.load();

                // Obtener el controlador de DecodeController
                DecodeController decodeController = loader.getController();

                // Recargar los árboles desde el archivo y actualizar el VBox en DecodeController
                decodeController.loadTreesFromFile();   // Vuelve a cargar los árboles desde el archivo
                decodeController.populateTreeList();    // Actualiza la lista de árboles en el VBox

                // Actualizar la escena para reflejar los cambios (opcional)
                Stage stage = (Stage) AP.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Hubo un error al guardar el árbol de Huffman: " + e.getMessage());
            }
        } else {
            showAlert("Error", "No hay un árbol de Huffman generado para guardar.");
        }
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




    private String serializeTree(HuffmanTreeNode root) {
        StringBuilder sb = new StringBuilder();
        serializeTreeHelper(root, sb);
        String serializedTree = sb.toString();
        System.out.println("Serialized Tree: " + serializedTree); // Verifica la salida
        return serializedTree;
    }


    private void serializeTreeHelper(HuffmanTreeNode node, StringBuilder sb) {
        if (node == null) {
            sb.append("# ");
            return;
        }
        sb.append(node.getData()).append(":").append(node.getFreq()).append(" ");
        serializeTreeHelper(node.getLeft(), sb);
        serializeTreeHelper(node.getRight(), sb);
    }

    
    @FXML
    private void showTree() {
        if (huffmanTreeRoot != null) {
            try {
                // Cargar el FXML del árbol generado
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/md_proyectofinal/interfaz/arbolGenerado.fxml"));
                Parent root = loader.load();

                // Obtener el controlador y pasarle el árbol de Huffman
                ArbolGeneradoController controller = loader.getController();
                controller.setHuffmanTree(huffmanTreeRoot);

                // Mostrar la nueva escena
                Stage stage = new Stage();
                stage.setTitle("Árbol Generado");
                stage.setScene(new Scene(root));
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "No se pudo cargar la visualización del árbol.");
            }
        } else {
            showAlert("Error", "No hay un árbol generado para mostrar.");
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
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private Map<Character, String> generateHuffmanCodeMap(String text) {
        // Contar la frecuencia de cada carácter en el texto
        Map<Character, Integer> frequencyMap = new HashMap<>();
        for (char c : text.toCharArray()) {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }

        // Crear arrays de caracteres y frecuencias
        char[] data = new char[frequencyMap.size()];
        int[] freq = new int[frequencyMap.size()];
        int index = 0;
        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            data[index] = entry.getKey();
            freq[index] = entry.getValue();
            index++;
        }

        // Generar el mapa de códigos de Huffman
        Map<Character, String> huffmanCodeMap = HuffmanTree.generateHuffmanCodes(data, freq);

        // Generar el árbol de Huffman
        PriorityQueue<HuffmanTreeNode> pq = new PriorityQueue<>();
        for (int i = 0; i < data.length; i++) {
            pq.add(new HuffmanTreeNode(data[i], freq[i]));
        }
        huffmanTreeRoot = HuffmanTree.generateTree(pq);

        return huffmanCodeMap;
    }  
}

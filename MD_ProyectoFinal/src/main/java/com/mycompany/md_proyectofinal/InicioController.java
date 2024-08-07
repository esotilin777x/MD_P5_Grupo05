/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.md_proyectofinal;
import modelo.*;
import java.net.URL;
import java.util.*;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author LENOVO
 */
public class InicioController implements Initializable {
    
    @FXML
    private AnchorPane AP;
    @FXML
    private TextField textTF;

    @FXML
    private TextField bytesTF;
    
    @FXML
    private TextArea encodedText;

    @FXML
    private TextArea decodedText;
    
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
    private void getBytesTF() {
        String text = bytesTF.getText();
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

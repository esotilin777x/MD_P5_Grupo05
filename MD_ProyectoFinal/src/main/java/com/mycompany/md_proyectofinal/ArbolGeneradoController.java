/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.md_proyectofinal;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import modelo.HuffmanTreeNode;

/**
 * FXML Controller class
 *
 * @author LENOVO
 */
public class ArbolGeneradoController implements Initializable {


    @FXML
    private Pane treePane;

    @FXML
    private Label treeLabel;
    
    @FXML
    private TextArea treeDisplay;

    private HuffmanTreeNode huffmanTreeRoot;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
         treeLabel.setText("Árbol Generado");
         if (huffmanTreeRoot != null) {
            displayTree();
        }
    }
    
    private void displayTree() {
        if (huffmanTreeRoot != null) {
            String treeText = getTreeAsText(huffmanTreeRoot);
            treeDisplay.setText(treeText);
        }
    }
    private String getTreeAsText(HuffmanTreeNode root) {
        // Reutiliza el método generateTreeText para convertir el árbol en texto
        return generateTreeText(root, "", true);
    }

    // Método para recibir el árbol de Huffman y mostrarlo
    public void setHuffmanTree(HuffmanTreeNode root) {
        this.huffmanTreeRoot = root;
        showTree();
    }

    // Mostrar el árbol en el Label (opcionalmente puedes mejorar esto con Canvas)
    private void showTree() {
        String treeText = generateTreeText(huffmanTreeRoot, "", true);
        treeLabel.setText(treeText);
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
    
}

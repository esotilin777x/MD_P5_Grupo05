/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author LENOVO
 */

public class HuffmanTreeNode implements Comparable<HuffmanTreeNode> {

    // Almacena el carácter
    char data;
    
    // Almacena la frecuencia del carácter
    int freq;
    
    // Hijo izquierdo del nodo actual
    HuffmanTreeNode left;
    
    // Hijo derecho del nodo actual
    HuffmanTreeNode right;

    public HuffmanTreeNode(char character, int frequency) {
        data = character;
        freq = frequency;
        left = right = null;
    }

    public int compareTo(HuffmanTreeNode other) {
        return freq - other.freq;
    }

    public char getData() {
        return data;
    }

    public void setData(char data) {
        this.data = data;
    }

    public int getFreq() {
        return freq;
    }

    public void setFreq(int freq) {
        this.freq = freq;
    }

    public HuffmanTreeNode getLeft() {
        return left;
    }

    public void setLeft(HuffmanTreeNode left) {
        this.left = left;
    }

    public HuffmanTreeNode getRight() {
        return right;
    }

    public void setRight(HuffmanTreeNode right) {
        this.right = right;
    }
    
    
}


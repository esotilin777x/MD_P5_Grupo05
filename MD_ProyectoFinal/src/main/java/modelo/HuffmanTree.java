/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.*;
/**
 *
 * @author LENOVO
 */
public class HuffmanTree {

    // Altura máxima del árbol de Huffman
    static final int MAX_SIZE = 100;

    // Función para imprimir el código Huffman para cada carácter
    private static void printCodes(HuffmanTreeNode root, int[] arr, int top, Map<Character, String> huffmanCodeMap) {
        if (root.getLeft() != null) {
            arr[top] = 0;
            printCodes(root.getLeft(), arr, top + 1, huffmanCodeMap);
        }

        if (root.getRight() != null) {
            arr[top] = 1;
            printCodes(root.getRight(), arr, top + 1, huffmanCodeMap);
        }

        if (root.getLeft() == null && root.getRight() == null) {
            StringBuilder code = new StringBuilder();
            for (int i = 0; i < top; ++i) {
                code.append(arr[i]);
            }
            huffmanCodeMap.put(root.getData(), code.toString());
            System.out.println(root.getData() + ": " + code);
        }
    }

    // Función para generar el árbol de codificación Huffman
    private static HuffmanTreeNode generateTree(PriorityQueue<HuffmanTreeNode> pq) {
        while (pq.size() != 1) {
            HuffmanTreeNode left = pq.poll();
            HuffmanTreeNode right = pq.poll();
            HuffmanTreeNode node = new HuffmanTreeNode('$', left.getFreq() + right.getFreq());
            node.setLeft(left);
            node.setRight(right);
            pq.add(node);
        }
        return pq.poll();
    }

    // Función para generar los códigos Huffman
    public static Map<Character, String> generateHuffmanCodes(char[] data, int[] freq) {
        PriorityQueue<HuffmanTreeNode> pq = new PriorityQueue<>();

        for (int i = 0; i < data.length; i++) {
            HuffmanTreeNode newNode = new HuffmanTreeNode(data[i], freq[i]);
            pq.add(newNode);
        }

        HuffmanTreeNode root = generateTree(pq);

        Map<Character, String> huffmanCodeMap = new HashMap<>();
        int[] arr = new int[MAX_SIZE];
        int top = 0;
        printCodes(root, arr, top, huffmanCodeMap);
        return huffmanCodeMap;
    }

    // Método para codificar un mensaje usando los códigos Huffman generados
    public static String encode(String message, Map<Character, String> huffmanCodeMap) {
        StringBuilder encodedMessage = new StringBuilder();
        for (char c : message.toCharArray()) {
            encodedMessage.append(huffmanCodeMap.get(c));
        }
        return encodedMessage.toString();
    }

    // Método para decodificar un mensaje usando el árbol de Huffman
    public static String decode(String encodedMessage, HuffmanTreeNode root) {
        StringBuilder decodedMessage = new StringBuilder();
        HuffmanTreeNode currentNode = root;
        for (char bit : encodedMessage.toCharArray()) {
            currentNode = (bit == '0') ? currentNode.getLeft() : currentNode.getRight();
            if (currentNode.getLeft() == null && currentNode.getRight() == null) {
                decodedMessage.append(currentNode.getData());
                currentNode = root;
            }
        }
        return decodedMessage.toString();
    }

    // Función principal
    public static void main(String[] args) {
        char[] data = { 'a', 'b', 'c', 'd', 'e', 'f' };
        int[] freq = { 5, 9, 12, 13, 16, 45 };

        Map<Character, String> huffmanCodeMap = generateHuffmanCodes(data, freq);

        String message = "abcdef";
        String encodedMessage = encode(message, huffmanCodeMap);
        System.out.println("Encoded Message: " + encodedMessage);

        PriorityQueue<HuffmanTreeNode> pq = new PriorityQueue<>();
        for (int i = 0; i < data.length; i++) {
            pq.add(new HuffmanTreeNode(data[i], freq[i]));
        }

        HuffmanTreeNode root = generateTree(pq);
        String decodedMessage = decode(encodedMessage, root);
        System.out.println("Decoded Message: " + decodedMessage);
    }
}

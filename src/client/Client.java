package client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.net.*;

public class Client extends JFrame {

    private JComboBox<Integer> nodeSelector;
    private JTextField idField, contentField;
    private JButton insertButton, deleteButton, queryButton;
    private JTextArea resultArea;
    private static final int[] PORTS = {2001, 2002, 2003, 2004, 2005, 2006};

    public Client() {
        setTitle("Token Ring System - Universal Client (Test)");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Control panel
        JPanel controlPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        
        controlPanel.add(new JLabel("Select Node:"));
        nodeSelector = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5, 6});
        nodeSelector.setSelectedIndex(0);
        controlPanel.add(nodeSelector);

        controlPanel.add(new JLabel("Data ID:"));
        idField = new JTextField();
        controlPanel.add(idField);

        controlPanel.add(new JLabel("Content:"));
        contentField = new JTextField();
        controlPanel.add(contentField);

        JPanel buttonPanel = new JPanel();
        insertButton = new JButton("Insert Data");
        deleteButton = new JButton("Delete Data");
        queryButton = new JButton("Query All");
        buttonPanel.add(insertButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(queryButton);

        resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(resultArea);

        add(controlPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scroll, BorderLayout.SOUTH);

        insertButton.addActionListener(e -> sendInsertRequest());
        deleteButton.addActionListener(e -> sendDeleteRequest());
        queryButton.addActionListener(e -> sendQueryRequest());

        setVisible(true);
    }

    private void sendInsertRequest() {
        int nodeId = (Integer) nodeSelector.getSelectedItem();
        int port = PORTS[nodeId - 1];
        String id = idField.getText();
        String content = contentField.getText();

        if (id.isEmpty() || content.isEmpty()) {
            resultArea.append("Error: ID and Content cannot be empty\n");
            return;
        }

        try {
            Socket socket = new Socket("localhost", port);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String message = "INSERT|" + id + "|" + content;
            out.println(message);

            String response = in.readLine();
            resultArea.append("[Node " + nodeId + "] Insert response: " + response + "\n");

            socket.close();
        } catch (Exception ex) {
            resultArea.append("Error connecting to Node " + nodeId + ": " + ex.getMessage() + "\n");
        }
    }

    private void sendDeleteRequest() {
        int nodeId = (Integer) nodeSelector.getSelectedItem();
        int port = PORTS[nodeId - 1];
        String id = idField.getText();

        if (id.isEmpty()) {
            resultArea.append("Error: ID cannot be empty\n");
            return;
        }

        try {
            Socket socket = new Socket("localhost", port);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String message = "DELETE|" + id;
            out.println(message);

            String response = in.readLine();
            resultArea.append("[Node " + nodeId + "] Delete response: " + response + "\n");

            socket.close();
        } catch (Exception ex) {
            resultArea.append("Error connecting to Node " + nodeId + ": " + ex.getMessage() + "\n");
        }
    }

    private void sendQueryRequest() {
        int nodeId = (Integer) nodeSelector.getSelectedItem();
        int port = PORTS[nodeId - 1];

        try {
            Socket socket = new Socket("localhost", port);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println("QUERY");

            String line;
            resultArea.append("\n[Node " + nodeId + "] Data:\n");
            while ((line = in.readLine()) != null && !line.isEmpty()) {
                resultArea.append(line + "\n");
            }
            resultArea.append("---\n");

            socket.close();
        } catch (Exception ex) {
            resultArea.append("Error connecting to Node " + nodeId + ": " + ex.getMessage() + "\n");
        }
    }

    public static void main(String[] args) {
        new Client();
    }
}
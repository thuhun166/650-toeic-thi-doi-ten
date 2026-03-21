package distributed.client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.net.*;

public class Client extends JFrame {

    private JTextField positionField, licenseField, typeField, colorField, timeField;
    private JButton insertButton, deleteButton;
    private JTextArea resultArea;

    public Client() {
        setTitle("Distributed System Client");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        inputPanel.add(new JLabel("Position:"));
        positionField = new JTextField();
        inputPanel.add(positionField);
        inputPanel.add(new JLabel("License:"));
        licenseField = new JTextField();
        inputPanel.add(licenseField);
        inputPanel.add(new JLabel("Type:"));
        typeField = new JTextField();
        inputPanel.add(typeField);
        inputPanel.add(new JLabel("Color:"));
        colorField = new JTextField();
        inputPanel.add(colorField);
        inputPanel.add(new JLabel("Time:"));
        timeField = new JTextField();
        inputPanel.add(timeField);

        JPanel buttonPanel = new JPanel();
        insertButton = new JButton("Insert");
        deleteButton = new JButton("Delete");
        buttonPanel.add(insertButton);
        buttonPanel.add(deleteButton);

        resultArea = new JTextArea();
        JScrollPane scroll = new JScrollPane(resultArea);

        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scroll, BorderLayout.SOUTH);

        insertButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendRequest("I");
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sendRequest("D");
            }
        });

        setVisible(true);
    }

    private void sendRequest(String action) {
        try {
            Socket socket = new Socket("127.0.0.1", 2001);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String message = "@$1|" + "100000" + "|1|Server1|DATA|" + action + "|1$$" + positionField.getText() + "|" + licenseField.getText() + "|" + typeField.getText() + "|" + colorField.getText() + "|" + timeField.getText() + "$@";
            out.writeBytes(message);
            out.write(13);
            out.write(10);
            out.flush();

            String response = in.readLine();
            resultArea.append("Response: " + response + "\n");

            socket.close();
        } catch (Exception ex) {
            resultArea.append("Error: " + ex.getMessage() + "\n");
        }
    }

    public static void main(String[] args) {
        new Client();
    }
}
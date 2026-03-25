package client;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URI;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Client extends JFrame {

    private static final NodeEndpoint[] DEFAULT_ENDPOINTS = new NodeEndpoint[]{
            new NodeEndpoint("localhost", 2001),
            new NodeEndpoint("localhost", 2002),
            new NodeEndpoint("localhost", 2003),
            new NodeEndpoint("localhost", 2004),
            new NodeEndpoint("localhost", 2005),
            new NodeEndpoint("localhost", 2006)
    };

    private final NodeEndpoint[] endpoints;
    private JComboBox<String> nodeSelector;
    private JTextField jobIdField;
    private JTextField contentField;
    private JTextArea resultArea;

    public Client() {
        this.endpoints = loadEndpoints();

        setTitle("Client In Phan Tan");
        setSize(760, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        controlPanel.add(new JLabel("Chon may in:"));
        nodeSelector = new JComboBox<>(buildNodeChoices());
        nodeSelector.setSelectedIndex(0);
        controlPanel.add(nodeSelector);

        controlPanel.add(new JLabel("Ma job:"));
        jobIdField = new JTextField();
        controlPanel.add(jobIdField);

        controlPanel.add(new JLabel("Noi dung tai lieu:"));
        contentField = new JTextField();
        controlPanel.add(contentField);

        JPanel buttonPanel = new JPanel();
        JButton printButton = new JButton("Gui Job In");
        JButton cancelButton = new JButton("Huy Job");
        JButton queryButton = new JButton("Xem Danh Sach Job");
        JButton statusButton = new JButton("Trang Thai Vong");
        JButton serverGuiButton = new JButton("Mo GUI Quan Ly Server");
        buttonPanel.add(printButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(queryButton);
        buttonPanel.add(statusButton);
        buttonPanel.add(serverGuiButton);

        resultArea = new JTextArea();
        resultArea.setEditable(false);

        add(controlPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(new JScrollPane(resultArea), BorderLayout.SOUTH);

        printButton.addActionListener(e -> submitPrintJob());
        cancelButton.addActionListener(e -> cancelJob());
        queryButton.addActionListener(e -> sendCommand("QUERY", "Nhat ky job"));
        statusButton.addActionListener(e -> sendCommand("STATUS", "Trang thai vong"));
        serverGuiButton.addActionListener(e -> new ServerManagerGUI());

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private String[] buildNodeChoices() {
        String[] nodeChoices = new String[endpoints.length];
        for (int i = 0; i < endpoints.length; i++) {
            nodeChoices[i] = "May in " + (i + 1) + " - " + endpoints[i].host + ":" + endpoints[i].port;
        }
        return nodeChoices;
    }

    private void submitPrintJob() {
        String jobId = jobIdField.getText().trim();
        String content = contentField.getText().trim();
        if (jobId.isEmpty() || content.isEmpty()) {
            resultArea.append("Loi: Ma job va noi dung tai lieu khong duoc de trong\n");
            return;
        }

        sendCommand("PRINT|" + jobId + "|" + content, "Gui job in");
    }

    private void cancelJob() {
        String jobId = jobIdField.getText().trim();
        if (jobId.isEmpty()) {
            resultArea.append("Loi: Ma job khong duoc de trong\n");
            return;
        }

        sendCommand("CANCEL|" + jobId, "Huy job");
    }

    private void sendCommand(String command, String label) {
        int selectedIndex = nodeSelector.getSelectedIndex();
        int selectedNode = selectedIndex + 1;
        NodeEndpoint endpoint = endpoints[selectedIndex];

        try (Socket socket = new Socket(endpoint.host, endpoint.port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println(command);
            resultArea.append("\n[Node " + selectedNode + " - " + endpoint.host + ":" + endpoint.port + "] " + label + ":\n");

            String line;
            boolean hasOutput = false;
            while ((line = in.readLine()) != null) {
                resultArea.append(line + "\n");
                hasOutput = true;
            }

            if (!hasOutput) {
                resultArea.append("(khong co phan hoi)\n");
            }

            resultArea.append("---\n");
        } catch (Exception ex) {
            resultArea.append("Loi ket noi toi Node " + selectedNode + " (" + endpoint.host + ":" + endpoint.port + "): "
                    + ex.getMessage() + "\n");
        }
    }

    private NodeEndpoint[] loadEndpoints() {
        String configuredNodes = System.getenv("NODE_ADDRESSES");
        if (configuredNodes == null || configuredNodes.trim().isEmpty()) {
            return DEFAULT_ENDPOINTS;
        }

        String[] rawNodes = configuredNodes.split(",");
        NodeEndpoint[] parsed = new NodeEndpoint[rawNodes.length];
        for (int i = 0; i < rawNodes.length; i++) {
            parsed[i] = parseEndpoint(rawNodes[i].trim());
        }
        return parsed;
    }

    private NodeEndpoint parseEndpoint(String rawValue) {
        if (rawValue.contains("://")) {
            URI uri = URI.create(rawValue);
            int port = uri.getPort() > 0 ? uri.getPort() : 8080;
            return new NodeEndpoint(uri.getHost(), port);
        }

        if (rawValue.contains(":")) {
            int lastColon = rawValue.lastIndexOf(':');
            String host = rawValue.substring(0, lastColon).trim();
            int port = Integer.parseInt(rawValue.substring(lastColon + 1).trim());
            return new NodeEndpoint(host, port);
        }

        return new NodeEndpoint(rawValue, 8080);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Client::new);
    }

    private static class NodeEndpoint {
        private final String host;
        private final int port;

        private NodeEndpoint(String host, int port) {
            this.host = host;
            this.port = port;
        }
    }
}

package client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URI;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class ServerManagerGUI extends JFrame {

    private static final NodeEndpoint[] DEFAULT_ENDPOINTS = new NodeEndpoint[]{
            new NodeEndpoint("localhost", 2001),
            new NodeEndpoint("localhost", 2002),
            new NodeEndpoint("localhost", 2003),
            new NodeEndpoint("localhost", 2004),
            new NodeEndpoint("localhost", 2005),
            new NodeEndpoint("localhost", 2006)
    };

    private static final int CONNECT_TIMEOUT_MS = 3000;
    private static final int READ_TIMEOUT_MS = 3000;
    private static final int AUTO_REFRESH_MS = 1000;

    private final NodeEndpoint[] endpoints;
    private final String[] cachedLogs;
    private final Timer autoRefreshTimer;

    private JComboBox<String> serverSelector;
    private JTextArea aggregateTokenArea;
    private JTextArea selectedServerArea;
    private JCheckBox autoRefreshCheckBox;

    public ServerManagerGUI() {
        this.endpoints = loadEndpoints();
        this.cachedLogs = new String[endpoints.length];
        this.autoRefreshTimer = new Timer(AUTO_REFRESH_MS, e -> refreshLogs(false));

        setTitle("GUI Quan Ly Server");
        setSize(1100, 760);
        setMinimumSize(new Dimension(960, 680));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        add(buildTopPanel(), BorderLayout.NORTH);
        add(buildCenterPanel(), BorderLayout.CENTER);

        refreshLogs(true);
        setLocationRelativeTo(null);
        setVisible(true);
        autoRefreshTimer.start();
    }

    private JPanel buildTopPanel() {
        JPanel container = new JPanel(new BorderLayout(8, 8));
        container.setBorder(BorderFactory.createTitledBorder("Man hinh xem log 6 server"));

        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        infoPanel.add(new JLabel("GUI nay chi doc log token tu 6 server, khong gui job in."));

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        JButton refreshButton = new JButton("Lam moi log");
        autoRefreshCheckBox = new JCheckBox("Tu dong lam moi 1 giay");
        autoRefreshCheckBox.setSelected(true);
        actionPanel.add(refreshButton);
        actionPanel.add(autoRefreshCheckBox);

        refreshButton.addActionListener(e -> refreshLogs(true));
        autoRefreshCheckBox.addActionListener(e -> toggleAutoRefresh());

        container.add(infoPanel, BorderLayout.NORTH);
        container.add(actionPanel, BorderLayout.SOUTH);
        return container;
    }

    private JSplitPane buildCenterPanel() {
        aggregateTokenArea = new JTextArea();
        aggregateTokenArea.setEditable(false);
        aggregateTokenArea.setLineWrap(true);
        aggregateTokenArea.setWrapStyleWord(true);

        JScrollPane topScroll = new JScrollPane(aggregateTokenArea);
        topScroll.setBorder(BorderFactory.createTitledBorder("Log token tong hop cua 6 server"));

        JPanel bottomPanel = new JPanel(new BorderLayout(8, 8));
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Log chi tiet theo tung server"));

        JPanel selectorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        selectorPanel.add(new JLabel("Chon server:"));
        serverSelector = new JComboBox<>(buildNodeChoices());
        serverSelector.setSelectedIndex(0);
        serverSelector.addActionListener(e -> updateSelectedServerArea());
        selectorPanel.add(serverSelector);

        selectedServerArea = new JTextArea();
        selectedServerArea.setEditable(false);
        selectedServerArea.setLineWrap(true);
        selectedServerArea.setWrapStyleWord(true);

        bottomPanel.add(selectorPanel, BorderLayout.NORTH);
        bottomPanel.add(new JScrollPane(selectedServerArea), BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topScroll, bottomPanel);
        splitPane.setResizeWeight(0.5);
        splitPane.setDividerLocation(320);
        return splitPane;
    }

    private String[] buildNodeChoices() {
        String[] nodeChoices = new String[endpoints.length];
        for (int i = 0; i < endpoints.length; i++) {
            nodeChoices[i] = "Server " + (i + 1) + " - " + endpoints[i].host + ":" + endpoints[i].port;
        }
        return nodeChoices;
    }

    private void refreshLogs(boolean announce) {
        runInBackground(() -> {
            if (announce) {
                appendAggregateMessage("Dang lay log tu 6 server...");
            }

            for (int i = 0; i < endpoints.length; i++) {
                cachedLogs[i] = sendCommandToNode(i, "LOGS");
            }

            updateAggregateArea();
            updateSelectedServerArea();
        });
    }

    private void updateAggregateArea() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < cachedLogs.length; i++) {
            String filtered = filterTokenLines(cachedLogs[i]);
            if (!filtered.isEmpty()) {
                if (builder.length() > 0 && builder.charAt(builder.length() - 1) != '\n') {
                    builder.append("\n");
                }
                builder.append(filtered);
                if (builder.charAt(builder.length() - 1) != '\n') {
                    builder.append("\n");
                }
            }
        }

        String content = builder.length() == 0 ? "CHUA_CO_LOG_TOKEN" : builder.toString().trim();
        SwingUtilities.invokeLater(() -> {
            aggregateTokenArea.setText(content);
            aggregateTokenArea.setCaretPosition(aggregateTokenArea.getDocument().getLength());
        });
    }

    private void updateSelectedServerArea() {
        int selectedIndex = serverSelector == null ? 0 : serverSelector.getSelectedIndex();
        String logContent = cachedLogs[selectedIndex];
        if (logContent == null || logContent.trim().isEmpty()) {
            logContent = "CHUA_CO_LOG";
        } else if ("LOI|Lenh khong hop le".equalsIgnoreCase(logContent.trim())) {
            logContent = "Server nay dang chay ban cu, chua ho tro lenh LOGS.\n"
                    + "Can compile lai va redeploy 6 server Railway de GUI doc duoc log token.";
        }

        String finalLogContent = logContent;
        SwingUtilities.invokeLater(() -> {
            selectedServerArea.setText(finalLogContent);
            selectedServerArea.setCaretPosition(selectedServerArea.getDocument().getLength());
        });
    }

    private String filterTokenLines(String rawLog) {
        if (rawLog == null || rawLog.trim().isEmpty()) {
            return "";
        }

        if ("LOI|Lenh khong hop le".equalsIgnoreCase(rawLog.trim())) {
            return "Server chua cap nhat ban moi co endpoint LOGS. Can redeploy lai server.";
        }

        if (rawLog.startsWith("LOI|")) {
            return rawLog;
        }

        StringBuilder builder = new StringBuilder();
        String[] lines = rawLog.split("\\R");
        for (String line : lines) {
            String lower = line.toLowerCase();
            if (lower.contains("token")) {
                builder.append(line).append("\n");
            }
        }
        return builder.toString().trim();
    }

    private void toggleAutoRefresh() {
        if (autoRefreshCheckBox.isSelected()) {
            autoRefreshTimer.start();
        } else {
            autoRefreshTimer.stop();
        }
    }

    private void appendAggregateMessage(String message) {
        SwingUtilities.invokeLater(() -> aggregateTokenArea.setText(message));
    }

    private String sendCommandToNode(int nodeIndex, String command) {
        NodeEndpoint endpoint = endpoints[nodeIndex];

        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(endpoint.host, endpoint.port), CONNECT_TIMEOUT_MS);
            socket.setSoTimeout(READ_TIMEOUT_MS);

            try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                out.println(command);

                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    if (builder.length() > 0) {
                        builder.append("\n");
                    }
                    builder.append(line);
                }

                if (builder.length() == 0) {
                    return "(khong co phan hoi)";
                }

                return builder.toString();
            }
        } catch (SocketTimeoutException ex) {
            return "LOI|Het thoi gian cho phan hoi tu " + endpoint.host + ":" + endpoint.port;
        } catch (Exception ex) {
            return "LOI|Khong ket noi duoc toi " + endpoint.host + ":" + endpoint.port + " - " + ex.getMessage();
        }
    }

    private void runInBackground(Runnable task) {
        Thread worker = new Thread(task, "server-manager-log-viewer");
        worker.setDaemon(true);
        worker.start();
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
        SwingUtilities.invokeLater(ServerManagerGUI::new);
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

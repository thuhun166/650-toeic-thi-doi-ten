package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class Main {

    private static final AtomicLong LAMPORT_CLOCK = new AtomicLong(0);

    public static void main(String[] args) {
        Map<String, String> env = System.getenv();

        int nodeId = parseInt(env.get("NODE_ID"), 1);
        int port = parseInt(env.get("PORT"), 8080);
        String bindHost = firstNonBlank(env.get("BIND_HOST"), "0.0.0.0");
        String mysqlUrl = firstNonBlank(env.get("MYSQL_URL"), env.get("DATABASE_URL"),
                "jdbc:mysql://localhost:3306/db" + nodeId);
        String peers = firstNonBlank(env.get("PEERS"), defaultPeers());
        long tokenPassDelayMs = parseLong(env.get("TOKEN_PASS_DELAY_MS"), 1500L);
        long tokenMonitorIntervalMs = parseLong(env.get("TOKEN_MONITOR_INTERVAL_MS"), 2000L);
        long tokenLossTimeoutMs = parseLong(env.get("TOKEN_LOSS_TIMEOUT_MS"),
                Math.max(tokenPassDelayMs * 8L, 12000L));
        int connectTimeoutMs = parseInt(env.get("SOCKET_CONNECT_TIMEOUT_MS"), 3000);

        RoutingTable routing = new RoutingTable(peers, port);
        if (nodeId < 1 || nodeId > routing.size()) {
            throw new IllegalArgumentException("NODE_ID must be between 1 and " + routing.size());
        }

        System.out.println("[Node " + nodeId + "] Khoi dong may chu in tai " + bindHost + ":" + port);
        System.out.println("[Node " + nodeId + "] Danh sach nut trong vong: " + routing.describe());

        Database db = new Database(mysqlUrl, nodeId);
        TokenRing tokenRing = new TokenRing(nodeId, routing, db, LAMPORT_CLOCK,
                tokenPassDelayMs, tokenMonitorIntervalMs, tokenLossTimeoutMs, connectTimeoutMs);

        Runtime.getRuntime().addShutdownHook(new Thread(tokenRing::shutdown));

        try (ServerSocket serverSocket = createServerSocket(bindHost, port)) {
            System.out.println("[Node " + nodeId + "] Dang lang nghe tai cong " + port);
            tokenRing.start();

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new NodeHandler(clientSocket, nodeId, tokenRing)).start();
            }
        } catch (IOException ex) {
            System.err.println("[Node " + nodeId + "] Khong the khoi dong may chu: " + ex.getMessage());
        }
    }

    private static ServerSocket createServerSocket(String bindHost, int port) throws IOException {
        IOException lastError = null;

        for (String candidateHost : new String[]{bindHost, "0.0.0.0"}) {
            try {
                ServerSocket serverSocket = new ServerSocket();
                serverSocket.setReuseAddress(true);
                serverSocket.bind(new InetSocketAddress(candidateHost, port));
                return serverSocket;
            } catch (IOException ex) {
                lastError = ex;
            }
        }

        throw lastError;
    }

    private static String defaultPeers() {
        return "localhost:2001,localhost:2002";
    }

    private static String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.trim().isEmpty()) {
                return value.trim();
            }
        }
        return "";
    }

    private static int parseInt(String rawValue, int defaultValue) {
        try {
            return rawValue == null ? defaultValue : Integer.parseInt(rawValue.trim());
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    private static long parseLong(String rawValue, long defaultValue) {
        try {
            return rawValue == null ? defaultValue : Long.parseLong(rawValue.trim());
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }
}

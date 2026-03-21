package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    static AtomicInteger lamportClock = new AtomicInteger(0);

    public static void main(String[] args) {
        int nodeId = Integer.parseInt(System.getenv().getOrDefault("NODE_ID", "1"));
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "2001"));
        String mysqlUrl = System.getenv().getOrDefault("MYSQL_URL", "jdbc:mysql://localhost:3306/db1");
        String peers = System.getenv().getOrDefault("PEERS", "localhost:2001,localhost:2002,localhost:2003,localhost:2004,localhost:2005,localhost:2006");

        System.out.println("[Node " + nodeId + "] Starting server on port " + port);

        // Initialize database with node-specific URL
        Database db = new Database(mysqlUrl, nodeId);

        // Initialize routing table with peers
        RoutingTable routing = new RoutingTable(peers);

        // Node 1 starts with token
        boolean hasToken = (nodeId == 1);
        TokenRing tokenRing = new TokenRing(nodeId, routing, hasToken, db, lamportClock);

        // Start node server
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("[Node " + nodeId + "] Listening on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new NodeHandler(clientSocket, nodeId, tokenRing, db, lamportClock)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
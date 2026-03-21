package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class TokenRing {

    private int nodeId;
    private RoutingTable routing;
    private boolean hasToken;
    private Database db;
    private AtomicInteger lamportClock;
    private int tokenCount = 0;

    public TokenRing(int nodeId, RoutingTable routing, boolean hasToken, Database db, AtomicInteger lamportClock) {
        this.nodeId = nodeId;
        this.routing = routing;
        this.hasToken = hasToken;
        this.db = db;
        this.lamportClock = lamportClock;
    }

    public synchronized void processRequest(String message) {
        lamportClock.incrementAndGet();
        System.out.println("[Node " + nodeId + "] Processing request with Lamport clock: " + lamportClock.get());
        System.out.println("[Node " + nodeId + "] Message: " + message);

        // Parse and store data
        String[] parts = message.split("\\|");
        if (parts.length >= 3) {
            String id = parts[0];
            String content = parts[1];
            String time = String.valueOf(System.currentTimeMillis());
            String status = "processed";
            db.insertData(id, content, time, status);
            System.out.println("[Node " + nodeId + "] Data stored in DB");
        }
    }

    public synchronized void passToken() {
        if (!hasToken) {
            System.out.println("[Node " + nodeId + "] Waiting for token...");
            return;
        }

        tokenCount++;
        System.out.println("[Node " + nodeId + "] Has token (count: " + tokenCount + "), passing to next node");

        // Find next node
        int nextNodeId = (nodeId % 6) + 1;
        VirtualCircle nextNode = routing.table[nextNodeId - 1];

        try {
            Socket socket = new Socket(nextNode.destination, nextNode.port);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println("TOKEN|" + nodeId + "|" + lamportClock.get());
            socket.close();
            System.out.println("[Node " + nodeId + "] Token passed to Node " + nextNodeId);
        } catch (IOException e) {
            System.out.println("[Node " + nodeId + "] Error passing token: " + e.getMessage());
        }

        hasToken = false;
    }

    public synchronized void receiveToken(int fromNode) {
        lamportClock.incrementAndGet();
        hasToken = true;
        System.out.println("[Node " + nodeId + "] Received token from Node " + fromNode);
        System.out.println("[Node " + nodeId + "] Lamport clock updated to: " + lamportClock.get());
        
        // Auto-pass token after short delay
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                passToken();
            } catch (InterruptedException e) {
            }
        }).start();
    }

    public boolean hasToken() {
        return hasToken;
    }

    public String getData() {
        return db.getAllData();
    }
}
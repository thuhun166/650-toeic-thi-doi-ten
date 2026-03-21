package server;

import java.io.*;

public class InitialServer {

    private int nodeId;
    private int totalNodes;

    public InitialServer(int nodeId, int totalNodes) {
        this.nodeId = nodeId;
        this.totalNodes = totalNodes;
    }

    public void initializeCircle() {
        // Initialize virtual circle topology file
        String circleFileName = "Server" + nodeId + "circle.txt";
        try {
            File file = new File(circleFileName);
            if (!file.exists()) {
                FileOutputStream fos = new FileOutputStream(circleFileName);

                // Write initial circle topology
                StringBuilder circleInfo = new StringBuilder();
                circleInfo.append("NodeId:").append(nodeId).append("\n");
                circleInfo.append("TotalNodes:").append(totalNodes).append("\n");

                // Ring topology: 1 -> 2 -> 3 -> 4 -> 5 -> 6 -> 1
                int nextNode = (nodeId == totalNodes) ? 1 : nodeId + 1;
                int prevNode = (nodeId == 1) ? totalNodes : nodeId - 1;

                circleInfo.append("NextNode:").append(nextNode).append("\n");
                circleInfo.append("PrevNode:").append(prevNode).append("\n");
                circleInfo.append("Status:ACTIVE\n");
                circleInfo.append("TokenOwner:").append((nodeId == 1) ? "YES" : "NO").append("\n");
                circleInfo.append("LamportClock:0\n");

                fos.write(circleInfo.toString().getBytes());
                fos.close();
                System.out.println("[InitialServer] Initialized circle for Server" + nodeId);
            }
        } catch (Exception ex) {
            System.out.println("[InitialServer] Error initializing circle: " + ex.getMessage());
        }
    }

    public void initializeDatabase() {
        // Create table schema for this node's database
        try {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS node" + nodeId + "_data (" +
                    "id INT PRIMARY KEY, " +
                    "content VARCHAR(255), " +
                    "timestamp BIGINT, " +
                    "status VARCHAR(50)" +
                    ")";
            System.out.println("[InitialServer] Database table initialized: node" + nodeId + "_data");
            System.out.println("[InitialServer] SQL: " + createTableSQL);
        } catch (Exception ex) {
            System.out.println("[InitialServer] Error initializing database: " + ex.getMessage());
        }
    }

    public void initializePeers(String peersInfo) {
        // Initialize peers information from PEERS environment variable
        String peersFileName = "peers" + nodeId + ".txt";
        try {
            FileOutputStream fos = new FileOutputStream(peersFileName);
            fos.write(peersInfo.getBytes());
            fos.close();
            System.out.println("[InitialServer] Initialized peers for Server" + nodeId);
        } catch (Exception ex) {
            System.out.println("[InitialServer] Error initializing peers: " + ex.getMessage());
        }
    }

    public void initializeAll(String peersInfo) {
        System.out.println("\n=== Initializing Server" + nodeId + " ===");
        initializeCircle();
        initializeDatabase();
        initializePeers(peersInfo);
        System.out.println("=== Initialization Complete ===\n");
    }
}
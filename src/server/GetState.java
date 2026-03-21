package server;

import java.io.*;

public class GetState {

    private String serverName;
    private int currentState;
    private String stateFileName;

    public GetState(String serverName) {
        this.serverName = serverName;
        this.stateFileName = serverName + "state.txt";
        loadState();
    }

    private void loadState() {
        try {
            File file = new File(stateFileName);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                byte[] data = new byte[4];
                fis.read(data);
                currentState = Integer.parseInt(new String(data).trim());
                fis.close();
                System.out.println("[GetState] Loaded state for " + serverName + ": " + currentState);
            } else {
                currentState = 0;
                saveState();
            }
        } catch (Exception ex) {
            System.out.println("[GetState] Error loading state: " + ex.getMessage());
            currentState = 0;
        }
    }

    public void saveState() {
        try {
            FileOutputStream fos = new FileOutputStream(stateFileName);
            fos.write(String.valueOf(currentState).getBytes());
            fos.close();
            System.out.println("[GetState] Saved state for " + serverName + ": " + currentState);
        } catch (Exception ex) {
            System.out.println("[GetState] Error saving state: " + ex.getMessage());
        }
    }

    public int getCurrentState() {
        return currentState;
    }

    public void setCurrentState(int state) {
        this.currentState = state;
        saveState();
    }

    public String getTokenState(int nodeId) {
        // Token representation: 6 bits
        // 100000 = Node 1 has token
        // 010000 = Node 2 has token, etc.
        String binaryToken = "";
        for (int i = 1; i <= 6; i++) {
            binaryToken += (i == nodeId) ? "1" : "0";
        }
        return binaryToken;
    }
}
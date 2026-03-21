package server;

public class RoutingTable {

    public VirtualCircle table[];
    public int max = 6;

    public RoutingTable(String peers) {
        table = new VirtualCircle[6];
        String[] peerList = peers.split(",");
        for (int i = 0; i < 6; i++) {
            String[] parts = peerList[i].split(":");
            table[i] = new VirtualCircle(parts[0], Integer.parseInt(parts[1]), "Server" + (i+1));
        }
        max = 6;
    }
}
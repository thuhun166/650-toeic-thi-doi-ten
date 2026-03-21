package distributed.server1;

public class VirtualCircle {

    public String destination;
    public int port;
    public String name;

    public VirtualCircle(String destination, int port, String name) {
        this.destination = destination;
        this.port = port;
        this.name = name;
    }
}
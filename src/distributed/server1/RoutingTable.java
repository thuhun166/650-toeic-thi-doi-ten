package distributed.server1;

public class RoutingTable {

    public VirtualCircle table[];
    public int max = 6;

    public RoutingTable() {
        table = new VirtualCircle[6];

        VirtualCircle Server1 = new VirtualCircle("127.0.0.1", 2001, "Server1");
        VirtualCircle Server2 = new VirtualCircle("127.0.0.1", 2002, "Server2");
        VirtualCircle Server3 = new VirtualCircle("127.0.0.1", 2003, "Server3");
        VirtualCircle Server4 = new VirtualCircle("127.0.0.1", 2004, "Server4");
        VirtualCircle Server5 = new VirtualCircle("127.0.0.1", 2005, "Server5");
        VirtualCircle Server6 = new VirtualCircle("127.0.0.1", 2006, "Server6");

        table[0] = Server1;
        table[1] = Server2;
        table[2] = Server3;
        table[3] = Server4;
        table[4] = Server5;
        table[5] = Server6;

        max = 6;
    }
}
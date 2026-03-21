package distributed.server1;

import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Color;
import java.awt.Font;
import javax.swing.border.TitledBorder;
import java.util.Hashtable;
import java.net.*;
import java.io.*;
import java.lang.Thread;
import java.util.Hashtable;
import java.util.Vector;

public class Server1 extends JFrame {

    private JFrame mainFrm;
    private JPanel jCPane;
    private JScrollPane scroll;
    static JTextArea display;
    int counter;
    ObjectOutputStream output;
    ObjectInputStream input;
    ServerSocket server;
    Socket client, connection;
    String serverName;
    String type;
    int pos;
    RoutingTable rount;
    int currentCircle;
    static String MESSAGE, replyMessage;
    Hashtable hash;
    DataOutputStream out;
    BufferedReader in;
    Database db1, db;
    ProcessData data, dt;

    public Server1() {
        JFrame mainFrm = new JFrame("Server 1");
        mainFrm.setSize(400, 400);

        jCPane = new JPanel();
        jCPane.setLayout(null);

        scroll = new JScrollPane();
        scroll.setBounds(new Rectangle());
        display = new JTextArea();
        display.setBounds(new Rectangle(10, 10, 370, 345));
        scroll.setViewportView(display);
        scroll.setBounds(new Rectangle(10, 10, 370, 345));
        scroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.blue, 2), "SERVER 1", TitledBorder.CENTER, TitledBorder.CENTER, new Font("Dialog", Font.BOLD, 12), Color.blue));

        jCPane.add(scroll);
        mainFrm.add(jCPane);
        mainFrm.setVisible(true);
        mainFrm.setResizable(false);
    }

    public static class sv1 implements Runnable {

        int counter;
        ObjectOutputStream output;
        ObjectInputStream input;
        ServerSocket server;
        Socket client, connection;
        String serverName;
        String type;
        int pos;
        RoutingTable rount;
        int currentCircle;
        static String MESSAGE, replyMessage;
        Hashtable hash;
        DataOutputStream out;
        BufferedReader in;
        Database db1, db;
        ProcessData data, dt;
        int lamportSave;

        sv1() {
            new Thread(this, "sv1").start();
        }

        public void handler(Socket newSocket, String serverName, int pos, int curr, Hashtable hash) {
            client = newSocket;
            this.serverName = serverName;
            rount = new RoutingTable();
            this.pos = pos;
            this.currentCircle = curr;
            MESSAGE = "";
            this.hash = hash;
        }

        public void run() {
            try {
                server = new ServerSocket(2001);
                hash = new Hashtable();
                db = new Database();
                rount = new RoutingTable();
                currentCircle = 1;
                lamportSave = 0;

                while (true) {
                    client = server.accept();
                    in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    out = new DataOutputStream(client.getOutputStream());

                    String recMess = in.readLine();
                    MessageProcess re = new MessageProcess(recMess);
                    String st = re.getStart();
                    String je = re.getJeton();
                    String lamport = re.getLamport();
                    String name = re.getServerName();
                    String ty = re.getType();
                    String ac = re.getAction();
                    String vo = re.getVong();
                    String mess = re.getMessage();

                    display.append("Thong tin nhan duoc :\n" + "start: " + st + "\n" + "jeton: " + je + "\n" + "lamport: " + lamport + "\n" + "servername: " + name + "\n" + "type: " + ty + "\n" + "action: " + ac + "\n" + "vong: " + vo + "\n" + "message: " + mess + "\n\n");

                    // Process based on action
                    if (ac.equals("I")) {
                        data = new ProcessData(mess);
                        db.insertData(data.getPos(), data.getNum(), data.getType(), data.getColor(), data.getTime());
                        display.append("Inserted data\n");
                    } else if (ac.equals("D")) {
                        db.delData(mess);
                        display.append("Deleted data\n");
                    }

                    // Pass token to next server
                    int nextPos = (pos + 1) % 6;
                    Connect co = new Connect(rount.table[nextPos].destination, rount.table[nextPos].port, rount.table[nextPos].name);
                    co.connect();
                    co.requestServer(recMess);
                    co.shutdown();
                }
            } catch (Exception e) {
                display.append("Error: " + e.getMessage() + "\n");
            }
        }
    }

    public static void main(String args[]) {
        Hashtable hash = new Hashtable();
        Server1 app = new Server1();
        app.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        sv1 sv1s = new sv1();
    }
}
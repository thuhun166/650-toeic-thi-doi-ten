package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Database {

    String drivername = "com.mysql.jdbc.Driver";
    String connectionURL;
    String username = "root";
    String password = "root";
    Statement stmt = null;
    ResultSet rs = null;
    Connection conn;
    String tableName;

    public Database(String mysqlUrl, int nodeId) {
        this.connectionURL = mysqlUrl;
        this.tableName = "server" + nodeId;
        try {
            Class.forName(drivername).newInstance();
            conn = DriverManager.getConnection(connectionURL, username, password);
            stmt = conn.createStatement();
        } catch (Exception ex) {
            System.out.println("SQLException: " + ex.getMessage());
        }
    }

    public void insertData(String id, String content, String time, String status) {
        String sSQL = "INSERT INTO " + tableName + " VALUES ('" + id + "','" + content + "','" + time + "','" + status + "')";
        try {
            stmt.executeUpdate(sSQL);
        } catch (Exception e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }

    public void delData(String id) {
        try {
            String sSQL = "DELETE FROM " + tableName + " WHERE id='" + id + "'";
            stmt.executeUpdate(sSQL);
        } catch (Exception e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }

    public String getAllData() {
        String result = "";
        try {
            rs = stmt.executeQuery("SELECT * FROM " + tableName);
            while (rs.next()) {
                result += rs.getString("id") + " | " + rs.getString("content") + " | " + rs.getString("time") + " | " + rs.getString("status") + "\n";
            }
        } catch (Exception e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return result;
    }
}
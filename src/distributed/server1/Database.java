package distributed.server1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class Database {

    String drivername = "com.mysql.jdbc.Driver";
    String connectionURL = "jdbc:mysql://localhost:3306/db1";
    String username = "root";
    String password = "root";
    Statement stmt = null;
    ResultSet rs = null;
    Connection conn;

    public Database() {
        try {
            Class.forName(drivername).newInstance();
            conn = DriverManager.getConnection(connectionURL, username, password);
            stmt = conn.createStatement();
        } catch (Exception ex) {
            System.out.println("SQLException: " + ex.getMessage());
        }
    }

    public void insertData(String vitri, String bienso, String loai, String mau, String gio) {
        String sSQL = "INSERT INTO server1 VALUES ('" + vitri + "','" + bienso + "','" + loai + "','" + mau + "','" + gio + "')";
        try {
            stmt.executeUpdate(sSQL);
        } catch (Exception e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }

    public void delData(String id) {
        try {
            String sSQL = "DELETE FROM server1 WHERE vitri='" + id + "'";
            stmt.executeUpdate(sSQL);
        } catch (Exception e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }

    public String getData() {
        String result = "";
        try {
            rs = stmt.executeQuery("SELECT * FROM server1");
            while (rs.next()) {
                result += rs.getString("vitri") + " " + rs.getString("bienso") + " " + rs.getString("loai") + " " + rs.getString("mau") + " " + rs.getString("gio") + "\n";
            }
        } catch (Exception e) {
            System.out.println("SQLException: " + e.getMessage());
        }
        return result;
    }
}
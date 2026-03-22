package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {

    private static final String MODERN_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String LEGACY_DRIVER = "com.mysql.jdbc.Driver";

    private final String connectionUrl;
    private final String jobsTableName;
    private final String metadataTableName;
    private String username = "root";
    private String password = "root";
    private Connection connection;

    public Database(String mysqlUrl, int nodeId) {
        String tablePrefix = "node" + nodeId;
        this.jobsTableName = tablePrefix + "_print_jobs";
        this.metadataTableName = tablePrefix + "_ring_metadata";
        this.connectionUrl = normalizeJdbcUrl(mysqlUrl, nodeId);
        connect();
        initializeSchema();
    }

    private String normalizeJdbcUrl(String mysqlUrl, int nodeId) {
        String normalized = mysqlUrl;

        if (normalized == null || normalized.trim().isEmpty()) {
            normalized = "jdbc:mysql://localhost:3306/db" + nodeId;
        }

        normalized = normalized.trim();
        if (normalized.startsWith("mysql://")) {
            normalized = "jdbc:" + normalized;
        }

        if (normalized.contains("@")) {
            String userPart = normalized.substring(normalized.indexOf("://") + 3, normalized.indexOf("@"));
            if (userPart.contains(":")) {
                this.username = userPart.substring(0, userPart.indexOf(":"));
                this.password = userPart.substring(userPart.indexOf(":") + 1);
                normalized = normalized.substring(0, normalized.indexOf("://") + 3)
                        + normalized.substring(normalized.indexOf("@") + 1);
            }
        }

        return normalized;
    }

    private synchronized void connect() {
        try {
            loadDriver();
            System.out.println("[DB] Dang ket noi toi " + connectionUrl + " voi tai khoan " + username);
            connection = DriverManager.getConnection(connectionUrl, username, password);
            System.out.println("[DB] Ket noi thanh cong");
        } catch (Exception ex) {
            connection = null;
            System.err.println("[DB ERROR] Khong the ket noi CSDL: " + ex.getMessage());
        }
    }

    private void loadDriver() throws Exception {
        try {
            Class.forName(MODERN_DRIVER).getDeclaredConstructor().newInstance();
        } catch (Exception modernError) {
            Class.forName(LEGACY_DRIVER).getDeclaredConstructor().newInstance();
        }
    }

    private synchronized boolean ensureConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connect();
            }
            return connection != null && !connection.isClosed();
        } catch (SQLException ex) {
            System.err.println("[DB ERROR] Loi kiem tra ket noi: " + ex.getMessage());
            return false;
        }
    }

    private synchronized void initializeSchema() {
        if (!ensureConnection()) {
            return;
        }

        String jobsSql = "CREATE TABLE IF NOT EXISTS " + jobsTableName + " ("
                + "job_id VARCHAR(100) PRIMARY KEY, "
                + "document_content TEXT NOT NULL, "
                + "requested_by VARCHAR(100) NOT NULL, "
                + "requested_node INT NOT NULL, "
                + "submitted_lamport BIGINT NOT NULL, "
                + "submitted_at BIGINT NOT NULL, "
                + "processed_node INT NULL, "
                + "processed_lamport BIGINT NULL, "
                + "processed_at BIGINT NULL, "
                + "status VARCHAR(50) NOT NULL, "
                + "note VARCHAR(255) NULL, "
                + "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP"
                + ")";

        String metadataSql = "CREATE TABLE IF NOT EXISTS " + metadataTableName + " ("
                + "key_name VARCHAR(100) PRIMARY KEY, "
                + "value_text VARCHAR(255) NOT NULL, "
                + "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP"
                + ")";

        try (PreparedStatement jobsStatement = connection.prepareStatement(jobsSql);
             PreparedStatement metadataStatement = connection.prepareStatement(metadataSql)) {
            jobsStatement.executeUpdate();
            metadataStatement.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("[DB ERROR] Khong the tao bang du lieu: " + ex.getMessage());
        }
    }

    public synchronized boolean recordPrintedJob(ProcessData job, int processedNode, long processedLamport, long processedAt) {
        if (!ensureConnection()) {
            return false;
        }

        String sql = "INSERT INTO " + jobsTableName + " (job_id, document_content, requested_by, requested_node, submitted_lamport, submitted_at, processed_node, processed_lamport, processed_at, status, note) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE "
                + "document_content = VALUES(document_content), "
                + "requested_by = VALUES(requested_by), "
                + "requested_node = VALUES(requested_node), "
                + "submitted_lamport = VALUES(submitted_lamport), "
                + "submitted_at = VALUES(submitted_at), "
                + "processed_node = VALUES(processed_node), "
                + "processed_lamport = VALUES(processed_lamport), "
                + "processed_at = VALUES(processed_at), "
                + "status = VALUES(status), "
                + "note = VALUES(note)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, job.getJobId());
            statement.setString(2, job.getDocumentContent());
            statement.setString(3, job.getRequestedBy());
            statement.setInt(4, job.getRequestedNode());
            statement.setLong(5, job.getSubmittedLamport());
            statement.setLong(6, job.getSubmittedAt());
            statement.setInt(7, processedNode);
            statement.setLong(8, processedLamport);
            statement.setLong(9, processedAt);
            statement.setString(10, "PRINTED");
            statement.setString(11, "Printed by node " + processedNode);
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.err.println("[DB ERROR] Khong the luu log in: " + ex.getMessage());
            return false;
        }
    }

    public synchronized boolean recordCancelledJob(ProcessData job, int processedNode, long processedLamport, long processedAt) {
        if (!ensureConnection()) {
            return false;
        }

        String sql = "INSERT INTO " + jobsTableName + " (job_id, document_content, requested_by, requested_node, submitted_lamport, submitted_at, processed_node, processed_lamport, processed_at, status, note) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE "
                + "processed_node = VALUES(processed_node), "
                + "processed_lamport = VALUES(processed_lamport), "
                + "processed_at = VALUES(processed_at), "
                + "status = VALUES(status), "
                + "note = VALUES(note)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, job.getJobId());
            statement.setString(2, job.getDocumentContent());
            statement.setString(3, job.getRequestedBy());
            statement.setInt(4, job.getRequestedNode());
            statement.setLong(5, job.getSubmittedLamport());
            statement.setLong(6, job.getSubmittedAt());
            statement.setInt(7, processedNode);
            statement.setLong(8, processedLamport);
            statement.setLong(9, processedAt);
            statement.setString(10, "CANCELLED");
            statement.setString(11, "Cancelled by node " + processedNode);
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.err.println("[DB ERROR] Khong the luu log huy job: " + ex.getMessage());
            return false;
        }
    }

    public synchronized String getAllJobs() {
        if (!ensureConnection()) {
            return "LOI|Khong the truy cap CSDL";
        }

        StringBuilder result = new StringBuilder();
        String sql = "SELECT job_id, status, requested_node, submitted_lamport, processed_node, processed_lamport, note "
                + "FROM " + jobsTableName + " ORDER BY updated_at DESC, submitted_at DESC";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                result.append("job=")
                        .append(resultSet.getString("job_id"))
                        .append(" | status=")
                        .append(resultSet.getString("status"))
                        .append(" | requestedNode=")
                        .append(resultSet.getInt("requested_node"))
                        .append(" | submittedLamport=")
                        .append(resultSet.getLong("submitted_lamport"))
                        .append(" | processedNode=")
                        .append(resultSet.getString("processed_node"))
                        .append(" | processedLamport=")
                        .append(resultSet.getString("processed_lamport"))
                        .append(" | note=")
                        .append(resultSet.getString("note"))
                        .append("\n");
            }
        } catch (SQLException ex) {
            System.err.println("[DB ERROR] Truy van that bai: " + ex.getMessage());
            return "LOI|Truy van that bai";
        }

        if (result.length() == 0) {
            return "CHUA_CO_JOB";
        }

        return result.toString();
    }

    public synchronized long getLongMetadata(String key, long defaultValue) {
        String value = getMetadata(key);
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }

        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    public synchronized String getMetadata(String key) {
        if (!ensureConnection()) {
            return null;
        }

        String sql = "SELECT value_text FROM " + metadataTableName + " WHERE key_name = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, key);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("value_text");
                }
            }
        } catch (SQLException ex) {
            System.err.println("[DB ERROR] Doc metadata that bai: " + ex.getMessage());
        }
        return null;
    }

    public synchronized boolean putMetadata(String key, String value) {
        if (!ensureConnection()) {
            return false;
        }

        String sql = "INSERT INTO " + metadataTableName + " (key_name, value_text) VALUES (?, ?) "
                + "ON DUPLICATE KEY UPDATE value_text = VALUES(value_text)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, key);
            statement.setString(2, value);
            statement.executeUpdate();
            return true;
        } catch (SQLException ex) {
            System.err.println("[DB ERROR] Ghi metadata that bai: " + ex.getMessage());
            return false;
        }
    }

    public synchronized boolean putLongMetadata(String key, long value) {
        return putMetadata(key, String.valueOf(value));
    }
}

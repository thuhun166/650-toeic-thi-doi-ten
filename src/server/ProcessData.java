package server;

public class ProcessData {

    private String id;
    private String content;
    private String time;
    private String status;

    public ProcessData(String dataString) {
        // Format: id|content|time|status
        try {
            String[] parts = dataString.split("\\|");
            if (parts.length >= 2) {
                this.id = parts[0];
                this.content = parts[1];
                this.time = (parts.length > 2) ? parts[2] : String.valueOf(System.currentTimeMillis());
                this.status = (parts.length > 3) ? parts[3] : "pending";
            }
        } catch (Exception ex) {
            System.out.println("Error parsing data: " + ex.getMessage());
        }
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return id + "|" + content + "|" + time + "|" + status;
    }
}
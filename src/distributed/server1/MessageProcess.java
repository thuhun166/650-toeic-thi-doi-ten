package distributed.server1;

public class MessageProcess {

    String startserver;
    String jeton;
    String lamportclock;
    String serverName;
    String type;
    String action;
    String vong;
    String message;
    String t;

    public MessageProcess(String recMess) {

        int i;
        String temp = recMess;
        try {
            i = temp.indexOf("@$");
        } catch (Exception ex) {
            i = 0;
        }
        temp = temp.substring(i);
        startserver = temp.substring(2, 3);
        temp = temp.substring(4);
        try {
            i = temp.indexOf("|");
        } catch (Exception ex) {
            i = 0;
        }
        jeton = temp.substring(0, i);
        i += 1;
        temp = temp.substring(i);

        try {
            i = temp.indexOf("|");
        } catch (Exception ex) {
            i = 0;
        }
        lamportclock = temp.substring(0, i);
        i += 1;
        temp = temp.substring(i);

        try {
            i = temp.indexOf("|");
        } catch (Exception ex) {
            i = 0;
        }
        serverName = temp.substring(0, i);
        i += 1;
        temp = temp.substring(i);

        try {
            i = temp.indexOf("|");
        } catch (Exception ex) {
            i = 0;
        }
        type = temp.substring(0, i);
        i += 1;
        temp = temp.substring(i);

        try {
            i = temp.indexOf("|");
        } catch (Exception ex) {
            i = 0;
        }
        action = temp.substring(0, 1);
        temp = temp.substring(2);

        try {
            i = temp.indexOf("$$");
        } catch (Exception ex) {
            i = 0;
        }
        vong = temp.substring(0, i);
        i += 2;
        temp = temp.substring(i);

        try {
            i = temp.indexOf("$@");
        } catch (Exception ex) {
            i = 0;
        }
        message = temp.substring(0, i);
    }

    public String getStart() {
        return startserver;
    }

    public String getJeton() {
        return jeton;
    }

    public String getLamport() {
        return lamportclock;
    }

    public String getServerName() {
        return serverName;
    }

    public String getType() {
        return type;
    }

    public String getAction() {
        return action;
    }

    public String getVong() {
        return vong;
    }

    public String getMessage() {
        return message;
    }
}
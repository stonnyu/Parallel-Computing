package Utils;

import java.io.Serializable;

public class PrivateMessage implements Serializable {
    private final String message;
    private final long time;

    public PrivateMessage(String message, long time) {
        this.message = message;
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public long getTime() {
        return time;
    }
}

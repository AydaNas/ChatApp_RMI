import java.io.Serializable;
import java.sql.Timestamp;


public class Message implements Serializable {
    private final int senderId;
    private final int receiverId;
    private final String content;
    private final Timestamp timestamp;
 
   
    public Message(int senderId,int receiverId, String content, Timestamp timestamp) {
        this.senderId = senderId;
        this.receiverId =receiverId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public int getSender() { return senderId; }
    public int getReceiverId() { return receiverId; }
    public String getContent() { return content; }
    public Timestamp getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return "[" + timestamp + "] " + senderId + ": " + receiverId + ":" + content;
    }

	
}

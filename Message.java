import java.io.Serializable;
import java.sql.Timestamp;

public class Message implements Serializable {
    private final int senderId;
    private final Integer receiverId;
    private final Integer groupId; 
    private final String content;
    private final Timestamp timestamp;

    public Message(int senderId, Integer receiverId, Integer groupId, String content, Timestamp timestamp) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.groupId = groupId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public Integer getGroupId() { return groupId; }
    public int getSenderId() { return senderId; }
    public Integer getReceiverId() { return receiverId; }
    public String getContent() { return content; }
    public Timestamp getTimestamp() { return timestamp; }
}

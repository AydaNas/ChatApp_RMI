import java.io.Serializable;

public class UserList implements Serializable {
	private int userId;
	private String userName;
	
	
	public UserList(int id, String name) {
		this.userId=id;
		this.userName=name;
	}
	
	public int getUserId() {
		return userId;
	}
	public String getUserName() {
		return userName; 
	}
}
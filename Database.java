import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Database{
	public static final String dbName = "jdbc:h2:~/Documents/DatabaseRMINew";
	public static final String userName = "Rmi";
	public static final String Dbpassword = "";
	public static Connection connection = null;
	public static PreparedStatement prepared;
	
	//CREATING DATABASE
    public static void databaseCreation() throws Exception {
      try {
    	  Connection connection = DriverManager.getConnection(dbName,userName,Dbpassword);
    	  Statement stat= connection.createStatement();
    	  stat.execute("""
    	  		CREATE TABLE IF NOT EXISTS users(
    	  		userId INT AUTO_INCREMENT PRIMARY KEY,
    	  		userName VARCHAR(50) NOT NULL,
    	  		password VARCHAR(50) NOT NULL)
    	  		
    	  		""");

    	stat.execute("""
    			CREATE TABLE IF NOT EXISTS messages(
    			messageId INT AUTO_INCREMENT PRIMARY KEY,
    			senderId INT NOT NULL,
    			receiverId INT,
    			groupId INT,
    			content TEXT NOT NULL,
    			timeStamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP)
    			"""); 
    	
    	stat.execute("""     			
    			  CREATE TABLE IF NOT EXISTS groups( 
                  groupId INT PRIMARY KEY,
                  groupName VARCHAR(50) NOT NULL)
    			""");
    	
    	stat.execute("""
    			CREATE TABLE IF NOT EXISTS groupMembers(
    			groupId INT NOT NULL,
    			userId INT NOT NULL,
    			PRIMARY KEY(groupId, userId))
    			""");
    	
    	System.out.println("Database table created successfully");
    	connection.close();
      }
      catch (Exception e) {
    	  e.printStackTrace();
      }
    }

//Create Group called Group Forum if it doesnt exist when initializing the server

public static void createGroup(){
	int GroupId= 1;
	String Groupname = "GroupForum";

	try {
		connection = DriverManager.getConnection(dbName,userName,Dbpassword);
		String insertsql = "MERGE INTO groups (groupId,groupName) KEY(groupId) VALUES (?,?)";
		PreparedStatement prepared = connection.prepareStatement(insertsql);
		prepared = connection.prepareStatement(insertsql,Statement.RETURN_GENERATED_KEYS);
	    prepared.setInt(1, GroupId);
		prepared.setString(2,Groupname);
		prepared.executeUpdate();
	} catch (Exception e) {
		e.printStackTrace();
	}
}
    
    //Registering user to the database
    public static int registerClient(String username,String password) {
    	
    	int userId = -1;
    	try {
    		connection = DriverManager.getConnection(dbName,userName,Dbpassword);
    		String insertsql = "INSERT INTO users(userName,password)VALUES(?,?)";
    		prepared = connection.prepareStatement(insertsql,Statement.RETURN_GENERATED_KEYS);		    
    		prepared.setString(1,username);
    		prepared.setString(2, password);
    		prepared.executeUpdate();
    		
    		ResultSet resultset= prepared.getGeneratedKeys();
    		if(resultset.next()) {
    			userId = resultset.getInt(1);
    		}
    		
    		//System.out.print("You have been registered in the database");
    	
    	}
    	catch (SQLException e) {
			e.printStackTrace();
		}
    	return userId;
    }
    
    
  //login confirmation 
    public static boolean userLogin(String inputUsername, String inputPassword){
    	String query = "SELECT userID,userName,password FROM users WHERE username = ? AND password = ?";
    	try {
    		Connection connection = DriverManager.getConnection(dbName,userName,Dbpassword);
    		PreparedStatement preparedstatement = connection.prepareStatement(query);
    		preparedstatement.setString(1, inputUsername);
    		preparedstatement.setString(2, inputPassword);
    		ResultSet result = preparedstatement.executeQuery();
    		
    		return result.next();
    	
    	}
    	 catch (SQLException e) {
    	    	e.printStackTrace();
    	    	return false;
    	    }
    }
    
      
    
        //Get active users using thte system
    public static List<UserList> getActiveusers() {
    	
    	List<UserList> users = new ArrayList<>();
    	try {
    			
    			Connection connection = DriverManager.getConnection(dbName,userName,Dbpassword);
    			Statement stmt = connection.createStatement();
    			ResultSet rs = stmt.executeQuery("SELECT userID, userName FROM users");
    			
    					while (rs.next()) {
    						int id = rs.getInt("userId");
    						String name = rs.getString("userName");
    						users.add(new UserList(id,name));
    					}
    					
    }
    catch (SQLException e) {
    	e.printStackTrace();
    }
    	
    	System.out.print("list of active users");
		return users;
}
   //inserting messagesq to the databases
    public static void insertMessage(int senderId, Integer receiverId , Integer groupId, String content) {
    	Timestamp timestamp =new Timestamp (System.currentTimeMillis());
    
		try {
			connection = DriverManager.getConnection(dbName,userName,Dbpassword);
		
    	String insertsql = "INSERT INTO messages(senderId, receiverId, groupId, content, timeStamp) VALUES (?,?,?,?,?)";

    	
		prepared = connection.prepareStatement(insertsql);		    
		prepared.setInt(1, senderId);	
       	prepared.setObject(2, receiverId, java.sql.Types.INTEGER);
       	prepared.setObject(3, groupId, java.sql.Types.INTEGER);
    	prepared.setString(4, content);
    	prepared.setTimestamp(5, timestamp);
    	prepared.executeUpdate();
    }
		catch (SQLException e) {		
			e.printStackTrace();
		}
		}


    //get message for the user as history
    public static List<Message> getMessagesForUser(int userId){
    	List<Message> messages = new ArrayList<>();
    	try {
    		connection = DriverManager.getConnection(dbName,userName,Dbpassword);
    	    String query = "SELECT senderId, receiverId, content, timeStamp " +
               "FROM messages " +
               "WHERE (senderId = ? OR receiverId = ?) AND (groupId IS NULL )"+ 
               "ORDER BY timeStamp ASC";

    		prepared = connection.prepareStatement(query);
    		prepared.setInt(1, userId);
			prepared.setInt(2,userId);

    		ResultSet result= prepared.executeQuery();
    		while(result.next()) {
    			int senderId = result.getInt("senderId");
    			int receiverId = result.getInt("receiverId");
    			String content = result.getString("content");
    			Timestamp timestamp = result.getTimestamp("timeStamp"); 
    			messages.add(new Message(senderId,receiverId,content,timestamp));
    		}
    	}
    		catch(SQLException e) {
    			e.printStackTrace();
    		}
    		return messages;
    	
    	
    	  
    }
    
    
   public static int getUserId(String username) {
	   String query = "SELECT userId FROM users WHERE username = ?";
	   try{
			   Connection connection = DriverManager.getConnection(dbName,userName,Dbpassword);
			   PreparedStatement preparedstatement = connection.prepareStatement(query);
			   preparedstatement.setString(1, username);
			   ResultSet result = preparedstatement.executeQuery();
			   if(result.next()) {
				   return result.getInt("userId");
			   }
			   
	   
   }
   catch(SQLException e) {
	   e.printStackTrace();
   }
   return -1;
   }


   //Inserting user to Group Forum 

   public static void joinGroup (int userId){
	int groupId = 1;
	
	try {
		connection = DriverManager.getConnection(dbName,userName,Dbpassword);
		String query = "SELECT * FROM groupMembers WHERE groupId = ? AND userId= ? ";
		PreparedStatement checkAlreadyExist = connection.prepareStatement(query);
		PreparedStatement ifexist = connection.prepareStatement(query);
 		ifexist.setInt(1,groupId);
		ifexist.setInt(2,userId);
	
		ResultSet result = ifexist.executeQuery();
		
		if(!result.next()){
			String insertsql = "INSERT INTO groupMembers (groupId, userId) VALUES (?,?)";
			PreparedStatement prepared = connection.prepareStatement(insertsql);
			prepared.setInt(1, groupId);
			prepared.setInt(2,userId);
			prepared.executeUpdate();
			System.out.println("User"+userId+"Joined the Group");
			
		}
		else {
			System.out.println("User"+userId + "is already in the group");
		}
		
		
		
	} catch (Exception e) {
		e.printStackTrace();
	}

   }

   //deleting the user from the Group Forum 

   public static void leaveGroup (int userId){
	//String alreadyExist = "SELECT * FROM groupMembers WHERE groupId =  AND userId = ?";
	int groupId = 1;
	String deletequery = "DELETE FROM groupMembers WHERE groupId = ? AND userId = ? ";
	try {
		connection = DriverManager.getConnection(dbName,userName,Dbpassword);
        PreparedStatement prepared = connection.prepareStatement(deletequery);
 
		prepared.setInt(1,groupId);
		prepared.setInt(2,userId);
		int rows = prepared.executeUpdate();
       
	   if(rows> 0){
		System.out.println("User" + userId + "has left the group");
	   }
	   else {
		System.out.println("User" + userId + "the user doesnt exist");
	   }

	
	} catch (Exception e) {
	}

   }

//send Group Message
public static List<Message> getGroupMessage (int groupId){
	List<Message> messages = new ArrayList<>();
	try {
		Connection connection = DriverManager.getConnection(dbName,userName,Dbpassword);
		String query = "SELECT senderId,content,timeStamp FROM messages WHERE groupId = ? ORDER BY timeStamp ASC";
		PreparedStatement prepared = connection.prepareStatement(query);
		prepared.setInt(1,groupId);
		ResultSet result = prepared.executeQuery();
		while(result.next()){
			int senderId = result.getInt("SenderId");
			String content = result.getString("content");
			Timestamp timestamp = result.getTimestamp("timestamp");
			messages.add(new Message(senderId,null,content,timestamp));
		}
	} catch (SQLException e) {
              e.printStackTrace();
	}
	return messages;
}
// Get all user IDs in a group
public static List<Integer> getGroupMembers(int groupId) {
    List<Integer> members = new ArrayList<>();
    try {
        connection = DriverManager.getConnection(dbName, userName, Dbpassword);
        String query = "SELECT userId FROM groupMembers WHERE groupId = ?";
        prepared = connection.prepareStatement(query);
        prepared.setInt(1, groupId);
        ResultSet rs = prepared.executeQuery();

        while (rs.next()) {
            members.add(rs.getInt("userId"));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return members;
}


}
    

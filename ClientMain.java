import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;

import static java.awt.SystemColor.text;

public class ClientMain {

    public static void main(String[] args) throws Exception {
    	Scanner input = new Scanner(System.in);
    	String host = "localhost";
    	int port = 8082;
    	

        Registry registry = LocateRegistry.getRegistry(host, port);
        ChatServer server = (ChatServer) registry.lookup("ChatService");
        ChatClientImpl client = new ChatClientImpl();
    	
        try {
        	
        	System.out.println("select your options");
        	System.out.println("enter 1 for connection");
        	System.out.println("enter 2 to create message");
        	System.out.println("enter 3 to create group");
        	System.out.println("enter 4 to join group");
        	System.out.println("enter 5 to leave group");
        
           int choice= input.nextInt();
           
           switch(choice){
           //login and connection
        	   case 1:
        		   input.nextLine();
        		   System.out.println("Username: ");
        		   String userName = input.nextLine();

                   System.out.println("Your password: ");
                   String password = input.nextLine();
                   
                   int userId = server.registerClient(userName, password,client);
                   client.setUserId(userId);
                   break;
                   
                   
              //send Message     
        	   case 2:
        		   input.nextLine();
        		   System.out.println("Active Users");
                   
                   List<UserList> users = server.getActiveUsers();
                   for(UserList use : users) {
                	   System.out.println(use.getUserId()+""+use.getUserName());
                   }
                	   System.out.println("Select person you want to chat with");
                	   int selection  = input.nextInt();
                	   input.nextLine();
                	   
                	   UserList selectedUser = null;
                	   for(UserList u:users) {
                		   if(u.getUserId() == selection) {
                			   selectedUser = u;
                			   break;
                		   }
                	   }
                	   if(selectedUser == null) {
                		   System.out.println("you havent selected user");
                		   break;
                	   }
                	   System.out.println("Enter message for user");
                	   String message = input.nextLine();
                	   
                	   server.sendMessage(client.getUserId(),selectedUser.getUserId(),null, message);
                   System.out.println("Message sent");
                   break;
                   
                   
           //creating group
        	   case 3:
        		   System.out.println("select users to form a group");
        		   input.nextLine();
        		   System.out.println("Active Users");
                   
                   List<UserList> activeusers = server.getActiveUsers();
                   for(UserList use : activeusers) {
                	   System.out.println(use.getUserId()+""+use.getUserName());
                   }
                	   System.out.println("Select person you want to form group with");
                	   int groupselection  = input.nextInt();
                	   input.nextLine();
                	   
                	   UserList groupselectedUser = null;
                	   for(UserList u:activeusers) {
                		   if(u.getUserId() == groupselection) {
                			   groupselectedUser = u;
                			   break;
                		   }
                	   }
                	   if(groupselectedUser == null) {
                		   System.out.println("you havent selected user");
                		   break;
                	   }
                	   System.out.println("Enter group name");
                	   String groupName = input.nextLine();
                	   
                	   //need to have create group method where it takes name of the group and id of users on that group
                	   
                	  
                   System.out.println("Message sent");
                   break;
           
        	  default:
        		  System.out.println("you havent made any choice");
                
           }
//          

          

            
            
            
            
            
            //server.join(userName, client);

//            for (Message m : server.getHistory()) {
//                System.out.println(m);
//            }
//
//            while (true) {
//                String msg = System.console().readLine();
//                if (msg.equalsIgnoreCase("q"))  {
//                    break;
//                };
//
//                server.sendMessage(new Message(userName, msg));
//            }
//
//            server.leave(userName);

        } catch (Exception e)  {
    		System.err.println("Error on client: " + e);
            e.printStackTrace();
        }
    }
    
 
    
}

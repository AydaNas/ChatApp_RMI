import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;

public class ClientMain {

    public static void main(String[] args) throws Exception {

    	Scanner input = new Scanner(System.in);
    	String host = "127.0.0.1";
    	int port = 8082;
    	

        Registry registry = LocateRegistry.getRegistry(host, port);
        ChatServer server = (ChatServer) registry.lookup("ChatService");
        ChatClientImpl client = new ChatClientImpl();
    
        
        boolean loogedIn = false;
        boolean Joined = false;
        
       
        while(true) {
        	if(!loogedIn) {
        		client.printReceivedMessages();       		
        		
        		System.out.println("Select your options");
        		System.out.println("1 Register");
        		System.out.println("2 Login");
        		System.out.println("3 Exit");
        		int choice = input.nextInt();
        		input.nextLine();
        		
        		
        		switch(choice) {
        		   
                //login and connection
             	   case 1:
             		   
             		   System.out.println("Username: ");
             		   String userName = input.nextLine();

                        System.out.println("Password: ");
                        String password = input.nextLine();
                        
                        int userId = server.registerClient(userName, password,client);
                        client.setUserId(userId);
                        break;                  
                        
                        
                   //login
             	   case 2: 
             		   
             		   System.out.println("Login");
             		   System.out.println("UserName:");
             		   String loginusername = input.nextLine();
             		   System.out.println("Password:");
             		   String loginpassword = input.nextLine();
             		   
             		   boolean sucess = server.loginUser(loginusername, loginpassword, client);
             		   if(sucess) {
             			   System.out.println("Login sucessful");
             			   loogedIn = true;
             		   }
             		   else {
             			   System.out.println("Login failed please try again");
             			   loogedIn = false;
             		   }  	   
             	    break;
                        
             	   case 3:
             		   System.out.println("Goodbye!");
             		   System.exit(0);
             		   break;
             	  default:
             		  System.out.println("Invalid Choice");
        		}
        	}
        	else if (!Joined) {
        		System.out.println("\n ===MAIN MENU===");
        		System.out.println("1 send Message");
        		System.out.println("2 Show History");
        		System.out.println("3 Join Group");
        		//System.out.println("4 Leave Group");
				//System.out.println("5 Show History");
				System.out.println("4 Logout");
        		
        		int choose = input.nextInt();
        		input.nextLine();
        		
        		switch(choose) {
        		//send Message     
         	   case 1:
         		
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
                    
               
         	  
			   case 2: 
			   System.out.println("Private Chat History");
			   List<Message> history = server.chatHistory(client.getUserId());

			    for(Message m : history){
					System.out.println("("+m.getTimestamp()+")From"+
					m.getSender()+"To"+
					m.getReceiverId()+"Message"+
					m.getContent());
				}
			   break;


			    //Join Group
         	   case 3:
                server.joinGroup(client.getUserId());
				Joined = true;
				System.out.println("Welcome to GroupForum");
                break;


         	   case 4: 
         		   System.out.println("Logging out");
         		   server.logoutUser(client.getUserId());
				   loogedIn = false;
         		   Joined = false;
				   break;
            
         	  default:
         		  System.out.println("you havent made any choice");
                 
            }
			}
			
			else {
				System.out.println("Enter 1 to send Group Message");
				System.out.println("Enter 2 to show Group chat history");
				System.out.println("Enter 3 to leave the Group");
				System.out.println("Enter 4 to Back to Main Menu");

				int groupchoice = input.nextInt();
				switch(groupchoice){
                    case 1: break;
					case 2: break;
					case 3: break;
					case 4: 
					System.out.print("Returning...");
					Joined = false;
					break;

				}
				
			}
 //    
        			
        		}
        		
        }

        	
        
           
        
              

          

            
            
            
            
            
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

      
    }
    
 
    


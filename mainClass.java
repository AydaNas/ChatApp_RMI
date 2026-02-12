import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class mainClass{
    public static void main(String args[] ) {
    	try {
			Database.databaseCreation();
		} catch (Exception e) {		
			e.printStackTrace();
		}
    	
      
      
      
     }
}

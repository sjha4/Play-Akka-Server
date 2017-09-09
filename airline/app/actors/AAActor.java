package actors;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.fasterxml.jackson.databind.node.ObjectNode;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import play.libs.Json;

public class AAActor extends AbstractActor{

	 public static Props getProps() {
	        return Props.create(AAActor.class);
	    }
	@Override
	public Receive createReceive() {
		      return receiveBuilder()
		        .match(FlightMessage.class, hello -> {
		        	String reply;
		        	int i =0;
		        	try {
		        		Connection conn = connect();
		        	    PreparedStatement pstmt  = conn.prepareStatement("SELECT fuck FROM Flights");
						//String sqlSelect = "SELECT * FROM Flights";
	        			//pstmt.setString(1,hello.getMessage().get("firstname"));
	        			ResultSet rs  = pstmt.executeQuery();
	        			while (rs.next()){
	        				i++;
	        			}
	        			reply = "Hello, " + hello.getMessage().get("firstname") + " " + i;
		        	}
		        	catch(Exception e){
		                System.out.println(e.getMessage());
		                ObjectNode errorObject = Json.newObject();
		                errorObject.put("Error", e.getMessage());
		                reply = e.getMessage();
		        	}
		            
		            sender().tell(reply, self());
		        })
		        .build();
		  }
	public static Connection connect() {
        Connection conn = null;
        try {
            // db parameters
            String url = "jdbc:sqlite:mydb.db";
            // create a connection to the database
            conn = DriverManager.getConnection(url);
            System.out.println("Connection to SQLite has been established.");
            return conn;
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
		return conn; 
    }
    

}

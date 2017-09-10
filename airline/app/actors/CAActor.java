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

public class CAActor extends AbstractActor{

	 public static Props getProps() {
	        return Props.create(CAActor.class);
	    }
	@Override
	public Receive createReceive() {
		      return receiveBuilder()
		        .match(FlightMessage.class, message -> {
		        	String action = message.getMessage().get("action").asText();
		        	int res = -999;
		        	String sqlStatmt = "" ;
		        	PreparedStatement pstmt = null;
		        	if(action.equals("availableSeats")){
		        		sqlStatmt = "Select Available from Flights where Name = ?";
		        	}
		        	String reply;
		        	try{
		        		Connection conn = connect();
		        	    pstmt  = conn.prepareStatement(sqlStatmt);
	        			pstmt.setString(1,message.getMessage().get("flight").asText());
		        		ResultSet rs  = pstmt.executeQuery();
	        			while (rs.next()){
	        				res = rs.getInt("Available");
	        			}
	        			if(res==-999){
	        				throw new Exception("No results");
	        			}
	        			reply = "Seats available: " + res;
		        	}
		        	catch(Exception e){
		                System.out.println(e.getMessage());
		                ObjectNode errorObject = Json.newObject();
		                errorObject.put("Error", e.getMessage());
		                reply = "Error: " + e.getMessage();
		        	}
		            
		            sender().tell(reply, self());
		        }).build();
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

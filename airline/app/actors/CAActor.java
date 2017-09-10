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
	public int count =0;
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
		        })
		        .match(TwoStageCommit.class, message -> {
		        	String action = message.getMessage().get("action").asText();
		        	System.out.println("in CA" + sender());
		        	int res = -999;
		        	String sqlStatmt = "" ;
		        	PreparedStatement pstmt = null;
		        	System.out.println("in CACA");
		        	String reply = "";
		        	if(action.equals("Hold")){
		        		System.out.println("in CACA Hold");
		        		reply = holdRequest(message.getMessage().get("flight").asText());
		        	}
		        	else if(action.equals("Confirm")){
		        		System.out.println("in CACA confirm");
		        		reply = commitRequest();
		        	}
		            
		            sender().tell(reply, self());
		        })
		        .build();
		  }
	private String commitRequest() {
		
		return "CCC";
	}
	private String holdRequest(String flight) {
		String sqlStatmt = "" ;
		PreparedStatement pstmt = null;
		String reply = "";
		int res = -999;
		sqlStatmt = "Select Available from Flights where Name = '"+flight+"'";
		try{
				        		Connection conn = connect();
				        	    pstmt  = conn.prepareStatement(sqlStatmt);
				        		ResultSet rs  = pstmt.executeQuery();
			        			while (rs.next()){
			        				res = rs.getInt("Available");
			        			}
			        			if(res==-999){
			        				throw new Exception("No results");
			        			}
			        			sqlStatmt = "INSERT INTO Booking(Id,Flight,Fro,Dest,Trip) VALUES('AA001','AA001','X','X','X')";
			        			pstmt  = conn.prepareStatement(sqlStatmt);
				        		ResultSet rs1  = pstmt.executeQuery();
			        			reply = "Seats available: " + res;
			}
			catch(Exception e){
				System.out.println(e.getMessage());
				ObjectNode errorObject = Json.newObject();
				errorObject.put("Error", e.getMessage());
				reply = "Error: " + e.getMessage();
			}

		return reply;
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

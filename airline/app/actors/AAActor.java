package actors;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.fasterxml.jackson.databind.node.ObjectNode;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import ch.qos.logback.core.net.SyslogOutputStream;
import play.libs.Json;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class AAActor extends AbstractActor{

	 private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	 private Timer timer;// = new Timer();
	 private Boolean fail = false;
	 private Boolean noResponse = false;
	 private AbstractActor.Receive failState;
	 private AbstractActor.Receive noResponseState;
	 private AbstractActor.Receive resetState;
	 public static Props getProps() {
	        return Props.create(AAActor.class);
	    }
	 public AAActor(){
		 failState = receiveBuilder()
					.match(DebugMessage.class, message -> {
			    		String action = message.getMessage().get("action").asText();
			    		String reply = "{ status: success} ";
			    		if(action.contains("fail")){
			    			fail = true;
			    			noResponse = false;
			    			getContext().become(failState);
			    		}
			    		else if(action.contains("noResponse")){
			    			fail = false;
			    			noResponse = true;
			    			getContext().become(noResponseState);
			    		}
			    		else if (action.contains("reset")){
			    			fail = false;
			    			noResponse = false;
			    			getContext().become(resetState);
			    		}
			    		getSender().tell(reply, self());
			    		
			    	})
			          .matchAny(s -> {
			        	  getSender().tell("{Status : Error}", self());
			          })
			          .build();
			noResponseState = receiveBuilder()
					.match(DebugMessage.class, message -> {
			    		String action = message.getMessage().get("action").asText();
			    		String reply = "{ status: success} ";
			    		if(action.contains("fail")){
			    			fail = true;
			    			noResponse = false;
			    			getContext().become(failState);
			    		}
			    		else if(action.contains("noResponse")){
			    			fail = false;
			    			noResponse = true;
			    			getContext().become(noResponseState);
			    		}
			    		else if (action.contains("reset")){
			    			fail = false;
			    			noResponse = false;
			    			getContext().become(resetState);
			    		}
			    		getSender().tell(reply, self());
			    		
			    	})
			          .build();
			resetState = receiveBuilder()
					.match(DebugMessage.class, message -> {
			    		String action = message.getMessage().get("action").asText();
			    		String reply = "{ status: success} ";
			    		if(action.contains("fail")){
			    			fail = true;
			    			noResponse = false;
			    			getContext().become(failState);
			    		}
			    		else if(action.contains("noResponse")){
			    			fail = false;
			    			noResponse = true;
			    			getContext().become(noResponseState);
			    		}
			    		else if (action.contains("reset")){
			    			fail = false;
			    			noResponse = false;
			    			getContext().become(resetState);
			    		}
			    		getSender().tell(reply, self());
			    		
			    	})
					.match(FlightMessage.class, message -> {
						String action = message.getMessage().get("action").asText();
						String operator = message.getMessage().get("operator").asText();
						String flight = message.getMessage().get("flight").asText();
			        	int total = 0;
			        	int booked = 0;
			        	int available = 0;
			        	String sqlStatmt = "" ;
			        	PreparedStatement pstmt = null;
			        	if(action.equals("availableSeats")){
			        		sqlStatmt = "Select count(*) as booked from Booking where Flight = ?";
			        	}
			        	String reply;
			        	try{
			        		if(!flight.contains(operator))
								throw new Exception("Invalid input");
			        		Connection conn = connect();
			        	    pstmt  = conn.prepareStatement(sqlStatmt);
		        			pstmt.setString(1,message.getMessage().get("flight").asText());
			        		ResultSet rs  = pstmt.executeQuery();
		        			while (rs.next()){
		        				booked = rs.getInt("booked");
		        				System.out.println("Booked C:" +booked);
		        			}
		        			//Connection conn1 = connect();
		        			sqlStatmt = "Select Total from Flights where Name = ? AND Operator = ?";
		        			pstmt  = conn.prepareStatement(sqlStatmt);
		        			pstmt.setString(1,message.getMessage().get("flight").asText());
		        			pstmt.setString(2,operator);
		        			ResultSet rs1  = pstmt.executeQuery();
			        		while (rs1.next()){
		        				total = rs1.getInt("Total");
		        				System.out.println("total C:" +total);
		        			}
			        		available = total - booked;
		        			System.out.println("Avaialble C:" +available);
		        			reply = "{Status: Success},{Seats available: " + available +"}";
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
			        	System.out.println("in AA" + sender());
			        	int res = -999;
			        	String sqlStatmt = "" ;
			        	PreparedStatement pstmt = null;
			        	
			        	String reply="";
			        	if(action.equals("Hold")){
			        		timer = new Timer(); 
			        		reply = holdRequest(message.getMessage().get("flight").asText(),
			        				message.getMessage().get("bookingId").asText(),
			        				message.getMessage().get("trip").asText());
			        	}
			        	else if(action.equals("Confirm")){
			        		timer.cancel();
			        		System.out.println("in Commit of A");
			        		reply = commitRequest(message.getMessage().get("bookingId").asText());
			        	}
			            
			            sender().tell(reply, self());
			        })
			        
			        .build();

	 }
	@Override
	public Receive createReceive() { 
		if(fail) return failState;
		if(noResponse) return noResponseState;
		else return resetState;
		
	}
	private String commitRequest(String bookingId) {
		
		String sqlStatmt = "" ;
		System.out.println("In A commit");
		PreparedStatement pstmt = null;
		String reply = "";
		int res = -999;
		sqlStatmt = "UPDATE Booking SET Hold = 'False' "
                + "WHERE Id = '"+bookingId+"'";
		try{
				        		Connection conn = connect();
				        	    pstmt  = conn.prepareStatement(sqlStatmt);
				        	    //pstmt.setBoolean(1, true);
				        	    pstmt.executeUpdate();
			        			reply = "Booking Confirmed: "+bookingId;
			}
			catch(Exception e){
				System.out.println(e.getMessage());
				ObjectNode errorObject = Json.newObject();
				errorObject.put("Error", e.getMessage());
				reply = "Error in  Commit B: " + e.getMessage();
			}

		return reply;
	}
	private String cancelBooking(String bookingId, String flight){
		String sqlStatmt = "" ;
		System.out.println("In A cancel Booking");
		PreparedStatement pstmt = null;
		String reply = "";
		int res = -999;
		sqlStatmt = "DELETE from Booking "
                + "WHERE Hold = 'true' AND Flight = '"+flight+"'";
		try{
				        		Connection conn = connect();
				        	    pstmt  = conn.prepareStatement(sqlStatmt);
				        	    //pstmt.setBoolean(1, true);
				        	    pstmt.executeUpdate();
				        	    sqlStatmt = "Select Available from Flights where Name = '"+flight+"'";
				        	    pstmt  = conn.prepareStatement(sqlStatmt);
				        		ResultSet rs  = pstmt.executeQuery();
			        			while (rs.next()){
			        				res = rs.getInt("Available");
			        			}
			        			if(res==-999){
			        				throw new Exception("Couldn't query available");
			        			}
			        			sqlStatmt = "UPDATE Flights SET Available = ? "
				                        + "WHERE Name = '"+flight+"'";
				        		pstmt  = conn.prepareStatement(sqlStatmt);
				        	    pstmt.setInt(1, res+1);
				        	    pstmt.executeUpdate();
			        			reply = "Booking Deleted: "+bookingId;
			}
			catch(Exception e){
				System.out.println(e.getMessage());
				ObjectNode errorObject = Json.newObject();
				errorObject.put("Error", e.getMessage());
				reply = "Error in  Deleting A: " + e.getMessage();
			}
		System.out.println(reply);
		return reply;		
	}
	
	private String holdRequest(String flight,String bookingId,String trip) {
		String fro ="",des="";
		//int ran = (int)(Math.random()*100);
		String sqlStatmt = "" ;
		System.out.println("In A create");
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
			        			if(res==-999||res==0){
			        				throw new Exception("No seats");
			        			}
			        			if(flight.contains("1")){
			        				fro = "X";
			        				des = "Z";
			        			}
			        			else if(flight.contains("2")){
			        				fro = "W";
			        				des = "Y";
			        			}
			        			sqlStatmt = "INSERT INTO Booking(Id,Flight,Fro,Dest,Trip,Hold) VALUES(?,?,?,?,?,?)";
			        			pstmt  = conn.prepareStatement(sqlStatmt);
			        			pstmt.setString(1, bookingId);
			        			pstmt.setString(2, flight);
			        			pstmt.setString(3, fro);
			        			pstmt.setString(4, des);
			        			pstmt.setString(5, trip);
			        			pstmt.setString(6, "true");
				        		pstmt.executeUpdate();
				        		sqlStatmt = "UPDATE Flights SET Available = ? "
				                        + "WHERE Name = '"+flight+"'";
				        		pstmt  = conn.prepareStatement(sqlStatmt);
				        	    pstmt.setInt(1, res-1);
				        	    pstmt.executeUpdate();
			        			reply = "Booking Id: "+bookingId;
			        			startTimer(bookingId,
			        					flight);
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
    
	private void startTimer(String bookingId, String flight) {
	    try{
	    	timer.schedule(new TimerTask() {
	        int n=0;
	        @Override
	        public void run(){
	            if(++n == 15){
	            	cancelBooking(bookingId,flight);
	                sender().tell("Hold on the booking has timed out",self());
	            }
	        }
	    },10000,1000);
	    }catch(Exception e){
	    	cancelBooking(bookingId,flight);
	    }
	}

}

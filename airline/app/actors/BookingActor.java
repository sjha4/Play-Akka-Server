package actors;

//import static akka.pattern.Patterns.ask;
import akka.pattern.Patterns;
import play.mvc.Results.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import scala.Int;
import scala.concurrent.Await;
import scala.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.node.ObjectNode;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import play.libs.Json;
import play.mvc.*;
import scala.compat.java8.FutureConverters;
import akka.dispatch.*;
import akka.japi.Function;
import java.util.concurrent.Callable;
import static akka.dispatch.Futures.future;
import static java.util.concurrent.TimeUnit.SECONDS;
import akka.util.Timeout;
import akka.actor.ActorIdentity;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.ActorSelection;
import akka.actor.Identify;
//import static akka.pattern.PatternsCS.ask;
import static akka.pattern.PatternsCS.pipe;
import java.util.concurrent.CompletableFuture;

public class BookingActor extends AbstractActor{
	final Integer identifyId = 1;
	static ActorRef AActor;
	static ActorRef BActor;
	static ActorRef CActor;
	static ActorSystem system;
	static String stageResp = "Initial";
	private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
	public int count =0;
	 public static Props getProps(ActorRef A, ActorRef B, ActorRef C) {
		 	AActor = A;
		 	BActor = B;
		 	CActor = C;
		 	return Props.create(BookingActor.class);
	        
	    }
	@Override
	public Receive createReceive() {
		System.out.println("Message class: " + BookingMessage.class);
		
		Receive getReceive = receiveBuilder()
		        .match(BookingMessage.class, message -> {
		        	count++;
		        	log.error("Message class: " + BookingMessage.class);
		        	Map<String,String> hm = new HashMap<>();
		        	String sqlStatmt = "";
		        	System.out.println("Here1");
		        	String action = message.getMessage().get("action").asText();
		        	String TripId = "";
		        	if(action.equals("get")){
		        		System.out.println("In index get");
		        		sqlStatmt = "Select * from Booking";
		        	}
		        	else if(action.equals("getDetails")){
		        		System.out.println("In trip get Details");
		        		sqlStatmt = "Select * from Booking where Trip = ?";
		        		TripId = message.getMessage().get("TripId").asText();
		        	}
		        	String reply = "No trips booked";
		        	int i =0;
		        	try {
		        		Connection conn = connect();
		        	    PreparedStatement pstmt  = conn.prepareStatement(sqlStatmt);
		        	    if(action.equals("getDetails"))
		        	    	pstmt.setString(1,TripId);
	        			ResultSet rs  = pstmt.executeQuery();
	        			while (rs.next()){
	        				if(reply.equals("No trips booked"))
	        					reply = "Count= "+count +"\n";
	        				if(hm.containsKey(rs.getString("Trip"))){
	        					hm.put(rs.getString("Trip"), hm.get(rs.getString("Trip"))+ "\n" +rs.getString("Id") + "," 
	        							+ rs.getString("Flight") + "," + rs.getString("Fro") 
	        							+ "," + rs.getString("Dest") + "," + rs.getString("Trip") + "," + rs.getBoolean("Hold"));
	        				}
	        				else{
	        					hm.put(rs.getString("Trip"), rs.getString("Id") + "," + rs.getString("Flight") + "," + rs.getString("Fro") 
		        				+ "," + rs.getString("Dest") + "," + rs.getString("Trip") + "," + rs.getBoolean("Hold"));
	        				}
	        			}
	        			if(hm!=null){
	        				for(String s:hm.keySet()){
	        					reply += "Count= "+count +"\n"+ "TRIP " + ++i +": \n" + hm.get(s)+"\n\n";
	        				}
	        			}
	        			if(reply.equals("No trips booked")){
	        				throw new Exception("Count= "+count +"\n"+"{Didn't find any records!}");
	        			}
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
		        	message.getMessage().put("flight", "CA001");
		        	ActorSystem system = ActorSystem.create();
		        	Timeout timeout = new Timeout(1000, TimeUnit.SECONDS);
		        	Future<Object> askSeats = akka.pattern.Patterns.ask(CActor, message,timeout);
		        	String resC = (String) Await.result(askSeats, timeout.duration());
		        	if(resC.contains("Seats available")){
		        		ObjectNode messageCommit = Json.newObject();
		        		messageCommit.put("action","Confirm");
		        		messageCommit.put("flight", "CA001");
		        		Future<Object> confirmSeats = akka.pattern.Patterns.ask(CActor, new TwoStageCommit(messageCommit),timeout);
		        		sender().tell((String) Await.result(confirmSeats, timeout.duration()), self());
		        	}
		        	else {//AA001+BA001;
		        		System.out.println("In A + B booking");
		        		message.getMessage().put("flight", "AA001");
		        		Future<Object> askSeatsA = akka.pattern.Patterns.ask(AActor, message,timeout);
			        	String resA = (String) Await.result(askSeatsA, timeout.duration());
			        	message.getMessage().put("flight", "BA001");
			        	Future<Object> askSeatsB = akka.pattern.Patterns.ask(BActor, message,timeout);
			        	String resB = (String) Await.result(askSeatsB, timeout.duration());
			        	if(resA.contains("Seats available") && resB.contains("Seats available") ){
			        		ObjectNode messageCommitA = Json.newObject();
			        		messageCommitA.put("action","Confirm");
			        		messageCommitA.put("flight", "AA001");
			        		Future<Object> confirmSeatsA = akka.pattern.Patterns.ask(AActor, new TwoStageCommit(messageCommitA),timeout);
			        		ObjectNode messageCommitB = Json.newObject();
			        		messageCommitB.put("action","Confirm");
			        		messageCommitB.put("flight", "BA001");
			        		Future<Object> confirmSeatsB = akka.pattern.Patterns.ask(BActor, new TwoStageCommit(messageCommitB),timeout);
			        		String Ares = (String) Await.result(confirmSeatsA, timeout.duration());
			        		String Bres = (String) Await.result(confirmSeatsB, timeout.duration());
			        		System.out.println("In A + B result:" + Ares + Bres);
			        		sender().tell(Ares + Bres, self());
			        	}
			        	sender().tell(resA + resB, self());
		        		
		        	}
		        		 
		        	
		        })
		        .build();
			return getReceive;
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
	static Result resp(String resp){
    	if(resp.contains("Error")){
    		stageResp = "Error from resp func";
    		return play.mvc.Results.notFound(resp);
    	}
    	else{
    		stageResp = "Success";
    		return play.mvc.Results.ok(resp); 
    	}
    	
    }


}

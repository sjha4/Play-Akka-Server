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
import akka.pattern.AskTimeoutException;
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
import java.util.Timer;
import java.util.TimerTask;
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
		        	Map<String,String> hm = new HashMap<>();
		        	String sqlStatmt = "";
		        	
		        	String action = message.getMessage().get("action").asText();
		        	String TripId = "";
		        	if(action.equals("get")){
		        		
		        		sqlStatmt = "Select * from Booking";
		        	}
		        	else if(action.equals("getDetails")){
		        		
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
	        					reply = "";
	        				if(hm.containsKey(rs.getString("Trip"))){
	        					hm.put(rs.getString("Trip"), hm.get(rs.getString("Trip"))+ "\n" +rs.getString("Id") + "," 
	        							+ rs.getString("Flight") + "," + rs.getString("Fro") 
	        							+ "," + rs.getString("Dest") + "," + rs.getString("Trip") + "," + rs.getString("Hold"));
	        				}
	        				else{
	        					hm.put(rs.getString("Trip"), rs.getString("Id") + "," + rs.getString("Flight") + "," + rs.getString("Fro") 
		        				+ "," + rs.getString("Dest") + "," + rs.getString("Trip") + "," + rs.getString("Hold"));
	        				}
	        			}
	        			if(hm!=null){
	        				for(String s:hm.keySet()){
	        					reply += "\n"+ "TRIP " + ++i +": (BookingId, Flight, From, To, Trip Id, On Hold) \n" + hm.get(s)+"\n\n";
	        				}
	        			}
	        			if(reply.equals("No trips booked")){
	        				throw new Exception("\n"+"{Didn't find any trips!}");
	        			}
		        	}
		        	catch(Exception e){
		                System.out.println(e.getMessage());
		                ObjectNode errorObject = Json.newObject();
		                errorObject.put("Error", e.getMessage());
		                reply = "Error: " + e.getMessage();
		        	}
		        	log.info("GET trip details");
		            sender().tell(reply, self());
		        })
		        .match(TwoStageCommit.class, message -> {
		        	log.debug("Starting Trip Booking");
		        	String trip = "T" + String.valueOf(System.currentTimeMillis());
		        	String  BookingIdC= "Booking" + String.valueOf(System.currentTimeMillis());
		        	System.out.println(BookingIdC);
		        	message.getMessage().put("flight", "CA001");
		        	message.getMessage().put("bookingId", BookingIdC);
		        	message.getMessage().put("trip", trip);
		        	ActorSystem system = ActorSystem.create();
		        	Timeout timeout = new Timeout(1, TimeUnit.SECONDS);
		        	Future<Object> askSeats = akka.pattern.Patterns.ask(CActor, message,timeout);
		        	String resC = (String) Await.result(askSeats, timeout.duration());
		        	log.info("Checking CA001");
		        	if(resC.contains("Booking Id")){
		        		ObjectNode messageCommit = Json.newObject();
		        		messageCommit.put("action","Confirm");
		        		messageCommit.put("flight", "CA001");
		        		messageCommit.put("bookingId", BookingIdC);
		        		messageCommit.put("trip", trip);
		        		Future<Object> confirmSeats = akka.pattern.Patterns.ask(CActor, new TwoStageCommit(messageCommit),timeout);
		        		String resultTrans =(String) Await.result(confirmSeats, timeout.duration());
		        		sender().tell(trip, self());
		        	}
		        	else if(resC.contains("No seats")) {//AA001+BA001;
		        		trip = "T" + String.valueOf(System.currentTimeMillis());
		        		String BookingIdA= "BookingA" + String.valueOf(System.currentTimeMillis());
		        		String BookingIdB= "BookingB" + String.valueOf(System.currentTimeMillis());
		        		System.out.println("In A + B booking");
		        		message.getMessage().put("flight", "AA001");
		        		message.getMessage().put("bookingId", BookingIdA);
			        	message.getMessage().put("trip", trip);
		        		Future<Object> askSeatsA = akka.pattern.Patterns.ask(AActor, message,timeout);
			        	String resA = (String) Await.result(askSeatsA, timeout.duration());
			        	message.getMessage().put("flight", "BA001");
			        	message.getMessage().put("bookingId", BookingIdB);
			        	message.getMessage().put("trip", trip);
			        	Future<Object> askSeatsB = akka.pattern.Patterns.ask(BActor, message,timeout);
			        	String resB = (String) Await.result(askSeatsB, timeout.duration());
			        	if(resA.contains("Booking Id") && resB.contains("Booking Id") ){
			        		ObjectNode messageCommitA = Json.newObject();
			        		messageCommitA.put("action","Confirm");
			        		messageCommitA.put("flight", "AA001");
			        		messageCommitA.put("bookingId", BookingIdA);
			        		Future<Object> confirmSeatsA = akka.pattern.Patterns.ask(AActor, new TwoStageCommit(messageCommitA),timeout);
			        		ObjectNode messageCommitB = Json.newObject();
			        		messageCommitB.put("action","Confirm");
			        		messageCommitB.put("flight", "BA001");
			        		messageCommitB.put("bookingId", BookingIdB);
			        		Future<Object> confirmSeatsB = akka.pattern.Patterns.ask(BActor, new TwoStageCommit(messageCommitB),timeout);
			        		String Ares = (String) Await.result(confirmSeatsA, timeout.duration());
			        		String Bres = (String) Await.result(confirmSeatsB, timeout.duration());
			        		System.out.println("In A + B result:" + Ares + Bres);
			        		sender().tell(trip, self());
			        	}
			        	else if(resA.contains("No seats") || resB.contains("No seats")){
			        		// AA001+CA002+AA002
			        		System.out.println("in 3rd case");
			        		trip = "T" + String.valueOf(System.currentTimeMillis());
			        		String BookingIdA1= "BookingA1" + String.valueOf(System.currentTimeMillis());
			        		String BookingIdC1= "BookingC" + String.valueOf(System.currentTimeMillis());
			        		String BookingIdA2= "BookingA2" + String.valueOf(System.currentTimeMillis());
			        		System.out.println("In A + C + A booking");
			        		message.getMessage().put("flight", "AA001");
			        		message.getMessage().put("bookingId", BookingIdA1);
				        	message.getMessage().put("trip", trip);
			        		Future<Object> askSeatsA1 = akka.pattern.Patterns.ask(AActor, message,timeout);
				        	String resA1 = (String) Await.result(askSeatsA1, timeout.duration());
				        	message.getMessage().put("flight", "CA002");
				        	message.getMessage().put("bookingId", BookingIdC1);
				        	message.getMessage().put("trip", trip);
				        	Future<Object> askSeatsC = akka.pattern.Patterns.ask(CActor, message,timeout);
				        	String resC1 = (String) Await.result(askSeatsC, timeout.duration());
				        	message.getMessage().put("flight", "AA002");
			        		message.getMessage().put("bookingId", BookingIdA2);
				        	message.getMessage().put("trip", trip);
			        		Future<Object> askSeatsA2 = akka.pattern.Patterns.ask(AActor, message,timeout);
				        	String resA2 = (String) Await.result(askSeatsA2, timeout.duration());
				        	if(resA1.contains("Booking Id") 
				        		&& resC1.contains("Booking Id") && resA2.contains("Booking Id") ){
				        		ObjectNode messageCommitA1 = Json.newObject();
				        		messageCommitA1.put("action","Confirm");
				        		messageCommitA1.put("flight", "AA001");
				        		messageCommitA1.put("bookingId", BookingIdA1);
				        		Future<Object> confirmSeatsA1 = akka.pattern.Patterns.ask(AActor, new TwoStageCommit(messageCommitA1),timeout);
				        		ObjectNode messageCommitC = Json.newObject();
				        		messageCommitC.put("action","Confirm");
				        		messageCommitC.put("flight", "CA002");
				        		messageCommitC.put("bookingId", BookingIdC1);
				        		Future<Object> confirmSeatsC = akka.pattern.Patterns.ask(CActor, new TwoStageCommit(messageCommitC),timeout);
				        		ObjectNode messageCommitA2 = Json.newObject();
				        		messageCommitA2.put("action","Confirm");
				        		messageCommitA2.put("flight", "AA002");
				        		messageCommitA2.put("bookingId", BookingIdA2);
				        		Future<Object> confirmSeatsA2 = akka.pattern.Patterns.ask(AActor, new TwoStageCommit(messageCommitA2),timeout);
				        		
				        		String Ares1 = (String) Await.result(confirmSeatsA1, timeout.duration());
				        		String Cres1 = (String) Await.result(confirmSeatsC, timeout.duration());
				        		String Ares2 = (String) Await.result(confirmSeatsA2, timeout.duration());
				        		System.out.println("In A + C + A result:" + Ares1 + Cres1 + Ares2);
				        		sender().tell(trip, self());
				        		
			        		
			        	}
				        	else{
				        		sender().tell("{Status: Error}", self());
				        	}
			        	
		        		
		        	}
			        	else{
			        		sender().tell("{Status: Error}", self());
			        	}
		        	}
		        	else{
		        		sender().tell("{Status: Error}", self());
		        	}
		        	
		        })
		        .matchAny(r -> {
		            // To turn it off
		        	sender().tell("Time Out", self());
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

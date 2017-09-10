package controllers;

import java.util.concurrent.CompletionStage;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;

import actors.*;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import play.libs.Json;
import play.mvc.*;
import scala.compat.java8.FutureConverters;
import static akka.pattern.Patterns.ask;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */
	final ActorRef AActor,BActor,CActor,BookActor;

    @Inject 
    public HomeController(ActorSystem system) {
    	AActor = system.actorOf(AAActor.getProps());
    	BActor = system.actorOf(BAActor.getProps());
    	CActor = system.actorOf(CAActor.getProps());
    	BookActor = system.actorOf(BookingActor.getProps(AActor,BActor,CActor));
    	
        
    }
    
    public CompletionStage<Result> sayHello(String msg) {
    	ObjectNode message = Json.newObject();
    	message.put("firstname", msg);
        return FutureConverters.toJava(ask(AActor, new FlightMessage(message), 1000))
                .thenApply(response -> ok((String) response));
    }
    public Result index() {
        return ok(views.html.index.render());
    }
    
    public Result getFlight(String operator, String flight){
    	return ok("operator: " + operator + ", flight: " + flight);
    	}
    
    public Result getOperators(){
    	return ok("Operators: " + "AA, BA, CA");
    	}
    
    public Result getOperatorFlights(String operator) {
    	String flights;
    	if(operator.equals("AA"))
    		flights = "AA001, AA002";
    	else if(operator.equals("BA"))
    		flights = "BA001";
    	else if(operator.equals("CA"))
    		flights = "CA001, CA002";
    	else return notFound("No such Operator found!");    	
    	return ok("operator: " + operator + ", flight: " + flights);
    	
    }
    
    public CompletionStage<Result> getOperatorFlightDetails(String operator, String flight){
    	ObjectNode message = Json.newObject();
    	message.put("action","availableSeats");
    	message.put("flight", flight);
    	message.put("operator",operator);
    	ActorRef op = null;
    	if(operator.equals("AA"))
    		op = AActor;
    	else if(operator.equals("BA"))
    		op = BActor;
    	else if(operator.equals("CA"))
    		op = CActor;
    	//else return (CompletionStage<Result>) notFound("No such Operator found!");  
        return FutureConverters.toJava(ask(op, new FlightMessage(message), 1000))
                .thenApply(response -> resp((String) response));
    }
    
    public CompletionStage<Result> getTrips(){
    	ActorRef op = null;
    	ObjectNode message = Json.newObject();
    	message.put("action","get");
    	op = BookActor;
    	return FutureConverters.toJava(ask(op, new BookingMessage(message), 10000))
                .thenApply(response -> resp((String) response));
    	
    }
    
    public CompletionStage<Result> postTrips(String from, String to){
    	ActorRef op = null;
    	ObjectNode message = Json.newObject();
    	message.put("action","Hold");
    	//message.put("flight", "AA001");
    	op = BookActor;
    	try{
    		CompletionStage<Result> res = FutureConverters.toJava(ask(op, new TwoStageCommit(message), 10000))
                    .thenApply(response -> resp((String) response));
    		return res;
    	}
    	catch(Exception e){
    		return (CompletionStage<Result>) ok("Timeout");
    	}
    	
    }
    public CompletionStage<Result> postFail(String airline){
    	ObjectNode message = Json.newObject();
    	message.put("action","fail");
    	ActorRef op = null;
    	if(airline.equals("AA"))
    		op = AActor;
    	else if(airline.equals("BA"))
    		op = BActor;
    	else if(airline.equals("CA"))
    		op = CActor;
    	return FutureConverters.toJava(ask(op, new DebugMessage(message), 10000))
                .thenApply(response -> resp((String) response));
    	
    }
    public CompletionStage<Result> postNoResponse(String airline){
    	ObjectNode message = Json.newObject();
    	message.put("action","noResponse");
    	ActorRef op = null;
    	if(airline.equals("AA"))
    		op = AActor;
    	else if(airline.equals("BA"))
    		op = BActor;
    	else if(airline.equals("CA"))
    		op = CActor;
    	return FutureConverters.toJava(ask(op, new DebugMessage(message), 10000))
                .thenApply(response -> resp((String) response));
    	
    }
    public CompletionStage<Result> postReset(String airline){
    	ObjectNode message = Json.newObject();
    	message.put("action","reset");
    	ActorRef op = null;
    	if(airline.equals("AA"))
    		op = AActor;
    	else if(airline.equals("BA"))
    		op = BActor;
    	else if(airline.equals("CA"))
    		op = CActor;
    	return FutureConverters.toJava(ask(op, new DebugMessage(message), 10000))
                .thenApply(response -> resp((String) response));
    	
    }
    public CompletionStage<Result> getTripDetails(String TripId){
    	ActorRef op = null;
    	ObjectNode message = Json.newObject();
    	message.put("action","getDetails");
    	message.put("TripId",TripId);
    	op = BookActor;
    	return FutureConverters.toJava(ask(op, new BookingMessage(message), 10000))
    			.thenApply(response -> resp((String) response));
               
    }
    
    static Result resp(String resp){
    	if(resp.contains("Error"))
    	return notFound(resp);
    	else return ok(resp); 
    	
    }
}

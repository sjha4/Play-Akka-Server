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
	final ActorRef helloActor;

    @Inject 
    public HomeController(ActorSystem system) {
        helloActor = system.actorOf(AAActor.getProps());
    }
    
    public CompletionStage<Result> sayHello(String msg) {
    	ObjectNode message = Json.newObject();
    	message.put("firstname", msg);
        return FutureConverters.toJava(ask(helloActor, new FlightMessage(message), 1000))
                .thenApply(response -> ok((String) response));
    }
    public Result index() {
        return ok(views.html.index.render());
    }
    
    public Result getFlight(String operator, String flight){
    	return ok("operator: " + operator + ", flight: " + flight);
    	}

}

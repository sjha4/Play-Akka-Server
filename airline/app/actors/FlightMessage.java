package actors;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class FlightMessage {
	private ObjectNode message;
	
	public FlightMessage()
    {
    }
	
	 public FlightMessage( ObjectNode message )
     {
          this.message = message;
     }
	 
	 public ObjectNode getMessage()
     {
          return message;
     }
	 
	 public void setMessage( ObjectNode message )
     {
          this.message = message;
     }
}

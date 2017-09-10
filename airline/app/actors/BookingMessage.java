package actors;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class BookingMessage {
	private ObjectNode message;
	
	public BookingMessage()
    {
    }
	
	 public BookingMessage( ObjectNode message )
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

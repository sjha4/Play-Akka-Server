package actors;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class DebugMessage {
	private ObjectNode message;
	
	public DebugMessage()
    {
    }
	
	 public DebugMessage( ObjectNode message )
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

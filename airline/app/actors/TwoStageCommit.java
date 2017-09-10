package actors;

import com.fasterxml.jackson.databind.node.ObjectNode;

public class TwoStageCommit {
	private ObjectNode message;
	
	public TwoStageCommit()
    {
    }
	
	 public TwoStageCommit( ObjectNode message )
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

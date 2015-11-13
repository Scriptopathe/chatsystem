package chatsystem.messages;
import org.json.*;

public class TextMessage extends Message {
	
	private String message;
	
	public TextMessage(String message) 
	{
		super();
		this.setMessage(message);
	}

	@Override
	public String toJSON() 
	{
		JSONObject obj = new JSONObject();
		obj.put("type", Message.MESSAGE_TYPE_MSG);
		obj.put("message", this.getMessage());
		return obj.toString();
	}
		
	/* ------------------------------------------------------------------------
	 * Getters / Setters
	 * ----------------------------------------------------------------------*/
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}

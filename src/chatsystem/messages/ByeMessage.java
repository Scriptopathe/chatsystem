package chatsystem.messages;
import org.json.JSONObject;

public class ByeMessage extends Message {
	
	public ByeMessage() 
	{
		super();
	}

	@Override
	public String toJSON() 
	{
		JSONObject obj = new JSONObject();
		obj.put("type", Message.MESSAGE_TYPE_BYE);
		return obj.toString();
	}
	
	
}

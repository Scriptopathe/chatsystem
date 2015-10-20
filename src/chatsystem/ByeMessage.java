package chatsystem;
import org.json.JSONObject;

public class ByeMessage extends Message {
	
	public ByeMessage(String nickname) 
	{
		super(nickname);
	}

	@Override
	public String toJSON() 
	{
		JSONObject obj = new JSONObject();
		obj.put("type", Message.MESSAGE_TYPE_BYE);
		obj.put("nickname", this.getNickname());
		return obj.toString();
	}
	
	
}

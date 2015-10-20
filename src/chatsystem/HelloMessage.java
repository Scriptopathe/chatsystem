package chatsystem;
import org.json.*;

public class HelloMessage extends Message {
	
	private Boolean reqReply;
	
	public HelloMessage(String nickname, Boolean reqReply) 
	{
		super(nickname);
		this.setReqReply(reqReply);
	}

	@Override
	public String toJSON() 
	{
		JSONObject obj = new JSONObject();
		obj.put("type", Message.MESSAGE_TYPE_HELLO);
		obj.put("nickname", this.getNickname());
		obj.put("reqReply", this.getReqReply());
		return obj.toString();
	}
		
	/* ------------------------------------------------------------------------
	 * Getters / Setters
	 * ----------------------------------------------------------------------*/
	private Boolean getReqReply() {
		return reqReply;
	}

	private void setReqReply(Boolean reqReply) {
		this.reqReply = reqReply;
	}
}

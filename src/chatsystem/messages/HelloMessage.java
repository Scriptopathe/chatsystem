package chatsystem.messages;
import org.json.*;

public class HelloMessage extends Message {
	
	private Boolean reqReply;
	private String nickname;
	
	public HelloMessage(String nickname, Boolean reqReply) 
	{
		super();
		this.setNickname(nickname);
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
	public Boolean getReqReply() {
		return reqReply;
	}

	public void setReqReply(Boolean reqReply) {
		this.reqReply = reqReply;
	}
	
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
}

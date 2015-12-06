package chatsystem.messages;
import org.json.*;


public class FileRequestResponseMessage extends Message{
	
	// accpet ou non le transfert de fichier
	private boolean ok;
	private int timestamp;
	
	public FileRequestResponseMessage(boolean ok, int timestamp)
	{
		super();
		this.setOk(ok);
		this.setTimestamp(timestamp);
		
	}
	
	public String toJSON() 
	{
		JSONObject obj = new JSONObject();
		obj.put("type", Message.MESSAGE_TYPE_RESP_REQUEST);
		obj.put("timestamp", this.getTimestamp());
		obj.put("ok", this.isOk());
		return obj.toString();
	}
	
	
	
	/* ------------------------------------------------------------------------
	 * Getters / Setters
	 * ----------------------------------------------------------------------*/
	public boolean isOk() {
		return ok;
	}

	public void setOk(boolean ok) {
		this.ok = ok;
	}

	public int getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}
	
	

}

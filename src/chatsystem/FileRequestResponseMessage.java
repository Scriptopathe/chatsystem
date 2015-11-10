package chatsystem;
import org.json.*;


public class FileRequestResponseMessage extends Message{
	
	// accpet ou non le transfert de fichier
	private boolean ok;

	public FileRequestResponseMessage(boolean ok)
	{
		super();
		this.setOk(ok);
	}
	
	public String toJSON() 
	{
		JSONObject obj = new JSONObject();
		obj.put("type", Message.MESSAGE_TYPE_RESP_REQUEST);
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
	
	

}

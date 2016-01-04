package chatsystem.messages;
import org.json.*;

public class FileRequestMessage extends Message{
	
	private String fileName;
	private int timestamp;
	
	public FileRequestMessage(String file, int timestamp)
	{
		this.setFileName(file);
		this.setTimestamp(timestamp);
	}

	public String toJSON() 
	{
		JSONObject obj = new JSONObject();
		obj.put("type", Message.MESSAGE_TYPE_FILE_REQUEST);
		obj.put("timestamp", this.getTimestamp());
		obj.put("name", this.getFileName());
		return obj.toString();
	}
	
	
	
	/* ------------------------------------------------------------------------
	 * Getters / Setters
	 * ----------------------------------------------------------------------*/
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}
	
}

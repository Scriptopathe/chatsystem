package chatsystem.messages;
import org.json.*;

public class FileRequestMessage extends Message{
	
	private String fileName ;
	
	public FileRequestMessage(String file)
	{
		this.setFileName(file);
	}

	public String toJSON() 
	{
		JSONObject obj = new JSONObject();
		obj.put("type", Message.MESSAGE_TYPE_FILE_REQUEST);
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
	
}

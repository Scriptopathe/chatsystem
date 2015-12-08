package chatsystem.gui;


public class FileTransfer {
	
	public int timestamp;
	public int progress;
	public boolean ended;
	public String filename;
	public boolean accepted;
	public boolean isIncoming;
	
	public FileTransfer(boolean isIncoming, int timestamp, String filename)
	{
		this.timestamp = timestamp;
		this.progress=0;
		this.ended = false;
		this.filename = filename;
		this.isIncoming = isIncoming;
		this.accepted = false;
	}
	
	public String getStateName()
	{
		if(this.accepted)
		{
			if(this.ended)
				return "Transfer complete";
			else
				return "In progress... " + progress/1000 + "Kb / ... Kb";
		}
		else
		{
			if(this.ended)
				return "Transfer rejeted.";
			else
				if(this.isIncoming)
					return "Accept ? ID = " + timestamp;
				else
					return "Waiting for accept...";
		}
	}
	
	public String toString()
	{
		return this.filename + " : " + getStateName();
	}
	
	
}

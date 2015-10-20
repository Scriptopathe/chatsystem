package tests;
import java.io.IOException;
import java.net.*;

import org.json.JSONArray;
import org.json.JSONObject;

import chatsystem.*;

public class UDPStackTest 
{
	
	public static void main(String[] args) throws UnknownHostException, IOException
	{
		/*
		JSONObject obj = new JSONObject();

		obj.put("name", "foo\"jdejiu\"");
		obj.put("num", new Integer(100));
		obj.put("balance", new Double(1000.21));
		obj.put("is_vip", new Boolean(true));
		
		System.out.print(obj);
		
		JSONObject blbl = new JSONObject(obj.toString());
		
		System.out.print(blbl.get("name"));*/
		
		int test = 0;
		if(test == 0)
		{
			// Crée un receiver TCP
			UDPReceiver rcv = new UDPReceiver(8045);
			rcv.addListener(new UDPReceiverListener() {
				
				@Override
				public void onMessageReceived(InetAddress source, String message) {
					// TODO Auto-generated method stub
					System.out.println("Source : " + source + " | Message : " + message);
				}
			});
			// Lance le receiver TCP
			rcv.start();
			
			// Envoie un message au receiver.
			UDPSender snd = new UDPSender(8045);
			snd.sendMessage(Inet4Address.getByName("localhost"), new HelloMessage("patate", false));
			snd.sendMessage(Inet4Address.getByName("10.1.5.85"), new HelloMessage("patate", false));
		}
		else
		{
			// Crée un receiver TCP
			UDPReceiver rcv = new UDPReceiver(8045);
			rcv.addListener(new UDPReceiverListener() {
				
				@Override
				public void onMessageReceived(InetAddress source, String message) {
					// TODO Auto-generated method stub
					System.out.println("Source : " + source + " | Message : " + message);
				}
			});
			// Lance le receiver TCP
			rcv.start();
			System.out.println("Server running...");
		}
		
		
		
	}
}

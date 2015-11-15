package tests;

import java.util.Scanner;

import chatsystem.controler.MainController;
import chatsystem.controler.MainControllerListener;
import chatsystem.model.User;
import chatsystem.gui.*;
public class MainControllerTest {
	public static void main(String[] args)
	{
		ChatGUI g = new ChatGUI();
		
		MainController ctrl1 = new MainController();
		ctrl1.connect("Benard");
		ctrl1.addListener(new MainControllerListener() {
			
			@Override
			public void OnUserDisconnected(User usr) {
				System.out.println("User " + usr + " disconnected");
				
			}
			
			@Override
			public void OnUserConnected(User usr) {
				// TODO Auto-generated method stub
				System.out.println("User " + usr + " connected");
				
			}
			
			@Override
			public void OnMessageReceived(User usr, String textMessage) {
				// TODO Auto-generated method stub
				System.out.println("User " + usr + " says : " + textMessage);
			}
			
			@Override
			public void OnLog(String text, boolean isError) {
				// TODO Auto-generated method stub
				System.out.println("[CtrlLog] " + text);
			}
			
			@Override
			public void OnFileTransferEnded(User usr, String filename) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void OnFileRequest(User usr, String filename) {
				// TODO Auto-generated method stub
				
			}
		});
		while(true)
		{
			Scanner s = new Scanner(System.in);
			
			User usr = ctrl1.getUserList().getUserByNickname("mikael");
			if(usr != null)
			{
				ctrl1.sendTextMessage(usr, s.nextLine());
				System.out.println("Sent message ! ");
			}
			
		}
	}
}

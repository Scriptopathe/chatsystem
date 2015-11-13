package tests;

import java.util.Scanner;

import chatsystem.controler.MainController;
import chatsystem.model.User;

public class MainControllerTest {
	public static void main(String[] args)
	{
		MainController ctrl1 = new MainController();
		ctrl1.connect("CTRL 1");
		
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

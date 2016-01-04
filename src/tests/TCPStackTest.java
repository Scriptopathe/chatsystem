package tests;

import java.net.*;
import java.io.*;

import chatsystem.network.TCPServer;
import chatsystem.network.TCPReceiver;
import chatsystem.network.TCPProgressListener;
import chatsystem.network.TCPSender;



public class TCPStackTest {


	public static void main(String[] args) throws Exception
	{
		// TODO : delete les socket dans la hashmap
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		TCPServer l = new TCPServer(8045);
		l.start();
		
		System.out.println("Creation de l'input stream");
		InputStream in = new ByteArrayInputStream("Salut ! szgiduhqsiduhiqsuhdiuqshiduqshiduhqsiudhiqsuhdiu".getBytes("UTF-8"));
		TCPSender s = new TCPSender(8045, Inet4Address.getByName("localhost"), in);
		s.start();

		System.out.println("Réception du receiver");
		while(l.popSocket(Inet4Address.getByName("localhost")) == null) { }

		System.out.println("Creation du socket receiver");
		System.out.println("Coucou Laura et Josué ! :)");
		
		TCPReceiver recv = new TCPReceiver(l, Inet4Address.getByName("localhost") , out);
		
		// Pseudo observer
		recv.addListener(new TCPProgressListener() {
			
			@Override
			public void onNotifyProgress(InetAddress source, int progress) {

			}
			
			@Override
			public void onNotifyEnd(InetAddress source) {
				try 
				{
					System.out.println(out.toString("UTF-8"));
				} 
				catch (UnsupportedEncodingException e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		recv.start();
		
		
		while(true)
		{
			String str = out.toString("UTF-8");
			// System.out.println(str);
			Thread.sleep(100);
		}
		
	}
}

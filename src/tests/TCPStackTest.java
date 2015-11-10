package tests;

import java.net.*;
import java.io.*;

import chatsystem.TCPListener;
import chatsystem.TCPReceiver;
import chatsystem.TCPReceiverListener;
import chatsystem.TCPSender;

public class TCPStackTest {


	public static void main(String[] args) throws Exception
	{
		// TODO : delete les socket dans la hashmap
		final ByteArrayOutputStream out = new ByteArrayOutputStream();
		TCPListener l = new TCPListener(8045);
		l.start();
		
		System.out.println("Creation de l'input stream");
		InputStream in = new ByteArrayInputStream("Salut ! szgiduhqsiduhiqsuhdiuqshiduqshiduhqsiudhiqsuhdiu".getBytes("UTF-8"));
		TCPSender s = new TCPSender(8045, Inet4Address.getByName("localhost"), in);
		s.start();

		System.out.println("Réception du receiver");
		while(l.getSocket(Inet4Address.getByName("localhost")) == null) { }

		System.out.println("Creation du socket receiver");
		System.out.println("Coucou Laura et Josué ! :)");
		
		Socket sock = l.getSocket(Inet4Address.getByName("localhost"));
		TCPReceiver recv = new TCPReceiver(sock, out);
		
		// Pseudo observer
		recv.addListeners(new TCPReceiverListener() {
			
			@Override
			public void onNotifyProgress(InetAddress source, int progress) {
				try {
					System.out.println(out.toString("UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			@Override
			public void onNotifyEnd(InetAddress source) {
				// TODO Auto-generated method stub	
				
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

package tests;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import chatsystem.controler.ChatSettings;
import chatsystem.gui.ConnectionFrame;
import chatsystem.gui.ConversationFrame;

public class TestGUI {

	/**
	 * @param args
	 * @throws UnsupportedLookAndFeelException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException {
		// UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        UIManager.setLookAndFeel(
            UIManager.getSystemLookAndFeelClassName());
        
		ConnectionFrame frame = new ConnectionFrame(new ChatSettings(8045, 8046));
		frame.setVisible(true);
		
		ConnectionFrame frame2 = new ConnectionFrame(new ChatSettings(8046, 8045));
		frame2.setVisible(true);
		// TODO : remontée des informations -> fileRequestResponse
	}

}

package tests;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import chatsystem.gui.ConnectionFrame;

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
		ConnectionFrame frame = new ConnectionFrame();
		frame.setVisible(true);
	}

}

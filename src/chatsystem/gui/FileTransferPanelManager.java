package chatsystem.gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class FileTransferPanelManager extends JScrollPane implements ActionListener
{
	HashMap<FileTransfer, FileTransferPanel> panels = new HashMap<FileTransfer, FileTransferPanel>();
	List<ActionListener> listeners;
	JPanel panel;
	
	public FileTransferPanelManager()
	{
		super();
		
		this.listeners = new ArrayList<ActionListener>();
		
		this.panel = new JPanel();
		this.panel.setLayout(new BoxLayout(this.panel, BoxLayout.Y_AXIS));
		this.panel.add(Box.createVerticalGlue());
		this.setViewportView(this.panel);
		this.setPreferredSize(new Dimension(150, 10));
	}
	
	/**
	 * Ajoute un FileTransfer au panel.
	 * @param transfer
	 */
	public void addFileTransfer(FileTransfer transfer)
	{
		FileTransferPanel panel = new FileTransferPanel(transfer);
		panels.put(transfer, panel);
		panel.addActionListener(this);
		
		this.panel.add(panel);
		
		this.updateUI();
	}	
	
	public void refresh()
	{
		for(FileTransfer ft : panels.keySet())
		{
			panels.get(ft).refresh();
		}
		this.updateUI();
	}
	
	public void addActionListener(ActionListener l)
	{
		this.listeners.add(l);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		this.notifyObservers((FileTransfer)e.getSource());
	}
	
	private void notifyObservers(FileTransfer transfer)
	{
		for(ActionListener l : this.listeners) l.actionPerformed(new ActionEvent(transfer, 0, ""));
	}
}

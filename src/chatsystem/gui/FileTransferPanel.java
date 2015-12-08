package chatsystem.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class FileTransferPanel extends JPanel implements ActionListener
{
	FileTransfer transfer;
	JLabel l2 = new JLabel();
	JButton btn = new JButton("Accept");
	List<ActionListener> listeners;
	
	public FileTransferPanel(FileTransfer transfer)
	{
		this.transfer = transfer;
		this.listeners = new ArrayList<ActionListener>();
		
		this.setLayout(new GridLayout(3, 1));
		this.add(l2);
		this.add(btn);
		
		this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE), transfer.filename));
		this.setPreferredSize(new Dimension(100, 70));
		this.setMaximumSize(new Dimension(1000, 70));
		this.btn.addActionListener(this);
		refresh();
	}
	
	public void refresh()
	{
    	// L2
    	l2.setText(transfer.getStateName());
    	l2.setLocation(10, 30);
    	
    	if(transfer.ended)
    		if(transfer.accepted)
    			l2.setForeground(Color.GREEN);
    		else
    			l2.setForeground(Color.RED);
    	else
    	{
    		l2.setForeground(Color.GRAY);
    	}
    	
    	if(this.hasFocus())
    	{
    		this.setBackground(new Color(200, 200, 200));
    	}
    	else
    	{
    		this.setBackground(new Color(255, 255, 255));
    	}
    	
    	if(transfer.ended || transfer.accepted || !transfer.isIncoming)
    		this.btn.setVisible(false);
    	else
    		this.btn.setVisible(true);
	}
	
	public void addActionListener(ActionListener l)
	{
		this.listeners.add(l);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		this.notifyObservers();
	}
	
	private void notifyObservers()
	{
		for(ActionListener l : this.listeners) l.actionPerformed(new ActionEvent(this.transfer, 0, ""));
	}
}

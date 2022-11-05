package com.diy.software;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.diy.hardware.DoItYourselfStation;
import com.diy.simulation.Customer;

public class AttendantGUI {

	
	JFrame attendantFrame;
	
	JPanel leftPanel;
	JPanel rightPanel;
	JPanel infoPanel;
	JPanel itemPanel;
	JPanel subItemPanel;
	JPanel payPanel;
	JPanel subPayPanel;
	JPanel pinPanel;
	
	JLabel stationIDLabel;
	JLabel pinLabel;
	JLabel billLabel;
	JLabel totalLabel;
	JLabel weightLabel;
	
	JTextArea billTextArea;
	
	CustomerGUI customerGUI;
	

	int currentStation;
	
	/*
	 * Constructor class
	 * takes a Customer as input as to create an instance of CustomerGUI
	 * for later iterations with multiple diyStations, change this to an array of customers,
	 * will also need a way to store currently selected customer/diyStation
	 * 
	 * @author Benjamin Niles
	 * 
	 */
	public AttendantGUI(Customer customer, DoItYourselfStation station) throws IOException{
	
		customerGUI = new CustomerGUI(customer, station);
		
		currentStation = 1;
		
		frameSetup();
		buildLeftPanel();
		buildRightPanel();

		attendantFrame.pack();
		addListeners();
		
		attendantFrame.setVisible(true);

	}
	

/*
 * sets up and populates the main JFrame instance attendantFrame
 * works by calling helper methods; this also decreases clutter
 * 
 * @author Benjamin Niles
 */
	public void frameSetup() {
		
		attendantFrame = new JFrame("ATTENDANT STATION");
		Dimension d = new Dimension(400,200);
		attendantFrame.setMinimumSize(d);//may want to specify a max size as well
		attendantFrame.setLocationRelativeTo(null);
		attendantFrame.setLayout(new GridLayout(0,2));
		attendantFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	
	/*
	 * method to add components to the right panel of attendantFrame
	 * 
	 * @author Benjamin Niles
	 */
	public void buildRightPanel() {
		
		rightPanel = new JPanel();
		rightPanel.setLayout(new BorderLayout());
		
		billLabel = new JLabel("Bill", JLabel.CENTER);
		
		
		//maybe make this a JTable or JList in future iterations- easier to find and remove specific items than lines of text
		billTextArea = new JTextArea(1,1);
		billTextArea.setEditable(false);
		JScrollPane scrollBill = new JScrollPane(billTextArea);
		
		
		infoPanel = new JPanel(new GridLayout());
		totalLabel = new JLabel("total: "+0);
		weightLabel = new JLabel("weight: "+0.0);
		
		infoPanel.add(totalLabel);
		infoPanel.add(weightLabel);
		
		rightPanel.add(billLabel, BorderLayout.NORTH);
		rightPanel.add(scrollBill, BorderLayout.CENTER);
		rightPanel.add(infoPanel, BorderLayout.SOUTH);

		
		attendantFrame.add(rightPanel);
		
	}
	
	/*
	 * method to add components to the left panel of attendantFrame
	 * 
	 * @author Benjamin Niles
	 */
	public void buildLeftPanel() {
		leftPanel = new JPanel(new GridLayout(2,1));
		itemPanel = new JPanel(new BorderLayout());
		
		stationIDLabel = new JLabel("Current diy station: "+currentStation,JLabel.CENTER);
		
		itemPanel.add(stationIDLabel, BorderLayout.NORTH);
		
		leftPanel.add(itemPanel);	
		attendantFrame.add(leftPanel);
	}
	
	
	/*
	 * method to add listeners to customerGUI, which will then update components in attendantGUI accordingly
	 * when customerGUI is updated, the attendantGUI window needs to be clicked on/brought into focus to update as well
	 * 
	 * for future iterations with multiple diyStations, will need something like currentDIYStation.addListener, 
	 * where currentDIYStation is a global variable that stores a customerGUI
	 * 
	 * @author Benjamin Niles
	 */
	public void addListeners() {
		
		customerGUI.leftPanel.addPropertyChangeListener(e->{
			billTextArea.setText(customerGUI.billTextArea.getText());
			totalLabel.setText(customerGUI.totalLabel.getText());
			weightLabel.setText(customerGUI.totalLabel.getText());
			
			
		});
		
	}
	

	
}

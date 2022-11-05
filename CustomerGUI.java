package com.diy.software;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.MutableComboBoxModel;
import javax.swing.text.BadLocationException;

import com.diy.hardware.BarcodedProduct;
import com.diy.hardware.external.ProductDatabases;
import com.diy.simulation.Customer;
import com.jimmyselectronics.Item;
import com.jimmyselectronics.necchi.Barcode;
import com.jimmyselectronics.necchi.BarcodedItem;
import com.jimmyselectronics.necchi.Numeral;
import com.jimmyselectronics.opeechee.Card;

import java.util.ArrayList;
import java.util.Arrays;

public class CustomerGUI{

	JFrame customerFrame;
	
	JPanel leftPanel;
	JPanel rightPanel;
	JPanel infoPanel;
	JPanel itemPanel;
	JPanel subItemPanel;
	JPanel payPanel;
	JPanel subPayPanel;
	JPanel pinPanel;
	
	JLabel itemLabel;
	JLabel cardLabel;
	JLabel pinLabel;
	JLabel billLabel;
	JLabel totalLabel;
	JLabel weightLabel;
	
	JButton addItemScanButton;
	JButton payCCButton;
	
	JTextArea billTextArea;
	JPasswordField pinInput;
	
	JComboBox<Object> shoppingCartList;
	JComboBox<Object> creditCardList;
	BarcodedItem barcodedItem1;
	Customer customer; //has a shopping cart
	PayByCC payBycc;
	
	ArrayList<BarcodedProduct> productList;
	

	int count;
	long totalCost = 0;
	double totalWeight = 0.0;
	
	
	/*
	 * Constructor class
	 * @author Benjamin Niles
	 */
	public CustomerGUI(Customer customer_given) throws IOException
	{
		customer = customer_given;
		count = 0;
		frameSetup();
		buildLeftPanel();
		buildRightPanel();
	
		customerFrame.pack();
		addListeners();
			
		customerFrame.setVisible(true);
	
	}


/*
 * sets up and populates the main JFrame instance customerFrame
 * works by calling helper methods; this also decreases clutter
 * @author Benjamin Niles
 */
	public void frameSetup() {
		
		customerFrame = new JFrame("CUSTOMER CHECKOUT ");
		Dimension d = new Dimension(400,200);
		customerFrame.setMinimumSize(d);//may want to specify a max size as well
		customerFrame.setLocationRelativeTo(null);
		customerFrame.setLayout(new GridLayout(0,2));
		customerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	
	/*
	 * method to add components to the right panel of customerFrame
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
		totalLabel = new JLabel("total: "+count);
		weightLabel = new JLabel("weight: "+count);
		
		infoPanel.add(totalLabel);
		infoPanel.add(weightLabel);
		
		rightPanel.add(billLabel, BorderLayout.NORTH);
		rightPanel.add(scrollBill, BorderLayout.CENTER);
		rightPanel.add(infoPanel, BorderLayout.SOUTH);

		
		customerFrame.add(rightPanel);
		
	}
	
	/*
	 * method to add components to the left panel of customerFrame
	 * 
	 * @author Benjamin Niles
	 */
	public void buildLeftPanel() {
		
		leftPanel = new JPanel(new GridLayout(2,1));
		itemPanel = new JPanel(new BorderLayout());
		subItemPanel = new JPanel(new GridLayout(1,2));
		payPanel = new JPanel(new BorderLayout());
		subPayPanel = new JPanel(new GridLayout(1,2));
		pinPanel = new JPanel(new GridLayout(1, 2));
		
		productList = new ArrayList<BarcodedProduct>(customer.shoppingCart.size());
		String[] shoppingList = new String[customer.shoppingCart.size()];
		for(int i =0; i<customer.shoppingCart.size();i++) {
			productList.add(ProductDatabases.BARCODED_PRODUCT_DATABASE.get(((BarcodedItem)customer.shoppingCart.get(i)).getBarcode()));
			shoppingList[i]= productList.get(i).getDescription();
			System.out.println("added item to shoppingList: " + shoppingList[i]);
			
		}
		
		shoppingCartList = new JComboBox<Object>(shoppingList);
		shoppingCartList.setModel((MutableComboBoxModel<Object>)shoppingCartList.getModel());
		shoppingCartList.setSelectedIndex(-1);
		itemLabel = new JLabel("Current item: ",JLabel.RIGHT);
		addItemScanButton = new JButton("Scan Item");
		
		String[] cardList = new String[customer.wallet.cards.size()];
		for(int i =0; i<customer.wallet.cards.size(); i++) {
			cardList[i]= customer.wallet.cards.get(i).kind;
			System.out.println("added card to cardList: " + cardList[i]);
			
		}
		
		creditCardList = new JComboBox<Object>(cardList);
		creditCardList.setSelectedIndex(-1);
		creditCardList.setEnabled(false);
		cardLabel = new JLabel("current card: ", JLabel.RIGHT);
		payCCButton = new JButton("Pay by Credit Card");
		cardLabel.setEnabled(false);
		payCCButton.setEnabled(false);
		pinLabel = new JLabel("Enter PIN: ", JLabel.RIGHT);
		pinInput = new JPasswordField(4);
		pinLabel.setEnabled(false);
		pinInput.setEnabled(false);
		
		subItemPanel.add(itemLabel);
		subItemPanel.add(shoppingCartList);
		itemPanel.add(subItemPanel, BorderLayout.NORTH);
		itemPanel.add(addItemScanButton, BorderLayout.CENTER);
	
		subPayPanel.add(cardLabel);
		subPayPanel.add(creditCardList);
		pinPanel.add(pinLabel);
		pinPanel.add(pinInput);
		payPanel.add(subPayPanel, BorderLayout.NORTH);
		payPanel.add(payCCButton, BorderLayout.SOUTH);
		payPanel.add(pinPanel, BorderLayout.CENTER);
		
		leftPanel.add(itemPanel);
		leftPanel.add(payPanel);
		
		
		customerFrame.add(leftPanel);
	}
	
	
	
	
	/*
	 * method to add listeners to various components in the GUI
	 * customer interactivity/simulation is handled here
	 * 
	 * @author Benjamin Niles
	 */
	public void addListeners() throws IOException {
	
		payCCButton.addActionListener(e->{
			
			//check for preconditions? In GUI or in PayByCC.java?
			
			//pay by Credit Card method call goes here!
			//is card blocking/bank verification handled by payByCC?
			
			if(payBycc.postTransaction(totalCost, totalCost+5))
			{
				
				pinInput.setText("");
				count = 0;
				totalLabel.setText("total: "+totalCost);
				weightLabel.setText("weight: "+totalWeight);
				billTextArea.setText("");
				payCCButton.setEnabled(false);
				pinInput.setEnabled(false);
				pinLabel.setEnabled(false);
				pinLabel.setText("Enter pin: ");
				creditCardList.setSelectedIndex(-1);
				//need to call customer.replaceCardInWallet
				creditCardList.setEnabled(false);
				cardLabel.setEnabled(false);
				
				System.out.println("Transaction approved");
				System.out.println("Total cost = "+ totalCost);
			}
			else
			{
				payCCButton.setEnabled(false);
				pinInput.setEnabled(false);
				pinLabel.setEnabled(false);
				pinLabel.setText("Enter pin: ");
				creditCardList.setEnabled(true);
				cardLabel.setEnabled(true);
				
				System.out.println("Transaction denied");
			}
			
			
			
		});
		
		addItemScanButton.addActionListener(e->{
		
			//selected items are removed from the list and added to the text field in the right panel
			//cost and weight information are also updated
			
			String selectedItem = (String) shoppingCartList.getSelectedItem();
			billTextArea.append(selectedItem+"\n");
			
			for(int i = 0; i< productList.size();i++) {
				if(productList.get(i).getDescription().equals(selectedItem))
					System.out.println("lookup found item "+productList.get(i).getDescription());
					totalCost+=productList.get(i).getPrice();
					totalWeight+=productList.get(i).getExpectedWeight();
			}
			
			shoppingCartList.removeItem(shoppingCartList.getSelectedItem());
			
			//GUI updates according to number of items left in cart
			//if items left in cart, reset item selection to start of list
			//if no items, can't add any more, so disable comboBox and scan button
			if(shoppingCartList.getItemCount()>0) {
				shoppingCartList.setSelectedIndex(0);
				//itemLabel.setText("current item: "+shoppingCartList.getSelectedItem()+" at index "+shoppingCartList.getSelectedIndex());
			}else {
				itemLabel.setText("No items in cart");
				shoppingCartList.setEnabled(false);
				addItemScanButton.setEnabled(false);
			}
			
			//if(payCCButton.isEnabled()==false) payCCButton.setEnabled(true);
			
			if(creditCardList.isEnabled()==false) {
				cardLabel.setEnabled(true);
				creditCardList.setEnabled(true);
			}
		});
		
		
		shoppingCartList.addItemListener(e->{
			//itemLabel.setText("current item: "+shoppingCartList.getSelectedItem()+" at index "+shoppingCartList.getSelectedIndex());
		});
		creditCardList.addItemListener(e->{
			//cardLabel.setText("current item: "+creditCardList.getSelectedItem()+" at index "+creditCardList.getSelectedIndex());
			System.out.println("selected card "+creditCardList.getSelectedItem());
			customer.selectCard("Visa");
			if(pinInput.isEnabled()==false) { 
				pinLabel.setEnabled(true);
				pinInput.setEnabled(true);
			}
		});
		
		pinInput.addActionListener(e->{
			
			String fakePIN = "1234";//placeholder
			String fakePIN2 = String.valueOf(pinInput.getPassword());
			System.out.println(fakePIN);
			System.out.println(fakePIN2); //character array
			System.out.println(fakePIN.equals(fakePIN2));
					
			
			try {
				customer.insertCard(fakePIN);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			System.out.println("pin ok");
			pinLabel.setText("pin ok");
			payCCButton.setEnabled(true);
			pinInput.setEnabled(false);
			
			
				
		//	}else {
		//		pinLabel.setText("incorrect pin");
		//		pinInput.setText("");
		//	}
		});
		
	}


	public void getPayByCCObject(PayByCC payBycc_given) 
	{
		this.payBycc = payBycc_given;		
	}


	
	

	
	//public static void main(String[] args) {
		
	//	Customer customer = new Customer();
	//	
	//	CustomerGUI gui = new CustomerGUI(customer);
	//}
	
}
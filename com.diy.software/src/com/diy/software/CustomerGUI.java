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
import javax.swing.MutableComboBoxModel;

import com.diy.hardware.BarcodedProduct;
import com.diy.hardware.external.ProductDatabases;
import com.diy.simulation.Customer;
import com.jimmyselectronics.Item;
import com.jimmyselectronics.necchi.Barcode;
import com.jimmyselectronics.necchi.BarcodedItem;
import com.jimmyselectronics.necchi.Numeral;
import com.jimmyselectronics.opeechee.Card;

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
	Customer customer; //has a shoppoing cart
	BarcodedProduct barcodedProduct1, barcodedProduct2, barcodedProduct3;//
	Card credit_Card;
	PayByCC payBycc;
	

	int count;
	long total_cost = 0;
	double total_weight = 0.0;
	
	
	/*
	 * Constructor class
	 * takes a Customer as input to access ShoppingCart, Wallet, etc- currently unimplemented
	 * will also need to take in a ProductDatabase to access item details- currently unimplemented
	 */
	public CustomerGUI(Customer customer_given)
	{
		
		count = 0;
		this.customer = customer_given;

	}
	

	public void createGUI() 
	{
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
	 */
	public void buildLeftPanel() {
		
		leftPanel = new JPanel(new GridLayout(2,1));
		itemPanel = new JPanel(new BorderLayout());
		subItemPanel = new JPanel(new GridLayout(1,2));
		payPanel = new JPanel(new BorderLayout());
		subPayPanel = new JPanel(new GridLayout(1,2));
		pinPanel = new JPanel(new GridLayout(1, 2));
		
		//this is a temporary list, in actual implementation will need to use Barcode items in Customer.ShoppingCart
		//and compare them to ProductDatabase to get detailed information
		//Maybe a 2D array? each product has a barcode, description, price, and weight.
		//String[] shoppingList = {"item 1", "item 2", "item 3", "item 4"};
		long[] itemCosts = {barcodedProduct1.getPrice(),barcodedProduct2.getPrice(),barcodedProduct3.getPrice()};
		double[] itemWeights = {barcodedProduct1.getExpectedWeight(),barcodedProduct2.getExpectedWeight(),barcodedProduct3.getExpectedWeight()};
		String[] shoppingList = {barcodedProduct1.getDescription(), barcodedProduct2.getDescription(), barcodedProduct3.getDescription()};
		shoppingCartList = new JComboBox<Object>(shoppingList);
		shoppingCartList.setModel((MutableComboBoxModel<Object>)shoppingCartList.getModel());
		shoppingCartList.setSelectedIndex(-1);
		itemLabel = new JLabel("Current item: ",JLabel.RIGHT);
		addItemScanButton = new JButton("Scan Item");
		
		
		//likewise, this is a temporary credit card list
		//String[] cardList = {"card 1", "card 2", "card 3", "card 4"};
		String[] cardList = {credit_Card.cardholder};
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
	 */
	public void addListeners() {
	
		payCCButton.addActionListener(e->{
			
			//check for preconditions? In GUI or in PayByCC.java?
			
			//pay by Credit Card method call goes here!
			//is card blocking/bank verification handled by payByCC?
			
			pinInput.setText("");
			count = 0;
			totalLabel.setText("total: "+total_cost);
			weightLabel.setText("weight: "+total_weight);
			billTextArea.setText("");
			payCCButton.setEnabled(false);
			pinInput.setEnabled(false);
			pinLabel.setEnabled(false);
			pinLabel.setText("Enter pin: ");
			creditCardList.setEnabled(false);
			cardLabel.setEnabled(false);
		});
		
		addItemScanButton.addActionListener(e->{
		
			/*
			 * addItemWithScanner method call goes here!
			 *this is currently set up so that the GUI updates based on its own list stored in shoppingCartList, not from the contents of Customer.ShoppingCart
			 *
			 *visual explanation:
			 * button-> item removed from shoppingCartList
			 *		\> item removed from Customer.ShoppingCart
			 *      
			 * not:
			 * button->item removed from Customer.shoppingCart->item removed from shoppingCartList
			 * 
			 * should it be changed to the second setup? Would need to compare/search Barcodes with ProductDatabase again
			 */
			
			//selected items are removed from the list and added to the text field in the right panel
			//cost and weight information are also updated
			billTextArea.append(shoppingCartList.getSelectedItem()+"\n");
			
			//get the price of the selected item
			//selected item is BarcodedItem description, so we have to get the item from the description
			
			total_cost = total_cost + barcodedProduct1.getPrice();
			total_weight = total_weight + barcodedProduct1.getExpectedWeight();
			totalLabel.setText("total: "+total_cost);
			weightLabel.setText("weight: "+total_weight);
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
			if(pinInput.isEnabled()==false) { 
				pinLabel.setEnabled(true);
				pinInput.setEnabled(true);
			}
		});
		
		pinInput.addActionListener(e->{
			
			String fakePIN = "1234";//placeholder
			System.out.println(pinInput.getPassword()); //character array
			System.out.println(pinInput.getPassword().length);
			String passwordString = String.valueOf(pinInput.getPassword());
		//	System.out.println("00000"+(String)passwordString+"00000");
			//actual pin of Customer#1's Credit Card
			try 
			{
				//credit_Card.insert((String)passwordString); //not working
				credit_Card.insert(fakePIN);
				System.out.println("correct password, making transaction");
				payCCButton.setEnabled(true);
				pinInput.setEnabled(false);
				pinLabel.setText("pin ok");
				if(payBycc.postTransaction(total_cost, total_cost+5))
				{
					System.out.println("Transaction approved");
					System.out.println("Total cost = "+ total_cost);
				}
				else
				{
					System.out.println("Transaction denied");
				}
				
				
				//if here --> password correct
			} catch (IOException e1) {
				//if here password incorrect
				System.out.println("incorrect password");
				//e1.printStackTrace();
				pinLabel.setText("incorrect pin");
				pinInput.setText("");
			}
		//	if(Arrays.equals(pinInput.getPassword(),fakePIN.toCharArray())) {
		//		payCCButton.setEnabled(true);
		//		pinInput.setEnabled(false);
		//		pinLabel.setText("pin ok");
				
		//	}else {
		//		pinLabel.setText("incorrect pin");
		//		pinInput.setText("");
		//	}
		});
		
	}


	public void getBarcodedProducts(BarcodedProduct barcodedProduct1, BarcodedProduct barcodedProduct2,
			BarcodedProduct barcodedProduct3) 
	{
		this.barcodedProduct1 = barcodedProduct1;
		this.barcodedProduct2 = barcodedProduct2;
		this.barcodedProduct3 = barcodedProduct3;
		
	}


	public void getCreditCard(Card creditCard) 
	{
		this.credit_Card = creditCard;
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

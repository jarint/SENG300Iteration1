package com.diy.software;

import com.diy.hardware.BarcodedProduct;
import com.diy.hardware.external.ProductDatabases;
import com.diy.simulation.Customer;
import com.diy.simulation.Wallet;
import com.jimmyselectronics.necchi.Barcode;
import com.jimmyselectronics.necchi.BarcodedItem;
import com.jimmyselectronics.necchi.Numeral;
import com.jimmyselectronics.opeechee.Card;


/*
	-set up Product Database
	- make up a few BarcodedProducts
	-instantiate Customer
	- BarcodedItems (barcodes match products) into ShoppingCart
	- set up other stuff as needed
	-main method
	- runs everything via GUI class
 */
public class Driver 
{

	public static void main(String[] args) 
	{
		Barcode barcode1, barcode2, barcode3;
		BarcodedProduct barcodedProduct1, barcodedProduct2, barcodedProduct3;
		BarcodedItem barcodedItem1, barcodedItem2, barcodedItem3;
		
		double weightOfItem1 = 100.0;
		double weightOfItem2 = 200.0;
		double weightOfItem3 = 300.0;
		
		long costOfItem1 = 100;
		long costOfItem2 = 200;
		long costOfItem3 = 300;
		
		Customer customer1 = new Customer();
		Wallet customer1_wallet = new Wallet();
		Card creditCard;		
		PayByCC customer1_creditCard = new PayByCC();
		
		double customer1_maxCreditLimit = 5000;
		
	
		//set up Barcode#1,2,3
		barcode1 = new Barcode(new Numeral[] { Numeral.one, Numeral.two, Numeral.three, Numeral.four }); // 1234
		barcode2 = new Barcode(new Numeral[] { Numeral.five, Numeral.six, Numeral.seven, Numeral.eight }); // 5678
		barcode3 = new Barcode(new Numeral[] { Numeral.three, Numeral.three, Numeral.three, Numeral.three }); // 5678
		
		//barcoded produce having barcode1 as barcode, description = "Barcoded Item#1", Cost = 100, weight = 100.0		
		barcodedProduct1 = new BarcodedProduct(barcode1,"Barcoded Item#1",costOfItem1, weightOfItem1);
			
		//barcoded produce having barcode1 as barcode, description = "Barcoded Item#2", Cost = 200, weight = 200.0		
		barcodedProduct2 = new BarcodedProduct(barcode2,"Barcoded Item#2",costOfItem2, weightOfItem2);
		//barcodedProduct2.getDescription() ==> returns string of descripion;
		
		//barcoded produce having barcode1 as barcode, description = "Barcoded Item#3", Cost = 300, weight = 300.0		
		barcodedProduct3 = new BarcodedProduct(barcode3,"Barcoded Item#3",costOfItem3, weightOfItem3);
		
		
		//adding barcoded Item #1 into the database
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode1, barcodedProduct1);		
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode2, barcodedProduct2);
		ProductDatabases.BARCODED_PRODUCT_DATABASE.put(barcode3, barcodedProduct3);		
		//product database is set
		
		//create barcoded Items (to be used by customer class)
		barcodedItem1 = new BarcodedItem(barcode1, weightOfItem1);
		barcodedItem2 = new BarcodedItem(barcode2, weightOfItem2);
		barcodedItem3 = new BarcodedItem(barcode3, weightOfItem3);
		
		//add items into customer's shopping cart
		customer1.shoppingCart.add(barcodedItem1);
		customer1.shoppingCart.add(barcodedItem2);
		//barcodedItem3 is not added into the shopping cart
		
		//create a credit card, with default and correct values
		creditCard = customer1_creditCard.initialiseCreditCard("Visa", "123456789", "Customer#1's Credit Card", "123", "1234", true, true);
		
		//create the credit card company 
		customer1_creditCard.initialiseCreditCardCompany("Credit Card Company # 1", 500);
		
		//add the card into credit card company database
		customer1_creditCard.addCardIntoDatabase(customer1_maxCreditLimit);
		
		//create the customer wallet;
		customer1_wallet.cards.add(creditCard);			
		//Customer created
		//Credit card set and running (not blocked)
		
		//System.out.println("no errors");
		
		//customer1 --> Customer Object
		//customer1_creditCard --> PayByCC.java Object
		
		//integrate Drive with Customer GUI
		//pass the customer
		//pass the BarcodedProduct --> for descriptions
		CustomerGUI customer_GUI = new CustomerGUI(customer1);
		customer_GUI.getBarcodedProducts(barcodedProduct1,barcodedProduct2,barcodedProduct3);
		customer_GUI.getCreditCard(creditCard);
		customer_GUI.getPayByCCObject(customer1_creditCard);
		customer_GUI.createGUI();
		
		
	}
}

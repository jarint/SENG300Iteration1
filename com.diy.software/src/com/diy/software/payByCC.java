package com.diy.software;

import java.io.IOException;
import java.util.Calendar;
import java.util.Objects;

import com.diy.hardware.external.CardIssuer;
import com.jimmyselectronics.Item;
import com.jimmyselectronics.opeechee.Card;
import com.jimmyselectronics.opeechee.Card.CardInsertData;
import com.jimmyselectronics.opeechee.CardReader;

import ca.ucalgary.seng300.simulation.NullPointerSimulationException;
import com.jimmyselectronics.opeechee.InvalidPINException;

public class PayByCC {
	private CardIssuer cardIssuerCompany;
	private Card creditCard;
	private CardInsertData creditCardData;
	private String kind;
	private String cardNumber;
	private String cardHolder;
	private String cvv;
	private String pin;
	private boolean isTapEnabled;
	private boolean hasChip;
	private String companyName;
	private long maximumHoldCount;
	private double creditCardLimit ;

	private int count = 0;

	public PayByCC() {
		//empty constructor
	}


	/**
	 * 1. Customer I/O : Signals the insertion of a credit card and pin
	 * @param card: card customer input
	 * @return boolean if card is inserted or not
	 */
	public boolean insertCard(Card card) {
		System.out.println("Card is inserted");
		return true;
	}


	/**
	 * 2. System: Validates the PIN against the credit card
	 * @param card: card customer input
	 * @param input_pin: pin customer input
	 * @return boolean result of if pin is valid or not
	 */
	public boolean validateCard (Card card, String input_pin) throws InvalidPINException {
		if (input_pin == pin) {
			count = 0;
			return true;
		} else {
			count += 1;
			return false;
		}
	}

	/**
	 * 3. System: Signals to the Bank the details of the credit card and the amount to be charged
	 * @param card: card customer input
	 * @param price: amount to be charged
	 */
	public void bankCharge(Card card, int price){
		//??
	}


	/**
	 * 4. Bank: Signals to the System the hold number against the account of the credit card.
	 * // Probably unnecessary //
	 * @param price: payment to be made
	 * @return int hold number
	 */
	public double holdNumber(double price) {
		return price;
	}


	/**
	 * 5. System: Signals to the Bank that the transaction identified with the hold number should be posted, reducing the amount of credit available.
	 * @param card: card customer input
	 * @param price: payment to be made
	 * @return boolean result if the card can pay or not
	 */
	public boolean transaction(Card card, double price){

		double credit;
		if (blockCard(card)){
			System.out.println("This card is blocked");
			return false;
		}
		else {
			credit = this.creditCardLimit - holdNumber(price);
			if (credit >= 0) {
				System.out.println("$" + credit);
				return true;
			} else {
				System.out.println("Payment fail: Not enough balance");
				return false;
			}
		}
	}

	/**
	 * 6. Bank: Signals to the System that the transaction was successful
	 * deduct creditCardLimit by the payment that customer made
	 * @param card: card customer input
	 * @param price: payment to be made
	 * @return boolean result if the payment made successfully or not
	 */
	public boolean transactionResult(Card card, double price) {
		if (blockCard(card)){
			System.out.println("This card is blocked");
			return false;
		}
		else {
			if (creditCardLimit >= holdNumber(price)) {
				creditCardLimit = creditCardLimit - holdNumber(price);
				return true;
			} else {
				return false;
			}
		}
	}


	/**
	 * Customer I/O: updates the amount due displayed to the customer.
	 * @param price: payment to be made
	 */
	public void displayAmount(double price) {
		System.out.println("$" + price + "\n");
	}


	/**
	 * block card if invalid pin input more than 5 times
	 * ---- Make sure we are checking if the card is blocked or not everytime it is used.
	 * @param card: card customer input
	 */
	public boolean blockCard(Card card) {
		if (count >= 3) {
			return true;}
		else {
			return false;
		}
	}
/**
	 * Creates the Credit Card with the given data (assumption: the data given is correct and no errors are there)
	 * 
	 * @param kind_given : the kind of credit card given (example: MasterCard, Visa etc)
	 * @param cardNumber_given : the number of the credit card given (example:123456789)
	 * @param cardHolder_given: the name of the card holder (customer)
	 * @param cvv_given: the cvv at the back of the credit card
	 * @param pin_given: the pin of the credit card
	 * @param isTapEnabled_given: if tap is enabled on the credit card or not
	 * @param hasChip_given: if the credit card has a chip (so that it can be inserted into the card reader)
	 * 
	 * @return the credit card initialised with the above values
	 * 
	 * @author simrat_benipal
	 */
	public Card initialiseCreditCard(String kind_given, String cardNumber_given, String cardHolder_given, String cvv_given, String pin_given, boolean isTapEnabled_given, boolean hasChip_given)
	{
		kind = kind_given;
		cardNumber = cardNumber_given;
		cardHolder = cardHolder_given;
		cvv = cvv_given;
		pin = pin_given;
		isTapEnabled = isTapEnabled_given;
		hasChip = hasChip_given;
		
		//create the card
		creditCard = new Card(kind,cardNumber, cardHolder, cvv, pin, isTapEnabled, hasChip);
		
		return creditCard; //(type Card)
		//this can be used by the customer to be added into the wallet
		
	}
	
	
	/**
	 * Initialises the Credit Card Company with the data supplied
	 * 
	 * @param companyName_given: the name of the credit card issuer company
	 * @param maximumHoldCount_given : the maximun number of holds this company can put
	 * 
	 * @author simrat_benipal
	 */
	public void initialiseCreditCardCompany(String companyName_given, long maximumHoldCount_given)
	{
		companyName = companyName_given;
		maximumHoldCount = maximumHoldCount_given;
		cardIssuerCompany = new CardIssuer(companyName, maximumHoldCount);
	}
	
	/**
	 * Adds the credit card into the credit card company database
	 * @param credit_limit_given: the credit limit of the credit card
	 * 
	 * @author simrat_benipal
	 */
	public void addCardIntoDatabase( double credit_limit_given)
	{	
		Calendar expiry = Calendar.getInstance();
		expiry.set(2060, 10, 10);
			
		cardIssuerCompany.addCardData(cardNumber, cardHolder, expiry, cvv, credit_limit_given);
		setCreditCardLimit(credit_limit_given);
		//card added to company database	
		//assuming the data entered is always correct, we should have no errors
	}
	
	/**
	 * Posts the transaction into the credit card
	 * 
	 * @param amountToBeDeducted : the amount that is to be deducted from the credit card, the total amount of the transaction, given from the Customer GUI
	 * @param holdAmount: the amount that will be placed on hold until the transaction is approved
	 * 
	 * @return true if the transaction is posted succesfully else false
	 * 
	 * @author simrat_benipal
	 */
	public boolean postTransaction( double amountToBeDeducted, double holdAmount )
	{		
		//put a hold on the credit card
		double amountToBeHeld = holdAmount; 
		long holdNumber = cardIssuerCompany.authorizeHold(cardNumber,amountToBeHeld);
		if(cardIssuerCompany.postTransaction(cardNumber, holdNumber, amountToBeDeducted)) {
			setCreditCardLimit(getCreditCardLimit() - amountToBeDeducted);
			return true;
		}
		else
			return false;		
	}
	/**
	 * @return the creditCardLimit
	 * @author simrat_benipal
	 */
	public double getCreditCardLimit() 
	{
		return creditCardLimit;
	}

	/**
	 * Setter method for the Credit Card Limit (also remaining balance)
	 * @param creditCardLimit the creditCardLimit to set
	 * @author simrat_benipal
	 */
	public void setCreditCardLimit(double creditCardLimit) 
	{
		this.creditCardLimit = creditCardLimit;
	}

}

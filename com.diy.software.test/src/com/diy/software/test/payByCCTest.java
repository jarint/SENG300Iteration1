/**
 * 
 */
package com.diy.software.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.diy.software.PayByCC;

/**
 * @author simrat_benipal
 *
 */
public class PayByCC_test 
{
	private PayByCC payByCCobject = null;
	private double creditLimit = 5000;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception 
	{
		//create the object
		//initalize the credit card
		//initialize the credit card company
		//add the card into the company database
		payByCCobject = new PayByCC();
		payByCCobject.initialiseCreditCard("Visa", "123456789", "Customer#1", "123", "1234", true, true);
		payByCCobject.initialiseCreditCardCompany("Credit Card Company # 1", 500);
		payByCCobject.addCardIntoDatabase(creditLimit);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception 
	{
		payByCCobject = null;
	}
	

	@Test
	public void Test_balanceNoTransaction() 
	{
		double balance = payByCCobject.getCreditCardLimit();
		assertEquals(this.creditLimit, balance, 0.0);
	}
	
	@Test
	public void Test_transaction_bad()
	{
		//credit limit is 5000
		//amount to be put on hold 100
		//transaction amount 110 --> should result in fail
		boolean result = payByCCobject.postTransaction(110, 100);
		assertFalse(result);
	}
	
	@Test
	public void Test_transaction_bad2()
	{
		//credit limit is 5000
		//amount to be put on hold = credit card
		//transaction amount 6000 --> should result in fail
		
		boolean result = payByCCobject.postTransaction(6000, payByCCobject.getCreditCardLimit());
		assertFalse(result);
	}
	
	@Test
	public void Test_transaction_good()
	{
		//credit limit is 5000
		//amount to be put on hold = 100
		//transaction amount 60 --> should result in true
		boolean result = payByCCobject.postTransaction(60, 100);
		assertTrue(result);
	}
	
	@Test
	public void Test_transaction_remaining_balance()
	{
		//credit limit is 5000
		//amount to be put on hold = 100
		//transaction amount 60 --> should result in true
		boolean result = payByCCobject.postTransaction(60, 100);
		assertTrue(result);
		
		//remaining balance = credit limit, should be equal to 5000 - 60 = 4940
		assertEquals(4940.00, payByCCobject.getCreditCardLimit(), 0.0);
	}
	
	@Test
	public void Test_getCreditCardData_false()
	{
		String invalid_pin = "4567";
		assertFalse(payByCCobject.getCreditCardData(invalid_pin));
	}
	
	@Test
	public void Test_getCreditCardData_true()
	{
		String valid_pin = "1234";
		assertTrue(payByCCobject.getCreditCardData(valid_pin));
	}
	

}

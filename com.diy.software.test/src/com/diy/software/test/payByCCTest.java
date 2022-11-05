package com.diy.software.test;

import static org.junit.Assert.*;

import com.jimmyselectronics.opeechee.Card;
import com.jimmyselectronics.opeechee.InvalidPINException;
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
	private Card test_card = new Card("Visa", "123456789", "Bob", "123", "1234", true, true);

	/**
	 * initial setup before every tests includes, set up the credit card, set up the credit card company, add the card into the company database
	 * 
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception 
	{
		payByCCobject = new PayByCC();
		payByCCobject.initialiseCreditCard("Visa", "123456789", "Customer#1", "123", "1234", true, true);
		payByCCobject.initialiseCreditCardCompany("Credit Card Company # 1", 500);
		payByCCobject.addCardIntoDatabase(creditLimit);
	}

	/**
	 * Tear down after every test in this test suite
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception 
	{
		payByCCobject = null;
	}
	
	/**
	 * Tests the balance in the credit card, since we do not have any transaction posted on the credit card, the current balance will be equal to the credit card limit set, (5000)
	 * @exceptedResult 5000
	 */
	@Test
	public void Test_balanceNoTransaction() 
	{
		double balance = payByCCobject.getCreditCardLimit();
		assertEquals(this.creditLimit, balance, 0.0);
	}
	
	/**
	 * Tests to see if the transaction is posted or not
	 * Credit card limit is 5000, and transaction hold amount is 100, transaction amount is 100, so this test should return false
	 * because we are placing a transaction of 110 while we are only holding 100
	 * 
	 * @Excepted_Result False
	 */
	@Test
	public void Test_transaction_bad()
	{
		//Transaction amount = 110
		//Hold Amount = 100
		boolean result = payByCCobject.postTransaction(110, 100);
		assertFalse(result);
	}
	
	/**
	 * Tests to see if the transaction is posted or not
	 * Credit card limit is 5000, and transaction hold amount is 5000(equal to the credit card limit), transaction amount is 6000, so this test should return false
	 * because we are placing a transaction of 6000 while we are only holding 5000
	 * 
	 * @Excepted_Result False
	 */
	@Test
	public void Test_transaction_bad2()
	{
		//Transaction amount = 6000
		//Hold Amount = 5000
		boolean result = payByCCobject.postTransaction(6000, payByCCobject.getCreditCardLimit());
		assertFalse(result);
	}
	
	/**
	 * Tests to see if the transaction is posted or not
	 * Credit card limit is 5000, and transaction hold amount is 100, transaction amount is 60, so this test should pass
	 * because we are placing a transaction of 60 while we are holding 100
	 * 
	 * @Excepted_Result True
	 */
	@Test
	public void Test_transaction_good()
	{
		//credit limit is 5000
		//amount to be put on hold = 100
		//transaction amount 60 --> should result in true
		boolean result = payByCCobject.postTransaction(60, 100);
		assertTrue(result);
	}
	
	/**
	 * Testing the balance of the credit card after the transaction is posted
	 * 
	 * @Expected_result  True [4940 (5000 - 60 = 4940]
	 */
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
	public void Test_insertCard() {
		assertTrue(payByCCobject.insertCard(test_card));
	}

	@Test
	public void Test_validateCard1() throws InvalidPINException {
		assertTrue(payByCCobject.validateCard(test_card, "1234"));
	}

	@Test
	public void Test_validateCard2() throws InvalidPINException {
		assertFalse(payByCCobject.validateCard(test_card, "4321"));
	}

	@Test
	public void Test_bankCharge() {

	}

	@Test
	public void Test_transaction() {
		double price = 500;
		assertTrue(payByCCobject.transaction(test_card, price));
	}

	@Test
	public void Test_transaction2(){
		double price = 500000;
		assertFalse(payByCCobject.transaction(test_card, price));
	}

	@Test
	public void Test_transactionResult() {
		double price = 500;
		assertTrue(payByCCobject.transactionResult(test_card, price));
	}

	@Test
	public void Test_transactionResult2() {
		double price = 500000;
		assertFalse(payByCCobject.transactionResult(test_card, price));
	}

	@Test
	public void Test_blockCard() {
		assertFalse(payByCCobject.blockCard(test_card));
	}

	@Test
	public void Test_blockCard2() throws InvalidPINException{
		payByCCobject.validateCard(test_card, "0000");
		payByCCobject.validateCard(test_card, "1111");
		payByCCobject.validateCard(test_card, "2222");
		assertTrue(payByCCobject.blockCard(test_card));
	}

}

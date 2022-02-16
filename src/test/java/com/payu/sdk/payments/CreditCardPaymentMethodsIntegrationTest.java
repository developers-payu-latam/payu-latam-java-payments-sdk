/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 developers-payu-latam
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.payu.sdk.payments;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.payu.sdk.PayU;
import com.payu.sdk.PayUPayments;
import com.payu.sdk.exceptions.ConnectionException;
import com.payu.sdk.exceptions.SDKException;
import com.payu.sdk.model.Currency;
import com.payu.sdk.model.Language;
import com.payu.sdk.model.PaymentCountry;
import com.payu.sdk.model.PaymentMethodComplete;
import com.payu.sdk.model.TransactionResponse;
import com.payu.sdk.model.TransactionResponseCode;
import com.payu.sdk.util.TestEnvironment;
import com.payu.sdk.utils.LoggerUtil;

/**
 * <p>
 * 	Test of Credit Card Payment Methods with and without CVV2 security code
 * </p>
 * @author <a href="fernando.moreno@payulatam.com">Fernando Moreno</a>
 * @date 19/08/2014
 */
public class CreditCardPaymentMethodsIntegrationTest {

	/**
	 * Default response log message
	 */
	private static final String RESPONSE_LOG_MESSAGE = "{0}";

	/**
	 * List of Payment Method of the server
	 */
	private List<PaymentMethodComplete> lstPaymentMethod;

	private Map<String, Integer> mapAccounts;

	/**
	 * the created referenceCode
	 */
	protected static String referenceCode;

	@BeforeClass
	private void init() {

		PayU.apiKey = "012345678901";
		PayU.apiLogin = "012345678901";
		PayU.merchantId = "1";
		PayU.language = Language.en;
		PayU.isTest = false;

		TestEnvironment environment = TestEnvironment.SANDBOX;

		PayU.paymentsUrl = environment.getPaymentsApiUrl();

		mapAccounts = new HashMap<String, Integer>();
		mapAccounts.put(PaymentCountry.CO.name(), 1);
		mapAccounts.put(PaymentCountry.BR.name(), 3);
		mapAccounts.put(PaymentCountry.PA.name(), 8);
		mapAccounts.put(PaymentCountry.AR.name(), 9);
		mapAccounts.put(PaymentCountry.CL.name(), 10);
		mapAccounts.put(PaymentCountry.MX.name(), 11);
		mapAccounts.put(PaymentCountry.PE.name(), 500198);

		LoggerUtil.setLogLevel(Level.ALL);
	}

	/**
	 * Do ping
	 */
	@Test(priority = 0)
	public void doPing() {

		LoggerUtil
				.info("init {0} version {1}", PayU.API_NAME, PayU.API_VERSION);

		Thread.currentThread().setName("doPing");

		try {
			// Invokes the ping operation
			boolean response = PayUPayments.doPing();
			Assert.assertTrue(response, "Invalid ping");

		} catch (ConnectionException e) {

			// Service Unavailable
			LoggerUtil.error(e.getMessage(), e);

		} catch (SDKException e) {

			// SDK error
			LoggerUtil.error(e.getMessage(), e);
			Assert.fail(e.getMessage());
		}

	}

	/**
	 * Get payment methods test
	 */
	@Test(priority = 1)
	public void getPaymentMethods() {

		Thread.currentThread().setName("getPaymentMethods");

		try {
			lstPaymentMethod = PayUPayments.getPaymentMethods();
			LoggerUtil.info(RESPONSE_LOG_MESSAGE, lstPaymentMethod);
			Assert.assertNotNull(lstPaymentMethod, "Null response");
			Assert.assertFalse(lstPaymentMethod.isEmpty(), "Empty response");

		} catch (ConnectionException e) {

			// Service Unavailable
			LoggerUtil.error(e.getMessage(), e);

		} catch (SDKException e) {

			// SDK error
			LoggerUtil.error(e.getMessage(), e);
			Assert.fail(e.getMessage());
		}

	}

	/**
	 * Test Credit Card Payment Methods with and without Security Code
	 *  @author fernando.moreno
	 */
	@Test(enabled=false, dependsOnMethods = "getPaymentMethods",description ="Test Credit Card Payment Methods with and without Security Code")
	public void testPaymentMethodsCreditCardWithAndWithoutSecurityCode(){
		Thread.currentThread().setName("testPaymentMethodsCreditCardWithSecurityCode");
		if (lstPaymentMethod != null && !lstPaymentMethod.isEmpty())
		{
			CreditCard creditCard;
			for (PaymentMethodComplete paymentMethod:lstPaymentMethod){
				creditCard = CreditCard.fromString(paymentMethod.getDescription(), paymentMethod.getCountry());
				if (creditCard != null){
					//Test with CVV2
					doAuthorizationAndCaptureWithCVV2(creditCard, paymentMethod);
					//Test without CVV2
					doAuthorizationAndCaptureWithoutCVV2(creditCard, paymentMethod);
				}
			}
		}
	}


	/**
	 * Do authorization and capture with CVV2
	 */
	public void doAuthorizationAndCaptureWithCVV2(CreditCard creditCard, PaymentMethodComplete paymentMethod){
		LoggerUtil.info(paymentMethod.getDescription()+"-"+paymentMethod.getCountry()+"-CVV2");
		Thread.currentThread().setName(paymentMethod.getDescription()+"-"+paymentMethod.getCountry()+"-CVV2");

		Map<String, String> parameters = new HashMap<String, String>();

		String nameOnCard = "NAME " + Long.toString(System.currentTimeMillis());
		parameters.put(PayU.PARAMETERS.PAYER_NAME, nameOnCard);

		parameters.put(PayU.PARAMETERS.INSTALLMENTS_NUMBER, "3");

		parameters.put(PayU.PARAMETERS.COUNTRY, paymentMethod.getCountry());

		//obtain the account for country
		Integer accountId = mapAccounts.get(paymentMethod.getCountry());

		parameters.put(PayU.PARAMETERS.ACCOUNT_ID, accountId.toString());

		// Currency
		Currency txCurrency = Currency.USD;
		parameters.put(PayU.PARAMETERS.CURRENCY, txCurrency.toString());

		String orderReferenceCode = "A1B2C3" + System.currentTimeMillis();
		parameters.put(PayU.PARAMETERS.REFERENCE_CODE, orderReferenceCode);

		String description = "Test "+paymentMethod.getDescription()+" in "+paymentMethod.getCountry()+" with CVV2";
		parameters.put(PayU.PARAMETERS.DESCRIPTION, description);

		BigDecimal txValue = new BigDecimal(100.00).setScale(2,
				RoundingMode.HALF_UP);
		parameters.put(PayU.PARAMETERS.VALUE, txValue.toString());

		parameters.put(PayU.PARAMETERS.CREDIT_CARD_NUMBER,
				creditCard.getCreditCardNumber());
		parameters.put(PayU.PARAMETERS.CREDIT_CARD_EXPIRATION_DATE,
				creditCard.getExpirationDate());
		parameters.put(PayU.PARAMETERS.CREDIT_CARD_SECURITY_CODE,
				creditCard.getSecurityCode());
		parameters.put(PayU.PARAMETERS.PAYMENT_METHOD, creditCard.name());

		try {
			TransactionResponse response = PayUPayments
					.doAuthorizationAndCapture(parameters);

			LoggerUtil.info(RESPONSE_LOG_MESSAGE, response);

			Assert.assertNotNull(response, "Invalid response");
			Assert.assertNotNull(response.getResponseCode(),
					"Invalid response code");

			if (TransactionResponseCode.APPROVED.equals(response
					.getResponseCode())) {

				String transactionId = response.getTransactionId();
				Long orderId = response.getOrderId();
				LoggerUtil.info("Transaction Aproveed. orderId ={0} transactionId = {1}", orderId, transactionId );
			}

		} catch (ConnectionException e) {

			// Service Unavailable
			LoggerUtil.error(e.getMessage(), e);

		} catch (SDKException e) {

			// SDK error
			LoggerUtil.error(e.getMessage(), e);
			//throw e;
			//Assert.fail(e.getMessage());
		}

	}




	/**
	 * Do authorization and capture without security code
	 */
	public void doAuthorizationAndCaptureWithoutCVV2(CreditCard creditCard, PaymentMethodComplete paymentMethod)  {

		LoggerUtil.info(paymentMethod.getDescription()+"-"+paymentMethod.getCountry()+"-NO_CVV2");
		Thread.currentThread().setName(paymentMethod.getDescription()+"-"+paymentMethod.getCountry()+"-NO_CVV2");

		Map<String, String> parameters = new HashMap<String, String>();

		String nameOnCard = "NAME " + Long.toString(System.currentTimeMillis());
		parameters.put(PayU.PARAMETERS.PAYER_NAME, nameOnCard);

		parameters.put(PayU.PARAMETERS.INSTALLMENTS_NUMBER, "3");

		parameters.put(PayU.PARAMETERS.COUNTRY, paymentMethod.getCountry());

		//obtain the account for country
		Integer accountId = mapAccounts.get(paymentMethod.getCountry());

		parameters.put(PayU.PARAMETERS.ACCOUNT_ID, accountId.toString());

		// Currency
		Currency txCurrency = Currency.USD;
		parameters.put(PayU.PARAMETERS.CURRENCY, txCurrency.toString());
		String orderReferenceCode = "A1B2C3" + System.currentTimeMillis();
		parameters.put(PayU.PARAMETERS.REFERENCE_CODE, orderReferenceCode);

		String description = "Test "+paymentMethod.getDescription()+" in "+paymentMethod.getCountry()+" without CVV2";
		parameters.put(PayU.PARAMETERS.DESCRIPTION, description);

		// Transaction value
		BigDecimal txValue = new BigDecimal(100.00).setScale(2,
				RoundingMode.HALF_UP);
		/*COP
		 BigDecimal txValue = new BigDecimal(2000.00).setScale(2,
				RoundingMode.HALF_UP);*/
		parameters.put(PayU.PARAMETERS.VALUE, txValue.toString());


		parameters.put(PayU.PARAMETERS.CREDIT_CARD_NUMBER,
				creditCard.getCreditCardNumber());
		parameters.put(PayU.PARAMETERS.CREDIT_CARD_EXPIRATION_DATE,
				creditCard.getExpirationDate());
		parameters.put(PayU.PARAMETERS.PROCESS_WITHOUT_CVV2,
				Boolean.toString(true));
		parameters.put(PayU.PARAMETERS.PAYMENT_METHOD,creditCard.name());

		try {
			TransactionResponse response = PayUPayments
					.doAuthorizationAndCapture(parameters);

			LoggerUtil.info(RESPONSE_LOG_MESSAGE, response);

			Assert.assertNotNull(response, "Invalid response");
			Assert.assertNotNull(response.getResponseCode(),
					"Invalid response code");

		} catch (ConnectionException e) {

			// Service Unavailable
			LoggerUtil.error(e.getMessage(), e);

		} catch (SDKException e) {

			// SDK error
			LoggerUtil.error(e.getMessage(), e);
			//throw e;
			//Assert.fail(e.getMessage());
		}

	}

	/**
	 * Some possible credit cards to be used in the tests
	 *
	 */
	protected enum CreditCard {

		// Colombian Visa
		VISA("4005580000029205", "2015/01", "495", PaymentCountry.CO ),

		// Panama Visa
		VISA_PA("4111111111111111", "2015/01", "123", PaymentCountry.PA),

		// Amex
		AMEX("378263031085366", "2015/01", "123",PaymentCountry.CO ),

		// Discover
		DISCOVER("6011954843652373", "2015/01", "123",PaymentCountry.CO ),

		// Master card
		MASTERCARD("5303715971862863", "2015/01", "453", PaymentCountry.CO ),
		//MASTERCARD("5156187105437715", "2015/01", "123", PaymentCountry.CO ),

		// Diners
		DINERS("30039546070543", "2015/01", "123", PaymentCountry.BR ),

		// ELO
		ELO("4462784368283422", "2015/01", "123",PaymentCountry.BR ),

		//Naranja-Dineromail
		NARANJA("5895625600534000","2014/12","478",PaymentCountry.AR);

		/**
		 * Credit card number
		 */
		private String creditCardNumber;

		/**
		 * Credit card expiration date
		 */
		private String expirationDate;

		/**
		 * credit card security code
		 */
		private String securityCode;

		private PaymentCountry country;

		/**
		 * The credit card constructor
		 *
		 * @param creditCardNumber
		 *            The credit card's number
		 * @param expirationDate
		 *            The credit card's expiration date
		 * @param securityCode
		 *            The credit card's security code
		 */
		CreditCard(String creditCardNumber, String expirationDate,
				String securityCode, PaymentCountry country) {
			this.creditCardNumber = creditCardNumber;
			this.expirationDate = expirationDate;
			this.securityCode = securityCode;
			this.country = country;
		}

		/**
		 * @return the credit card number
		 */
		public String getCreditCardNumber() {
			return creditCardNumber;
		}

		/**
		 * @return the expirationDate
		 */
		public String getExpirationDate() {
			return expirationDate;
		}

		/**
		 * @return the securityCode
		 */
		public String getSecurityCode() {
			return securityCode;
		}

		public PaymentCountry getCountry() {
			return country;
		}

		public static CreditCard fromString(String desc, String country ) {
			if (desc != null) {
				for (CreditCard creditCard : CreditCard.values()) {
					if (desc.equalsIgnoreCase(creditCard.name())) {
						if (creditCard.equals(VISA) && country.equals(PaymentCountry.PA)){
							return VISA_PA;
						}else{
							return creditCard;
						}
					}
				}
			}
			return null;
		}

	}

}

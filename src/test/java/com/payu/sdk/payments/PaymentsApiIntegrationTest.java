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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.payu.sdk.PayU;
import com.payu.sdk.PayUPayments;
import com.payu.sdk.PayUPlans;
import com.payu.sdk.PayUTokens;
import com.payu.sdk.constants.Constants;
import com.payu.sdk.exceptions.ConnectionException;
import com.payu.sdk.exceptions.InvalidParametersException;
import com.payu.sdk.exceptions.PayUException;
import com.payu.sdk.exceptions.SDKException;
import com.payu.sdk.model.Bank;
import com.payu.sdk.model.CreditCard;
import com.payu.sdk.model.CreditCardToken;
import com.payu.sdk.model.Currency;
import com.payu.sdk.model.DocumentType;
import com.payu.sdk.model.Language;
import com.payu.sdk.model.PaymentCountry;
import com.payu.sdk.model.PaymentMethod;
import com.payu.sdk.model.PaymentMethodApi;
import com.payu.sdk.model.PaymentMethodComplete;
import com.payu.sdk.model.PaymentMethodType;
import com.payu.sdk.model.PersonType;
import com.payu.sdk.model.Transaction;
import com.payu.sdk.model.TransactionResponse;
import com.payu.sdk.model.TransactionResponseCode;
import com.payu.sdk.model.TransactionType;
import com.payu.sdk.paymentplan.model.SubscriptionPlan;
import com.payu.sdk.payments.model.CreditCardTokenResponse;
import com.payu.sdk.payments.model.PaymentAttemptRequest;
import com.payu.sdk.util.CreditCards;
import com.payu.sdk.util.TestEnvironment;
import com.payu.sdk.util.builders.ContentFileMother;
import com.payu.sdk.util.builders.MassiveTokenPaymentParametersMother;
import com.payu.sdk.utils.LoggerUtil;

/**
 *
 * @author Payulatam.com
 * @since 4.3.7
 * @date 21/08/2013
 * @version 1.0
 */
@SuppressWarnings("deprecation")
public class PaymentsApiIntegrationTest {

	/**
	 * Default response log message
	 */
	private static final String RESPONSE_LOG_MESSAGE = "{0}";

	/**
	 * Default payer id
	 */
	private static final String DEFAULT_PAYER_ID = "payerId_123";

	/**
	 * Invalid transaction id
	 */
	private static final String INVALID_TRANSACTION_ID = "AAAAAA-BBBBBB-CCCCCC-DDDDDD-EEEEEE";

	/**
	 * Invalid order id
	 */
	private static final String INVALID_ORDER_ID = "1";

	/**
	 * Valid installments number for Panama
	 */
	private static final String INSTALLMENT_NUMBRES_PA = "1";

	/**
	 * Valid colombia account
	 */
	private static final Integer COLOMBIA_ACCOUNT = 1;

	/**
	 * The created token id
	 */
	private String tokenId;

	/**
	 * The created order id
	 */
	private Long orderId;

	/**
	 * The created order id for Authorization
	 */
	private Long orderIdAuthorization;

	/**
	 * the created transactionId
	 */
	private String transactionId;

	/**
	 * the created transactionId for Authorization
	 */
	private String transactionIdAuthorization;

	/**
	 * the created referenceCode
	 */
	protected static String referenceCode;

	@BeforeClass
	private void init() {

		PayU.apiKey = "012345678901";
		PayU.apiLogin = "012345678901";
		//PayU.apiLogin = "x7AWjPyUagNK4q8";
		//PayU.apiKey = "DUA90B8A3hA4Us4NY8T6gJ1V7M";
		PayU.merchantId = "1";
		PayU.language = Language.en;
		PayU.isTest = false;

		TestEnvironment environment = TestEnvironment.SANDBOX;

		PayU.paymentsUrl = environment.getPaymentsApiUrl();

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

		List<PaymentMethodComplete> response;
		try {
			response = PayUPayments.getPaymentMethods();

			LoggerUtil.info(RESPONSE_LOG_MESSAGE, response);

			Assert.assertNotNull(response, "Null response");
			Assert.assertFalse(response.isEmpty(), "Empty response");

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
	 * Get payment method availability test
	 * @author <a href="fernando.moreno@payulatam.com">Fernando Moreno</a>
	 * @date 3/09/2014
	 */
	@Test(description="Test the payment method availability")
	public void getPaymentMethodAvailability() {

		Thread.currentThread().setName("getPaymentMethodAvailability");

		try {
			//VISA
			PaymentMethodApi response = PayUPayments.getPaymentMethodAvailability("VISA");

			LoggerUtil.info(RESPONSE_LOG_MESSAGE, response);

			Assert.assertNotNull(response, "Null response");
			Assert.assertEquals(response.getType(), PaymentMethodType.CREDIT_CARD);

			//CENCOSUD
			response = PayUPayments.getPaymentMethodAvailability("CENCOSUD");

			LoggerUtil.info(RESPONSE_LOG_MESSAGE, response);

			Assert.assertNotNull(response, "Null response");
			Assert.assertEquals(response.getType(), PaymentMethodType.CREDIT_CARD);

			//BOLETO_BANCARIO
			response = PayUPayments.getPaymentMethodAvailability("BOLETO_BANCARIO");
			LoggerUtil.info(RESPONSE_LOG_MESSAGE, response);
			Assert.assertNotNull(response, "Null response");
			Assert.assertEquals(response.getType(), PaymentMethodType.REFERENCED);


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
	 * Get subscription plan list test
	 */
	@Test
	public void getSubscriptionPlans() {

		Thread.currentThread().setName("SubscriptionPlans");

		try {
			Map<String, String> parameters = new HashMap<String, String>();
			List<SubscriptionPlan> response = PayUPlans.findList(parameters);
			LoggerUtil.info(RESPONSE_LOG_MESSAGE, response);
			Assert.assertNotNull(response, "Empty sunbscription plan response");
		}
		catch (ConnectionException e) {
			// Service Unavailable
			LoggerUtil.error(e.getMessage(), e);
		}
		catch (SDKException e) {
			// SDK error
			LoggerUtil.error(e.getMessage(), e);
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * Gets the PSE bank list
	 */
	@Test
	public void getPseBanks() {

		Thread.currentThread().setName("getPseBanks");

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(PayU.PARAMETERS.COUNTRY, PaymentCountry.CO.name());

		try {
			List<Bank> response = PayUPayments.getPSEBanks(parameters);

			Assert.assertNotNull(response, "Null response");
			Assert.assertFalse(response.isEmpty(), "Empty response");

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
	 * Do authorization
	 */
	@Test
	public void doAuthorization() {

		Thread.currentThread().setName("doAuthorization");

		Map<String, String> parameters = new HashMap<String, String>();

		String nameOnCard = "NAME " + Long.toString(System.currentTimeMillis());
		parameters.put(PayU.PARAMETERS.PAYER_NAME, nameOnCard);

		parameters.put(PayU.PARAMETERS.PAYER_CITY, "Bogotá");
		parameters.put(PayU.PARAMETERS.PAYER_STREET, "ABC 123");

		parameters.put(PayU.PARAMETERS.INSTALLMENTS_NUMBER, INSTALLMENT_NUMBRES_PA);

		PaymentCountry paymentCountry = PaymentCountry.PA;// PaymentCountry.PA;
		parameters.put(PayU.PARAMETERS.COUNTRY, paymentCountry.name());
		Integer accountId = 8;// 8;//3;//1;//null;//1;// PA - Merchant 1

		parameters.put(PayU.PARAMETERS.ACCOUNT_ID,
				accountId != null ? String.format("%d", accountId) : null);

		// Currency
		Currency txCurrency = Currency.USD;
		parameters.put(PayU.PARAMETERS.CURRENCY, txCurrency.toString());

		String orderReferenceCode = "A1B2C3" + System.currentTimeMillis();
		parameters.put(PayU.PARAMETERS.REFERENCE_CODE, orderReferenceCode);

		String description = "ALL IN 5";
		parameters.put(PayU.PARAMETERS.DESCRIPTION, description);

		// Cookie of the current user session
		parameters.put(PayU.PARAMETERS.COOKIE, "cookie_" + System.nanoTime());

		// Transaction value
		BigDecimal txValue = new BigDecimal(100.00).setScale(2,
				RoundingMode.HALF_UP);
		parameters.put(PayU.PARAMETERS.VALUE, txValue.toString());

		// Tax Value
		BigDecimal taxValue = new BigDecimal(16.00).setScale(2,
				RoundingMode.HALF_UP);
		parameters.put(PayU.PARAMETERS.TAX_VALUE, taxValue.toString());

		// Tax return base
		BigDecimal taxReturnBase = new BigDecimal(100.00).setScale(2,
				RoundingMode.HALF_UP);
		parameters.put(PayU.PARAMETERS.TAX_RETURN_BASE,
				taxReturnBase.toString());

		// Credit card values
		CreditCards creditCard = CreditCards.VISA;

		parameters.put(PayU.PARAMETERS.CREDIT_CARD_NUMBER,
				creditCard.getCreditCardNumber());
		parameters.put(PayU.PARAMETERS.CREDIT_CARD_EXPIRATION_DATE,
				creditCard.getExpirationDate());
		parameters.put(PayU.PARAMETERS.CREDIT_CARD_SECURITY_CODE,
				creditCard.getSecurityCode());
		parameters.put(PayU.PARAMETERS.PAYMENT_METHOD, creditCard.name());

		try {
			TransactionResponse response = PayUPayments
					.doAuthorization(parameters);

			LoggerUtil.info(RESPONSE_LOG_MESSAGE, response);

			Assert.assertNotNull(response, "Invalid response");
			Assert.assertNotNull(response.getResponseCode(),
					"Invalid response code");

			if (TransactionResponseCode.APPROVED.equals(response
					.getResponseCode())) {
				transactionIdAuthorization = response.getTransactionId();
				orderIdAuthorization = response.getOrderId();
			}

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
	 * Do massive token payments when the parameters are valid then the batchId returns
	 */
	@Test
	public void testDoMassiveTokenPayments_whenParametersAreValid_thenBatchIdReturns()
			throws InvalidParametersException, PayUException, ConnectionException {

		Thread.currentThread().setName("doMassiveTokenPaymentsWhenParamsAreValid");

		// Arrange
		final Map<String, String> parameters = MassiveTokenPaymentParametersMother
				.getValidMassiveTokenPaymentParameters();

		// Act
		final String batchId = PayUPayments.doMassiveTokenPayments(parameters);

		// Assert
		LoggerUtil.info(RESPONSE_LOG_MESSAGE, batchId);
		Assert.assertNotNull(batchId, "BatchId can't be null");
	}

	/**
	 * Do massive token payments when content file is null then throw invalid parameters exception
	 */
	@Test(expectedExceptions = InvalidParametersException.class)
	public void testDoMassiveTokenPayments_whenContentFileIsNull_thenThrowInvalidParametersException()
			throws InvalidParametersException, PayUException, ConnectionException {

		Thread.currentThread().setName("doMassiveTokenPaymentsWhenContentFileIsNull");

		// Arrange
		final Map<String, String> parameters = MassiveTokenPaymentParametersMother
				.getInvalidMassiveTokenPaymentParameters();

		// Act
		final String batchId = PayUPayments.doMassiveTokenPayments(parameters);

		// Assert
		LoggerUtil.info(RESPONSE_LOG_MESSAGE, batchId);
		Assert.assertNotNull(batchId, "BatchId can't be null");
	}

	/**
	 * Do massive token payments when the content file is invalid then throw invalid parameters exception
	 */
	@Test(expectedExceptions = InvalidParametersException.class)
	public void testDoMassiveTokenPayments_whenContentFileIsInvalid_thenThrowPayUException()
			throws InvalidParametersException, PayUException, ConnectionException {

		Thread.currentThread().setName("doMassiveTokenPaymentsWhenContentFileIsInvalid");

		// Arrange
		final Map<String, String> parameters = MassiveTokenPaymentParametersMother
				.buildMassiveTokenPaymentParameters(ContentFileMother.getInvalidContentFile());

		// Act
		final String batchId = PayUPayments.doMassiveTokenPayments(parameters);

		// Assert
		LoggerUtil.info(RESPONSE_LOG_MESSAGE, batchId);
		Assert.assertNotNull(batchId, "BatchId can't be null");
	}
	
	/**
	 * Do capture
	 */
	@Test(dependsOnMethods = "doAuthorization")
	public void doCapture() {

		Thread.currentThread().setName("doCapture");

		Map<String, String> parameters = new HashMap<String, String>();

		parameters = new HashMap<String, String>();

		parameters.put(PayU.PARAMETERS.TRANSACTION_ID, transactionIdAuthorization);

		parameters.put(PayU.PARAMETERS.ORDER_ID,
				orderIdAuthorization != null ? String.format("%d", orderIdAuthorization) : null);

		if (orderId == null) {
			String orderReferenceCode = "A1B2C3" + System.currentTimeMillis();
			parameters.put(PayU.PARAMETERS.REFERENCE_CODE, orderReferenceCode);

			String description = "ALL IN 5";
			parameters.put(PayU.PARAMETERS.DESCRIPTION, description);

			// Transaction value
			BigDecimal txValue = new BigDecimal(100.00).setScale(2,
					RoundingMode.HALF_UP);
			parameters.put(PayU.PARAMETERS.VALUE, txValue.toString());

			// Tax Value
			BigDecimal taxValue = new BigDecimal(16.00).setScale(2,
					RoundingMode.HALF_UP);
			parameters.put(PayU.PARAMETERS.TAX_VALUE, taxValue.toString());

			// Tax return base
			BigDecimal taxReturnBase = new BigDecimal(100.00).setScale(2,
					RoundingMode.HALF_UP);
			parameters.put(PayU.PARAMETERS.TAX_RETURN_BASE,
					taxReturnBase.toString());
		}

		try {

			TransactionResponse response = PayUPayments.doCapture(parameters);

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
			Assert.fail(e.getMessage());
		}

	}

	/**
	 * Do authorization and capture
	 */
	@Test//(invocationCount=150, threadPoolSize=15)
	public void doAuthorizationAndCapture() {

		Thread.currentThread().setName("doAuthorizationAndCapture");

		Map<String, String> parameters = new HashMap<String, String>();

		String nameOnCard = "NAME " + Long.toString(System.currentTimeMillis());
		parameters.put(PayU.PARAMETERS.PAYER_NAME, nameOnCard);

		parameters.put(PayU.PARAMETERS.INSTALLMENTS_NUMBER, INSTALLMENT_NUMBRES_PA);

		parameters.put(PayU.PARAMETERS.COUNTRY, PaymentCountry.PA.name());

		// Panama account
		Integer accountId = 8;

		parameters.put(PayU.PARAMETERS.ACCOUNT_ID, accountId.toString());

		// Currency
		Currency txCurrency = Currency.USD;
		parameters.put(PayU.PARAMETERS.CURRENCY, txCurrency.toString());

		String orderReferenceCode = "A1B2C3" + System.currentTimeMillis();
		parameters.put(PayU.PARAMETERS.REFERENCE_CODE, orderReferenceCode);

		String description = "ALL IN 5";
		parameters.put(PayU.PARAMETERS.DESCRIPTION, description);

		BigDecimal txValue = new BigDecimal(100.00).setScale(2,
				RoundingMode.HALF_UP);
		parameters.put(PayU.PARAMETERS.VALUE, txValue.toString());

		// Credit card values
		CreditCards creditCard = CreditCards.VISA;

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
				transactionId = response.getTransactionId();
				orderId = response.getOrderId();
			}

		} catch (ConnectionException e) {

			// Service Unavailable
			LoggerUtil.error(e.getMessage(), e);
			Assert.fail("The conection couldn't be stablished");

		} catch (SDKException e) {

			// SDK error
			LoggerUtil.error(e.getMessage(), e);
			Assert.fail(e.getMessage());
		}

	}
	
	
	/**
	 * Do submit auth and capture transaction.
	 * 
	 * @author <a href="mailto:juan.roman@payulatam.com">Juan Roman</a>
	 * @since 1.5.1
	 */
	@Test
	public void doSubmitAuthAndCaptureTransaction() {

		Thread.currentThread().setName("doSubmitAuthAndCaptureTransaction");

		Map<String, String> parameters = new HashMap<String, String>();

		String nameOnCard = "NAME " + Long.toString(System.currentTimeMillis());
		parameters.put(PayU.PARAMETERS.PAYER_NAME, nameOnCard);

		parameters.put(PayU.PARAMETERS.INSTALLMENTS_NUMBER, INSTALLMENT_NUMBRES_PA);

		parameters.put(PayU.PARAMETERS.COUNTRY, PaymentCountry.PA.name());

		// Panama account
		Integer accountId = 8;

		parameters.put(PayU.PARAMETERS.ACCOUNT_ID, accountId.toString());

		// Currency
		Currency txCurrency = Currency.USD;
		parameters.put(PayU.PARAMETERS.CURRENCY, txCurrency.toString());

		String orderReferenceCode = "A1B2C3" + System.currentTimeMillis();
		parameters.put(PayU.PARAMETERS.REFERENCE_CODE, orderReferenceCode);

		String description = "ALL IN 5";
		parameters.put(PayU.PARAMETERS.DESCRIPTION, description);

		BigDecimal txValue = new BigDecimal(100.00).setScale(2,
				RoundingMode.HALF_UP);
		parameters.put(PayU.PARAMETERS.VALUE, txValue.toString());

		// Credit card values
		CreditCards creditCard = CreditCards.VISA;

		parameters.put(PayU.PARAMETERS.CREDIT_CARD_NUMBER,
				creditCard.getCreditCardNumber());
		parameters.put(PayU.PARAMETERS.CREDIT_CARD_EXPIRATION_DATE,
				creditCard.getExpirationDate());
		parameters.put(PayU.PARAMETERS.CREDIT_CARD_SECURITY_CODE,
				creditCard.getSecurityCode());
		parameters.put(PayU.PARAMETERS.PAYMENT_METHOD, creditCard.name());
		
		try {
			Transaction transaction = PayUPayments.fromParametersMapToTransaction(parameters, TransactionType.AUTHORIZATION_AND_CAPTURE);
			TransactionResponse response = PayUPayments.submitTransaction(transaction, 60);

			LoggerUtil.info(RESPONSE_LOG_MESSAGE, response);

			Assert.assertNotNull(response, "Invalid response");
			Assert.assertNotNull(response.getResponseCode(),
					"Invalid response code");

			if (TransactionResponseCode.APPROVED.equals(response
					.getResponseCode())) {
				transactionId = response.getTransactionId();
				orderId = response.getOrderId();
			}

		} catch (ConnectionException e) {

			// Service Unavailable
			LoggerUtil.error(e.getMessage(), e);
			Assert.fail("The conection couldn't be stablished");

		} catch (SDKException e) {

			// SDK error
			LoggerUtil.error(e.getMessage(), e);
			Assert.fail(e.getMessage());
		}

	}
	
	/**
	 * Do submit auth and capture transaction.
	 * 
	 * @author <a href="mailto:juan.roman@payulatam.com">Juan Roman</a>
	 * @since 1.5.1
	 */
	@Test
	public void testSendConfirmationPage() {
		Thread.currentThread().setName("sendConfirmationPageTest");

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(PayU.PARAMETERS.API_KEY, PayU.apiKey);
		parameters.put(PayU.PARAMETERS.API_LOGIN, PayU.apiLogin);
		parameters.put(PayU.PARAMETERS.TRANSACTION_ID, "1");
		
		try {
			Boolean response = PayUPayments.sendConfirmationPage(parameters);
			LoggerUtil.info(RESPONSE_LOG_MESSAGE, response);
			Assert.assertNotNull(response, "Invalid response");
		} catch (ConnectionException e) {
			// Service Unavailable
			LoggerUtil.error(e.getMessage(), e);
			Assert.fail("The conection couldn't be stablished");

		} catch (SDKException e) {
			// SDK error
			LoggerUtil.error(e.getMessage(), e);
			Assert.fail(e.getMessage());
		}
	}

	/**
	 * Do authorization and capture with a specific confirmation page URL
	 * @author <a href="manuel.vieda@payulatam.com">Manuel E. Vieda</a>
	 * @date 3/09/2014
	 */
	@Test(dependsOnMethods = "doAuthorizationAndCapture")
	public void doAuthorizationAndCaptureWithNotifyUrl() {

		Thread.currentThread().setName("doAuthorizationAndCaptureWithNotifyUrl");

		Map<String, String> parameters = new HashMap<String, String>();

		String nameOnCard = "NAME " + Long.toString(System.currentTimeMillis());
		parameters.put(PayU.PARAMETERS.PAYER_NAME, nameOnCard);

		parameters.put(PayU.PARAMETERS.INSTALLMENTS_NUMBER, "1");

		parameters.put(PayU.PARAMETERS.COUNTRY, PaymentCountry.PA.name());

		parameters.put(PayU.PARAMETERS.NOTIFY_URL, "http://www.payulatam.com/");

		// Panama account
		Integer accountId = 8;

		parameters.put(PayU.PARAMETERS.ACCOUNT_ID, accountId.toString());

		// Currency
		Currency txCurrency = Currency.USD;
		parameters.put(PayU.PARAMETERS.CURRENCY, txCurrency.toString());

		String orderReferenceCode = "A1B2C3" + System.currentTimeMillis();
		parameters.put(PayU.PARAMETERS.REFERENCE_CODE, orderReferenceCode);

		String description = "ALL IN 5";
		parameters.put(PayU.PARAMETERS.DESCRIPTION, description);

		BigDecimal txValue = new BigDecimal(100.00).setScale(2,
				RoundingMode.HALF_UP);
		parameters.put(PayU.PARAMETERS.VALUE, txValue.toString());

		// Credit card values
		CreditCards creditCard = CreditCards.VISA;

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
				transactionId = response.getTransactionId();
				orderId = response.getOrderId();
			}

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
	 * Do void transaction
	 */
	@Test (dependsOnMethods = "doAuthorizationAndCapture")
	public void doVoid() {

		Thread.currentThread().setName("doVoid");

		Map<String, String> parameters = new HashMap<String, String>();

		parameters.put(PayU.PARAMETERS.TRANSACTION_ID, transactionId);

		parameters.put(PayU.PARAMETERS.ORDER_ID,
				orderId != null ? String.format("%d", orderId) : null);

		try {
			TransactionResponse response = PayUPayments.doVoid(parameters);

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
			Assert.fail(e.getMessage());
		}

	}

	/**
	 * Does a void transaction and sends the reason
	 */
	@Test (dependsOnMethods = "doAuthorizationAndCapture")
	public void doVoidWithReason() {

		Thread.currentThread().setName("doVoid");

		Map<String, String> parameters = new HashMap<String, String>();

		parameters.put(PayU.PARAMETERS.TRANSACTION_ID, transactionId);

		parameters.put(PayU.PARAMETERS.ORDER_ID,
				orderId != null ? String.format("%d", orderId) : null);

		parameters.put(PayU.PARAMETERS.REASON, "A testing reason");

		try {
			TransactionResponse response = PayUPayments.doVoid(parameters);

			LoggerUtil.info(RESPONSE_LOG_MESSAGE, response);

			Assert.assertNotNull(response, "Invalid response");
			Assert.assertNotNull(response.getResponseCode(),
					"Invalid response code");

		} catch (ConnectionException e) {

			LoggerUtil.error(e.getMessage(), e);
			Assert.fail("The conection couldn't be stablished");

		} catch (SDKException e) {

			// SDK error
			LoggerUtil.error(e.getMessage(), e);
			Assert.fail(e.getMessage());
		}

	}

	/**
	 * Do refund transaction
	 */
	@Test(dependsOnMethods = "doAuthorizationAndCapture")
	public void doRefund() {

		Thread.currentThread().setName("doRefund");

		Map<String, String> parameters = new HashMap<String, String>();

		// ############# Begin Refund
		parameters = new HashMap<String, String>();

		parameters.put(PayU.PARAMETERS.TRANSACTION_ID, transactionId);

		parameters.put(PayU.PARAMETERS.ORDER_ID,
				orderId != null ? String.format("%d", orderId) : null);

		try {

			TransactionResponse response = PayUPayments.doRefund(parameters);

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
			Assert.fail(e.getMessage());
		}

	}

	/**
	 * Does a refund transaction and sends the reason
	 */
	@Test(dependsOnMethods = "doAuthorizationAndCapture")
	public void doRefundWithReason() {

		Thread.currentThread().setName("doRefund");

		Map<String, String> parameters = new HashMap<String, String>();

		// ############# Begin Refund
		parameters = new HashMap<String, String>();

		parameters.put(PayU.PARAMETERS.TRANSACTION_ID, transactionId);

		parameters.put(PayU.PARAMETERS.ORDER_ID,
				orderId != null ? String.format("%d", orderId) : null);

		parameters.put(PayU.PARAMETERS.REASON, "A testing reason for refund");

		try {

			TransactionResponse response = PayUPayments.doRefund(parameters);

			LoggerUtil.info(RESPONSE_LOG_MESSAGE, response);

			Assert.assertNotNull(response, "Invalid response");
			Assert.assertNotNull(response.getResponseCode(),
					"Invalid response code");

		} catch (ConnectionException e) {

			// Service Unavailable
			LoggerUtil.error(e.getMessage(), e);
			Assert.fail("The conection couldn't be stablished");

		} catch (SDKException e) {

			// SDK error
			LoggerUtil.error(e.getMessage(), e);
			Assert.fail(e.getMessage());
		}

	}

	/**
	 * Creates a credit card token
	 */
	@Test
	public void createCreditCardToken() {

		Thread.currentThread().setName("createCreditCardToken");

		Map<String, String> parameters = new HashMap<String, String>();

		String nameOnCard = "NAME " + Long.toString(System.currentTimeMillis());
		parameters.put(PayU.PARAMETERS.PAYER_NAME, nameOnCard);

		CreditCards creditCard = CreditCards.VISA;

		parameters.put(PayU.PARAMETERS.CREDIT_CARD_NUMBER,
				creditCard.getCreditCardNumber());
		parameters.put(PayU.PARAMETERS.CREDIT_CARD_EXPIRATION_DATE,
				creditCard.getExpirationDate());
		parameters.put(PayU.PARAMETERS.PAYMENT_METHOD, creditCard.name());

		parameters.put(PayU.PARAMETERS.PAYER_ID, "payerId_123");

		try {
			// Creates the credit card token
			CreditCardToken response = PayUTokens.create(parameters);

			LoggerUtil.info(RESPONSE_LOG_MESSAGE, response);

			Assert.assertNotNull(response, "Null response");
			Assert.assertNotNull(response.getTokenId(), "Null token id");

			tokenId = response.getTokenId();

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
	 * Do authorization with token
	 */
	@Test(dependsOnMethods = { "createCreditCardToken" })
	public void doAuthorizationWithToken() {

		Thread.currentThread().setName("doAuthorizationWithToken");

		Map<String, String> parameters = new HashMap<String, String>();

		parameters.put(PayU.PARAMETERS.INSTALLMENTS_NUMBER, INSTALLMENT_NUMBRES_PA);

		Integer accountId = 8;
		parameters.put(PayU.PARAMETERS.ACCOUNT_ID, accountId.toString());

		String orderReferenceCode = "A1B2C3" + System.currentTimeMillis();
		parameters.put(PayU.PARAMETERS.REFERENCE_CODE, orderReferenceCode);

		String description = "ALL IN 5";
		parameters.put(PayU.PARAMETERS.DESCRIPTION, description);

		// Values
		Currency txCurrency = Currency.USD;
		parameters.put(PayU.PARAMETERS.CURRENCY, txCurrency.toString());

		BigDecimal txValue = new BigDecimal(100.0).setScale(2,
				RoundingMode.HALF_UP);
		parameters.put(PayU.PARAMETERS.VALUE, txValue.toString());

		// Token id
		parameters.put(PayU.PARAMETERS.TOKEN_ID, tokenId);

		// Credit card security code
		parameters.put(PayU.PARAMETERS.CREDIT_CARD_SECURITY_CODE, "1234");

		try {

			// Do authorization
			TransactionResponse paymentResponse = PayUPayments
					.doAuthorization(parameters);

			LoggerUtil.info(RESPONSE_LOG_MESSAGE, paymentResponse);

			Assert.assertNotNull(paymentResponse, "Invalid response");
			Assert.assertNotNull(paymentResponse.getResponseCode(),
					"Invalid response code");

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
	 * Find the created token(s)
	 */
	@Test(dependsOnMethods = "createCreditCardToken")
	public void getCreditCardTokens() {

		Thread.currentThread().setName("getCreditCardTokens");

		Map<String, String> parameters = new HashMap<String, String>();

		parameters.put(PayU.PARAMETERS.PAYER_ID, DEFAULT_PAYER_ID);
		parameters.put(PayU.PARAMETERS.TOKEN_ID, tokenId);

		try {
			List<CreditCardToken> response = PayUTokens.find(parameters);

			LoggerUtil.info(RESPONSE_LOG_MESSAGE, response);

			Assert.assertNotNull(response, "Null response");
			Assert.assertFalse(response.isEmpty(), "Empty response");

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
	 * Find the created token(s)
	 */
	@Test(dependsOnMethods = "createCreditCardToken")
	public void getCreditCardTokensDates() {

		Thread.currentThread().setName("getCreditCardTokensDates");

		Map<String, String> parameters = new HashMap<String, String>();

		parameters.put(PayU.PARAMETERS.PAYER_ID, DEFAULT_PAYER_ID);

		parameters.put(PayU.PARAMETERS.START_DATE, "2013-01-01T00:00:00");

		SimpleDateFormat sdf = new SimpleDateFormat(
				Constants.DEFAULT_DATE_FORMAT);
		String crDate = sdf.format(new Date());
		parameters.put(PayU.PARAMETERS.END_DATE, crDate);

		try {
			List<CreditCardToken> response = PayUTokens.find(parameters);

			LoggerUtil.info(RESPONSE_LOG_MESSAGE, response);

			Assert.assertNotNull(response, "Null response");
			Assert.assertFalse(response.isEmpty(), "Empty response");

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
	 * Removes the created credit card token
	 */
	@Test(dependsOnMethods = { "doAuthorizationWithToken",
			"createCreditCardToken", "getCreditCardTokens" })
	public void removeCreditCardToken() {

		Thread.currentThread().setName("doRemoveCreditCardToken");

		Map<String, String> parameters = new HashMap<String, String>();

		parameters.put(PayU.PARAMETERS.PAYER_ID, DEFAULT_PAYER_ID);
		parameters.put(PayU.PARAMETERS.TOKEN_ID, tokenId);

		try {
			CreditCardToken response = PayUTokens.remove(parameters);

			LoggerUtil.info(RESPONSE_LOG_MESSAGE, response);

			Assert.assertNotNull(response, "Null token");

			Assert.assertEquals(response.getTokenId(), tokenId);

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
	 * Do authorization and capture with token
	 */
	@Test(dependsOnMethods = { "createCreditCardToken" })
	public void doAuthorizationAndCaptureWithToken() {

		Thread.currentThread().setName("doAuthorizationAndCaptureWithToken");

		Map<String, String> parameters = new HashMap<String, String>();

		parameters.put(PayU.PARAMETERS.INSTALLMENTS_NUMBER, INSTALLMENT_NUMBRES_PA);

		Integer accountId = 8;
		parameters.put(PayU.PARAMETERS.ACCOUNT_ID, accountId.toString());

		String orderReferenceCode = "A1B2C3" + System.currentTimeMillis();
		parameters.put(PayU.PARAMETERS.REFERENCE_CODE, orderReferenceCode);

		String description = "ALL IN 5";
		parameters.put(PayU.PARAMETERS.DESCRIPTION, description);

		// Values
		Currency txCurrency = Currency.USD;
		parameters.put(PayU.PARAMETERS.CURRENCY, txCurrency.toString());

		BigDecimal txValue = new BigDecimal(100).setScale(2,
				RoundingMode.HALF_UP);
		parameters.put(PayU.PARAMETERS.VALUE, txValue.toString());

		// Token id
		parameters.put(PayU.PARAMETERS.TOKEN_ID, tokenId);

		//parameters.put(PayU.PARAMETERS.PROCESS_WITHOUT_CVV2, "true");
		// Security code
		parameters.put(PayU.PARAMETERS.CREDIT_CARD_SECURITY_CODE, "1234");

		try {
			TransactionResponse paymentResponse = PayUPayments
					.doAuthorizationAndCapture(parameters);

			LoggerUtil.info(RESPONSE_LOG_MESSAGE, paymentResponse);

			Assert.assertNotNull(paymentResponse, "Invalid response");
			Assert.assertNotNull(paymentResponse.getResponseCode(),
					"Invalid response code");

			if (TransactionResponseCode.APPROVED.equals(paymentResponse
					.getResponseCode())) {
				transactionId = paymentResponse.getTransactionId();
				orderId = paymentResponse.getOrderId();
			}

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
	 * Do authorization and capture with Cash
	 */
	@Test
	public void doAuthorizationAndCaptureWithOXXO() {

		Thread.currentThread().setName("doAuthorizationAndCaptureWithOXXO");

		Map<String, String> parameters = new HashMap<String, String>();

		String orderReferenceCode = "A1B2C3" + System.currentTimeMillis();
		parameters.put(PayU.PARAMETERS.REFERENCE_CODE, orderReferenceCode);

		String description = "ALL IN 5";
		parameters.put(PayU.PARAMETERS.DESCRIPTION, description);

		parameters.put(PayU.PARAMETERS.PAYER_NAME, "PayerName");

		String payerDni = "52494";
		parameters.put(PayU.PARAMETERS.PAYER_DNI, payerDni);

		// Values
		Currency txCurrency = Currency.USD;
		parameters.put(PayU.PARAMETERS.CURRENCY, txCurrency.toString());

		BigDecimal txValue = new BigDecimal(100.0).setScale(2,
				RoundingMode.HALF_UP);
		parameters.put(PayU.PARAMETERS.VALUE, txValue.toString());

		// Payment Method OXXO
		// Mexican account
		Integer accountId = 11;
		parameters.put(PayU.PARAMETERS.ACCOUNT_ID, accountId.toString());
		parameters.put(PayU.PARAMETERS.PAYMENT_METHOD,
				PaymentMethod.OXXO.name());

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, 1);

		SimpleDateFormat sdf = new SimpleDateFormat(
				Constants.DEFAULT_DATE_FORMAT);

		parameters.put(PayU.PARAMETERS.EXPIRATION_DATE,
				sdf.format(cal.getTime()));

		try {
			TransactionResponse paymentResponse = PayUPayments
					.doAuthorizationAndCapture(parameters);

			LoggerUtil.info(RESPONSE_LOG_MESSAGE, paymentResponse);

			Assert.assertNotNull(paymentResponse, "Invalid response");
			Assert.assertNotNull(paymentResponse.getResponseCode(),
					"Invalid response code");

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
	 * Do authorization and capture with Baloto
	 */
	@Test
	public void doAuthorizationAndCaptureWithBaloto() {

		Thread.currentThread().setName("doAuthorizationAndCaptureWithBaloto");

		Map<String, String> parameters = new HashMap<String, String>();

		String orderReferenceCode = "A1B2C3" + System.currentTimeMillis();
		parameters.put(PayU.PARAMETERS.REFERENCE_CODE, orderReferenceCode);

		String description = "ALL IN 5";
		parameters.put(PayU.PARAMETERS.DESCRIPTION, description);

		parameters.put(PayU.PARAMETERS.PAYER_NAME, "PayerName");

		String payerDni = "52494";
		parameters.put(PayU.PARAMETERS.PAYER_DNI, payerDni);

		// Values
		Currency txCurrency = Currency.USD;
		parameters.put(PayU.PARAMETERS.CURRENCY, txCurrency.toString());

		BigDecimal txValue = new BigDecimal(10.0).setScale(2,
				RoundingMode.HALF_UP);
		parameters.put(PayU.PARAMETERS.VALUE, txValue.toString());

		// Payment Method BALOTO
		// Colombian account
		parameters.put(PayU.PARAMETERS.ACCOUNT_ID, COLOMBIA_ACCOUNT.toString());
		parameters.put(PayU.PARAMETERS.PAYMENT_METHOD,
				PaymentMethod.BALOTO.name());

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, 1);

		SimpleDateFormat sdf = new SimpleDateFormat(
				Constants.DEFAULT_DATE_FORMAT);

		parameters.put(PayU.PARAMETERS.EXPIRATION_DATE,
				sdf.format(cal.getTime()));

		try {

			TransactionResponse paymentResponse = PayUPayments
					.doAuthorizationAndCapture(parameters);

			LoggerUtil.info(RESPONSE_LOG_MESSAGE, paymentResponse);

			Assert.assertNotNull(paymentResponse, "Invalid response");
			Assert.assertNotNull(paymentResponse.getResponseCode(),
					"Invalid response code");

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
	 * Do authorization and capture with Boleto
	 */
	@Test
	public void doAuthorizationAndCaptureWithBoleto() {

		Thread.currentThread().setName("doAuthorizationAndCaptureWithBoleto");

		Map<String, String> parameters = new HashMap<String, String>();

		String orderReferenceCode = "A1B2C3" + System.currentTimeMillis();
		parameters.put(PayU.PARAMETERS.REFERENCE_CODE, orderReferenceCode);

		String description = "ALL IN 5";
		parameters.put(PayU.PARAMETERS.DESCRIPTION, description);

		parameters.put(PayU.PARAMETERS.PAYER_NAME, "PayerName");

		String payerDni = "52494";
		parameters.put(PayU.PARAMETERS.PAYER_DNI, payerDni);

		// Values
		Currency txCurrency = Currency.USD;
		parameters.put(PayU.PARAMETERS.CURRENCY, txCurrency.toString());

		BigDecimal txValue = new BigDecimal(100.0).setScale(2,
				RoundingMode.HALF_UP);
		parameters.put(PayU.PARAMETERS.VALUE, txValue.toString());


		// ############# Buyer Basic Parameters
		parameters.put(PayU.PARAMETERS.BUYER_NAME,
				"BUYER NAME " + Long.toString(System.currentTimeMillis()));
		parameters.put(PayU.PARAMETERS.BUYER_ID,
				"BUYer201" + System.currentTimeMillis());
		parameters.put(PayU.PARAMETERS.BUYER_EMAIL, "buyer@payu.com");
		parameters
				.put(PayU.PARAMETERS.BUYER_CONTACT_PHONE, "(+BUY-er) 7563126");
		parameters.put(PayU.PARAMETERS.BUYER_CNPJ, "47.247.606");
		parameters.put(PayU.PARAMETERS.BUYER_DNI, "47.247.606/0001-90");

		// ############# Buyer's Address Parameters
		parameters.put(PayU.PARAMETERS.BUYER_STREET, "Calle Buyer");
		parameters.put(PayU.PARAMETERS.BUYER_STREET_2, "Carrera Buyer");
		parameters.put(PayU.PARAMETERS.BUYER_STREET_3, "Piso Buyer");
		parameters.put(PayU.PARAMETERS.BUYER_CITY, "BogoBuyer");
		parameters.put(PayU.PARAMETERS.BUYER_STATE, "CunBuyer");
		parameters.put(PayU.PARAMETERS.BUYER_COUNTRY, "BU");
		parameters.put(PayU.PARAMETERS.BUYER_PHONE, "(+BUY-dir) 7563126");
		parameters.put(PayU.PARAMETERS.BUYER_POSTAL_CODE, "Buyer 51973");


		// Payment Method BOLETO
		// Brazilian account
		Integer accountId = 3;
		parameters.put(PayU.PARAMETERS.ACCOUNT_ID, accountId.toString());
		parameters.put(PayU.PARAMETERS.PAYMENT_METHOD,
				PaymentMethod.BOLETO_BANCARIO.name());
		parameters.put(PayU.PARAMETERS.PAYER_DNI, "83661983000134");
		parameters.put(PayU.PARAMETERS.PAYER_STREET, "ABC 123");
		parameters.put(PayU.PARAMETERS.PAYER_STREET_2, "ABC 123");
		parameters.put(PayU.PARAMETERS.PAYER_CITY, "Bogotá");
		parameters.put(PayU.PARAMETERS.PAYER_STATE, "Bogotá");
		parameters.put(PayU.PARAMETERS.PAYER_POSTAL_CODE, "0190030");

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, 1);

		SimpleDateFormat sdf = new SimpleDateFormat(
				Constants.DEFAULT_DATE_FORMAT);

		parameters.put(PayU.PARAMETERS.EXPIRATION_DATE,
				sdf.format(cal.getTime()));

		try {

			TransactionResponse paymentResponse = PayUPayments
					.doAuthorizationAndCapture(parameters);

			LoggerUtil.info(RESPONSE_LOG_MESSAGE, paymentResponse);

			Assert.assertNotNull(paymentResponse, "Invalid response");
			Assert.assertNotNull(paymentResponse.getResponseCode(),
					"Invalid response code");

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
	 * Empty parameters exception
	 *
	 * @throws PayUException
	 */
	@Test(expectedExceptions = InvalidParametersException.class)
	public void testEmtpyParametersException() throws SDKException {

		Thread.currentThread().setName("testEmtpyParametersException");
		HashMap<String, String> parameters = new HashMap<String, String>();
		PayUPayments.doAuthorization(parameters);
		Assert.fail("No exception was thrown");
	}

	/**
	 * Empty parameters exception
	 *
	 * @throws PayUException
	 */
	@Test(expectedExceptions = InvalidParametersException.class)
	public void testInvalidParametersException() throws SDKException {

		Thread.currentThread().setName("testInvalidParametersException");
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put(PayU.PARAMETERS.CREDIT_CARD_NUMBER, "ABC");

		PayUPayments.doAuthorization(parameters);
		Assert.fail("No exception was thrown");
	}

	/**
	 * Test payments error response
	 */
	@Test
	public void testErrorPaymentsResponse() {

		Thread.currentThread().setName("testErrorPaymentsResponse");

		Map<String, String> parameters = new HashMap<String, String>();

		parameters = new HashMap<String, String>();

		parameters.put(PayU.PARAMETERS.TRANSACTION_ID, INVALID_TRANSACTION_ID);

		parameters.put(PayU.PARAMETERS.ORDER_ID, INVALID_ORDER_ID);

		try {
			TransactionResponse response = PayUPayments.doRefund(parameters);

			LoggerUtil.info(RESPONSE_LOG_MESSAGE, response);

			Assert.fail("No error response");

		} catch (PayUException e) {

			// Error response
			LoggerUtil.error(e.getMessage(), e);

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
	 * Empty parameters exception
	 *
	 * @throws PayUException
	 */
	@Test(expectedExceptions = ConnectionException.class)
	public void testConnectionException() throws SDKException {

		String defaultURL = PayU.paymentsUrl;
		try {

			PayU.paymentsUrl = "http://invalidUrl/test";
			Thread.currentThread().setName("testConnectionException");
			PayUPayments.doPing();
			Assert.fail("No exception was thrown");

		} finally {

			PayU.paymentsUrl = defaultURL;
		}

	}

	/**
	 * Connection exception
	 *
	 * @throws PayUException
	 */
	@Test(expectedExceptions = PayUException.class)
	public void testConnectionException2() throws SDKException {

		String defaultURL = PayU.paymentsUrl;
		try {

			PayU.paymentsUrl = defaultURL + "/invalid";
			Thread.currentThread().setName("testConnectionException");
			PayUPayments.doPing();
			Assert.fail("No exception was thrown");

		} finally {

			PayU.paymentsUrl = defaultURL;
		}

	}

	/**
	 * PayUException exception
	 *
	 * @throws PayUException
	 */
	@Test(expectedExceptions = PayUException.class)
	public void testPayUException() throws SDKException {

		Thread.currentThread().setName("testPayUException");

		String xml = "</abc>";
		CreditCardTokenResponse.fromXml(xml);

		Assert.fail("No exception was thrown");

	}

	/**
	 * Do authorization without security code
	 */
	@Test
	public void doAuthorizationWithoutSecurityCode() {

		Thread.currentThread().setName("doAuthorizationWithoutSecurityCode");

		Map<String, String> parameters = new HashMap<String, String>();

		String nameOnCard = "NAME " + Long.toString(System.currentTimeMillis());
		parameters.put(PayU.PARAMETERS.PAYER_NAME, nameOnCard);

		parameters.put(PayU.PARAMETERS.PAYER_CITY, "Bogotá");
		parameters.put(PayU.PARAMETERS.PAYER_STREET, "ABC 123");

		String installmentsNumber = "1";
		parameters.put(PayU.PARAMETERS.INSTALLMENTS_NUMBER, installmentsNumber);

		parameters.put(PayU.PARAMETERS.COUNTRY, PaymentCountry.CO.name());

		// Colombia account
		parameters.put(PayU.PARAMETERS.ACCOUNT_ID, COLOMBIA_ACCOUNT.toString());

		// Currency
		Currency txCurrency = Currency.COP;
		parameters.put(PayU.PARAMETERS.CURRENCY, txCurrency.toString());
		String orderReferenceCode = "A1B2C3" + System.currentTimeMillis();
		parameters.put(PayU.PARAMETERS.REFERENCE_CODE, orderReferenceCode);

		String description = "ALL IN 5";
		parameters.put(PayU.PARAMETERS.DESCRIPTION, description);

		//Device ID
		parameters.put(PayU.PARAMETERS.DEVICE_SESSION_ID, "1254000");

		// Transaction value
		BigDecimal txValue = new BigDecimal(2000.00).setScale(2,
				RoundingMode.HALF_UP);
		parameters.put(PayU.PARAMETERS.VALUE, txValue.toString());

		// Credit card values
		CreditCards creditCard = CreditCards.VISA_PA;

		parameters.put(PayU.PARAMETERS.CREDIT_CARD_NUMBER,
				creditCard.getCreditCardNumber());
		parameters.put(PayU.PARAMETERS.CREDIT_CARD_EXPIRATION_DATE,
				creditCard.getExpirationDate());
		parameters.put(PayU.PARAMETERS.PROCESS_WITHOUT_CVV2,
				Boolean.toString(true));
		parameters.put(PayU.PARAMETERS.PAYMENT_METHOD, CreditCards.VISA.name());

		try {
			TransactionResponse response = PayUPayments
					.doAuthorization(parameters);

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
			Assert.fail(e.getMessage());
		}

	}

	/**
	 * Do authorization and capture without security code
	 */
	@Test
	public void doAuthorizationAndCaptureWithoutSecurityCode() {

		Thread.currentThread().setName(
				"doAuthorizationAndCaptureWithoutSecurityCode");

		Map<String, String> parameters = new HashMap<String, String>();

		String nameOnCard = "NAME " + Long.toString(System.currentTimeMillis());
		parameters.put(PayU.PARAMETERS.PAYER_NAME, nameOnCard);

		parameters.put(PayU.PARAMETERS.INSTALLMENTS_NUMBER, "3");

		parameters.put(PayU.PARAMETERS.COUNTRY, PaymentCountry.CO.name());

		// Colombia account
		parameters.put(PayU.PARAMETERS.ACCOUNT_ID, COLOMBIA_ACCOUNT.toString());

		// Currency
		Currency txCurrency = Currency.COP;
		parameters.put(PayU.PARAMETERS.CURRENCY, txCurrency.toString());
		String orderReferenceCode = "A1B2C3" + System.currentTimeMillis();
		parameters.put(PayU.PARAMETERS.REFERENCE_CODE, orderReferenceCode);

		String description = "ALL IN 5";
		parameters.put(PayU.PARAMETERS.DESCRIPTION, description);

		// Transaction value
		BigDecimal txValue = new BigDecimal(2000.00).setScale(2,
				RoundingMode.HALF_UP);
		parameters.put(PayU.PARAMETERS.VALUE, txValue.toString());

		// Credit card values
		CreditCards creditCard = CreditCards.VISA_PA;

		parameters.put(PayU.PARAMETERS.CREDIT_CARD_NUMBER,
				creditCard.getCreditCardNumber());
		parameters.put(PayU.PARAMETERS.CREDIT_CARD_EXPIRATION_DATE,
				creditCard.getExpirationDate());
		parameters.put(PayU.PARAMETERS.PROCESS_WITHOUT_CVV2,
				Boolean.toString(true));
		parameters.put(PayU.PARAMETERS.PAYMENT_METHOD, CreditCards.VISA.name());

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
			Assert.fail(e.getMessage());
		}

	}

	/**
	 * Do authorization and capture without security code
	 */
	@Test
	public void doAuthorizationAndCaptureWithPSE() {

		Thread.currentThread().setName("doAuthorizationAndCaptureWithPSE");

		Map<String, String> parameters = new HashMap<String, String>();

		String nameOnCard = "NAME " + Long.toString(System.currentTimeMillis());
		parameters.put(PayU.PARAMETERS.PAYER_NAME, nameOnCard);
		parameters.put(PayU.PARAMETERS.PAYER_EMAIL, "test@pse.com");
		parameters.put(PayU.PARAMETERS.PAYER_DOCUMENT_TYPE, DocumentType.CC.name());
		parameters.put(PayU.PARAMETERS.PAYER_DNI, "654654321");
		parameters.put(PayU.PARAMETERS.PAYER_CONTACT_PHONE, "211424540");

		//MAF
		parameters.put(PayU.PARAMETERS.COOKIE, "ABCde201" + System.currentTimeMillis());
		parameters.put(PayU.PARAMETERS.IP_ADDRESS, "10.20.30.40");
		parameters.put(PayU.PARAMETERS.USER_AGENT, "Mozilla-" + System.currentTimeMillis());
		parameters.put(PayU.PARAMETERS.DEVICE_SESSION_ID, "DSI-" + System.currentTimeMillis());

		parameters.put(PayU.PARAMETERS.COUNTRY, PaymentCountry.CO.name());

		// Colombia account
		parameters.put(PayU.PARAMETERS.ACCOUNT_ID, COLOMBIA_ACCOUNT.toString());

		// Currency
		Currency txCurrency = Currency.COP;
		parameters.put(PayU.PARAMETERS.CURRENCY, txCurrency.toString());
		String orderReferenceCode = "A1B2C3" + System.currentTimeMillis();
		parameters.put(PayU.PARAMETERS.REFERENCE_CODE, orderReferenceCode);

		String description = "ALL IN 5";
		parameters.put(PayU.PARAMETERS.DESCRIPTION, description);

		// Transaction value
		BigDecimal txValue = new BigDecimal(2000.00).setScale(2,
				RoundingMode.HALF_UP);
		parameters.put(PayU.PARAMETERS.VALUE, txValue.toString());

		parameters.put(PayU.PARAMETERS.PAYMENT_METHOD, PaymentMethod.PSE.name());

		//
		parameters.put(PayU.PARAMETERS.PSE_FINANCIAL_INSTITUTION_CODE, "1022");
		parameters.put(PayU.PARAMETERS.PSE_FINANCIAL_INSTITUTION_NAME, "Banco Union Colombiano");
		parameters.put(PayU.PARAMETERS.PAYER_PERSON_TYPE, PersonType.NATURAL.name());

		parameters.put(PayU.PARAMETERS.NOTIFY_URL, "https://www.confirmacion.com");
		parameters.put(PayU.PARAMETERS.RESPONSE_URL, "https://www.respuesta.com");


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
			Assert.fail(e.getMessage());
		}

	}

	/**
	 * Do authorization and capture with Baloto and full Buyer and Payer
	 * Information
	 */
	@Test
	public void doAuthorizationAndCaptureBuyerPayer() {

		Thread.currentThread().setName("doAuthorizationAndCaptureBuyerPayer");

		Map<String, String> parameters = new HashMap<String, String>();

		String orderReferenceCode = "A1B2C3" + System.currentTimeMillis();
		parameters.put(PayU.PARAMETERS.REFERENCE_CODE, orderReferenceCode);

		String description = "ALL IN 5";
		parameters.put(PayU.PARAMETERS.DESCRIPTION, description);

		// ############# Payer Basic Parameters
		parameters.put(PayU.PARAMETERS.PAYER_NAME,
				"PAYER NAME " + Long.toString(System.currentTimeMillis()));
		parameters.put(PayU.PARAMETERS.PAYER_ID,
				"PAYer201" + System.currentTimeMillis());
		parameters.put(PayU.PARAMETERS.PAYER_EMAIL, "payer@payu.com");
		parameters
				.put(PayU.PARAMETERS.PAYER_CONTACT_PHONE, "(+PAY-er) 7563126");
		parameters.put(PayU.PARAMETERS.PAYER_CNPJ, "PAYER CNPJ");
		parameters.put(PayU.PARAMETERS.PAYER_DNI, "PAYer 123456");
		parameters.put(PayU.PARAMETERS.PAYER_BUSINESS_NAME,
				"Payer Business Name");
		parameters.put(PayU.PARAMETERS.PAYER_PERSON_TYPE,
				PersonType.NATURAL.name());

		// ############# Payer's Address Parameters
		parameters.put(PayU.PARAMETERS.PAYER_STREET, "Calle Payer");
		parameters.put(PayU.PARAMETERS.PAYER_STREET_2, "Carrera Payer");
		parameters.put(PayU.PARAMETERS.PAYER_STREET_3, "Piso Payer");
		parameters.put(PayU.PARAMETERS.PAYER_CITY, "BogoPayer");
		parameters.put(PayU.PARAMETERS.PAYER_STATE, "CunPayer");
		parameters.put(PayU.PARAMETERS.PAYER_COUNTRY, "PA");
		parameters.put(PayU.PARAMETERS.PAYER_PHONE, "(+PAY-dir) 7563126");
		parameters.put(PayU.PARAMETERS.PAYER_POSTAL_CODE, "Payer 51973");

		// ############# Buyer Basic Parameters
		parameters.put(PayU.PARAMETERS.BUYER_NAME,
				"BUYER NAME " + Long.toString(System.currentTimeMillis()));
		parameters.put(PayU.PARAMETERS.BUYER_ID,
				"BUYer201" + System.currentTimeMillis());
		parameters.put(PayU.PARAMETERS.BUYER_EMAIL, "buyer@payu.com");
		parameters
				.put(PayU.PARAMETERS.BUYER_CONTACT_PHONE, "(+BUY-er) 7563126");
		parameters.put(PayU.PARAMETERS.BUYER_CNPJ, "BUYER CNPJ");
		parameters.put(PayU.PARAMETERS.BUYER_DNI, "BUYer 123456");

		// ############# Buyer's Address Parameters
		parameters.put(PayU.PARAMETERS.BUYER_STREET, "Calle Buyer");
		parameters.put(PayU.PARAMETERS.BUYER_STREET_2, "Carrera Buyer");
		parameters.put(PayU.PARAMETERS.BUYER_STREET_3, "Piso Buyer");
		parameters.put(PayU.PARAMETERS.BUYER_CITY, "BogoBuyer");
		parameters.put(PayU.PARAMETERS.BUYER_STATE, "CunBuyer");
		parameters.put(PayU.PARAMETERS.BUYER_COUNTRY, "BU");
		parameters.put(PayU.PARAMETERS.BUYER_PHONE, "(+BUY-dir) 7563126");
		parameters.put(PayU.PARAMETERS.BUYER_POSTAL_CODE, "Buyer 51973");

		// Values
		Currency txCurrency = Currency.USD;
		parameters.put(PayU.PARAMETERS.CURRENCY, txCurrency.toString());

		BigDecimal txValue = new BigDecimal(10.0).setScale(2,
				RoundingMode.HALF_UP);
		parameters.put(PayU.PARAMETERS.VALUE, txValue.toString());

		// Payment Method BALOTO
		// Colombian account
		parameters.put(PayU.PARAMETERS.ACCOUNT_ID, COLOMBIA_ACCOUNT.toString());
		parameters.put(PayU.PARAMETERS.PAYMENT_METHOD,
				PaymentMethod.BALOTO.name());

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, 1);

		SimpleDateFormat sdf = new SimpleDateFormat(
				Constants.DEFAULT_DATE_FORMAT);

		parameters.put(PayU.PARAMETERS.EXPIRATION_DATE,
				sdf.format(cal.getTime()));

		try {

			TransactionResponse paymentResponse = PayUPayments
					.doAuthorizationAndCapture(parameters);

			LoggerUtil.info(RESPONSE_LOG_MESSAGE, paymentResponse);

			Assert.assertNotNull(paymentResponse, "Invalid response");
			Assert.assertNotNull(paymentResponse.getResponseCode(),
					"Invalid response code");

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
	 * Do authorization and capture with Payment Method that isn't in the Enum
	 * @author <a href="fernando.moreno@payulatam.com">Fernando Moreno</a>
	 * @date 2/09/2014
	 */
	@Test(description="Test authorization and capture with Payment Method that isn't in the Enum")
	public void doAuthorizationAndCaptureWithPaymentMethodIsNotEnum() {

		Thread.currentThread().setName("doAuthorizationAndCaptureWithPaymentMethodIsNotEnum");

		Map<String, String> parameters = new HashMap<String, String>();

		String nameOnCard = "NAME " + Long.toString(System.currentTimeMillis());
		parameters.put(PayU.PARAMETERS.PAYER_NAME, nameOnCard);
		parameters.put(PayU.PARAMETERS.INSTALLMENTS_NUMBER, "3");
		parameters.put(PayU.PARAMETERS.COUNTRY, PaymentCountry.AR.name());

		// Argentina account
		Integer accountId = 9;
		parameters.put(PayU.PARAMETERS.ACCOUNT_ID, accountId.toString());

		// Currency
		Currency txCurrency = Currency.ARS;
		parameters.put(PayU.PARAMETERS.CURRENCY, txCurrency.toString());
		String orderReferenceCode = "A1B2C3" + System.currentTimeMillis();
		parameters.put(PayU.PARAMETERS.REFERENCE_CODE, orderReferenceCode);

		String description = "Test Payment Method in't in the Enum";
		parameters.put(PayU.PARAMETERS.DESCRIPTION, description);

		// Transaction value
		BigDecimal txValue = new BigDecimal(2000.00).setScale(2, RoundingMode.HALF_UP);
		parameters.put(PayU.PARAMETERS.VALUE, txValue.toString());

		//Credit card 'CENCOSUD' isn't in the PaymentMethod enum
		CreditCards creditCard = CreditCards.CENCOSUD;

		parameters.put(PayU.PARAMETERS.CREDIT_CARD_NUMBER, creditCard.getCreditCardNumber());
		parameters.put(PayU.PARAMETERS.CREDIT_CARD_EXPIRATION_DATE, creditCard.getExpirationDate());
		parameters.put(PayU.PARAMETERS.CREDIT_CARD_SECURITY_CODE, creditCard.getSecurityCode());
		//The 'CENCOSUD' isn't in the PaymentMethod enum in the SDK
		parameters.put(PayU.PARAMETERS.PAYMENT_METHOD, "CENCOSUD");

		try {
			TransactionResponse response = PayUPayments.doAuthorizationAndCapture(parameters);

			LoggerUtil.info(RESPONSE_LOG_MESSAGE, response);

			Assert.assertNotNull(response, "Invalid response");
			Assert.assertNotNull(response.getResponseCode(), "Invalid response code");

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
	 * Do authorization and capture with Invalid Payment Method
	 * @author <a href="fernando.moreno@payulatam.com">Fernando Moreno</a>
	 * @date 2/09/2014
	 */
	@Test(expectedExceptions = PayUException.class, description="Test authorization and capture with Invalid Payment Method")
	public void doAuthorizationAndCaptureWithInvalidPaymentMethod() throws PayUException{

		Thread.currentThread().setName("doAuthorizationAndCaptureWithInvalidPaymentMethod");

		Map<String, String> parameters = new HashMap<String, String>();

		String orderReferenceCode = "A1B2C3" + System.currentTimeMillis();
		parameters.put(PayU.PARAMETERS.REFERENCE_CODE, orderReferenceCode);

		String description = "Test Invalid Payment Method";
		parameters.put(PayU.PARAMETERS.DESCRIPTION, description);

		parameters.put(PayU.PARAMETERS.PAYER_NAME, "PayerName");

		String payerDni = "52494";
		parameters.put(PayU.PARAMETERS.PAYER_DNI, payerDni);

		// Values
		Currency txCurrency = Currency.USD;
		parameters.put(PayU.PARAMETERS.CURRENCY, txCurrency.toString());

		BigDecimal txValue = new BigDecimal(10.0).setScale(2, RoundingMode.HALF_UP);
		parameters.put(PayU.PARAMETERS.VALUE, txValue.toString());

		// Colombian account
		parameters.put(PayU.PARAMETERS.ACCOUNT_ID, COLOMBIA_ACCOUNT.toString());
		//The invalid Payment Method
		parameters.put(PayU.PARAMETERS.PAYMENT_METHOD, "INVALID_PAYMENT_METHOD");

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, 1);

		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);

		parameters.put(PayU.PARAMETERS.EXPIRATION_DATE,sdf.format(cal.getTime()));

		try {
			TransactionResponse paymentResponse;
			paymentResponse = PayUPayments.doAuthorizationAndCapture(parameters);

			LoggerUtil.info(RESPONSE_LOG_MESSAGE, paymentResponse);
			Assert.fail("No PayUException was thrown");
		} catch (ConnectionException e) {
			// Service Unavailable
			LoggerUtil.error(e.getMessage(), e);

		} catch (InvalidParametersException e) {
			LoggerUtil.error(e.getMessage(), e);
		}

	}

	/**
	 * Do authorization and capture without Payment Method
	 * @author <a href="fernando.moreno@payulatam.com">Fernando Moreno</a>
	 * @date 2/09/2014
	 */
	@Test(expectedExceptions = InvalidParametersException.class, description="Test authorization and capture without Payment Method")
	public void doAuthorizationAndCaptureWithoutPaymentMethod() throws InvalidParametersException{

		Thread.currentThread().setName("doAuthorizationAndCaptureWithInvalidPaymentMethod");

		Map<String, String> parameters = new HashMap<String, String>();

		String orderReferenceCode = "A1B2C3" + System.currentTimeMillis();
		parameters.put(PayU.PARAMETERS.REFERENCE_CODE, orderReferenceCode);

		String description = "Test Invalid Payment Method";
		parameters.put(PayU.PARAMETERS.DESCRIPTION, description);

		parameters.put(PayU.PARAMETERS.PAYER_NAME, "PayerName");

		String payerDni = "52494";
		parameters.put(PayU.PARAMETERS.PAYER_DNI, payerDni);

		// Values
		Currency txCurrency = Currency.USD;
		parameters.put(PayU.PARAMETERS.CURRENCY, txCurrency.toString());

		BigDecimal txValue = new BigDecimal(10.0).setScale(2, RoundingMode.HALF_UP);
		parameters.put(PayU.PARAMETERS.VALUE, txValue.toString());

		// Colombian account
		parameters.put(PayU.PARAMETERS.ACCOUNT_ID, COLOMBIA_ACCOUNT.toString());
		//Without Payment Method or Null
		//parameters.put(PayU.PARAMETERS.PAYMENT_METHOD, null);

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, 1);

		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DEFAULT_DATE_FORMAT);

		parameters.put(PayU.PARAMETERS.EXPIRATION_DATE,sdf.format(cal.getTime()));

		try {
			TransactionResponse paymentResponse;
			paymentResponse = PayUPayments.doAuthorizationAndCapture(parameters);

			LoggerUtil.info(RESPONSE_LOG_MESSAGE, paymentResponse);
			Assert.fail("No InvalidParametersException was thrown");
		} catch (ConnectionException e) {
			// Service Unavailable
			LoggerUtil.error(e.getMessage(), e);
		}  catch (PayUException e) {
			// SDK error
			LoggerUtil.error(e.getMessage(), e);
			Assert.fail(e.getMessage());
		}
		catch (InvalidParametersException e) {
			LoggerUtil.error(e.getMessage(), e);
			throw e;
		}

	}


	/**
	 * Do authorization and capture with Birth Date Payer
	 * @author <a href="fernando.moreno@payulatam.com">Fernando Moreno</a>
	 * @date 3/09/2014
	 */
	@Test (description="Test for authorization and/or capture with Birth Date Payer")
	public void doAuthorizationAndCaptureWithBirthDatePayer() {

		Thread.currentThread().setName("doAuthorizationAndCaptureWithBirthDatePayer");

		Map<String, String> parameters = new HashMap<String, String>();

		String nameOnCard = "NAME " + Long.toString(System.currentTimeMillis());
		parameters.put(PayU.PARAMETERS.PAYER_NAME, nameOnCard);

		//Set Payer Birth date
		parameters.put(PayU.PARAMETERS.PAYER_BIRTH_DATE,"1980-02-13");

		parameters.put(PayU.PARAMETERS.INSTALLMENTS_NUMBER, "3");

		parameters.put(PayU.PARAMETERS.COUNTRY, PaymentCountry.MX.name());

		// Mexico account
		Integer accountId = 11;
		parameters.put(PayU.PARAMETERS.ACCOUNT_ID, accountId.toString());

		// Currency
		Currency txCurrency = Currency.MXN;
		parameters.put(PayU.PARAMETERS.CURRENCY, txCurrency.toString());
		String orderReferenceCode = "A1B2C3" + System.currentTimeMillis();
		parameters.put(PayU.PARAMETERS.REFERENCE_CODE, orderReferenceCode);

		String description = "Test Birth Date Payer MX";
		parameters.put(PayU.PARAMETERS.DESCRIPTION, description);

		// Transaction value
		BigDecimal txValue = new BigDecimal(200.00).setScale(2, RoundingMode.HALF_UP);
		parameters.put(PayU.PARAMETERS.VALUE, txValue.toString());

		CreditCards creditCard = CreditCards.VISA;

		parameters.put(PayU.PARAMETERS.CREDIT_CARD_NUMBER, creditCard.getCreditCardNumber());
		parameters.put(PayU.PARAMETERS.CREDIT_CARD_EXPIRATION_DATE, creditCard.getExpirationDate());
		parameters.put(PayU.PARAMETERS.CREDIT_CARD_SECURITY_CODE, creditCard.getSecurityCode());
		parameters.put(PayU.PARAMETERS.PAYMENT_METHOD, creditCard.name());

		try {
			TransactionResponse response = PayUPayments.doAuthorizationAndCapture(parameters);

			LoggerUtil.info(RESPONSE_LOG_MESSAGE, response);

			Assert.assertNotNull(response, "Invalid response");
			Assert.assertNotNull(response.getResponseCode(), "Invalid response code");

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
	 * test for when is Authorization And Capture Cash
	 * <ul>Test uses :
	 * 	<li>1. {@link PayUPayments#doAuthorizationAndCapture(Map)}.</li>
	 * 	<li>2. Payment for the {@link PaymentCountry#AR}.
	 * 	<li>3. Payment method is {@link PaymentMethod#PAGOFACIL}.
	 * </ul>
	 *
	 * @throws ConnectionException
	 * @throws InvalidParametersException
	 * @throws PayUException
	 *
	 */
	@Test
	public void doAuthorizationAndCaptureCashTest() throws PayUException, InvalidParametersException, ConnectionException{

		String reference = "TEST_CASH";
		String value= "870";

		Map<String, String> parameters = new HashMap<String, String>();

		parameters.put(PayU.PARAMETERS.ACCOUNT_ID, "9");

		parameters.put(PayU.PARAMETERS.REFERENCE_CODE, ""+reference);

		parameters.put(PayU.PARAMETERS.DESCRIPTION, "payment test");

		parameters.put(PayU.PARAMETERS.LANGUAGE, "Language.es");

		parameters.put(PayU.PARAMETERS.VALUE, ""+value);

		parameters.put(PayU.PARAMETERS.CURRENCY, ""+Currency.ARS.name());

		parameters.put(PayU.PARAMETERS.BUYER_EMAIL, "test@test.com");

		parameters.put(PayU.PARAMETERS.PAYER_NAME, "Test_cash");

		parameters.put(PayU.PARAMETERS.PAYMENT_METHOD, PaymentMethod.PAGOFACIL.name());

		parameters.put(PayU.PARAMETERS.COUNTRY, PaymentCountry.AR.name());

		parameters.put(PayU.PARAMETERS.IP_ADDRESS, "127.0.0.1");

		parameters.put(PayU.PARAMETERS.NOTIFY_URL, "https://www.Confirmacion.com");

		TransactionResponse response = PayUPayments.doAuthorizationAndCapture(parameters);

		if(response != null){
			response.getOrderId();
			response.getTransactionId();
			response.getState();
			LoggerUtil.info(" \t Order id : {0} \n\t Transaction id : {1} \n\t State : {2}", response.getOrderId(), response.getTransactionId(), response.getState());

			Assert.assertNotNull(response);

		}

	}

	/**
	 * Creates a transaction from payment request sent.
	 *
	 * @author <a href="johan.navarrete@payulatam.com">Johan Navarrete</a>
	 * @throws ConnectionException
	 * @throws PayUException
	 * @since 1.2.7
	 */
	@Test
	public void createTransactionFromPaymentRequestTest() throws PayUException, ConnectionException {

		Thread.currentThread().setName("createTransactionFromPaymentRequestTest");

		final PaymentAttemptRequest paymentRequest = new PaymentAttemptRequest(1);
		final Transaction transaction = new Transaction();

		CreditCards creditCardVisa = CreditCards.VISA;
		final CreditCard creditCard = new CreditCard();
		creditCard.setExpirationDate(creditCardVisa.getExpirationDate());
		creditCard.setNumber(creditCardVisa.getCreditCardNumber());
		creditCard.setSecurityCode(creditCardVisa.getSecurityCode());

		transaction.setCreditCard(creditCard);
		transaction.setPaymentMethod("VISA");
		paymentRequest.setTransaction(transaction);

		final TransactionResponse response = PayUPayments.createTransactionFromPaymentRequest(paymentRequest);

		LoggerUtil.info(RESPONSE_LOG_MESSAGE, response);

		Assert.assertNotNull(response, "Invalid response");
		Assert.assertNotNull(response.getResponseCode(), "Invalid response code");

		if (TransactionResponseCode.APPROVED.equals(response.getResponseCode())) {
			Assert.assertNotNull(response.getTransactionId());
			Assert.assertNotNull(response.getOrderId());
		}
	}

	/**
	 * Creates a transaction from payment request sent, should fail.
	 *
	 * @author <a href="johan.navarrete@payulatam.com">Johan Navarrete</a>
	 * @throws ConnectionException
	 * @throws PayUException
	 * @since 1.2.7
	 */
	@Test(expectedExceptions = { PayUException.class })
	public void createTransactionFromPaymentRequestTest_ShouldFail() throws PayUException, ConnectionException {

		Thread.currentThread().setName("createTransactionFromPaymentRequestTest");
		LoggerUtil.info(RESPONSE_LOG_MESSAGE,
				PayUPayments.createTransactionFromPaymentRequest(new PaymentAttemptRequest(1)));
	}
}

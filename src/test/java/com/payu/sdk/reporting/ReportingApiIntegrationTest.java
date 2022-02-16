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
package com.payu.sdk.reporting;

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
import com.payu.sdk.PayUReports;
import com.payu.sdk.exceptions.ConnectionException;
import com.payu.sdk.exceptions.PayUException;
import com.payu.sdk.exceptions.SDKException;
import com.payu.sdk.model.Currency;
import com.payu.sdk.model.Language;
import com.payu.sdk.model.Order;
import com.payu.sdk.model.PaymentCountry;
import com.payu.sdk.model.TransactionResponse;
import com.payu.sdk.model.TransactionResponseCode;
import com.payu.sdk.util.TestEnvironment;
import com.payu.sdk.utils.LoggerUtil;

/**
 *
 * @author PayuLatam
 * @since 1.0.0
 * @date 21/08/2013
 * @version 1.0
 */
public class ReportingApiIntegrationTest {

	/**
	 * Default response log message
	 */
	private static final String RESPONSE_LOG_MESSAGE = "{0}";

	/**
	 * Invalid order id
	 */
	private static final String INVALID_ORDER_ID = "1";

	/**
	 * The created order id
	 */
	private static Long orderId;

	/**
	 * the created transactionId
	 */
	private static String transactionId;

	/**
	 * Panama Installments
	 */
	private String PANANA_INSTALLMENTS = "1";

	/**
	 * the created referenceCode
	 */
	private static String referenceCode;

	@BeforeClass
	private void init() {

		PayU.apiKey = "012345678901";
		PayU.apiLogin = "012345678901";
		PayU.merchantId = "1";
		PayU.language = Language.en;
		PayU.isTest = false;

		TestEnvironment environment = TestEnvironment.QA;

		PayU.paymentsUrl = environment.getPaymentsApiUrl();
		PayU.reportsUrl = environment.getReportsApiUrl();

		LoggerUtil.setLogLevel(Level.ALL);
	}

	/**
	 * Do ping
	 */
	@Test
	public void doPing() {

		Thread.currentThread().setName("doPing");

		try {
			boolean response = PayUReports.doPing();

			LoggerUtil.info(RESPONSE_LOG_MESSAGE, response);

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
	 * Do authorization
	 */
	@Test
	public void doAuthorization() {

		Thread.currentThread().setName("doAuthorization");

		Map<String, String> parameters = new HashMap<String, String>();

		String nameOnCard = "NAME " + Long.toString(System.currentTimeMillis());
		parameters.put(PayU.PARAMETERS.PAYER_NAME, nameOnCard);

		parameters.put(PayU.PARAMETERS.INSTALLMENTS_NUMBER, PANANA_INSTALLMENTS);

		PaymentCountry paymentCountry = PaymentCountry.PA;
		parameters.put(PayU.PARAMETERS.COUNTRY, paymentCountry.name());

		// Panama account
		Integer accountId = 8;

		parameters.put(PayU.PARAMETERS.ACCOUNT_ID,
				accountId != null ? String.format("%d", accountId) : null);

		// Values
		Currency txCurrency = Currency.USD;
		parameters.put(PayU.PARAMETERS.CURRENCY, txCurrency.toString());

		String orderReferenceCode = "A1B2C3" + System.currentTimeMillis();
		parameters.put(PayU.PARAMETERS.REFERENCE_CODE, orderReferenceCode);

		String description = "ALL IN 5";
		parameters.put(PayU.PARAMETERS.DESCRIPTION, description);

		BigDecimal txValue = new BigDecimal(100.00).setScale(2,
				RoundingMode.HALF_UP);
		parameters.put(PayU.PARAMETERS.VALUE, txValue.toString());

		// credit card values
		parameters.put(PayU.PARAMETERS.CREDIT_CARD_NUMBER, "4005580000029205");
		parameters.put(PayU.PARAMETERS.CREDIT_CARD_EXPIRATION_DATE, "2030/01");
		parameters.put(PayU.PARAMETERS.CREDIT_CARD_SECURITY_CODE, "495");
		parameters.put(PayU.PARAMETERS.PAYMENT_METHOD, "VISA");

		try {

			TransactionResponse response = PayUPayments
					.doAuthorization(parameters);

			LoggerUtil.info(RESPONSE_LOG_MESSAGE, response);

			Assert.assertNotNull(response, "Invalid response");
			Assert.assertNotNull(response.getResponseCode(),
					"Invalid response code");

			if (TransactionResponseCode.APPROVED.equals(response
					.getResponseCode())) {
				transactionId = response.getTransactionId();
				orderId = response.getOrderId();
			} else {
				Assert.fail(String.format("Transaction state: %s",
						response.getResponseCode()));
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
	 * Do Order Detail Reporting by Id
	 */
	@Test(dependsOnMethods = "doAuthorization")
	public void doOrderDetailReporting() {

		Thread.currentThread().setName("doOrderDetailReporting");

		try {

			HashMap<String, String> parameters = new HashMap<String, String>();
			parameters.put(PayU.PARAMETERS.ORDER_ID, orderId == null ? null
					: String.format("%d", orderId));

			Order response = PayUReports.getOrderDetail(parameters);

			LoggerUtil.info(RESPONSE_LOG_MESSAGE, response);

			Assert.assertNotNull(response, "Order not found");

			Assert.assertEquals(response.getId(), orderId);

			if(!response.getTransactions().isEmpty()) {
				Assert.assertNotNull(response.getTransactions().get(0).getId());
			}

			referenceCode = response.getReferenceCode();

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
	 * Do Order Detail Reporting by Reference Code
	 */
	@Test(dependsOnMethods = "doOrderDetailReporting")
	public void doOrderDetailByReferenceCode() {

		Thread.currentThread().setName("doOrderDetailByReferenceCode");

		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put(PayU.PARAMETERS.REFERENCE_CODE, referenceCode);

		try {
			List<Order> response = PayUReports
					.getOrderDetailByReferenceCode(parameters);

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
	 * Do Transaction Response Reporting by Id
	 */
	@Test(dependsOnMethods = "doAuthorization")
	public void doTransactionResponseReporting() {

		Thread.currentThread().setName("doTransactionResponseReporting");

		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put(PayU.PARAMETERS.TRANSACTION_ID, transactionId);

		try {

			TransactionResponse response = PayUReports
					.getTransactionResponse(parameters);

			LoggerUtil.info(RESPONSE_LOG_MESSAGE, response);

			Assert.assertNotNull(response, "Null Response");

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
	 * Test reports error response
	 */
	@Test
	public void testErrorReportsResponse() {

		Thread.currentThread().setName("testErrorReportsResponse");

		Map<String, String> parameters = new HashMap<String, String>();

		parameters = new HashMap<String, String>();

		parameters.put(PayU.PARAMETERS.ORDER_ID, INVALID_ORDER_ID);

		try {
			Order response = PayUReports.getOrderDetail(parameters);

			LoggerUtil.info(RESPONSE_LOG_MESSAGE, response);

			Assert.fail("No error response");

		} catch (PayUException e) {

			// Expected error
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
}

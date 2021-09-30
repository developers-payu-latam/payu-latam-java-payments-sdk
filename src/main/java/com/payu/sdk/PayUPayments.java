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
package com.payu.sdk;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.payu.sdk.constants.Resources.RequestMethod;
import com.payu.sdk.exceptions.ConnectionException;
import com.payu.sdk.exceptions.InvalidParametersException;
import com.payu.sdk.exceptions.PayUException;
import com.payu.sdk.exceptions.SDKException.ErrorCode;
import com.payu.sdk.helper.HttpClientHelper;
import com.payu.sdk.model.Bank;
import com.payu.sdk.model.Merchant;
import com.payu.sdk.model.PaymentCountry;
import com.payu.sdk.model.PaymentMethodApi;
import com.payu.sdk.model.PaymentMethodComplete;
import com.payu.sdk.model.Transaction;
import com.payu.sdk.model.TransactionResponse;
import com.payu.sdk.model.TransactionType;
import com.payu.sdk.model.request.Command;
import com.payu.sdk.model.response.ResponseCode;
import com.payu.sdk.payments.model.BankListResponse;
import com.payu.sdk.payments.model.MassiveTokenPaymentsResponse;
import com.payu.sdk.payments.model.PaymentAttemptRequest;
import com.payu.sdk.payments.model.PaymentMethodListResponse;
import com.payu.sdk.payments.model.PaymentMethodResponse;
import com.payu.sdk.payments.model.PaymentRequest;
import com.payu.sdk.payments.model.PaymentResponse;
import com.payu.sdk.utils.CommonRequestUtil;
import com.payu.sdk.utils.PaymentMethodMap;
import com.payu.sdk.utils.PaymentPlanRequestUtil;
import com.payu.sdk.utils.RequestUtil;

/**
 * Manages all PayU payments operations
 *
 * @author PayU Latam
 * @since 1.0.0
 * @version 1.0.0, 21/08/2013
 */
public final class PayUPayments extends PayU {

	/**
	 * Private constructor
	 */
	private PayUPayments() {
	}

	/**
	 * Makes a ping petition
	 *
	 * @return true if the ping is done successfully
	 * @throws PayUException
	 * @throws ConnectionException
	 */
	public static boolean doPing() throws PayUException, ConnectionException {
		String res = HttpClientHelper.sendRequest(
				RequestUtil.buildPaymentsPingRequest(), RequestMethod.POST);

		PaymentResponse response = PaymentResponse.fromXml(res);

		return ResponseCode.SUCCESS.equals(response.getCode());
	}

	/**
	 * Makes a get payment methods petition
	 *
	 * @return The payment method list
	 * @throws PayUException
	 * @throws ConnectionException
	 */
	public static List<PaymentMethodComplete> getPaymentMethods()
			throws PayUException, ConnectionException {
		String res = HttpClientHelper.sendRequest(
				RequestUtil.buildPaymentMethodsListRequest(),
				RequestMethod.POST);

		PaymentMethodListResponse response = PaymentMethodListResponse
				.fromXml(res);

		return response.getPaymentMethods();

	}

	/**
	 * Get payment method availability
	 *
	 * @param paymentMethod
	 * @return the payment method list
	 * @throws PayUException
	 * @throws ConnectionException
	 */
	public static PaymentMethodApi getPaymentMethodAvailability(String paymentMethod)
			throws PayUException, ConnectionException {
		
		// The api key and api login are not needed
		return getPaymentMethodAvailabilityLogic(paymentMethod, null, null);
	}
	
	/**
	 * Get payment method availability
	 * 
	 * @param paymentMethod
	 * @param apiKey
	 * @param apiLogin
	 * @return the payment method
	 * @throws PayUException
	 * @throws ConnectionException
	 */
	public static PaymentMethodApi getPaymentMethodAvailability(String paymentMethod, String apiKey, String apiLogin)
			throws PayUException, ConnectionException {
		
		return getPaymentMethodAvailabilityLogic(paymentMethod, apiKey, apiLogin);
	}
	
	/**
	 * Contains the bussiness logic for
	 * {@link PayUPayments#getPaymentMethodAvailability(String)} and
	 * {@link PayUPayments#getPaymentMethodAvailability(String,String,String)}
	 * 
	 * @param paymentMethod
	 * @param apiKey
	 * @param apiLogin
	 * @return the payment method
	 * @throws PayUException
	 * @throws ConnectionException
	 */
	private static PaymentMethodApi getPaymentMethodAvailabilityLogic(String paymentMethod, String apiKey, String apiLogin)
			throws PayUException, ConnectionException {
		
		// Finds the PaymentMethod object in the map
		PaymentMethodApi paymentMethodApi = PaymentMethodMap.getInstance().getPaymentMethod(paymentMethod);
		if (paymentMethodApi == null) {
			
			if (apiKey != null && !apiKey.trim().equals("") && apiLogin != null && !apiLogin.trim().equals("")) {
				// Makes a get payment method availability request in the API using api key and api login sent by user as parameters
				paymentMethodApi = getPaymentMethodAvailabilityFromAPI(paymentMethod, apiKey, apiLogin);
			} else {
				// Makes a get payment method Availability request in the API
				paymentMethodApi = getPaymentMethodAvailabilityFromAPI(paymentMethod);
			}
			
			// If obtains the paymentMethod object, adds this to the map
			if (paymentMethodApi != null){
				PaymentMethodMap.getInstance().putPaymentMethod(paymentMethodApi);
			}
		}
		return paymentMethodApi;
	}

	/**
	 * Makes a get payment method availability request
	 * 
	 * @param paymentMethod
	 * @return
	 * @throws PayUException
	 * @throws ConnectionException
	 */
	protected static PaymentMethodApi getPaymentMethodAvailabilityFromAPI(String paymentMethod)
			throws PayUException, ConnectionException {

		// The api key and api login are not needed
		return getPaymentMethodAvailabilityFromAPILogic(paymentMethod, null, null);
	}
	
	/**
	 * Makes a get payment method availability request
	 * 
	 * @param paymentMethod
	 * @param apiKey
	 * @param apiLogin
	 * @return
	 * @throws PayUException
	 * @throws ConnectionException
	 */
	protected static PaymentMethodApi getPaymentMethodAvailabilityFromAPI(String paymentMethod, String apiKey, String apiLogin)
			throws PayUException, ConnectionException {

		return getPaymentMethodAvailabilityFromAPILogic(paymentMethod, apiKey, apiLogin);
	}
	
	/**
	 * Contains the bussiness logic for
	 * {@link PayUPayments#getPaymentMethodAvailabilityFromAPI(String)} and
	 * {@link PayUPayments#getPaymentMethodAvailabilityFromAPI(String,String,String)}.
	 * 
	 * @param paymentMethod
	 * @param apiKey
	 * @param apiLogin
	 * @return
	 * @throws PayUException
	 * @throws ConnectionException
	 */
	protected static PaymentMethodApi getPaymentMethodAvailabilityFromAPILogic(String paymentMethod, String apiKey, String apiLogin)
			throws PayUException, ConnectionException {
		
		String res = HttpClientHelper.sendRequest(
				RequestUtil.buildPaymentMethodAvailability(paymentMethod, apiKey, apiLogin),
				RequestMethod.POST);

		PaymentMethodResponse response = PaymentMethodResponse.fromXml(res);

		return response.getPaymentMethod();
	}

	/**
	 * Makes a get PSE Banks petition
	 *
	 * @param parameters
	 *            The parameters to be sent to the server
	 * @return The bank list information
	 * @throws PayUException
	 * @throws InvalidParametersException
	 * @throws ConnectionException
	 */
	public static List<Bank> getPSEBanks(Map<String, String> parameters)
			throws PayUException, InvalidParametersException,
			ConnectionException {

		RequestUtil.validateParameters(parameters, PayU.PARAMETERS.COUNTRY);

		PaymentCountry paymentCountry = PaymentCountry.valueOf(parameters
				.get(PayU.PARAMETERS.COUNTRY));

		String res = HttpClientHelper.sendRequest(
				RequestUtil.buildBankListRequest(paymentCountry),
				RequestMethod.POST);

		BankListResponse response = BankListResponse.fromXml(res);

		return response.getBanks();

	}

	/**
	 * Do an authorization transaction
	 *
	 * @param parameters
	 *            The parameters to be sent to the server
	 * @return The transaction response to the request sent
	 * @throws PayUException
	 * @throws ConnectionException
	 * @throws InvalidParametersException
	 */
	public static TransactionResponse doAuthorization(
			Map<String, String> parameters) throws PayUException,
			InvalidParametersException, ConnectionException {

		return doPayment(parameters, TransactionType.AUTHORIZATION);
	}

	/**
	 * Do a capture transaction
	 *
	 * @param parameters
	 *            The parameters to be sent to the server
	 * @return The transaction response to the request sent
	 * @throws PayUException
	 * @throws ConnectionException
	 * @throws InvalidParametersException
	 */
	public static TransactionResponse doCapture(Map<String, String> parameters)
			throws PayUException, InvalidParametersException,
			ConnectionException {

		return processTransaction(parameters, TransactionType.CAPTURE);
	}

	/**
	 * Do an authorization and capture transaction
	 *
	 * @param parameters
	 *            The parameters to be sent to the server
	 * @return The transaction response to the request sent
	 * @throws PayUException
	 * @throws ConnectionException
	 * @throws InvalidParametersException
	 */
	public static TransactionResponse doAuthorizationAndCapture(
			Map<String, String> parameters) throws PayUException,
			InvalidParametersException, ConnectionException {

		return doPayment(parameters, TransactionType.AUTHORIZATION_AND_CAPTURE, HttpClientHelper.SOCKET_TIMEOUT);
	}


	/**
	 * Do an authorization and capture transaction
	 *
	 * @param parameters
	 *            The parameters to be sent to the server
	 * @param the socket time out.
	 * @return The transaction response to the request sent
	 * @throws PayUException
	 * @throws ConnectionException
	 * @throws InvalidParametersException
	 */
	public static TransactionResponse doAuthorizationAndCapture(
			Map<String, String> parameters, Integer socketTimeOut) throws PayUException,
			InvalidParametersException, ConnectionException {

		return doPayment(parameters, TransactionType.AUTHORIZATION_AND_CAPTURE, socketTimeOut);
	}


	/**
	 * Do a void (Cancel) transaction
	 *
	 * @param parameters
	 *            The parameters to be sent to the server
	 * @return The transaction response to the request sent
	 * @throws PayUException
	 * @throws ConnectionException
	 * @throws InvalidParametersException
	 */
	public static TransactionResponse doVoid(Map<String, String> parameters)
			throws PayUException, InvalidParametersException,
			ConnectionException {

		return processTransaction(parameters, TransactionType.VOID);
	}

	/**
	 * Do a refund transaction
	 *
	 * @param parameters
	 *            The parameters to be sent to the server
	 * @return The transaction response to the request sent
	 * @throws PayUException
	 * @throws ConnectionException
	 * @throws InvalidParametersException
	 */
	public static TransactionResponse doRefund(Map<String, String> parameters)
			throws PayUException, InvalidParametersException,
			ConnectionException {

		return processTransaction(parameters, TransactionType.REFUND);
	}
	
	/**
	 * Do refund with request headers.
	 *
	 * @param parameters the parameters
	 * @param headers the headers
	 * @return the transaction response
	 * @throws PayUException the pay U exception
	 * @throws InvalidParametersException the invalid parameters exception
	 * @throws ConnectionException the connection exception
	 */
	public static TransactionResponse doRefundWithRequestHeaders(Map<String, String> parameters,
			Map<String, String> headers)
			throws PayUException, InvalidParametersException, ConnectionException {

		return processTransactionWithRequestHeaders(parameters, headers, TransactionType.REFUND);
	}

	/**
	 * Do a partial refund transaction
	 *
	 * @param parameters
	 *            The parameters to be sent to the server
	 * @return The transaction response to the request sent
	 * @throws PayUException
	 * @throws ConnectionException
	 * @throws InvalidParametersException
	 */
	public static TransactionResponse doPartialRefund(Map<String, String> parameters)
			throws PayUException, InvalidParametersException,
			ConnectionException {

		return processTransaction(parameters, TransactionType.PARTIAL_REFUND);
	}
	
	/**
	 * Do partial refund with request headers.
	 *
	 * @param parameters the parameters
	 * @param headers the headers
	 * @return the transaction response
	 * @throws PayUException the pay U exception
	 * @throws InvalidParametersException the invalid parameters exception
	 * @throws ConnectionException the connection exception
	 */
	public static TransactionResponse doPartialRefundWithRequestHeaders(
			Map<String, String> parameters, Map<String, String> headers)
			throws PayUException, InvalidParametersException, ConnectionException {

		return processTransactionWithRequestHeaders(parameters, headers,
				TransactionType.PARTIAL_REFUND);
	}

	/**
	 * Makes massive token payments petition
	 *
	 * @param parameters The parameters to be sent to the server
	 * @return The transaction response to the request sent
	 * @throws PayUException when the response of payments-api is error
	 * @throws InvalidParametersException when a received parameter is invalid
	 * @throws ConnectionException Connection error with payments-api
	 */
	public static String doMassiveTokenPayments(final Map<String, String> parameters)
			throws PayUException, InvalidParametersException, ConnectionException {

		validateMassiveTokenPaymentsRequest(parameters);
		return sendMassiveTokenPayments(parameters).getId();
	}
	
	/**
	 * Makes payment petition
	 *
	 * @param parameters
	 *            The parameters to be sent to the server
	 * @param transactionType
	 *            The type of the payment transaction
	 * @return The transaction response to the request sent
	 * @throws PayUException
	 * @throws InvalidParametersException
	 * @throws ConnectionException
	 */
	private static TransactionResponse doPayment(
			Map<String, String> parameters, TransactionType transactionType)
			throws PayUException, InvalidParametersException,
			ConnectionException {

		return doPayment(parameters, transactionType, HttpClientHelper.SOCKET_TIMEOUT);
	}


	/**
	 * Makes payment petition
	 *
	 * @param parameters
	 *            The parameters to be sent to the server
	 * @param transactionType
	 *            The type of the payment transaction
	 * @param the socket time out.
	 * @return The transaction response to the request sent
	 * @throws PayUException
	 * @throws InvalidParametersException
	 * @throws ConnectionException
	 */
	private static TransactionResponse doPayment(
			Map<String, String> parameters, TransactionType transactionType, Integer socketTimeOut)
			throws PayUException, InvalidParametersException,
			ConnectionException {

		String[] required = getRequiredParameters(parameters);

		RequestUtil.validateParameters(parameters, required);

		String res = HttpClientHelper.sendRequest(
				RequestUtil.buildPaymentRequest(parameters, transactionType),
				RequestMethod.POST, socketTimeOut);

		PaymentResponse response = PaymentResponse.fromXml(res);

		return response.getTransactionResponse();
	}
	
	/**
	 * From parameters map to transaction.
	 *
	 * @param parameters the parameters
	 * @param transactionType the transaction type
	 * @return the transaction
	 * @throws PayUException the pay u exception
	 * @throws InvalidParametersException the invalid parameters exception
	 * @throws ConnectionException the connection exception
	 */
	public static Transaction fromParametersMapToTransaction(Map<String, String> parameters, TransactionType transactionType) throws PayUException, InvalidParametersException, ConnectionException {
		
		String[] required = getRequiredParameters(parameters);

		RequestUtil.validateParameters(parameters, required);
		
		return RequestUtil.buildTransaction(parameters, transactionType);
	}
	
	/**
	 * Submit transaction.
	 *
	 * @param transaction the transaction
	 * @param timeoutMs the timeout milliseconds
	 * @return the transaction response
	 * @throws PayUException the pay u exception
	 * @throws ConnectionException the connection exception
	 */
	public static TransactionResponse submitTransaction(Transaction transaction, Integer timeoutMs) throws PayUException, ConnectionException {
		
		Merchant merchant = new Merchant();
		merchant.setApiKey(PayU.apiKey);
		merchant.setApiLogin(PayU.apiLogin);
		
		PaymentRequest request = new PaymentRequest();
		request.setLanguage(PayU.language);
		request.setTest(PayU.isTest);
		request.setCommand(Command.SUBMIT_TRANSACTION);
		
		request.setMerchant(merchant);
		request.setTransaction(transaction);
		
		String res = HttpClientHelper.sendRequest(request, RequestMethod.POST, timeoutMs);
		
		PaymentResponse response = PaymentResponse.fromXml(res);

		return response.getTransactionResponse();
	}

	/**
	 * Validate massive token payments request
	 *
	 * @param parameters The parameters to be sent to the server
	 * @throws PayUException when the response of payments-api is error
	 * @throws InvalidParametersException when a received parameter is invalid
	 * @throws ConnectionException Connection error with payments-api
	 */
	private static void validateMassiveTokenPaymentsRequest(final Map<String, String> parameters)
			throws InvalidParametersException {

		RequestUtil.validateParameters(parameters, getRequiredParametersForMassiveTokenTransactions());
	}

	/**
	 * Makes payment petition
	 *
	 * @param parameters The parameters to be sent to the server
	 * @return The transaction response to the request sent
	 * @throws PayUException when the response of payments-api is error
	 * @throws InvalidParametersException when a received parameter is invalid
	 * @throws ConnectionException Connection error with payments-api
	 */
	private static MassiveTokenPaymentsResponse sendMassiveTokenPayments(final Map<String, String> parameters)
			throws PayUException, InvalidParametersException, ConnectionException {

		return MassiveTokenPaymentsResponse.fromXml(HttpClientHelper.sendRequest(
				RequestUtil.buildMassiveTokenPaymentsRequest(parameters),
				RequestMethod.POST,
				HttpClientHelper.SOCKET_TIMEOUT));
	}

	/**
	 * Initialize the list with default parameters
	 *
	 * @return array with required parameters
	 */
	private static String[] getRequiredParametersForMassiveTokenTransactions() {

		return new String[] {PARAMETERS.API_KEY, PARAMETERS.API_LOGIN, PARAMETERS.CONTENT_FILE};
	}

	/**
	 * Submit transaction.
	 *
	 * @param parameters the parameters map
	 * @return the transaction response
	 * @throws PayUException       the pay u exception
	 * @throws ConnectionException the connection exception
	 */
	public static boolean sendConfirmationPage(final Map<String, String> parameters)
			throws PayUException, ConnectionException, InvalidParametersException {

		String[] requiredParams = { PARAMETERS.TRANSACTION_ID };
		PaymentPlanRequestUtil.validateParameters(parameters, requiredParams);

		String res = HttpClientHelper.sendRequest(
				RequestUtil.buildConfirmationPageRequest(parameters), RequestMethod.POST);
		return res.contains("true");
	}

	/**
	 * Gets the PaymentMethod value parameter from the parameters map.
	 * If the parameter is not null find the Payment method in the map, but if not found then make request to API
	 * @param parameters The parameters to be sent to the server
	 * @param paramName  the parameter to get
	 * @return the PaymentMethod value or null
	 */
	private static PaymentMethodApi getPaymentMethodParameter(Map<String, String> parameters, String paramName)
			throws PayUException, InvalidParametersException, ConnectionException{
		PaymentMethodApi paymentMethod = null;
		String parameter = CommonRequestUtil.getParameter(parameters, paramName);
		if (parameter != null) {
			
			String apiKey = RequestUtil.getParameter(parameters, PayU.PARAMETERS.API_KEY);
			String apiLogin = RequestUtil.getParameter(parameters, PayU.PARAMETERS.API_LOGIN);
			
			if (apiKey != null && !apiKey.trim().equals("") && apiLogin != null && !apiLogin.trim().equals("")) {
				// Finds the payment method using api key and api login sent by user as parameters
				paymentMethod = getPaymentMethodAvailability(parameter, apiKey, apiLogin);
			} else {
				// Find the PaymentMethod object in the map
				paymentMethod = getPaymentMethodAvailability(parameter);
			}
		}
		return paymentMethod;
	}

	/**
	 *
	 * Returns the required parameters based on the payment method
	 *
	 * @param parameters
	 * @return
	 * @throws PayUException
	 * @throws InvalidParametersException
	 */
	private static String[] getRequiredParameters(Map<String, String> parameters)
			throws PayUException, InvalidParametersException, ConnectionException {

		//Initialize the list with default parameters
		List<String> requiredParameters= new ArrayList<String>();
		requiredParameters.add(PayU.PARAMETERS.REFERENCE_CODE);
		requiredParameters.add(PayU.PARAMETERS.DESCRIPTION);
		requiredParameters.add(PayU.PARAMETERS.CURRENCY);
		requiredParameters.add(PayU.PARAMETERS.VALUE);

		/* Token methods */
		if (parameters.containsKey(PayU.PARAMETERS.TOKEN_ID)) {
			requiredParameters.add(PayU.PARAMETERS.INSTALLMENTS_NUMBER);
			requiredParameters.add(PayU.PARAMETERS.TOKEN_ID);
			return requiredParameters.toArray(new String[requiredParameters.size()]);
		}

		RequestUtil.validateParameters(parameters, PayU.PARAMETERS.PAYMENT_METHOD);

		//Obtains the payment method. If the parameter is a value that doesn't available this return null and continue
		PaymentMethodApi paymentMethod = getPaymentMethodParameter(parameters,PayU.PARAMETERS.PAYMENT_METHOD);

		if (paymentMethod != null){
			if ("BOLETO_BANCARIO".equals(paymentMethod.getName())) {
				requiredParameters.add(PayU.PARAMETERS.PAYER_NAME);
				requiredParameters.add(PayU.PARAMETERS.PAYER_DNI);
				requiredParameters.add(PayU.PARAMETERS.PAYMENT_METHOD);
				requiredParameters.add(PayU.PARAMETERS.PAYER_STREET);
				requiredParameters.add(PayU.PARAMETERS.PAYER_STREET_2);
				requiredParameters.add(PayU.PARAMETERS.PAYER_CITY);
				requiredParameters.add(PayU.PARAMETERS.PAYER_STATE);
				requiredParameters.add(PayU.PARAMETERS.PAYER_POSTAL_CODE);

			}else if (paymentMethod.getType() != null){
				switch (paymentMethod.getType()) {
				case CASH:
					requiredParameters.add(PayU.PARAMETERS.PAYER_NAME);
					requiredParameters.add(PayU.PARAMETERS.PAYMENT_METHOD);
					break;
				case REFERENCED:
					requiredParameters.add(PayU.PARAMETERS.PAYER_NAME);
					requiredParameters.add(PayU.PARAMETERS.PAYMENT_METHOD);
					requiredParameters.add(PayU.PARAMETERS.PAYER_DNI);
					break;
				case PSE:
					requiredParameters.add(PayU.PARAMETERS.PAYER_NAME);
					requiredParameters.add(PayU.PARAMETERS.PAYMENT_METHOD);
					requiredParameters.add(PayU.PARAMETERS.PAYER_DOCUMENT_TYPE);
					requiredParameters.add(PayU.PARAMETERS.PAYER_DNI);
					requiredParameters.add(PayU.PARAMETERS.PAYER_EMAIL);
					requiredParameters.add(PayU.PARAMETERS.PAYER_CONTACT_PHONE);
					requiredParameters.add(PayU.PARAMETERS.PSE_FINANCIAL_INSTITUTION_CODE);
					requiredParameters.add(PayU.PARAMETERS.PAYER_PERSON_TYPE);
					requiredParameters.add(PayU.PARAMETERS.IP_ADDRESS);
					requiredParameters.add(PayU.PARAMETERS.COOKIE);
					requiredParameters.add(PayU.PARAMETERS.USER_AGENT);
					break;
				case CREDIT_CARD:

					boolean optionalSecurityCode = (parameters.containsKey(PayU.PARAMETERS.PROCESS_WITHOUT_CVV2) &&
							Boolean.TRUE.toString().equalsIgnoreCase(parameters.get(PayU.PARAMETERS.PROCESS_WITHOUT_CVV2)));

					requiredParameters.add(PayU.PARAMETERS.PAYER_NAME);
					requiredParameters.add(PayU.PARAMETERS.PAYMENT_METHOD);
					requiredParameters.add(PayU.PARAMETERS.INSTALLMENTS_NUMBER);
					requiredParameters.add(PayU.PARAMETERS.CREDIT_CARD_NUMBER);
					requiredParameters.add(PayU.PARAMETERS.CREDIT_CARD_EXPIRATION_DATE);
					if (!optionalSecurityCode) {
						requiredParameters.add(PayU.PARAMETERS.CREDIT_CARD_SECURITY_CODE);
					}
					break;

				case BANK_TRANSFER:

                    requiredParameters.add(PayU.PARAMETERS.PAYER_NAME);
                    requiredParameters.add(PayU.PARAMETERS.PAYER_STREET);
                    requiredParameters.add(PayU.PARAMETERS.PAYER_STREET_2);
                    requiredParameters.add(PayU.PARAMETERS.PAYER_CITY);
                    requiredParameters.add(PayU.PARAMETERS.PAYER_STATE);
                    requiredParameters.add(PayU.PARAMETERS.PAYER_POSTAL_CODE);
                    break;

				case BANK_REFERENCED:

					break;
						
				default:
					throw new PayUException(ErrorCode.API_ERROR,"Unsupported payment method");
				}
			}
		}else{
			throw new PayUException(ErrorCode.API_ERROR, "Unsupported payment method");
		}
		return requiredParameters.toArray(new String[requiredParameters.size()]);
	}

	/**
	 * Makes a transaction dependent petition
	 *
	 * @param parameters
	 *            The parameters to be sent to the server
	 * @param transactionType
	 *            The type of the payment transaction
	 * @return The transaction response to the request sent
	 * @throws PayUException
	 * @throws InvalidParametersException
	 * @throws ConnectionException
	 */
	private static TransactionResponse processTransaction(
			Map<String, String> parameters, TransactionType transactionType)
			throws PayUException, InvalidParametersException,
			ConnectionException {

		String[] required = new String[] { PayU.PARAMETERS.ORDER_ID,
				PayU.PARAMETERS.TRANSACTION_ID };

		RequestUtil.validateParameters(parameters, required);

		String res = HttpClientHelper.sendRequest(
				RequestUtil.buildPaymentRequest(parameters, transactionType),
				RequestMethod.POST);

		PaymentResponse response = PaymentResponse.fromXml(res);

		return response.getTransactionResponse();
	}
	
	/**
	 * Process transaction with request headers.
	 *
	 * @param parameters the parameters
	 * @param headers the headers
	 * @param transactionType the transaction type
	 * @return the transaction response
	 * @throws PayUException the pay U exception
	 * @throws InvalidParametersException the invalid parameters exception
	 * @throws ConnectionException the connection exception
	 */
	private static TransactionResponse processTransactionWithRequestHeaders(
			Map<String, String> parameters, Map<String, String> headers, TransactionType transactionType)
			throws PayUException, InvalidParametersException,
			ConnectionException {

		String[] required = new String[] { PayU.PARAMETERS.ORDER_ID,
				PayU.PARAMETERS.TRANSACTION_ID };

		RequestUtil.validateParameters(parameters, required);

		String res = HttpClientHelper.sendRequestWithExtraHeaders(
				RequestUtil.buildPaymentRequest(parameters, transactionType), headers,
				RequestMethod.POST);

		PaymentResponse response = PaymentResponse.fromXml(res);

		return response.getTransactionResponse();
	}

	/**
	 * Creates a transaction using a payment request.
	 * 
	 * @param paymentRequest the payment request to create a transaction.
	 * @return a transaction response.
	 * @throws PayUException
	 * @throws ConnectionException
	 */
	public static TransactionResponse createTransactionFromPaymentRequest(final PaymentAttemptRequest paymentRequest)
			throws PayUException, ConnectionException {

		return PaymentResponse.fromXml(HttpClientHelper.sendRequest(paymentRequest, RequestMethod.POST))
				.getTransactionResponse();
	}

}

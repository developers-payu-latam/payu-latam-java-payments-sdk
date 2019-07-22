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

import com.payu.sdk.model.Language;

/**
 * Holds basic request information
 *
 * @author PayU Latam
 * @since 1.0.0
 * @version 1.0.0, 21/08/2013
 */
public abstract class PayU {

	/**
	 * Api version
	 */
	public static final String API_VERSION = "4.0.1";

	/**
	 * Api name
	 */
	public static final String API_NAME = "PayU SDK";

	/**
	 * Custom Payments-API endpoint URL
	 */
	public static String paymentsUrl = null;

	/**
	 * Custom Reports-API endponint URL
	 */
	public static String reportsUrl = null;

	/**
	 * The method invocation is test?
	 */
	public static boolean isTest = false;

	/**
	 * The merchant API key
	 */
	public static String apiKey = null;

	/**
	 * The merchant API Login
	 */
	public static String apiLogin = null;

	/**
	 * The merchant Id
	 */
	public static String merchantId = null;

	/**
	 * The request language
	 */
	public static Language language = Language.es;

	/**
	 * API invocation parameters to be used as key when putting items into the
	 * parameters Map.
	 */
	public interface PARAMETERS {

		String API_LOGIN = "apiLogin";
		String API_KEY = "apiKey";
		String MARCHANT_ID = "merchantId";
		String LANGUAGE = "language";
		String IS_TEST = "isTest";

		String LIMIT = "limit";
		String OFFSET = "offset";

		/** The payment method's country, see {@link #com.payu.sdk.model.PaymentCountry}*/
		String COUNTRY = "country";
		/** The order's id. */
		String ORDER_ID = "orderId";
		/** El order's reference code. */
		String REFERENCE_CODE = "referenceCode";
		/** The order's or payment method's description. */
		String DESCRIPTION = "description";
		/** The account's id. */
		String ACCOUNT_ID = "accountId";
		/** The ISO code for the currency to use. */
		String CURRENCY = "currency";
		/** The value of the purchase. */
		String VALUE = "value";
		/** The tax value. */
		String TAX_VALUE = "taxValue";
		/** The tax return base. */
		String TAX_RETURN_BASE = "taxDevolutionBase";
		/** The transactions signature. */
		String SIGNATURE = "signature";
		/** The transaction identifier. */
		String TRANSACTION_ID = "transactionId";
		/** The transaction source */
		String TRANSACTION_SOURCE = "transactionSource";
		/** The credit card token id. */
		String TOKEN_ID = "tokenId";
		/** the batch token id. */
		String BATCH_TOKEN_ID = "batchTokenId";
		/** The payment method to use, {@link #com.payu.sdk.model.PaymentMethod} */
		String PAYMENT_METHOD = "paymentMethod";

		/**
		 * Explains why a transaction is requested to be cancelled
		 */
		String REASON = "reason";

		/** The number of installments on the purchase. */
		String INSTALLMENTS_NUMBER = "installmentsNumber";
                /** The Promotion id number **/
                String PROMOTION_ID = "promotionId";
		/** The number on the credit card. */
		String CREDIT_CARD_NUMBER = "creditCardNumber";
		/** The credit card's expiration date. */
		String CREDIT_CARD_EXPIRATION_DATE = "creditCardExpirationDate";
		/** The credit card's security code. */
		String CREDIT_CARD_SECURITY_CODE = "creditCardSecurityCode";
		/** The credit card holder's identification number */
		String CREDIT_CARD_DOCUMENT = "document";
		/** The credit card holder's name */
		String CREDIT_CARD_HOLDER_NAME = "creditCardHolderName";

		/** Create a credit card token **/
		String CREATE_CREDIT_CARD_TOKEN = "createCreditCardToken";

		/** Id of the account holder */
		String BANK_ACCOUNT_ID = "bankAccountId";
		/** Document Number of the account holder */
		String BANK_ACCOUNT_DOCUMENT_NUMBER = "documentNumber";
		/** Document number type of the account holder */
		String BANK_ACCOUNT_DOCUMENT_NUMBER_TYPE = "documentNumberType";
		/** ACH Bank */
		String BANK_ACCOUNT_BANK_NAME = "bank";
		/** ACH Bank */
		String BANK_ACCOUNT_CUSTOMER_NAME = "name";
		/** ACH bank account type */
		String BANK_ACCOUNT_TYPE = "type";
		/** ACH bank account number */
		String BANK_ACCOUNT_NUMBER = "accountNumber";
		/** ACH bank state */
		String BANK_ACCOUNT_STATE = "accountState";
		/** Account digit (Only valid for Brazil) */
		String BANK_ACCOUNT_ACCOUNT_DIGIT = "accountDigit";
		/** Agency digit (Only valid for Brazil) */
		String BANK_ACCOUNT_AGENCY_DIGIT = "agencyNumber";
		/** Agency number (Only valid for Brazil) */
		String BANK_ACCOUNT_AGENCY_NUMBER = "agencyDigit";

		/** The payer's name */
		String PAYER_NAME = "payerName";
		/** The payer's id on the merchant. */
		String PAYER_ID = "payerId";
		/** The payer's contact e-mail. */
		String PAYER_EMAIL = "payerEmail";
		/** The payer's contact phone. */
		String PAYER_CONTACT_PHONE = "payerContactPhone";
		/** The payer's CNPJ. */
		String PAYER_CNPJ = "payerCNPJ";
		/** The payer's DNI. */
		String PAYER_DNI = "payerDNI";
		/** The payer's DNI type. */
		String PAYER_DNI_TYPE = "payerDniType";
		/** The payer's Business Name. */
		String PAYER_BUSINESS_NAME = "payerBusinessName";

		/** The payer's country, see {@link #com.payu.sdk.model.PaymentCountry}*/
		String PAYER_COUNTRY = "payerCountry";
		/** The payer's city. */
		String PAYER_CITY = "payerCity";
		/** The payer's phone. */
		String PAYER_PHONE = "payerPhone";
		/** The payer's postal code. */
		String PAYER_POSTAL_CODE = "payerPostalCode";
		/** The payer's state. */
		String PAYER_STATE = "payerState";
		/** The payer's address (part 1). */
		String PAYER_STREET = "payerStreet";
		/** The payer's address (part 2). */
		String PAYER_STREET_2 = "payerStreet2";
		/** The payer's address (part 3). */
		String PAYER_STREET_3 = "payerStreet3";
		/** The payer's birthdate*/
		String PAYER_BIRTH_DATE = "birthdate";

		/** The buyer's name */
		String BUYER_NAME = "buyerName";
		/** The buyer's id on the merchant. */
		String BUYER_ID = "buyerId";
		/** The buyer's contact e-mail. */
		String BUYER_EMAIL = "buyerEmail";
		/** The buyer's contact phone. */
		String BUYER_CONTACT_PHONE = "buyerContactPhone";
		/** The buyer's CNPJ. */
		String BUYER_CNPJ = "buyerCNPJ";

		/** The buyer's DNI. */
		String BUYER_DNI = "buyerDNI";
		/** The buyer's DNI type. */
		String BUYER_DNI_TYPE = "buyerDniType";

		/** The buyer's address (part 1). */
		String BUYER_STREET = "buyerStreet";
		/** The buyer's address (part 2). */
		String BUYER_STREET_2 = "buyerStreet2";
		/** The buyer's address (part 3). */
		String BUYER_STREET_3 = "buyerStreet3";
		/** The buyer's city. */
		String BUYER_CITY = "buyerCity";
		/** The buyer's state. */
		String BUYER_STATE = "buyerState";
		/** The buyer's country. */
		String BUYER_COUNTRY = "buyerCountry";
		/** The buyer's phone. */
		String BUYER_PHONE = "buyerPhone";
		/** The buyer's postal code. */
		String BUYER_POSTAL_CODE = "buyerPostalCode";

		/** Start date to filter. */
		String START_DATE = "startDate";
		/** Last date to filter. */
		String END_DATE = "endDate";

		/** The credit card's expiration date. */
		String EXPIRATION_DATE = "expirationDate";

		/** The customer's name. */
		String CUSTOMER_NAME = "customerName";
		/** The customer's id on the merchant. */
		String CUSTOMER_ID = "customerId";
		/** The customer's contact e-mail. */
		String CUSTOMER_EMAIL = "customerEmail";

		/** The plan id. */
		String PLAN_ID = "planId";
		/** The plan code. */
		String PLAN_CODE = "planCode";
		/** The plan description. */
		String PLAN_DESCRIPTION = "planDescription";
		/** The plan interval. */
		String PLAN_INTERVAL = "planInterval";
		/** The plan interval count. */
		String PLAN_INTERVAL_COUNT = "planIntervalCount";
		/** The number of trial days of the plan. */
		String PLAN_TRIAL_PERIOD_DAYS = "planTrialPeriodDays";
		/** The currency to use on the plan. */
		String PLAN_CURRENCY = "planCurrency";
		/** The plan value. */
		String PLAN_VALUE = "planValue";
		/** The plan tax. */
		String PLAN_TAX = "planTax";
		/** The plan tax return base. */
		String PLAN_TAX_RETURN_BASE = "planTaxReturnBase";
		/** The plan maximum number of payments. */
		String PLAN_MAX_PAYMENTS = "maxPaymentsAllowed";
		/** The plan attempts delay. */
		String PLAN_ATTEMPTS_DELAY = "paymentAttemptsDelay";

		/** The plan max payments attempts delay. */
		String PLAN_MAX_PAYMENT_ATTEMPTS = "maxPaymentAttempts";
		/** The plan max pending payments. */
		String PLAN_MAX_PENDING_PAYMENTS = "maxPendingPayments";

		/** The plan additional value */
		String PLAN_ADDITIONAL_VALUE = "planAdditionalValue";

		/** The subscription id. */
		String SUBSCRIPTION_ID = "subscriptionId";

		/** The state. */
		String STATE = "state";

		/** The number of trial days. */
		String TRIAL_DAYS = "trialDays";
		/** The immediate payment flag. */
		String IMMEDIATE_PAYMENT = "immediatePayment";
		/** The quantity to purchase. */
		String QUANTITY = "quantity";

		/** If the client accepted the terms and conditions document. */
		String TERMS_AND_CONDITIONS_ACEPTED = "termsAndConditionsAcepted";


		/** The recurring bill id. */
		String RECURRING_BILL_ID = "recurringBillId";
		/** The recurring bill date begin. */
		/* Uses a different parameter name because that's how the API operation was implemented */
		String RECURRING_BILL_DATE_BEGIN = "dateBegin";
		/** The recurring bill date final. */
		/* Uses a different parameter name because that's how the API operation was implemented */
		String RECURRING_BILL_DATE_FINAL = "dateFinal";
		/** The recurring bill state. */
		String RECURRING_BILL_STATE = "state";
		/* Uses a different parameter name because that's how the API operation was implemented */
		String RECURRING_BILL_PAYMENT_METHOD_TYPE = "paymentMethod";

		/** The recurring bill item id. */
		String RECURRING_BILL_ITEM_ID = "recurringBillItemId";
		/** The item value. */
		String ITEM_VALUE = "itemValue";
		/** The tax to apply over the item. */
		String ITEM_TAX = "itemTax";
		/** The item tax return base. */
		String ITEM_TAX_RETURN_BASE = "itemTaxReturnBase";
		/** Whether a transaction must process the cvv2 field */
		String PROCESS_WITHOUT_CVV2 = "processWithoutCvv2";
		/** The recurring bill id. */
		String RECURRING_ITEM_ID = "recurringItemId";

		/** The cookie corresponding with the current web session (Only for
		 * validation purposes) */
		String COOKIE = "cookie";
		/*** MAF device session id */
		String DEVICE_SESSION_ID = "deviceSessionId";
		/*** User agent */
		String USER_AGENT = "userAgent";
		/*** Client(Payer) IP */
		String IP_ADDRESS = "ipAddress";

		/** PSE payer ISO document type, see {@link #com.payu.sdk.model.DocumentType}  */
		String PAYER_DOCUMENT_TYPE = "payerDocumentType";
		/** PSE payer person type (N - Natural or J - Legal), see {@link #com.payu.sdk.model.PersonType} */
		String PAYER_PERSON_TYPE = "payerPersonType";
		/** PSE financial institution code (Bank code) */
		String PSE_FINANCIAL_INSTITUTION_CODE = "pseFinancialInstitutionCode";
		/** PSE financial institution name (Bank Name) */
		String PSE_FINANCIAL_INSTITUTION_NAME = "pseFinancialInstitutionName";

		/** The confirmation page URL */
		String NOTIFY_URL = "notifyUrl";
		/**The response page URL */
		String RESPONSE_URL = "responseUrl";
		
		
		/** The EXTRA 1 transaction extra parameter. */
		String EXTRA1 = "extra1";
		
		/** The EXTRA 2 transaction extra parameter. */
		String EXTRA2 = "extra2";
		
		/** The EXTRA 3 transaction extra parameter. */
		String EXTRA3 = "extra3";
		
		/** Delivery Address Fields */
		
		/** Delivery Address1 field */
		String DELIVERY_ADDRESS_1 = "line1";
		/** Delivery Address2 field */
		String DELIVERY_ADDRESS_2= "line2";
		/** Delivery Address3 field */
		String DELIVERY_ADDRESS_3 = "line3";
		/** Delivery city field */
		String DELIVERY_CITY = "city";
		/** Delivery state field */
		String DELIVERY_STATE = "state";
		/** Delivery country field */
		String DELIVERY_COUNTRY = "country";
		/** Delivery postal code field */
		String DELIVERY_POSTAL_CODE = "postalCode";
		/** Delivery phone field */
		String DELIVERY_PHONE = "phone";
		
		/** Shipping Address Fields */

		/** Shipping Address1 field */
		String SHIPPING_ADDRESS_1 = "shippingAddressLine1";
		/** Shipping Address2 field */
		String SHIPPING_ADDRESS_2= "shippingAddressLine2";
		/** Shipping Address3 field */
		String SHIPPING_ADDRESS_3 = "shippingAddressLine3";
		/** Shipping city field */
		String SHIPPING_CITY = "shippingAddressCity";
		/** Shipping state field */
		String SHIPPING_STATE = "shippingAddressState";
		/** Shipping country field */
		String SHIPPING_COUNTRY = "shippingAddressCountry";
		/** Shipping postal code field */
		String SHIPPING_POSTAL_CODE = "shippingAddressPostalCode";
		/** Shipping phone field */
		String SHIPPING_PHONE = "shippingAddressPhone";
		
		/** The subscription extra charges description */
		String SUBSCRIPTION_EXTRA_CHARGES_DESCRIPTION = "subscriptionExtraChargesDescription";
		
		/** The subscription source reference */
		String SOURCE_REFERENCE = "sourceReference";
		
		/** The subscription ID from POL */
		String SOURCE_ID = "sourceId";
		
		/**
		 * To be used by migrated subscriptions. The buyer IP as 
		 * informed by the origin platform.
		 */
		String SOURCE_BUYER_IP = "sourceBuyerIp";
		
		/**
		 * To be used by migrated subscriptions. The number of payments 
		 * charged on the origin platform at the moment that the subscription 
		 * was migrated.
		 */
		String SOURCE_NUMBER_OF_PAYMENTS = "sourceNumberOfPayments";

		/**
		 * To be used by migrated subscriptions. The number of the next payment
		 * to be charged on the origin platform at the moment that the subscription 
		 * was migrated. 
		 */
		String SOURCE_NEXT_PAYMENT_NUMBER = "sourceNextPaymentNumber";

		/** The creation source field */
		String CREATION_SOURCE = "creationSource";

		/** The Bcash request content type */
		String BCASH_REQUEST_CONTENT_TYPE = "bcashRequestContentType";

		/** The Bcash request content */
		String BCASH_REQUEST_CONTENT = "bcashRequestContent";

		/** The IntegrationMethod */
		String INTEGRATION_METHOD = "integrationMethod";

		/** The platform id field */
		String PLATFORM_ID = "platformId";

		/** The Dinero Mail api subject field */
		String DM_API_SUBJECT = "dmApiSubject";

		/** The Dinero Mail api message field */
		String DM_API_MESSAGE = "dmApiMessage";

		/** The Dinero Mail api unique message id field */
		String DM_API_UNIQUE_MESSAGE_ID = "dmApiUniqueMessageId";

	}
}

/**
 * The MIT License (MIT) Copyright (c) 2016 developers-payu-latam Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without
 * limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so, subject to the following conditions: The above copyright notice and this permission
 * notice shall be included in all copies or substantial portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY
 * KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.payu.sdk.model;

/**
 * Enum representing a response code for a transaction in the PayU SDK.
 *
 * @author PayU Latam
 * @version 1.0.0, 21/08/2013
 * @since 1.0.0
 */
public enum TransactionResponseCode {

	/**
	 * Error transaction code
	 */
	ERROR("0"),
	/**
	 * Approved transaction code
	 */
	APPROVED("1"),
	/**
	 * Transaction declined by the entity code
	 */
	ENTITY_DECLINED("5"),
	/**
	 * Transaction rejected by anti fraud system code
	 */
	ANTIFRAUD_REJECTED("23"),
	/**
	 * Transaction rejected on suspicion of fraud at the financial institution
	 */
	BANK_FRAUD_REJECTED("10011"),
	/**
	 * Transaction review pending code
	 */
	PENDING_TRANSACTION_REVIEW("15"),
	/**
	 * Transaction expired code
	 */
	EXPIRED_TRANSACTION("50"),
	/**
	 * The payment provider had an internal error code
	 */
	INTERNAL_PAYMENT_PROVIDER_ERROR("51"),
	/**
	 * The payment provider is not active code
	 */
	INACTIVE_PAYMENT_PROVIDER("52"),
	/**
	 * The digital certificate could not be found code
	 */
	DIGITAL_CERTIFICATE_NOT_FOUND("9995"),
	/**
	 * Transaction rejected by payment network
	 */
	PAYMENT_NETWORK_REJECTED("4"),
	/**
	 * Invalid data code
	 */
	INVALID_EXPIRATION_DATE_OR_SECURITY_CODE("12"),
	/**
	 * Insufficient funds code
	 */
	INSUFFICIENT_FUNDS("6"),
	/**
	 * Credit card not authorized code
	 */
	CREDIT_CARD_NOT_AUTHORIZED_FOR_INTERNET_TRANSACTIONS("22"),
	/**
	 * Transaction is not valid code
	 */
	INVALID_TRANSACTION("14"),
	/**
	 * Credit card is not valid code
	 */
	INVALID_CARD("7"),
	/**
	 * Credit card expired code
	 */
	EXPIRED_CARD("9"),
	/**
	 * Credit card is restricted code
	 */
	RESTRICTED_CARD("10"),
	/**
	 * Need to contact the entity code
	 */
	CONTACT_THE_ENTITY("8"),
	/**
	 * Need to repeat transaction code
	 */
	REPEAT_TRANSACTION("13"),
	/**
	 * Entity sent an error message code
	 */
	ENTITY_MESSAGING_ERROR("9997"),
	/**
	 * Transaction confirmation is pending code
	 */
	PENDING_TRANSACTION_CONFIRMATION("9994"),
	/**
	 * Bank could not be reached code
	 */
	BANK_UNREACHABLE("53"),
	/**
	 * Amount not valid code
	 */
	EXCEEDED_AMOUNT("17"),
	/**
	 * Transaction not accepted code
	 */
	NOT_ACCEPTED_TRANSACTION("54"),
	/**
	 * Transaction amounts could not be converted code
	 */
	ERROR_CONVERTING_TRANSACTION_AMOUNTS("55"),
	/**
	 * Transaction transmission is pending code
	 */
	PENDING_TRANSACTION_TRANSMISSION("9998"),
	/**
	 * Bad response from the payment network code
	 */
	PAYMENT_NETWORK_BAD_RESPONSE("100"),
	/**
	 * Connection failure with the payment network code
	 */
	PAYMENT_NETWORK_NO_CONNECTION("101"),
	/**
	 * Payment network not sending response code
	 */
	PAYMENT_NETWORK_NO_RESPONSE("102"),

	//Clinic
	/**
	 * Fix was not required code
	 */
	FIX_NOT_REQUIRED("10000"),
	/**
	 * Transaction was automatically fixed  and could make a reversal code
	 */
	AUTOMATICALLY_FIXED_AND_SUCCESS_REVERSAL("10001"),
	/**
	 * Transaction was automatically fixed  and couldn't make a reversal code
	 */
	AUTOMATICALLY_FIXED_AND_UNSUCCESS_REVERSAL("10002"),
	/**
	 * Transaction can't be automatically fixed code
	 */
	AUTOMATIC_FIXED_NOT_SUPPORTED("10003"),
	/**
	 * Transaction was not fixed due to an error state code
	 */
	NOT_FIXED_FOR_ERROR_STATE("10004"),
	/**
	 * Transaction could not be fixed and reversed code
	 */
	ERROR_FIXING_AND_REVERSING("10005"),
	/**
	 * Transaction was not fixed due to incomplete data code
	 */
	ERROR_FIXING_INCOMPLETE_DATA("10006"),
	/**
	 * Awaiting PSE confirmation
	 **/
	PENDING_AWAITING_PSE_CONFIRMATION("28"),
	/**
	 *
	 **/
	INVALID_RESPONSE_PARTIAL_APPROVAL("10007"),

	/**
	 * The transaction was declined by test mode.
	 */
	DECLINED_TEST_MODE_NOT_ALLOWED("40"),
	/**
	 *
	 **/
	ERROR_CONVERTING_DEPOSIT_AMOUNTS("56"),
	/**
	 * The transaction is related to a bank account with an error during its activation process
	 */
	BANK_ACCOUNT_ACTIVATION_ERROR("31"),
	/**
	 * The bank account is not authorized for automatic debit.
	 */
	BANK_ACCOUNT_NOT_AUTHORIZED_FOR_AUTOMATIC_DEBIT("32"),
	/**
	 * The bank account agency value is invalid.
	 */
	INVALID_AGENCY_BANK_ACCOUNT("33"),
	/**
	 * The bank account is invalid.
	 */
	INVALID_BANK_ACCOUNT("34"),
	/**
	 * The bank is invalid.
	 */
	INVALID_BANK("35"),
	/**
	 * The transaction was sent to answer questionnaire.
	 */
	PENDING_ANSWER_MAF_QUESTIONNAIRE("16"),
	/**
	 * The transaction is pending for payment in entity
	 */
	PENDING_PAYMENT_IN_ENTITY("25"),
	/**
	 * The transaction is pending for payment in bank
	 */
	PENDING_PAYMENT_IN_BANK("26"),
	/**
	 * The transaction is pending to sent tot financial entity
	 */
	PENDING_SENT_TO_FINANCIAL_ENTITY("29"),
	/**
	 * The transaction is pending for notifying entity
	 */
	PENDING_NOTIFYING_ENTITY("30"),

	/**
	 * The transaction was cancelled by the payer before being processed
	 */
	CANCELLED_TRANSACTION_PAYER("10008"),
	/**
	 * The transaction was cancelled by the merchant before being processed
	 */
	CANCELLED_TRANSACTION_MERCHANT("10009"),
	/**
	 * The transaction was abandoned by the client at gateway
	 * The transaction was abandoned (Cash)
	 */
	ABANDONED_TRANSACTION("19"),

	/**
	 * The first refund transaction of dispersion was Approved but is necessary to execute a second refund transaction by a clinic
	 * <ol>
	 * 	<li>First transaction = Airline values</li>
	 * 	<li>Second transaction = Agency values</li>
	 * </ol>
	 */
	APPROVED_REFUND_DISPERSION("1"),

	/**
	 * The code for ACH BANCOLOMBIA process
	 */
	INVALID_FIELD_STRUCTURE("10010");

	/**
	 * The transaction response code.
	 */
	private String code;

	/**
	 * Sets the transaction response code.
	 *
	 * @param code the transaction response code.
	 */
	private TransactionResponseCode(String code) {

		this.code = code;
	}

	/**
	 * Returns the transaction response code.
	 *
	 * @return the transaction response code.
	 */
	public String getCode() {

		return code;
	}

}

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
package com.payu.sdk.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.payu.sdk.utils.xml.DateAdapter;
import com.payu.sdk.utils.xml.MapAdditionalInfoAdapter;
import com.payu.sdk.utils.xml.MapExtraParameterAdapter;

/**
 * Represents a response for a transaction in the PayU SDK.
 *
 * @author PayU Latam
 * @since 1.0.0
 * @version 1.0.0, 21/08/2013
 */
@XmlRootElement(name = "transactionResponse")
@XmlAccessorType(XmlAccessType.FIELD)
public class TransactionResponse implements Serializable {

	/** The generated serial version Id */
	private static final long serialVersionUID = 7073372114746122059L;
	/** the related order id. */
	@XmlElement(required = false)
	private Long orderId;
	/** the order's reference code */
	@XmlElement(required = false)
	private String orderReferenceCode;
	/** the related transaction id. */
	@XmlElement(required = false)
	private String transactionId;
	/** The transaction state */
	@XmlElement(required = false)
	private TransactionState state;
	/** The payment network response code. */
	@XmlElement(required = false)
	private String paymentNetworkResponseCode;
	/** The error message related to the payment network response. */
	@XmlElement(required = false)
	private String paymentNetworkResponseErrorMessage;
	/** The trazability code related with the response. */
	@XmlElement(required = false)
	private String trazabilityCode;
	/** The authorization code. */
	@XmlElement(required = false)
	private String authorizationCode;
	/** The transaction pending reason */
	@XmlElement(required = false)
	private TransactionPendingReason pendingReason;
	/** The transaction response code */
	@XmlElement(required = false)
	private TransactionResponseCode responseCode;
	/** The error code. */
	@XmlElement(required = false)
	private TransactionErrorCode errorCode;
	/** The message related to the response code. */
	@XmlElement(required = false)
	private String responseMessage;
	/** The payment date. */
	@XmlElement(required = false)
	private String transactionDate;
	/** The payment time. */
	@XmlElement(required = false)
	private String transactionTime;
	/** The operation date. */
	@XmlElement(required = false)
	@XmlJavaTypeAdapter(DateAdapter.class)
	private Date operationDate;

	/** The extra parameters. */
	@XmlJavaTypeAdapter(MapExtraParameterAdapter.class)
	private Map<String, Object> extraParameters;

	/** The addicional info fields */
        @XmlJavaTypeAdapter(MapAdditionalInfoAdapter.class)
        private Map<String, String> additionalInfo;


	/**
	 * Returns the order id
	 *
	 * @return the orderId
	 */
	public Long getOrderId() {
		return orderId;
	}
	
	/**
	 * Gets the order's reference code
	 * 
	 * @return the reference code from order
	 */
	public String getOrderReferenceCode() {
		return orderReferenceCode;
	}

	/**
	 * Returns the transaction id
	 *
	 * @return the transactionId
	 */
	public String getTransactionId() {
		return transactionId;
	}

	/**
	 * Returns the transaction state
	 *
	 * @return the state
	 */
	public TransactionState getState() {
		return state;
	}

	/**
	 * Returns the payment network response code
	 *
	 * @return the paymentNetworkResponseCode
	 */
	public String getPaymentNetworkResponseCode() {
		return paymentNetworkResponseCode;
	}

	/**
	 * Returns the payment network response error message
	 *
	 * @return the paymentNetworkResponseErrorMessage
	 */
	public String getPaymentNetworkResponseErrorMessage() {
		return paymentNetworkResponseErrorMessage;
	}

	/**
	 * Returns the trazability code
	 *
	 * @return the trazabilityCode
	 */
	public String getTrazabilityCode() {
		return trazabilityCode;
	}

	/**
	 * Returns the authorization code
	 *
	 * @return the authorizationCode
	 */
	public String getAuthorizationCode() {
		return authorizationCode;
	}

	/**
	 * Returns the transaction pending reason
	 *
	 * @return the pendingReason
	 */
	public TransactionPendingReason getPendingReason() {
		return pendingReason;
	}

	/**
	 * Returns the transaction response code
	 *
	 * @return the responseCode
	 */
	public TransactionResponseCode getResponseCode() {
		return responseCode;
	}

	/**
	 * Returns the transaction error code
	 *
	 * @return the errorCode
	 */
	public TransactionErrorCode getErrorCode() {
		return errorCode;
	}

	/**
	 * Returns the response message
	 *
	 * @return the responseMessage
	 */
	public String getResponseMessage() {
		return responseMessage;
	}

	/**
	 * Returns the transaction date
	 *
	 * @return the transactionDate
	 */
	public String getTransactionDate() {
		return transactionDate;
	}

	/**
	 * Returns the transaction time
	 *
	 * @return the transactionTime
	 */
	public String getTransactionTime() {
		return transactionTime;
	}

	/**
	 * Returns the operation date
	 *
	 * @return the operationDate
	 */
	public Date getOperationDate() {
		return operationDate;
	}

	/**
	 * Returns the extra parameters map
	 *
	 * @return the extraParameters
	 */
	public Map<String, Object> getExtraParameters() {
		return extraParameters;
	}

	/**
         * Returns the additional info map
         *
         * @return the additional info
         */
        public Map<String, String> getAdditionalInfo() {

                return additionalInfo;
        }


	/**
	 * Sets the order id
	 *
	 * @param orderId
	 *            the orderId to set
	 */
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	
	/**
	 * Sets the order's reference code
	 * 
	 * @param orderReferenceCode
	 * 
	 */
	public void setOrderReferenceCode(String orderReferenceCode) {
		this.orderReferenceCode = orderReferenceCode;
	}

	/**
	 * Sets the transaction id
	 *
	 * @param transactionId
	 *            the transactionId to set
	 */
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	/**
	 * Sets the transaction state
	 *
	 * @param state
	 *            the state to set
	 */
	public void setState(TransactionState state) {
		this.state = state;
	}

	/**
	 * Sets the payment network response code
	 *
	 * @param paymentNetworkResponseCode
	 *            the paymentNetworkResponseCode to set
	 */
	public void setPaymentNetworkResponseCode(String paymentNetworkResponseCode) {
		this.paymentNetworkResponseCode = paymentNetworkResponseCode;
	}

	/**
	 * Sets the payment network response error message
	 *
	 * @param paymentNetworkResponseErrorMessage
	 *            the paymentNetworkResponseErrorMessage to set
	 */
	public void setPaymentNetworkResponseErrorMessage(
			String paymentNetworkResponseErrorMessage) {
		this.paymentNetworkResponseErrorMessage = paymentNetworkResponseErrorMessage;
	}

	/**
	 * Sets the trazability code
	 *
	 * @param trazabilityCode
	 *            the trazabilityCode to set
	 */
	public void setTrazabilityCode(String trazabilityCode) {
		this.trazabilityCode = trazabilityCode;
	}

	/**
	 * Sets the authorization code
	 *
	 * @param authorizationCode
	 *            the authorizationCode to set
	 */
	public void setAuthorizationCode(String authorizationCode) {
		this.authorizationCode = authorizationCode;
	}

	/**
	 * Sets the transaction pending reason
	 *
	 * @param pendingReason
	 *            the pendingReason to set
	 */
	public void setPendingReason(TransactionPendingReason pendingReason) {
		this.pendingReason = pendingReason;
	}

	/**
	 * Sets the transaction response code
	 *
	 * @param responseCode
	 *            the responseCode to set
	 */
	public void setResponseCode(TransactionResponseCode responseCode) {
		this.responseCode = responseCode;
	}

	/**
	 * Sets the transaction error code
	 *
	 * @param errorCode
	 *            the errorCode to set
	 */
	public void setErrorCode(TransactionErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * Sets the response message
	 *
	 * @param responseMessage
	 *            the responseMessage to set
	 */
	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	/**
	 * Sets the transaction date
	 *
	 * @param transactionDate
	 *            the transactionDate to set
	 */
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	/**
	 * Sets the transaction time
	 *
	 * @param transactionTime
	 *            the transactionTime to set
	 */
	public void setTransactionTime(String transactionTime) {
		this.transactionTime = transactionTime;
	}

	/**
	 * Sets the operation date
	 *
	 * @param operationDate
	 *            the operationDate to set
	 */
	public void setOperationDate(Date operationDate) {
		this.operationDate = operationDate;
	}

	/**
	 * Sets the extra parameters map
	 *
	 * @param extraParameters
	 *            the extraParameters to set
	 */
	public void setExtraParameters(Map<String, Object> extraParameters) {
		this.extraParameters = extraParameters;
	}

	/**
         * Sets the additional info map
         *
         * @param additionalInfo the additional info to set
         */
        public void setAdditionalInfo(Map<String, String> additionalInfo) {

                this.additionalInfo = additionalInfo;
        }
}

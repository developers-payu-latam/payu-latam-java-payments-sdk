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
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.payu.sdk.utils.xml.MapAdditionalValueAdapter;

/**
 * Represents a order in the PayU SDK.
 *
 * @author PayU Latam
 * @since 1.0.0
 * @version 1.0.0, 21/08/2013
 */
@XmlRootElement(name = "order")
@XmlAccessorType(XmlAccessType.FIELD)
public class Order implements Serializable {


	/** The generated serial version Id */
	private static final long serialVersionUID = -8727807302421724650L;
	/** The order's identifier. */
	@XmlElement(required = false)
	private Long id;
	/** The account identifier. */
	@XmlElement(required = false)
	private Integer accountId;
	/** The status of the order. */
	@XmlElement(required = false)
	private OrderStatus status;
	/** The order's cancellation code */
	@XmlElement(required = false)
	private OrderCancellationCode cancellationCode;
	/**
	 * The reference code of the order. This is the identifier that the merchant
	 * assigns to the order.
	 */
	@XmlElement(required = false)
	private String referenceCode;
	/** The description of the order. */
	@XmlElement(required = false)
	private String description;
	/** The airline code related to this order. */
	@XmlElement(required = false)
	private String airlineCode;
	/** The ISO 639-1 language of the order. */
	@XmlElement(required = false)
	private Language language;
	/** The notify URL of the order. */
	@XmlElement(required = false)
	private String notifyUrl;
	/** The signature. */
	@XmlElement(required = false)
	private String signature;

	/** The buyer. */
	@XmlElement(required = false)
	private Buyer buyer;

	/** The list of transactions. */
	@XmlElementWrapper(name = "transactions")
	@XmlElement(name = "transaction")
	private List<Transaction> transactions;

	/** The map of the values. */
	@XmlJavaTypeAdapter(MapAdditionalValueAdapter.class)
	private Map<String, AdditionalValue> additionalValues;

	@XmlElement(name = "shippingAddress", required = false)
	private AddressV4 shippingAddress;

	/**
	 * Returns the order's id
	 *
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Returns the account id
	 *
	 * @return the account id
	 */
	public Integer getAccountId() {
		return accountId;
	}

	/**
	 * Returns the order's status
	 *
	 * @return the status
	 */
	public OrderStatus getStatus() {
		return status;
	}

	/**
	 * Returns the order's cancellation code
	 *
	 * @return the cancellation code
	 */
	public OrderCancellationCode getCancellationCode() {
		return cancellationCode;
	}

	/**
	 * Returns the order's reference code
	 *
	 * @return the reference code
	 */
	public String getReferenceCode() {
		return referenceCode;
	}

	/**
	 * Returns the order's description
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns the order's airline code
	 *
	 * @return the airlineCode
	 */
	public String getAirlineCode() {
		return airlineCode;
	}

	/**
	 * Returns the order's language
	 *
	 * @return the language
	 */
	public Language getLanguage() {
		return language;
	}

	/**
	 * Returns the order's notify url
	 *
	 * @return the notify url
	 */
	public String getNotifyUrl() {
		return notifyUrl;
	}

	/**
	 * Returns the order's signature
	 * @return the signature
	 */
	public String getSignature() {
		return signature;
	}

	/**
	 * Returns the order's buyer
	 *
	 * @return the buyer
	 */
	public Buyer getBuyer() {
		return buyer;
	}

	/**
	 * Returns the order's transactions
	 *
	 * @return the transactions
	 */
	public List<Transaction> getTransactions() {
		return transactions;
	}

	/**
	 * Returns the order's additional values
	 *
	 * @return the additional values
	 */
	public Map<String, AdditionalValue> getAdditionalValues() {
		return additionalValues;
	}

	/**
	 * Returns a specific order's additional value
	 *
	 * @param additionalValueName The name of the additional value to get
	 * @return The additional value it got
	 */
	public AdditionalValue getAdditionalValue(String additionalValueName) {

		if (additionalValues != null) {
			return additionalValues.get(additionalValueName);
		}

		return null;
	}

	/**
	 * Returns the order's shipping address.
	 *
	 * @return the order's shipping address.
	 */
	public AddressV4 getShippingAddress() {
		return shippingAddress;
	}

	/**
	 * Sets the order's id
	 *
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Sets the order's account id
	 *
	 * @param accountId
	 *            the accountId to set
	 */
	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	/**
	 * Sets the order's status
	 *
	 * @param status
	 *            the status to set
	 */
	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	/**
	 * Sets the order's cancellation code
	 *
	 * @param cancellationCode
	 *            the cancellation code to set
	 */
	public void setCancellationCode(OrderCancellationCode cancellationCode) {
		this.cancellationCode = cancellationCode;
	}

	/**
	 * Sets the order's reference code
	 *
	 * @param referenceCode
	 *            the reference code to set
	 */
	public void setReferenceCode(String referenceCode) {
		this.referenceCode = referenceCode;
	}

	/**
	 * Sets the order's description
	 *
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Sets the order's airline code
	 *
	 * @param airlineCode
	 *            the airline code to set
	 */
	public void setAirlineCode(String airlineCode) {
		this.airlineCode = airlineCode;
	}

	/**
	 * Sets the order's language
	 *
	 * @param language
	 *            the language to set
	 */
	public void setLanguage(Language language) {
		this.language = language;
	}

	/**
	 * Sets the order's notify url
	 *
	 * @param notifyUrl
	 *            the notify url to set
	 */
	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	/**
	 * Sets the order's signature
	 *
	 * @param signature
	 *            the signature to set
	 */
	public void setSignature(String signature) {
		this.signature = signature;
	}

	/**
	 * Sets the order's buyer
	 *
	 * @param buyer
	 *            the buyer to set
	 */
	public void setBuyer(Buyer buyer) {
		this.buyer = buyer;
	}

	/**
	 * Sets the order's transactions
	 *
	 * @param transactions
	 *            the transactions to set
	 */
	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	/**
	 * Sets the order's additional values
	 *
	 * @param additionalValues
	 *            the additional values to set
	 */
	public void setAdditionalValues(
			Map<String, AdditionalValue> additionalValues) {
		this.additionalValues = additionalValues;
	}

	/**
	 * Sets the order's shipping address
	 *
	 * @param shippingAddress
	 *            the shipping address to set.
	 */
	public void setShippingAddress(AddressV4 shippingAddress) {
		this.shippingAddress = shippingAddress;
	}

}

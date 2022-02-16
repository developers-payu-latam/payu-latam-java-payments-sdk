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
package com.payu.sdk.paymentplan.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.payu.sdk.constants.Resources;
import com.payu.sdk.constants.Resources.RequestMethod;
import com.payu.sdk.exceptions.PayUException;
import com.payu.sdk.model.Currency;
import com.payu.sdk.model.Order;
import com.payu.sdk.model.PaymentMethodType;
import com.payu.sdk.model.request.Request;
import com.payu.sdk.utils.JaxbUtil;

/**
 * Represents a recurring bill in the PayU SDK.
 *
 * @author PayU Latam
 * @since 1.1.1
 * @version 1.1.1, 07/01/2014
 */
@XmlRootElement(name = "recurringBill")
@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(propOrder = { "id", "orderId", "paymentMethodType", "creditCard", "bankAccount", "planId",
		"planCode", "subscriptionId", "customerId", "state", "amount", "currency", "dateCharge",
		"recurringBillItems" })
public class RecurringBill extends Request {

	/** The class serial version */
	private static final long serialVersionUID = 1L;

	/** The recurring bill identifier */
	private String id;

	/** The {@link Order} identifier */
	private Long orderId;

	/** The {@link PaymentMethodType} used */
	private PaymentMethodType paymentMethodType;

	/** The {@link PaymentPlanCreditCard} used */
	private PaymentPlanCreditCard creditCard;

	/** The {@link BankAccount} used */
	private BankAccount bankAccount;

	/** The identifier of the {@link SubscriptionPlan} to which the recurring bill belongs */
	private String planId;

	/** The code of the {@link SubscriptionPlan} to which the recurring bill belongs */
	private String planCode;

	/** The identifier of the {@link Subscription} to which the recurring bill belongs */
	private String subscriptionId;

	/** The identifier of the {@link Customer} to which the recurring bill belongs */
	private String customerId;

	/** The state of the recurring bill */
	private String state;

	/** The amount to pay in the recurring bill */
	private BigDecimal amount;

	/** The {@link Currency} of the amount to pay in the recurring bill */
	private Currency currency;

	/** The date in which the recurring bill will be charged */
	private Date dateCharge;

	/** The list of {@link RecurringBillItem} associated to the recurring bill */
	private List<RecurringBillItem> recurringBillItems;


	// ------------------------------------------------------
	// GETTERS
	// ------------------------------------------------------

	/**
	 * Gets the recurring bill identifier.
	 *
	 * @return The recurring bill identifier.
	 */
	@XmlElement
	public String getId() {
		return id;
	}

	/**
	 * Gets the {@link Order} identifier.
	 *
	 * @return The {@link Order} identifier.
	 */
	@XmlElement
	public Long getOrderId() {
		return orderId;
	}

	/**
	 * Gets the {@link PaymentMethodType} used.
	 *
	 * @return The {@link PaymentMethodType} used.
	 */
	@XmlElement(name="paymentMethod")
	public PaymentMethodType getPaymentMethodType() {
		return paymentMethodType;
	}

	/**
	 * Gets the {@link PaymentPlanCreditCard} used.
	 *
	 * @return The {@link PaymentPlanCreditCard} used.
	 */
	@XmlElement
	public PaymentPlanCreditCard getCreditCard() {
		return creditCard;
	}

	/**
	 * Gets the {@link BankAccount} used.
	 *
	 * @return The {@link BankAccount} used.
	 */
	@XmlElement
	public BankAccount getBankAccount() {
		return bankAccount;
	}

	/**
	 * Gets the identifier of the {@link SubscriptionPlan} to which the recurring bill belongs.
	 *
	 * @return The identifier of the {@link SubscriptionPlan} to which the recurring bill belongs.
	 */
	@XmlElement
	public String getPlanId() {
		return planId;
	}

	/**
	 * Gets the code of the {@link SubscriptionPlan} to which the recurring bill belongs.
	 *
	 * @return The code of the {@link SubscriptionPlan} to which the recurring bill belongs.
	 */
	@XmlElement
	public String getPlanCode() {
		return planCode;
	}

	/**
	 * Gets the identifier of the {@link Subscription} to which the recurring bill belongs.
	 *
	 * @return The identifier of the {@link Subscription} to which the recurring bill belongs.
	 */
	@XmlElement
	public String getSubscriptionId() {
		return subscriptionId;
	}

	/**
	 * Gets the identifier of the {@link Customer} to which the recurring bill belongs.
	 *
	 * @return The identifier of the {@link Customer} to which the recurring bill belongs.
	 */
	@XmlElement
	public String getCustomerId() {
		return customerId;
	}

	/**
	 * Gets the state of the recurring bill.
	 *
	 * @return The state of the recurring bill.
	 */
	@XmlElement
	public String getState() {
		return state;
	}

	/**
	 * Gets the amount to pay in the recurring bill.
	 *
	 * @return The amount to pay in the recurring bill.
	 */
	@XmlElement
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * Gets the {@link Currency} of the amount to pay in the recurring bill.
	 *
	 * @return The {@link Currency} of the amount to pay in the recurring bill.
	 */
	@XmlElement
	public Currency getCurrency() {
		return currency;
	}

	/**
	 * Gets the date in which the recurring bill will be charged.
	 *
	 * @return The date in which the recurring bill will be charged.
	 */
	@XmlElement
	public Date getDateCharge() {
		return dateCharge;
	}

	/**
	 * Gets the list of {@link RecurringBillItem} associated to the recurring bill.
	 *
	 * @return The list of {@link RecurringBillItem} associated to the recurring bill.
	 */
	@XmlElementWrapper(name = "recurringBillItems")
	@XmlElement(name = "recurringBillItem")
	public List<RecurringBillItem> getRecurringBillItems() {
		return recurringBillItems;
	}


	// ------------------------------------------------------
	// SETTERS
	// ------------------------------------------------------

	/**
	 * Sets the recurring bill identifier.
	 *
	 * @param id the recurring bill identifier.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Sets the {@link Order} identifier.
	 *
	 * @param orderId The {@link Order} identifier.
	 */
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	/**
	 * Sets the {@link PaymentMethodType} used.
	 *
	 * @param paymentMethodType The {@link PaymentMethodType} used.
	 */
	public void setPaymentMethodType(PaymentMethodType paymentMethodType) {
		this.paymentMethodType = paymentMethodType;
	}

	/**
	 * Sets the {@link PaymentPlanCreditCard} used.
	 *
	 * @param creditCard The {@link PaymentPlanCreditCard} used.
	 */
	public void setCreditCard(PaymentPlanCreditCard creditCard) {
		this.creditCard = creditCard;
	}

	/**
	 * Sets the {@link BankAccount} used.
	 *
	 * @param bankAccount The {@link BankAccount} used.
	 */
	public void setBankAccount(BankAccount bankAccount) {
		this.bankAccount = bankAccount;
	}

	/**
	 * Sets the identifier of the {@link SubscriptionPlan} to which the recurring bill belongs.
	 *
	 * @param planId The identifier of the {@link SubscriptionPlan} to which the recurring bill belongs.
	 */
	public void setPlanId(String planId) {
		this.planId = planId;
	}

	/**
	 * Sets the code of the {@link SubscriptionPlan} to which the recurring bill belongs.
	 *
	 * @param planCode The code of the {@link SubscriptionPlan} to which the recurring bill belongs.
	 */
	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}

	/**
	 * Sets the identifier of the {@link Subscription} to which the recurring bill belongs.
	 *
	 * @param subscriptionId The identifier of the {@link Subscription} to which the recurring bill belongs.
	 */
	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	/**
	 * Sets the identifier of the {@link Customer} to which the recurring bill belongs.
	 *
	 * @param customerId The identifier of the {@link Customer} to which the recurring bill belongs.
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	/**
	 * Sets the state of the recurring bill.
	 *
	 * @param state The state of the recurring bill.
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * Sets the amount to pay in the recurring bill.
	 *
	 * @param amount The amount to pay in the recurring bill.
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * Sets the {@link Currency} of the amount to pay in the recurring bill.
	 *
	 * @param currency The {@link Currency} of the amount to pay in the recurring bill.
	 */
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	/**
	 * Sets the date in which the recurring bill will be charged.
	 *
	 * @param dateCharge The date in which the recurring bill will be charged.
	 */
	public void setDateCharge(Date dateCharge) {
		this.dateCharge = dateCharge;
	}

	/**
	 * Sets the list of {@link RecurringBillItem} associated to the recurring bill.
	 *
	 * @param recurringBillItems The list of {@link RecurringBillItem} associated to the recurring bill.
	 */
	public void setRecurringBillItems(List<RecurringBillItem> recurringBillItems) {
		this.recurringBillItems = recurringBillItems;
	}


	/*
	 * (non-Javadoc)
	 * @see com.payu.sdk.model.request.Request#getBaseRequestUrl(
	 * java.lang.String, com.payu.sdk.constants.Resources.RequestMethod)
	 */
	@Override
	protected String getBaseRequestUrl(String baseUrl, RequestMethod requestMethod) {
		return String.format(Resources.PARAM_ENTITY_API_URL_PATTERN,
				baseUrl, Resources.PAYMENT_PLAN_VERSION,
				Resources.URI_RECURRING_BILL, this.id);
	}

	/**
	 * Converts a xml string into a recurring bill response object.
	 *
	 * @param xml The object in a xml format.
	 * @return The recurring bill response format.
	 * @throws PayUException If there is a problem unmarshalling the XML.
	 */
	public static RecurringBill fromXml(String xml) throws PayUException {
		return JaxbUtil.convertXmlToJava(RecurringBill.class, xml);
	}
}

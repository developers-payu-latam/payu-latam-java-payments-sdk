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

/**
 * Enum representing a payment method type in the PayU SDK.
 *
 * @author PayU Latam
 * @since 1.0.0
 * @version 1.0.0, 21/08/2013
 */
public enum PaymentMethodType {

	/**
	 * Payment with credit card.
	 */
	CREDIT_CARD(2, "CREDIT_CARD"),

	/**
	 * Payment using PSE.
	 */
	PSE(4, "PSE"),

	/**
	 * Cash Payment
	 */
	CASH(7, "CASH", Boolean.TRUE),
	
	/**
	 * Cash on Delivery Payment
	 */
	CASH_ON_DELIVERY(11, "CASH_ON_DELIVERY", Boolean.TRUE),
	
	/**
	 * Cash on Delivery Payment
	 */
	LENDING(13, "LENDING", Boolean.FALSE),

	/**
	 * Referenced payment.
	 */
	REFERENCED(8, "REFERENCED"),

	/**
	 * Payment with check account.
	 */
	CHECK_ACCOUNT(1, "CHECK_ACCOUNT"),

	/**
	 * Payment using verified by visa.
	 */
	VERIFIED_BY_VISA(3, "VERIFIED_BY_VISA"),

	/**
	 * Payment using ACH.
	 */
	ACH(5, "ACH", Boolean.TRUE),

	/**
	 * Payment using debit card.
	 */
	DEBIT_CARD(6, "DEBIT_CARD"),

	/**
	 * Payment with Special card.
	 */
	SPECIAL_CARD(9, "SPECIAL_CARD"),
	
	/**
	 * Online referenced payment.
	 */
	BANK_REFERENCED(10, "BANK_REFERENCED", Boolean.TRUE),

	/** PayU global network payment type **/
	PAYU_GLOBAL_PAYMENT(12, "GLOBAL_PAYMENT", Boolean.TRUE),

	/**
	 * Payment with bank transfer.
	 */
	BANK_TRANSFER(14, "BANK_TRANSFER", Boolean.TRUE),
	
	/**
	 * Payment using an external wallet.
	 */
	WALLET(15, "WALLET", Boolean.TRUE),

	/**
	 * BNPL payments
	 */
	BNPL(16, "BNPL", Boolean.TRUE),

	/**
	 * Payment using PSE AVANZA.
	 */
	PSE_AVANZA(17, "PSE_AVANZA"),

	/**
	 * Payment using PROCCESSOR INTEGRATED WALLET.
	 */
	PROCCESSOR_INTEGRATED_WALLET(18, "PROCCESSOR_INTEGRATED_WALLET"),

	/**
	 * Payment using BANCOLOMBIA_BUTTON.
	 */
	BANCOLOMBIA_BUTTON(19, "BANCOLOMBIA_BUTTON"),

	/**
	 * Payment using INTEROPERABLE_QR_BANK_TRANSFER.
	 */
	INTEROPERABLE_QR_BANK_TRANSFER(20, "INTEROPERABLE_QR_BANK_TRANSFER");

	/**
	 * The payment method identifier.
	 */
	private Integer id;

	/** The payment method description. */
	private String description;
	
	/** The antiFraud skipping flag. */
	private Boolean antiFraudSkipping;

	/**
	 * The constructor which receives all the enum data.
	 *
	 * @param id The payment Method type id.
	 * @param description The payment Method type description.
	 */
	private PaymentMethodType(Integer id, String description) {
		this.id = id;
		this.description = description;
	}
	
	/**
	 * The constructor which receives all the enum data.
	 *
	 * @param id The payment Method type id.
	 * @param description The payment Method type description.
	 * @param antiFraudSkipping The antiFraud skipping flag
	 */
	private PaymentMethodType(Integer id, String description, Boolean antiFraudSkipping) {
		this.id = id;
		this.description = description;
		this.antiFraudSkipping = antiFraudSkipping;
	}

	/**
	 * Returns the payment method type identifier.
	 *
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}
	
	/**
	 * Returns the antiFraud skipping flag.
	 *
	 * @return the antiFraud skipping flag
	 */
	public Boolean isAntiFraudSkipping() {

		return antiFraudSkipping;
	}

	/**
	 * Returns the payment method type description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
}

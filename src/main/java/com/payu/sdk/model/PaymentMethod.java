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

import java.util.ArrayList;
import java.util.List;

/**
 * Enum representing a payment method in the PayU SDK.
 *
 *
 * @author PayU Latam
 * @since 1.0.0
 * @version 1.0.0, 21/08/2013
 * @deprecated use {@link #com.payu.sdk.model.PaymentMethodApi} instead.
 */
@Deprecated
public enum PaymentMethod {

	VISA(PaymentMethodType.CREDIT_CARD),

	AMEX(PaymentMethodType.CREDIT_CARD),
	
	HIPERCARD(PaymentMethodType.CREDIT_CARD),

	DINERS(PaymentMethodType.CREDIT_CARD),

	MASTERCARD(PaymentMethodType.CREDIT_CARD),

	DISCOVER(PaymentMethodType.CREDIT_CARD),

	ELO(PaymentMethodType.CREDIT_CARD),

	SHOPPING(PaymentMethodType.CREDIT_CARD),

	NARANJA(PaymentMethodType.CREDIT_CARD),

	CABAL(PaymentMethodType.CREDIT_CARD),

	ARGENCARD(PaymentMethodType.CREDIT_CARD),

	PRESTO(PaymentMethodType.CREDIT_CARD),

	RIPLEY(PaymentMethodType.CREDIT_CARD),

	CODENSA(PaymentMethodType.CREDIT_CARD),

	PSE(PaymentMethodType.PSE),

	BALOTO(PaymentMethodType.CASH),

	EFECTY(PaymentMethodType.CASH),

	BCP(PaymentMethodType.REFERENCED),

	SEVEN_ELEVEN(PaymentMethodType.CASH),

	OXXO(PaymentMethodType.CASH),

	BOLETO_BANCARIO(PaymentMethodType.CASH),

	RAPIPAGO(PaymentMethodType.CASH),

	PAGOFACIL(PaymentMethodType.CASH),

	BAPRO(PaymentMethodType.CASH),

	COBRO_EXPRESS(PaymentMethodType.CASH),

	SERVIPAG(PaymentMethodType.CASH),

	SENCILLITO(PaymentMethodType.CASH),

	BAJIO(PaymentMethodType.REFERENCED),

	BANAMEX(PaymentMethodType.REFERENCED),

	BANCOMER(PaymentMethodType.REFERENCED),

	HSBC(PaymentMethodType.REFERENCED),

	IXE(PaymentMethodType.REFERENCED),

	SANTANDER(PaymentMethodType.REFERENCED),

	SCOTIABANK(PaymentMethodType.REFERENCED),

	BANK_REFERENCED(PaymentMethodType.BANK_REFERENCED);

	/**
	 * Default constructor
	 *
	 * @param type
	 *            the payment method type
	 */
	private PaymentMethod(PaymentMethodType type) {
		this.type = type;
	}

	/**
	 * The type of the payment method.
	 */
	private PaymentMethodType type;

	/**
	 * <p>
	 * Return a PaymentMethodMain Object from the String.<br>
	 * Ex. PaymentMethodMain.fromString("visa") return PaymentMethodMain.VISA
	 * Ex. PaymentMethodMain.fromString("VISA") return PaymentMethodMain.VISA
	 * Ex. PaymentMethodMain.fromString("XX") return null
	 * </p>
	 *
	 * @param paymentMethodMain
	 * @return
	 */
	public static PaymentMethod fromString(String paymentMethod) {
		if (paymentMethod != null) {
			for (PaymentMethod pmm : PaymentMethod.values()) {
				if (paymentMethod.equalsIgnoreCase(pmm.toString())) {
					return pmm;
				}
			}
		}
		return null;
	}

	/**
	 * Filters the payments methods list by type.
	 *
	 * @param type
	 *            , the filter type.
	 * @return the list of the payments method given type.
	 */
	public static List<PaymentMethod> getPaymentMethods(PaymentMethodType type) {
		List<PaymentMethod> paymentsMethod = new ArrayList<PaymentMethod>();
		PaymentMethod[] values = PaymentMethod.values();
		for (int i = 0; i < values.length; i++) {
			if (values[i].getType() == type) {
				paymentsMethod.add(values[i]);
			}
		}
		return paymentsMethod;
	}

	/**
	 * Returns the type of the payment method.
	 *
	 * @return the type of the payment method.
	 */
	public PaymentMethodType getType() {
		return type;
	}

}

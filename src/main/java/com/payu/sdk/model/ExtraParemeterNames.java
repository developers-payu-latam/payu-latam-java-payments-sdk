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
 *
 * Transaction and Order extra parameters names
 *
 * @author PayU Latam
 *
 */
public enum ExtraParemeterNames {

	/**
	 * Installment number.
	 */
	INSTALLMENTS_NUMBER,

	/**
	 * Promotion ID value
	 */
	PROMOTION_ID,

	/**
	 * PSE Reference Number
	 */
	PSE_REFERENCE1,

	/**
	 * PSE Reference Number
	 */
	PSE_REFERENCE2,

	/**
	 * PSE Reference Number
	 */
	PSE_REFERENCE3,

	/**
	 * User Type
	 */
	USER_TYPE,

	/**
	 * Financial Institution Code
	 */
	FINANCIAL_INSTITUTION_CODE,

	/**
	 * Financial Institution Name
	 */
	FINANCIAL_INSTITUTION_NAME,

	/**
	 * Response URL
	 */
	RESPONSE_URL,

	/**
	 * 	Generic extra parameter 1
	 */
	EXTRA1,

	/**
	 * 	Generic extra parameter 2
	 */
	EXTRA2,

	/**
	 * 	Generic extra parameter 3
	 */
	EXTRA3,

	/**
	 * The Dinero Mail api subject field
	 */
	DM_API_SUBJECT,

	/**
	 * The Dinero Mail api message field *
	 */
	DM_API_MESSAGE,

	/**
	 * The Dinero Mail api unique message id field
	 */
	DM_API_UNIQUE_MESSAGE_ID

}

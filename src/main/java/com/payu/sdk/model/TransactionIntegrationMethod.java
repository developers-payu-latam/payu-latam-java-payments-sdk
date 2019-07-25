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

import org.apache.commons.lang3.StringUtils;

/**
 * Enum representing the transaction integration method in the PayU SDK.
 *
 * @author PayU Latam
 * @since 1.1.20
 * @version 1.1.20, 23/12/2016
 */
public enum TransactionIntegrationMethod {

	/**
     * Standard HTML integration method (payments on PayU web site)
     */
    STANDARD_HTML_v4_0,

    /**
     * Post API (restful)
     */
	POST_API_v4_0,

	/**
	 * Post DM API
	 */
	DM_API,

	/**
	 * Post bcash API (restful)
	 */
	BCASH_API_TX;

	/**
	 * Tries to convert the String parameter to a {@link TransactionIntegrationMethod}
	 *
	 * @param integrationMethod The string to convert
	 * @return it's corresponding {@link TransactionIntegrationMethod}
	 */
	public static TransactionIntegrationMethod fromString(final String integrationMethod) {

		if (StringUtils.isNotBlank(integrationMethod)) {
			for (final TransactionIntegrationMethod method : TransactionIntegrationMethod.values()) {

				if (integrationMethod.equalsIgnoreCase(method.toString())) {
					return method;
				}
			}
		}
		return null;
	}	
}

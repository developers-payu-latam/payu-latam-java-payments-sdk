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
package com.payu.sdk.model.request;

/**
 * Represents the environment in the PayU SDK.
 *
 * @author PayU Latam
 * @since 1.0.0
 * @version 1.0.0, 21/08/2013
 */
public enum Environment {

	/**
	 * Default url for payments and reports requests
	 */
	API_URL("https://api2.payulatam.com/payments-api/",
			"https://api2.payulatam.com/reports-api/");

	/** Payments request's url */
	private String paymentsUrl;
	/** Reports request's url */
	private String reportsUrl;

	/**
	 * Private constructor
	 *
	 * @param paymentsUrl
	 *            the payments url to use
	 * @param reportsUrl
	 *            the reports url to use
	 */
	private Environment(String paymentsUrl, String reportsUrl) {
		this.paymentsUrl = paymentsUrl;
		this.reportsUrl = reportsUrl;
	}

	/**
	 * Creates an environment with the given urls
	 *
	 * @param paymentsUrl
	 *            the payments url to use
	 * @param reportsUrl
	 *            the reports url to use
	 * @return The created environment
	 */
	public static Environment createEnvironment(String paymentsUrl,
			String reportsUrl) {
		Environment environment = Environment.API_URL;
		environment.paymentsUrl = paymentsUrl;
		environment.reportsUrl = reportsUrl;

		return environment;
	}

	/**
	 * Returns the payments url
	 *
	 * @return the payments url
	 */
	public String getPaymentsUrl() {
		return paymentsUrl;
	}

	/**
	 * Returns the reports url
	 *
	 * @return the reports Url
	 */
	public String getReportsUrl() {
		return reportsUrl;
	}
}

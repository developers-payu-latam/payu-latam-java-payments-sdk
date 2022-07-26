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
package com.payu.sdk.constants;

/**
 * Holds all SDK constants
 *
 * @author PayU Latam
 * @since 1.0.0
 * @version 1.0.0, 06/09/2013
 */
public interface Constants {

	/**
	 * Default encoding format for the http connection
	 */
	String DEFAULT_ENCODING = "UTF-8";

	/**
	 * Empty string value
	 */
	String EMPTY_STRING = "";

	/**
	 * Space string value
	 */
	String SPACE_STRING = " ";

	/**
	 * Line break string value
	 */
	String LINE_BREAK = "\n";

	/**
	 * The Api request default date format
	 */
	String DEFAULT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

	/**
	 * The Api request secundary date format
	 */
	String SECUNDARY_DATE_FORMAT = "yyyy/MM";

	/**
	 * The Api request default date format without hour
	 */
	String DEFAULT_DATE_WITHOUT_HOUR_FORMAT = "yyyy-MM-dd";

	/**
	 * SSL security provider
	 */
	String SSL_PROVIDER = "TLS";

	/**
	 * The line separator system property
	 */
	String LINE_SEPARATOR = System.getProperty("line.separator");

	/**
	 * The default HTTPS port to use
	 */
	int HTTPS_PORT = 443;

	/**
	 * The plan value variable
	 */
	String PLAN_VALUE = "PLAN_VALUE";

	/**
	 * The plan tax variable
	 */
	String PLAN_TAX = "PLAN_TAX";

	/**
	 * The plan tax return base variable
	 */
	String PLAN_TAX_RETURN_BASE = "PLAN_TAX_RETURN_BASE";

	/**
	 * The item value variable
	 */
	String ITEM_VALUE = "ITEM_VALUE";

	/**
	 * The item tax variable
	 */
	String ITEM_TAX = "ITEM_TAX";

	/**
	 * The item tax return base variable
	 */
	String ITEM_TAX_RETURN_BASE = "ITEM_TAX_RETURN_BASE";

	/**
	 * The plan additional value variable
	 */
	String PLAN_ADDITIONAL_VALUE = "PLAN_ADDITIONAL_VALUE";
	
	/** MD5 Algorithm */
	String ALGORITHM_MD5 = "md5";

	/** SHA1 algorithm */
	String ALGORITHM_SHA = "sha";

	/** SHA-256 algorithm */
	String ALGORITHM_SHA_256 = "sha256";

	/** The production environment prefix URL*/
	String PAYMENTS_PRD_URL = "https://api2.payulatam.com";
	
	/** The STG environment prefix URL*/
	String PAYMENTS_STG_URL = "https://stg.api.payulatam.com";
	
	/** The STG environment prefix URL*/
	String PAYMENTS_SANDBOX_URL = "https://sandbox.api.payulatam";	


}

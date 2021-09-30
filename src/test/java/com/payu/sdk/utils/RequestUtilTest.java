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
package com.payu.sdk.utils;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.payu.sdk.PayU;
import com.payu.sdk.exceptions.InvalidParametersException;
import com.payu.sdk.model.Language;
import com.payu.sdk.model.Transaction;
import com.payu.sdk.model.TransactionSource;
import com.payu.sdk.model.TransactionType;
import com.payu.sdk.model.request.CommandRequest;
import com.payu.sdk.model.request.Request;
import com.payu.sdk.payments.model.MassiveTokenPaymentsRequest;
import com.payu.sdk.payments.model.PaymentRequest;
import com.payu.sdk.reporting.model.ReportingRequest;
import com.payu.sdk.util.builders.MassiveTokenPaymentParametersMother;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * @author raphael.batista
 * @since 1.1.19
 * @date 14/12/2016
 * @version 1.0
 */
public class RequestUtilTest {

	private static final Set<TransactionType> TRANSACTION_TYPES_WITH_ORDER = Collections.unmodifiableSet(EnumSet.of(
			TransactionType.AUTHORIZATION,
			TransactionType.AUTHORIZATION_AND_CAPTURE,
			TransactionType.CAPTURE,
			TransactionType.VOID,
			TransactionType.REFUND,
			TransactionType.PARTIAL_REFUND));

	/**
	 * Reset the static variables for each test
	 */
	@BeforeMethod
	public void before() {
		PayU.merchantId = null;
		PayU.apiKey = null;
		PayU.apiLogin = null;
		PayU.language = null;
	}
	
	/**
	 * Test if the method {@link RequestUtil#buildOrderReportingDetails()} return authentication 
	 * from static variables.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testBuildOrderReportingDetails_returnAuthenticationFromStaticVars() throws Exception {
		String expectedApiKey = "apikey";
		String expectedApiLogin = "apilogin";
		
		PayU.apiKey = expectedApiKey;
		PayU.apiLogin = expectedApiLogin;
		
		Map<String, String> parameters = new HashMap<String, String>();
		ReportingRequest reportingRequest = RequestUtil.buildOrderReportingDetails(parameters);
		
		Assert.assertEquals(reportingRequest.getMerchant().getApiKey(), expectedApiKey);
		Assert.assertEquals(reportingRequest.getMerchant().getApiLogin(), expectedApiLogin);
		
		PayU.apiKey = null;
		PayU.apiLogin = null;
	}
	
	/**
	 * Test if the method {@link RequestUtil#buildOrderReportingDetails()} return authentication
	 * from parameters.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testBuildOrderReportingDetails_returnAuthenticationFromParameters() throws Exception {
		String expectedApiKey = "apikeyParam";
		String expectedApiLogin = "apiloginParam";
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(PayU.PARAMETERS.API_KEY, expectedApiKey);
		parameters.put(PayU.PARAMETERS.API_LOGIN, expectedApiLogin);
		
		ReportingRequest reportingRequest = RequestUtil.buildOrderReportingDetails(parameters);
		
		Assert.assertEquals(reportingRequest.getMerchant().getApiKey(), expectedApiKey);
		Assert.assertEquals(reportingRequest.getMerchant().getApiLogin(), expectedApiLogin);
	}
	
	/**
	 * Test if the method {@link RequestUtil#buildOrderReportingDetails()} return authentication 
	 * from static variables over parameters.
	 *
	 * @throws Exception the exception
	 */
	@Test
	public void testBuildOrderReportingDetails_returnAuthenticationFromStaticVarsOverParams() throws Exception {
		String expectedApiKey = "apikey";
		String expectedApiLogin = "apilogin";
		
		PayU.apiKey = expectedApiKey;
		PayU.apiLogin = expectedApiLogin;
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(PayU.PARAMETERS.API_KEY, expectedApiKey + "Params");
		parameters.put(PayU.PARAMETERS.API_LOGIN, expectedApiLogin + "Params");
		
		ReportingRequest reportingRequest = RequestUtil.buildOrderReportingDetails(parameters);
		
		Assert.assertEquals(reportingRequest.getMerchant().getApiKey(), expectedApiKey);
		Assert.assertEquals(reportingRequest.getMerchant().getApiLogin(), expectedApiLogin);
		
		PayU.apiKey = null;
		PayU.apiLogin = null;
	}

	/**
	 * Test if the method buildMassiveTokenPaymentsRequest return massive token payments
	 * from parameters.
	 *
	 * @throws InvalidParametersException when a parameter is invalid
	 */
	@Test
	public void testBuildMassiveTokenPaymentsRequest_whenParametersAreValid_thenValidMassiveRequestReturns()
			throws InvalidParametersException {

		Map<String, String> parameters = MassiveTokenPaymentParametersMother.getValidMassiveTokenPaymentParameters();

		final MassiveTokenPaymentsRequest request = (MassiveTokenPaymentsRequest) RequestUtil
				.buildMassiveTokenPaymentsRequest(parameters);

		Assert.assertEquals(request.getMerchant().getApiKey(), parameters.get(PayU.PARAMETERS.API_KEY));
		Assert.assertEquals(request.getMerchant().getApiLogin(), parameters.get(PayU.PARAMETERS.API_LOGIN));
		Assert.assertEquals(request.getLanguage(), Language.es);
		Assert.assertEquals(request.getContentFile(), parameters.get(PayU.PARAMETERS.CONTENT_FILE).getBytes(
				StandardCharsets.UTF_8));
	}

	@Test(dataProvider = "transactionSources")
	public void shouldBuildATransactionWithTheCorrectTransactionSource(Map<String, String> parameters,
			TransactionType transactionType, TransactionSource expectedTransactionSource) throws InvalidParametersException {

		Transaction transaction = RequestUtil.buildTransaction(parameters, transactionType);
		Assert.assertEquals(transaction.getSource(), expectedTransactionSource);
	}

	@Test(dataProvider = "languages")
	public void shouldBuildAReportingPingRequestWithTheCorrectLanguage(Language payuLanguage, Map<String, String> parameters,
			Language expectedLanguage) {

		PayU.language = payuLanguage;
		CommandRequest request = RequestUtil.buildReportingPingRequest(parameters);
		Assert.assertEquals(request.getLanguage(), expectedLanguage);
	}

	@Test(dataProvider = "languages")
	public void shouldBuildAnOrderReportingDetailsWithTheCorrectLanguage(Language payuLanguage, Map<String, String> parameters,
			Language expectedLanguage) throws InvalidParametersException {

		PayU.language = payuLanguage;
		CommandRequest request = RequestUtil.buildOrderReportingDetails(parameters);
		Assert.assertEquals(request.getLanguage(), expectedLanguage);
	}

	@Test(dataProvider = "languages")
	public void shouldBuildAnOrderReportingByReferenceCodeWithTheCorrectLanguage(Language payuLanguage,
			Map<String, String> parameters, Language expectedLanguage) {

		PayU.language = payuLanguage;
		CommandRequest request = RequestUtil.buildOrderReportingByReferenceCode(parameters);
		Assert.assertEquals(request.getLanguage(), expectedLanguage);
	}

	@Test(dataProvider = "languages")
	public void shouldBuildATransactionResponseWithTheCorrectLanguage(Language payuLanguage, Map<String, String> parameters,
			Language expectedLanguage) {

		PayU.language = payuLanguage;
		CommandRequest request = RequestUtil.buildTransactionResponse(parameters);
		Assert.assertEquals(request.getLanguage(), expectedLanguage);
	}

	@Test(dataProvider = "languages")
	public void shouldBuildACreateTokenRequestWithTheCorrectLanguage(Language payuLanguage, Map<String, String> parameters,
			Language expectedLanguage) throws InvalidParametersException {

		PayU.language = payuLanguage;
		Request request = RequestUtil.buildCreateTokenRequest(parameters);
		Assert.assertEquals(request.getLanguage(), expectedLanguage);
	}

	@Test(dataProvider = "languages")
	public void shouldBuildAGetCreditCardTokensRequestWithTheCorrectLanguage(Language payuLanguage,
			Map<String, String> parameters, Language expectedLanguage) throws InvalidParametersException {

		PayU.language = payuLanguage;
		Request request = RequestUtil.buildGetCreditCardTokensRequest(parameters);
		Assert.assertEquals(request.getLanguage(), expectedLanguage);
	}

	@Test(dataProvider = "languages")
	public void shouldBuildARemoveTokenRequestWithTheCorrectLanguage(Language payuLanguage, Map<String, String> parameters,
			Language expectedLanguage) {

		PayU.language = payuLanguage;
		Request request = RequestUtil.buildRemoveTokenRequest(parameters);
		Assert.assertEquals(request.getLanguage(), expectedLanguage);
	}

	@Test(dataProvider = "languages")
	public void shouldBuildAPaymentsPingRequestWithTheCorrectLanguage(Language payuLanguage, Map<String, String> parameters,
			Language expectedLanguage) {

		PayU.language = payuLanguage;
		PaymentRequest request = RequestUtil.buildPaymentsPingRequest(parameters);
		Assert.assertEquals(request.getLanguage(), expectedLanguage);
	}

	@Test(dataProvider = "languagesAndTransactionTypes")
	public void shouldBuildAPaymentRequestWithTheCorrectLanguage(Language payuLanguage, Map<String, String> parameters,
			TransactionType transactionType, Language expectedLanguage) throws InvalidParametersException {

		PayU.language = payuLanguage;
		Request request = RequestUtil.buildPaymentRequest(parameters, transactionType);
		Assert.assertEquals(request.getLanguage(), expectedLanguage);
		if (TRANSACTION_TYPES_WITH_ORDER.contains(transactionType)) {
			Assert.assertEquals(((PaymentRequest) request).getTransaction().getOrder().getLanguage(), expectedLanguage);
		}
	}

	@Test(dataProvider = "languages")
	public void shouldBuildAPaymentMethodsListRequestWithTheCorrectLanguage(Language payuLanguage, Map<String, String> parameters,
			Language expectedLanguage) {

		PayU.language = payuLanguage;
		Request request = RequestUtil.buildPaymentMethodsListRequest(parameters);
		Assert.assertEquals(request.getLanguage(), expectedLanguage);
	}

	@DataProvider
	private static Object[][] transactionSources() {

		return new Object[][] { { Collections.emptyMap(), null, TransactionSource.PAYU_SDK },
				{ Collections.singletonMap(PayU.PARAMETERS.TRANSACTION_SOURCE, TransactionSource.PAYU_SDK.name()),
						null,
						TransactionSource.PAYU_SDK },
				{ Collections.singletonMap(PayU.PARAMETERS.TRANSACTION_SOURCE, TransactionSource.OCCS_API.name()),
						null,
						TransactionSource.OCCS_API },
				{ Collections.emptyMap(), TransactionType.AUTHORIZATION, TransactionSource.PAYU_SDK },
				{ Collections.singletonMap(PayU.PARAMETERS.TRANSACTION_SOURCE, TransactionSource.PAYU_SDK.name()),
						TransactionType.AUTHORIZATION,
						TransactionSource.PAYU_SDK },
				{ Collections.singletonMap(PayU.PARAMETERS.TRANSACTION_SOURCE, TransactionSource.OCCS_API.name()),
						TransactionType.AUTHORIZATION,
						TransactionSource.OCCS_API },
				{ Collections.emptyMap(), TransactionType.AUTHORIZATION_AND_CAPTURE, TransactionSource.PAYU_SDK },
				{ Collections.singletonMap(PayU.PARAMETERS.TRANSACTION_SOURCE, TransactionSource.PAYU_SDK.name()),
						TransactionType.AUTHORIZATION_AND_CAPTURE,
						TransactionSource.PAYU_SDK },
				{ Collections.singletonMap(PayU.PARAMETERS.TRANSACTION_SOURCE, TransactionSource.OCCS_API.name()),
						TransactionType.AUTHORIZATION_AND_CAPTURE,
						TransactionSource.OCCS_API },
				{ Collections.emptyMap(), TransactionType.CAPTURE, TransactionSource.PAYU_SDK },
				{ Collections.singletonMap(PayU.PARAMETERS.TRANSACTION_SOURCE, TransactionSource.PAYU_SDK.name()),
						TransactionType.CAPTURE,
						TransactionSource.PAYU_SDK },
				{ Collections.singletonMap(PayU.PARAMETERS.TRANSACTION_SOURCE, TransactionSource.OCCS_API.name()),
						TransactionType.CAPTURE,
						TransactionSource.OCCS_API },
				{ Collections.emptyMap(), TransactionType.CANCELLATION, TransactionSource.PAYU_SDK },
				{ Collections.singletonMap(PayU.PARAMETERS.TRANSACTION_SOURCE, TransactionSource.PAYU_SDK.name()),
						TransactionType.CANCELLATION,
						TransactionSource.PAYU_SDK },
				{ Collections.singletonMap(PayU.PARAMETERS.TRANSACTION_SOURCE, TransactionSource.OCCS_API.name()),
						TransactionType.CANCELLATION,
						TransactionSource.OCCS_API },
				{ Collections.emptyMap(), TransactionType.VOID, TransactionSource.PAYU_SDK },
				{ Collections.singletonMap(PayU.PARAMETERS.TRANSACTION_SOURCE, TransactionSource.PAYU_SDK.name()),
						TransactionType.VOID,
						TransactionSource.PAYU_SDK },
				{ Collections.singletonMap(PayU.PARAMETERS.TRANSACTION_SOURCE, TransactionSource.OCCS_API.name()),
						TransactionType.VOID,
						TransactionSource.OCCS_API },
				{ Collections.emptyMap(), TransactionType.REFUND, TransactionSource.PAYU_SDK },
				{ Collections.singletonMap(PayU.PARAMETERS.TRANSACTION_SOURCE, TransactionSource.PAYU_SDK.name()),
						TransactionType.REFUND,
						TransactionSource.PAYU_SDK },
				{ Collections.singletonMap(PayU.PARAMETERS.TRANSACTION_SOURCE, TransactionSource.OCCS_API.name()),
						TransactionType.REFUND,
						TransactionSource.OCCS_API },
				{ Collections.emptyMap(), TransactionType.CREDIT, TransactionSource.PAYU_SDK },
				{ Collections.singletonMap(PayU.PARAMETERS.TRANSACTION_SOURCE, TransactionSource.PAYU_SDK.name()),
						TransactionType.CREDIT,
						TransactionSource.PAYU_SDK },
				{ Collections.singletonMap(PayU.PARAMETERS.TRANSACTION_SOURCE, TransactionSource.OCCS_API.name()),
						TransactionType.CREDIT,
						TransactionSource.OCCS_API },
				{ Collections.emptyMap(), TransactionType.PARTIAL_REFUND, TransactionSource.PAYU_SDK },
				{ Collections.singletonMap(PayU.PARAMETERS.TRANSACTION_SOURCE, TransactionSource.PAYU_SDK.name()),
						TransactionType.PARTIAL_REFUND,
						TransactionSource.PAYU_SDK },
				{ Collections.singletonMap(PayU.PARAMETERS.TRANSACTION_SOURCE, TransactionSource.OCCS_API.name()),
						TransactionType.PARTIAL_REFUND,
						TransactionSource.OCCS_API } };
	}

	@DataProvider
	private static Object[][] languages() {

		return new Object[][] { { null, Collections.emptyMap(), null },
				{ Language.es, Collections.emptyMap(), Language.es },
				{ Language.en, Collections.emptyMap(), Language.en },
				{ Language.pt, Collections.emptyMap(), Language.pt },
				{ null, Collections.singletonMap(PayU.PARAMETERS.LANGUAGE, Language.es.name()), Language.es },
				{ Language.es, Collections.singletonMap(PayU.PARAMETERS.LANGUAGE, Language.es.name()), Language.es },
				{ Language.en, Collections.singletonMap(PayU.PARAMETERS.LANGUAGE, Language.es.name()), Language.es },
				{ Language.pt, Collections.singletonMap(PayU.PARAMETERS.LANGUAGE, Language.es.name()), Language.es },
				{ null, Collections.singletonMap(PayU.PARAMETERS.LANGUAGE, Language.en.name()), Language.en },
				{ Language.es, Collections.singletonMap(PayU.PARAMETERS.LANGUAGE, Language.en.name()), Language.en },
				{ Language.en, Collections.singletonMap(PayU.PARAMETERS.LANGUAGE, Language.en.name()), Language.en },
				{ Language.pt, Collections.singletonMap(PayU.PARAMETERS.LANGUAGE, Language.en.name()), Language.en },
				{ null, Collections.singletonMap(PayU.PARAMETERS.LANGUAGE, Language.pt.name()), Language.pt },
				{ Language.es, Collections.singletonMap(PayU.PARAMETERS.LANGUAGE, Language.pt.name()), Language.pt },
				{ Language.en, Collections.singletonMap(PayU.PARAMETERS.LANGUAGE, Language.pt.name()), Language.pt },
				{ Language.pt, Collections.singletonMap(PayU.PARAMETERS.LANGUAGE, Language.pt.name()), Language.pt } };
	}

	@DataProvider
	private static Object[][] languagesAndTransactionTypes() {

		return new Object[][] { { null, Collections.emptyMap(), TransactionType.AUTHORIZATION, null },
				{ Language.es, Collections.emptyMap(), TransactionType.AUTHORIZATION_AND_CAPTURE, Language.es },
				{ Language.en, Collections.emptyMap(), TransactionType.CAPTURE, Language.en },
				{ Language.pt, Collections.emptyMap(), TransactionType.CANCELLATION, Language.pt },
				{ null,
						Collections.singletonMap(PayU.PARAMETERS.LANGUAGE, Language.es.name()),
						TransactionType.VOID,
						Language.es },
				{ Language.es,
						Collections.singletonMap(PayU.PARAMETERS.LANGUAGE, Language.es.name()),
						TransactionType.REFUND,
						Language.es },
				{ Language.en,
						Collections.singletonMap(PayU.PARAMETERS.LANGUAGE, Language.es.name()),
						TransactionType.CREDIT,
						Language.es },
				{ Language.pt,
						Collections.singletonMap(PayU.PARAMETERS.LANGUAGE, Language.es.name()),
						TransactionType.PARTIAL_REFUND,
						Language.es },
				{ null, Collections.singletonMap(PayU.PARAMETERS.LANGUAGE, Language.en.name()), null, Language.en },
				{ Language.es,
						Collections.singletonMap(PayU.PARAMETERS.LANGUAGE, Language.en.name()),
						TransactionType.AUTHORIZATION,
						Language.en },
				{ Language.en,
						Collections.singletonMap(PayU.PARAMETERS.LANGUAGE, Language.en.name()),
						TransactionType.AUTHORIZATION_AND_CAPTURE,
						Language.en },
				{ Language.pt,
						Collections.singletonMap(PayU.PARAMETERS.LANGUAGE, Language.en.name()),
						TransactionType.CAPTURE,
						Language.en },
				{ null,
						Collections.singletonMap(PayU.PARAMETERS.LANGUAGE, Language.pt.name()),
						TransactionType.CANCELLATION,
						Language.pt },
				{ Language.es,
						Collections.singletonMap(PayU.PARAMETERS.LANGUAGE, Language.pt.name()),
						TransactionType.VOID,
						Language.pt },
				{ Language.en,
						Collections.singletonMap(PayU.PARAMETERS.LANGUAGE, Language.pt.name()),
						TransactionType.REFUND,
						Language.pt },
				{ Language.pt,
						Collections.singletonMap(PayU.PARAMETERS.LANGUAGE, Language.pt.name()),
						TransactionType.CREDIT,
						Language.pt } };
	}

}

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
package com.payu.sdk.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.ssl.SSLContexts;

import com.payu.sdk.PayU;
import com.payu.sdk.constants.Constants;
import com.payu.sdk.constants.Resources.RequestMethod;
import com.payu.sdk.exceptions.AuthenticationException;
import com.payu.sdk.exceptions.ConnectionException;
import com.payu.sdk.exceptions.PayUException;
import com.payu.sdk.exceptions.SDKException;
import com.payu.sdk.exceptions.SDKException.ErrorCode;
import com.payu.sdk.model.Language;
import com.payu.sdk.model.MediaType;
import com.payu.sdk.model.Merchant;
import com.payu.sdk.model.error.ErrorResponse;
import com.payu.sdk.model.request.CommandRequest;
import com.payu.sdk.model.request.Request;
import com.payu.sdk.utils.JaxbUtil;
import com.payu.sdk.utils.LoggerUtil;
import com.payu.sdk.utils.xml.XmlFormatter;

/**
 * The PayU SDK Helper for Http Client
 *
 * @author PayU Latam
 * @since 1.0.0
 * @version 1.0.0, 21/08/2013
 * @see HttpClient
 */
public final class HttpClientHelper {

	/** The connection timeout in ms. */
	private static final int CONNECTION_TIMEOUT = 10000;

	/** The socket timeout in ms. */
	public static final int SOCKET_TIMEOUT = 85000;

	/**
	 * Default private empty constructor
	 */
	private HttpClientHelper() {
	}

	/**
	 * Sends the post request to the server
	 *
	 * @param request
	 *            The request to be sent to the server
	 * @param requestMethod
	 *            The request method to be sent to the server
	 * @return The response in a xml format
	 * @throws ConnectionException
	 * @throws SDKException
	 */
	public static String sendRequest(Request request, RequestMethod requestMethod)
			throws PayUException, ConnectionException {

		return sendRequest(request, requestMethod, SOCKET_TIMEOUT);
	}
	
	/**
	 * Send request with extra headers.
	 *
	 * @param request the request
	 * @param headers the headers
	 * @param requestMethod the request method
	 * @return the string
	 * @throws PayUException the pay U exception
	 * @throws ConnectionException the connection exception
	 */
	public static String sendRequestWithExtraHeaders(Request request, Map<String, String> headers,
			RequestMethod requestMethod) throws PayUException, ConnectionException {

		return sendRequest(request, headers, requestMethod, SOCKET_TIMEOUT);
	}


	/**
	 * Sends the post request to the server
	 *
	 * @param request
	 *            The request to be sent to the server
	 * @param requestMethod
	 *            The request method to be sent to the server
	 * @param socketTimeOut
	 * 			  The socket time out.
	 * @return The response in a xml format
	 * @throws ConnectionException
	 * @throws SDKException
	 */
	public static String sendRequest(Request request, RequestMethod requestMethod, Integer socketTimeOut)
			throws PayUException, ConnectionException {

		HttpClient httpClient = new DefaultHttpClient();
		setHttpClientParameters(httpClient.getParams(), socketTimeOut);
		httpClient = doModifySSLOptions(request, requestMethod, httpClient);

		try {

			HttpRequestBase httpRequest = createHttpRequest(request, requestMethod);

			HttpResponse httpResponse = httpClient.execute(httpRequest);

			if (httpResponse == null) {
				throw new ConnectionException("No response from server");
			}

			Integer httpStatus = httpResponse.getStatusLine().getStatusCode();

			Integer[] successStatus = { HttpStatus.SC_OK, HttpStatus.SC_CREATED,
					HttpStatus.SC_ACCEPTED };

			if (Arrays.asList(successStatus).contains(httpStatus)) {

				return getXmlResponse(httpResponse);
			}
			else {
				return manageResponse(httpResponse);
			}

		}
		catch (PayUException e) {
			throw e;
		}
		catch (Exception e) {
			throw new ConnectionException(e.getMessage(), e);
		}
		finally {
			httpClient.getConnectionManager().shutdown();
		}
	}
	
	/**
	 * Send request.
	 *
	 * @param request the request
	 * @param headers the headers
	 * @param requestMethod the request method
	 * @param socketTimeOut the socket time out
	 * @return the string
	 * @throws PayUException the pay U exception
	 * @throws ConnectionException the connection exception
	 */
	public static String sendRequest(Request request, Map<String, String> headers,
			RequestMethod requestMethod, Integer socketTimeOut)
			throws PayUException, ConnectionException {

		HttpClient httpClient = new DefaultHttpClient();
		setHttpClientParameters(httpClient.getParams(), socketTimeOut);

		httpClient = doModifySSLOptions(request, requestMethod, httpClient);

		try {

			HttpRequestBase httpRequest = createHttpRequest(request, requestMethod);
			
			addRequestExtraHeaders(httpRequest, headers);

			HttpResponse httpResponse = httpClient.execute(httpRequest);

			if (httpResponse == null) {
				throw new ConnectionException("No response from server");
			}

			Integer httpStatus = httpResponse.getStatusLine().getStatusCode();

			Integer[] successStatus = { HttpStatus.SC_OK, HttpStatus.SC_CREATED,
					HttpStatus.SC_ACCEPTED };

			if (Arrays.asList(successStatus).contains(httpStatus)) {

				return getXmlResponse(httpResponse);
			}
			else {
				return manageResponse(httpResponse);
			}

		}
		catch (PayUException e) {
			throw e;
		}
		catch (Exception e) {
			throw new ConnectionException(e.getMessage(), e);
		}
		finally {
			httpClient.getConnectionManager().shutdown();
		}
	}

	/**
	 * This method validates the url and call the method that disable the SSL options when the case is not
	 * PRD or Sandbox enviroments
	 * @param request
	 * @param requestMethod
	 * @param httpClient
	 * @return httpClient
	 * @throws ConnectionException
	 */
	private static HttpClient doModifySSLOptions(final Request request, final RequestMethod requestMethod, HttpClient httpClient)
			throws ConnectionException {
		String url = request.getRequestUrl(requestMethod);
		if(!url.contains(Constants.PAYMENTS_PRD_URL) && !url.contains(Constants.PAYMENTS_SANDBOX_URL) && 
				!url.contains(Constants.PAYMENTS_STG_URL)){
			httpClient = WebClientDevWrapper.wrapClient(httpClient);
		}else{
			httpClient = buildHttpClient(httpClient);
		}
		return httpClient;
	}

	/**
	 * Manage the response with specific exceptions
	 *
	 * @param httpResponse
	 *            The response sent by the server
	 * @return The reason of the error response
	 * @throws IOException
	 * @throws SDKException
	 */
	private static String manageResponse(HttpResponse httpResponse)
			throws IOException, SDKException {
		int httpStatus = httpResponse.getStatusLine().getStatusCode();
		switch (httpStatus) {
		case HttpStatus.SC_UNAUTHORIZED: {
			throw new AuthenticationException("Invalid credentials");
		}
		case HttpStatus.SC_SERVICE_UNAVAILABLE: {
			throw new ConnectionException(httpResponse.getStatusLine()
					.getReasonPhrase());
		}
		case HttpStatus.SC_NOT_FOUND: {
			String error;
			try{
				error = getErrorMessage(httpResponse);
			}catch(Exception exception){
				throw new ConnectionException(httpResponse.getStatusLine().toString());
			}
			throw new PayUException(ErrorCode.NO_RESULTS_FOUND, error);
		}
		case HttpStatus.SC_UNPROCESSABLE_ENTITY: {
			String error = getErrorMessage(httpResponse);
			throw new PayUException(ErrorCode.INVALID_PARAMETERS, error);
		}
		default: {
			String error = getErrorMessage(httpResponse);
			throw new ConnectionException(error);
		}
		}
	}

	/**
	 * Gets the response error message
	 *
	 * @param httpResponse
	 *            The response sent by the server
	 * @return The message associated to the error response
	 * @throws PayUException
	 * @throws IOException
	 */
	private static String getErrorMessage(HttpResponse httpResponse)
			throws PayUException, IOException, ConnectionException {

		String error = httpResponse.getStatusLine().getReasonPhrase();
		try{
			String xml = getXmlResponse(httpResponse);

			if (xml != null && !xml.isEmpty()) {

				ErrorResponse response;

				try {

					response = JaxbUtil.convertXmlToJava(ErrorResponse.class, xml);

				} catch (PayUException ex) {
					LoggerUtil.debug("Invalid XML entity");
					return error;
				}

				if (response.getDescription() != null) {
					return response.getDescription();
				}

				if (response.getErrorList() != null
						&& response.getErrorList().length > 0) {
					return Arrays.toString(response.getErrorList());
				}
			}
		} catch(Exception exception){
			throw new ConnectionException(httpResponse.getStatusLine().toString());
		}

		return error;
	}

	/**
	 * Get xml response
	 *
	 * @param httpResponse
	 *            The response sent by the server
	 * @return The xml associated to the response
	 * @throws PayUException
	 * @throws IOException
	 */
	private static String getXmlResponse(HttpResponse httpResponse)
			throws PayUException, IOException {

		HttpEntity entity = httpResponse.getEntity();
		InputStream inputStream = (InputStream) entity.getContent();
		String xml = inputStreamToString(inputStream);

		if (!xml.isEmpty()) {
			LoggerUtil.debug("Response:\n {0}", XmlFormatter.prettyFormat(xml));
		}

		return xml;
	}

	/**
	 * Sets the parameters to the request
	 *
	 * @param params
	 *            The parameters to be set
	 * @param socketTimeOut socket time out.
	 */
	private static void setHttpClientParameters(HttpParams params, Integer socketTimeOut) {

		HttpProtocolParams
				.setContentCharset(params, Constants.DEFAULT_ENCODING);
		HttpConnectionParams.setConnectionTimeout(params, CONNECTION_TIMEOUT);
		HttpConnectionParams.setSoTimeout(params, socketTimeOut);
	}

	/**
	 * Creates a http request with the given request
	 *
	 * @param request
	 *            The original request
	 * @param requestMethod
	 *            The request method to be sent to the server
	 * @return The created http request
	 * @throws URISyntaxException
	 * @throws UnsupportedEncodingException
	 * @throws PayUException
	 * @throws ConnectionException
	 */
	private static HttpRequestBase createHttpRequest(Request request,
			RequestMethod requestMethod) throws URISyntaxException,
			UnsupportedEncodingException, PayUException, ConnectionException {

		String url = request.getRequestUrl(requestMethod);

		URI postUrl = new URI(url);

		HttpRequestBase httpMethod;

		LoggerUtil.debug("sending request...");

		String xml = Constants.EMPTY_STRING;

		switch (requestMethod) {
		case POST:
			httpMethod = new HttpPost();
			break;
		case GET:
			httpMethod = new HttpGet();
			break;
		case DELETE:
			httpMethod = new HttpDelete();
			break;
		case PUT:
			httpMethod = new HttpPut();
			break;
		default:
			throw new ConnectionException("Invalid connection method");
		}

		httpMethod.addHeader("Content-Type", MediaType.XML.getCode()
				+ "; charset=utf-8");

		Language lng = request.getLanguage() != null ? request.getLanguage() : PayU.language;
		httpMethod.setHeader("Accept-Language", lng.name());

		// Sets the method entity
		if (httpMethod instanceof HttpEntityEnclosingRequestBase) {
			xml = request.toXml();
			((HttpEntityEnclosingRequestBase) httpMethod)
					.setEntity(new StringEntity(xml, Constants.DEFAULT_ENCODING));
			LoggerUtil.debug("Message to send:\n {0}", xml);
		}

		httpMethod.setURI(postUrl);

		addRequestHeaders(request, httpMethod);

		LoggerUtil.debug("URL to send:\n {0}", url);

		return httpMethod;
	}

	/**
	 * Updates the base request to add the header
	 *
	 * @param requestBase
	 *            The request that needs its header
	 */
	private static void addRequestHeaders(Request apiRequest, HttpRequestBase requestBase) {

		requestBase.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.XML.getCode()
				+ "; charset=utf-8");
		requestBase.addHeader(HttpHeaders.ACCEPT, MediaType.XML.getCode());

		String username = getUserName(apiRequest);

		String password = getPassword(apiRequest);

		Credentials credentials = new UsernamePasswordCredentials(username, password);

		requestBase.addHeader(BasicScheme.authenticate(credentials,
				Constants.DEFAULT_ENCODING, false));
	}
	
	/**
	 * Adds the request extra headers.
	 *
	 * @param httpRequest the http request
	 * @param headers the headers
	 * @throws ConnectionException 
	 */
	private static void addRequestExtraHeaders(HttpRequestBase httpRequest,
			Map<String, String> headers) throws ConnectionException {

		try {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				httpRequest.setHeader(entry.getKey(), entry.getValue());
			}
		}
		catch (Exception e) {
			throw new ConnectionException(
					"Exception caught when adding the extra headers to be sent", e);
		}
	}

	/**
	 * Gets the user name.
	 * 
	 * @param apiRequest
	 * @return
	 */
	private static String getUserName(Request apiRequest) {

		Merchant merchant = null;

		if (apiRequest instanceof CommandRequest) {
			merchant = ((CommandRequest) apiRequest).getMerchant();
		}

		return apiRequest.getApiLogin() != null ? apiRequest.getApiLogin()
				: merchant != null && merchant.getApiLogin() != null ? merchant
						.getApiLogin() : PayU.apiLogin;
	}

	/**
	 * Gets the password.
	 * 
	 * @param apiRequest
	 * @return
	 */
	private static String getPassword(Request apiRequest) {
		
		Merchant merchant = null;

		if (apiRequest instanceof CommandRequest) {
			merchant = ((CommandRequest) apiRequest).getMerchant();
		}

		return apiRequest.getApiKey() != null ? apiRequest.getApiKey()
				: merchant != null && merchant.getApiKey() != null ? merchant
						.getApiKey() : PayU.apiKey;
	}

	/**
	 * Transforms an incoming InputStream into a String
	 *
	 * @param inputStream
	 *            The stream to be transformed
	 * @return The transformed stream
	 * @throws IOException
	 * @throws PayUException
	 */
	private static String inputStreamToString(InputStream inputStream)
			throws IOException, PayUException {

		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream, Constants.DEFAULT_ENCODING));
		StringBuilder stringBuilder = new StringBuilder();

		String line = null;
		try {
			while ((line = bufferedReader.readLine()) != null) {
				stringBuilder.append(line).append(Constants.LINE_SEPARATOR);
			}
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				throw new PayUException(
						PayUException.ErrorCode.XML_DESERIALIZATION_ERROR, e);
			}
		}

		return stringBuilder.toString();
	}

	/**
	 * Builds a HttpClient
	 * @param base The http client to wrap
	 * @return a {@link HttpClient}
	 */
	private static HttpClient buildHttpClient(final HttpClient base) {

		final SSLSocketFactory ssf = new SSLSocketFactory(SSLContexts.createDefault(),
				new String[] { "TLSv1", "TLSv1.1", "TLSv1.2" }, null, null);
		base.getConnectionManager()
				.getSchemeRegistry().register(new Scheme("https", Constants.HTTPS_PORT, ssf));

		return new DefaultHttpClient(base.getConnectionManager(), base.getParams());

	}
}

package com.payu.sdk.util.builders;

import java.util.HashMap;
import java.util.Map;

import com.payu.sdk.PayU;
import com.payu.sdk.model.Language;

/**
 * Class to instantiate massive token payment parameters for tests
 *
 * @author Alejandro Cadena (alejandro.cadena@payu.com)
 * @version 1.3.9
 * @since 29/09/21
 */
public final class MassiveTokenPaymentParametersMother {

	private MassiveTokenPaymentParametersMother() { }

	public static Map<String, String> getValidMassiveTokenPaymentParameters() {

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(PayU.PARAMETERS.API_KEY, PayU.apiKey);
		parameters.put(PayU.PARAMETERS.API_LOGIN, PayU.apiLogin);
		parameters.put(PayU.PARAMETERS.LANGUAGE, Language.es.name());
		parameters.put(PayU.PARAMETERS.CONTENT_FILE, ContentFileMother.getValidContentFile());
		return parameters;
	}

	public static Map<String, String> getInvalidMassiveTokenPaymentParameters() {

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(PayU.PARAMETERS.API_KEY, PayU.apiKey);
		parameters.put(PayU.PARAMETERS.API_LOGIN, PayU.apiLogin);
		return parameters;
	}

	public static Map<String, String> buildMassiveTokenPaymentParameters(final String contentFile) {

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(PayU.PARAMETERS.API_KEY, PayU.apiKey);
		parameters.put(PayU.PARAMETERS.API_LOGIN, PayU.apiLogin);
		parameters.put(PayU.PARAMETERS.LANGUAGE, Language.es.name());
		parameters.put(PayU.PARAMETERS.CONTENT_FILE, contentFile);
		return parameters;
	}

}

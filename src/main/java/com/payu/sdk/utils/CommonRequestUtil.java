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

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.payu.sdk.constants.Constants;
import com.payu.sdk.exceptions.InvalidParametersException;
import com.payu.sdk.model.AdditionalValue;
import com.payu.sdk.model.Currency;
import com.payu.sdk.model.PaymentMethod;

/**
 * Utility for post requests in the PayU SDK.
 *
 * @author PayU Latam
 * @since 1.0.0
 * @version 1.0.0, 11/09/2013
 */
@SuppressWarnings("deprecation")
public class CommonRequestUtil {

	/**
	 * Private Constructor
	 */
	protected CommonRequestUtil() {
	}

	/* Validation Methods */

	/**
	 * Validates the parameters given, against the required ones.
	 *
	 * @param parameters
	 *            The parameters that need to be validated. If the parameters
	 *            are null or empty, exception is thrown.
	 * @param required
	 *            The parameters that are required by the application to work.
	 * @throws InvalidParametersException
	 *             Thrown if given parameters are not complete.
	 */
	public static void validateParameters(Map<String, String> parameters,
			String... required) throws InvalidParametersException {

		StringBuilder errorMessage = new StringBuilder();
		boolean isError = false;

		if (parameters == null || parameters.isEmpty()) {
			throw new InvalidParametersException(
					"Parameters can not be null or empty.");
		} else {
			for (String r : required) {
				if (!parameters.keySet().contains(r)
						|| parameters.get(r) == null
						|| parameters.get(r).trim().isEmpty()) {

					errorMessage.append("Parameter [").append(r)
							.append("] is required.")
							.append(Constants.LINE_SEPARATOR);
					isError = true;
				}

			}
		}

		if (isError) {
			throw new InvalidParametersException(errorMessage.toString());
		}

	}

	/**
	 * Validates the given parameters against the not allowed
	 *
	 * @param parameters
	 *            The parameters that need to be validated. If the parameters
	 *            are null or empty, exception is thrown.
	 * @param notAllowed
	 *            The parameters that aren't allowed by the application.
	 * @throws InvalidParametersException
	 *             Thrown if given parameters don't comply with not allowed ones.
	 */
	public static void validateNotAllowedParameters(Set<String> parameters,
			String... notAllowed) throws InvalidParametersException {

		StringBuilder errorMessage = new StringBuilder();
		boolean isError = false;

		if (parameters == null || parameters.isEmpty()) {
			throw new InvalidParametersException(
					"Parameters can not be null or empty.");
		} else {
			for (String r : notAllowed) {
				if (parameters.contains(r)) {
					errorMessage.append("Parameter [").append(r)
							.append("] is not allowed.")
							.append(Constants.LINE_SEPARATOR);
					isError = true;
				}

			}
		}

		if (isError) {
			throw new InvalidParametersException(errorMessage.toString());
		}

	}

	/**
	 * Validates a date string whit the payments date format
	 *
	 * @param dateParameter
	 *            the parameter to validate
	 * @param name
	 *            the name of the parameter (log purposes)
	 * @return The date it got
	 * @throws InvalidParametersException
	 */
	protected static Date validateDateParameter(String dateParameter,
			String parameterName, String dateFormat)
			throws InvalidParametersException {

		return validateDateParameter(dateParameter, parameterName, dateFormat, true);
	}

	/**
	 * Validates a date string whit the payments date format.
	 * Evaluates the format of permissive form or not as indicated in the parameter
	 *
	 * @param dateParameter the parameter to validate
	 * @param parameterName the name of the parameter (log purposes)
	 * @param dateFormat the date format
	 * @param permissive indicates whether the format validation is permissive
	 * @return The date it got
	 * @throws InvalidParametersException
	 */
	protected static Date validateDateParameter(String dateParameter,
			String parameterName, String dateFormat, boolean permissive)
			throws InvalidParametersException {

		Date date = null;

		if (dateParameter != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			sdf.setLenient(permissive);
			try {
				date = sdf.parse(dateParameter);
			} catch (ParseException e) {
				throw new InvalidParametersException(String.format(
						"The [%s] format is invalid. Use [%s] ", parameterName,
						dateFormat), e);
			}
		}

		return date;
	}

	/* Get Values Methods */

	/**
	 * Gets a big decimal value from parameters
	 *
	 * @param value
	 *            the value to convert to big decimal
	 * @param name
	 *            the name of the parameter (exception purposes)
	 * @return the big decimal it got
	 * @throws InvalidParametersException
	 */
	private static BigDecimal getBigDecimal(String value, String name)
			throws InvalidParametersException {
		BigDecimal bigDecimalValue = null;
		try {
			bigDecimalValue = new BigDecimal(value);
		} catch (Exception e) {
			throw new InvalidParametersException(String.format(
					"The parameter [%s] isn't a valid BigDecimal value", name),
					e);
		}

		return bigDecimalValue;
	}
	
	/**
	 * Gets a long value from parameters
	 *
	 * @param value
	 *            the value to convert to long
	 * @param name
	 *            the name of the parameter (exception purposes)
	 * @return the long it got
	 * @throws InvalidParametersException
	 */
	private static Long getLong(String value, String name)
			throws InvalidParametersException {
		Long longValue = null;
		try {
			longValue = Long.valueOf(value);
		} catch (Exception e) {
			throw new InvalidParametersException(String.format(
					"The parameter [%s] isn't a valid Long value", name),
					e);
		}

		return longValue;
	}

	/**
	 * Gets a parameter based on it's name
	 *
	 * @param enumClass
	 *            The class that defines the parameters
	 * @param value
	 *            the value to get form the class
	 * @return The value it got
	 * @throws InvalidParametersException
	 */
	private static <E extends Enum<E>> E getEnumValue(Class<E> enumClass,
			String value) throws InvalidParametersException {
		E enumValue = null;
		try {
			enumValue = Enum.valueOf(enumClass, value);
		} catch (Exception e) {
			throw new InvalidParametersException(String.format(
					"The parameter [%s] isn't a valid [%s] value", value,
					enumClass.getSimpleName()), e);
		}

		return enumValue;
	}

	/**
	 * Gets a integer value from the parameters
	 *
	 * @param value
	 *            the value to convert to integer
	 * @param name
	 *            the name of the parameter (exception purposes)
	 * @return the integer value it got
	 * @throws InvalidParametersException
	 */
	private static Integer getInteger(String value, String name)
			throws InvalidParametersException {
		Integer integerValue = null;
		try {
			integerValue = Integer.parseInt(value);
		} catch (Exception e) {
			throw new InvalidParametersException(String.format(
					"The parameter [%s] isn't a valid integer value", name), e);
		}

		return integerValue;
	}

	/**
	 * Gets a boolean value from the parameters
	 *
	 * @param value
	 *            the value to convert to Boolean
	 * @param name
	 *            the name of the parameter (exception purposes)
	 * @return the Boolean value it got
	 * @throws InvalidParametersException
	 */
	private static Boolean getBoolean(String value, String name)
			throws InvalidParametersException {
		Boolean booleanValue = null;
		try {
			booleanValue = Boolean.valueOf(value);
		} catch (Exception e) {
			throw new InvalidParametersException(String.format(
					"The parameter [%s] isn't a valid boolean value", name), e);
		}

		return booleanValue;
	}

	/**
	 * Gets a byte array value from the parameters
	 *
	 * @param value the value to convert to byte array
	 * @param name the name of the parameter (exception purposes)
	 * @return the byte array value it got
	 * @throws InvalidParametersException
	 */
	public static byte[] getByteArray(String value, String name)
			throws InvalidParametersException {

		try {
			return value.getBytes(StandardCharsets.UTF_8);
		} catch (Exception e) {
			throw new InvalidParametersException(String.format(
					"The parameter [%s] isn't a valid byte array", name), e);
		}
	}

	/**
	 * Get a parameter from the parameters map
	 *
	 * @param parameters
	 *            The parameters to be sent to the server
	 * @param paramName
	 *            the parameter to get
	 * @return The parameter it got
	 */
	public static String getParameter(Map<String, String> parameters,
			String paramName) {
		String parameter = parameters.get(paramName);

		if(parameter != null){
			parameter = parameter.trim();
			if(parameter.isEmpty()){
				parameter = null;
			}
		}

		return parameter;
	}

	/**
	 * Gets an integer parameter from the parameters map
	 *
	 * @param parameters
	 *            The parameters to be sent to the server
	 * @param paramName
	 *            the parameter to get
	 * @return The integer parameter it got
	 * @throws InvalidParametersException
	 */
	protected static Integer getIntegerParameter(
			Map<String, String> parameters, String paramName)
			throws InvalidParametersException {
		String parameter = getParameter(parameters, paramName);
		if (parameter != null && parameter.trim().isEmpty()) {
			parameter = null;
		}
		Integer integerParameter = (parameter != null ? getInteger(parameter,
				paramName) : null);

		return integerParameter;
	}

	/**
	 * Gets a BigDecimal parameter from the parameters map
	 *
	 * @param parameters
	 *            The parameters to be sent to the server
	 * @param paramName
	 *            the parameter to get
	 * @return The BigDecimal parameter it got
	 * @throws InvalidParametersException
	 */
	protected static BigDecimal getBigDecimalParameter(
			Map<String, String> parameters, String paramName)
			throws InvalidParametersException {
		String parameter = getParameter(parameters, paramName);
		if (parameter != null && parameter.trim().isEmpty()) {
			parameter = null;
		}
		BigDecimal bigDecimalParameter = (parameter != null ? getBigDecimal(
				parameter, paramName) : null);

		return bigDecimalParameter;
	}
	
	/**
	 * Gets a long parameter from the parameters map
	 *
	 * @param parameters
	 *            The parameters to be sent to the server
	 * @param paramName
	 *            the parameter to get
	 * @return The long parameter it got
	 * @throws InvalidParametersException
	 */
	protected static Long getLongParameter(
			Map<String, String> parameters, String paramName)
			throws InvalidParametersException {
		String parameter = getParameter(parameters, paramName);
		if (parameter != null && parameter.trim().isEmpty()) {
			parameter = null;
		}
		Long longParameter = (parameter != null ? getLong(parameter,
				paramName) : null);

		return longParameter;
	}

	/**
	 * Gets an Enum value parameter from the parameters map
	 *
	 * @param enumClass The enum class
	 * @param parameters
	 *            The parameters to be sent to the server
	 * @param paramName
	 *            the parameter to get
	 * @return The Enum value parameter it got
	 * @throws InvalidParametersException
	 */
	public static <E extends Enum<E>> E getEnumValueParameter(
			Class<E> enumClass, Map<String, String> parameters, String paramName)
			throws InvalidParametersException {
		String parameter = getParameter(parameters, paramName);

		E enumValueParameter = (parameter != null ? getEnumValue(enumClass,
				parameter) : null);

		return enumValueParameter;
	}

	/**
	 * Gets the PaymentMethod value parameter from the parameters map.
	 * If the parameter is null or does not belong to the enumeration returns null
	 * @param parameters The parameters to be sent to the server
	 * @param paramName  the parameter to get
	 * @return the PaymentMethod value or null
	 * @deprecated Not for public use in the future because the class PaymentMethod is deprecated
	 * @see #com.payu.sdk.PayUPayments.getPaymentMethodParameter(Map<String, String> parameters, String paramName)
	 */
	@Deprecated
	public static PaymentMethod getPaymentMethodParameter(Map<String, String> parameters, String paramName){
		PaymentMethod paymentMethod = null;
		String parameter = getParameter(parameters, paramName);
		if (parameter != null){
			paymentMethod = PaymentMethod.fromString(parameter);
		}
		return paymentMethod;
	}

	/**
	 * Gets a Boolean parameter from the parameters map
	 *
	 * @param parameters
	 *            The parameters to be sent to the server
	 * @param paramName
	 *            the parameter to get
	 * @return The Boolean parameter it got
	 * @throws InvalidParametersException
	 */
	protected static Boolean getBooleanParameter(
			Map<String, String> parameters, String paramName)
			throws InvalidParametersException {
		String parameter = getParameter(parameters, paramName);
		if (parameter != null && parameter.trim().isEmpty()) {
			parameter = null;
		}
		Boolean booleanParameter = (parameter != null ? getBoolean(parameter,
				paramName) : null);

		return booleanParameter;
	}

	/**
	 * Gets an integer parameter from the parameters map
	 *
	 * @param parameters The parameters to be sent to the server
	 * @param paramName the parameter to get
	 * @return The integer parameter it got
	 * @throws InvalidParametersException
	 */
	public static byte[] getByteArrayParameter(
			Map<String, String> parameters, String paramName)
			throws InvalidParametersException {

		String parameter = getParameter(parameters, paramName);
		if (parameter != null && parameter.trim().isEmpty()) {
			return null;
		}
		return getByteArray(parameter, paramName);
	}

	/* Build Methods */

	/**
	 * Build the transaction additional values (currency and value)
	 *
	 * @param txCurrency
	 *            The currency of the transaction
	 * @param txValue
	 *            The value of the transaction
	 * @param taxValue
	 *            The tax value associated with the payment
	 * @param taxReturnBase
	 *            The tax(IVA) return base associated with the payment
	 * @return The created map of additional values
	 */
	protected static Map<String, AdditionalValue> buildAdditionalValues(
			Currency txCurrency, BigDecimal txValue, BigDecimal taxValue,
			BigDecimal taxReturnBase) {

		if (txCurrency == null) {
			return null;
		}

		Map<String, AdditionalValue> values = new HashMap<String, AdditionalValue>();

		addAdditionalValue(txCurrency, "TX_VALUE", txValue, values);
		addAdditionalValue(txCurrency, "TX_TAX", taxValue, values);
		addAdditionalValue(txCurrency, "TX_TAX_RETURN_BASE", taxReturnBase,
				values);

		if (!values.isEmpty()) {
			return values;
		}

		return null;
	}

	/**
	 * Adds an additional value
	 *
	 * @param txCurrency
	 *            The currency of the transaction
	 * @param name
	 *            The name of the new value
	 * @param value
	 *            The value of the transaction
	 * @param additionalValues
	 *            The additional values map where the new value will go
	 */
	private static void addAdditionalValue(Currency txCurrency, String name,
			BigDecimal value, Map<String, AdditionalValue> additionalValues) {

		if (value != null) {
			AdditionalValue additionalValue = new AdditionalValue();
			additionalValue.setCurrency(txCurrency);
			additionalValue.setValue(value);
			additionalValue.setName(name);

			additionalValues.put(name, additionalValue);
		}
	}

}

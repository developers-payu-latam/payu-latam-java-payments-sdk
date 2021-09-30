package com.payu.sdk.util.builders;

/**
 * Class to instantiate content files for tests
 *
 * @author Alejandro Cadena (alejandro.cadena@payu.com)
 * @version 1.3.9
 * @since 29/09/21
 */
public final class ContentFileMother {

	private ContentFileMother() {}

	public static String getValidContentFile() {

		return "NTEyMzIxLDZhZTVjMmJkLTMyZmUtNDA5ZC1hNWMxLWRjMThmZDBjNjU0Myw3NzcsMSxUU19jb2Jy"
				+ "b3NfbWFzaXZvc19jb25fdG9rZW5fY29uY3Z2XzEwMjcxLFBydWViYSBUb2tlbiBNYXNpdm9zIFRT"
				+ "LCxDT1AsNTAwMDAsMCwwLDAsRVM=";
	}

	public static String getInvalidContentFile() {

		return "";
	}
}

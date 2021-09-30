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
package com.payu.sdk.payments.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.payu.sdk.model.request.CommandRequest;


/**
 * Represents a massive token payments request in the PayU SDK.
 *
 * @author Alejandro Cadena (alejandro.cadena@payu.com)
 * @version 1.3.9
 * @since 29/09/21
 */
@XmlRootElement(name = "request")
@XmlAccessorType(XmlAccessType.FIELD)
public class MassiveTokenPaymentsRequest extends CommandRequest {

	/** The generated serial version Id */
	private static final long serialVersionUID = -7744394539231049957L;

	/** The content file with the massive token payments */
	@XmlElement
	private byte[] contentFile;


	/**
	 * Returns the content file
	 *
	 * @return the content file
	 */
	public byte[] getContentFile() {

		return contentFile;
	}

	/**
	 * Sets the content file
	 *
	 * @param contentFile the content file
	 */
	public void setContentFile(byte[] contentFile) {

		this.contentFile = contentFile;
	}

}

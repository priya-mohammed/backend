
package com.spantag.socialMediaAppln.exceptions;

public class ErrorResponse {

	private String errorCode;

	private String errorMessage;
	
	private String requestURL;

	private String errorCause;
	
	public ErrorResponse(String errorCode, String errorMessage) {

		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		
	}

	public ErrorResponse(String errorCode, String errorMessage, String requestURL) {

		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.requestURL = requestURL;
	}
	
	public ErrorResponse(String errorCode, String errorMessage, String requestURL,String errorCause) {

		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.requestURL = requestURL;
		this.errorCause = errorCause;
	}

	public String getErrorCause() {
	
		return errorCause;
	}

	public void setErrorCause(String errorCause) {
	
		this.errorCause = errorCause;
	}

	public String getErrorCode() {
	
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
	
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
	
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
	
		this.errorMessage = errorMessage;
	}

	public String getRequestURL() {
	
		return requestURL;
	}

	public void setRequestURL(String requestURL) {
	
		this.requestURL = requestURL;
	}



	
}

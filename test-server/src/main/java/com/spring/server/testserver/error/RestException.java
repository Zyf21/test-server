package com.spring.server.testserver.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class RestException extends RuntimeException{

	private static final long serialVersionUID = 7271682196890718126L;

	@JsonProperty
	private String errorCode;
	@JsonProperty
	private String message;
	private HttpStatus httpStatus;

	public RestException(ErrorCodes error) {
		errorCode = error.getErrorCode();
		message = error.getMessage();
		httpStatus = error.getHttpStatus();

	}

	public RestException(String errorCode, String message, HttpStatus httpStatus) {
		this.message = message;
		this.errorCode = errorCode;
		this.httpStatus = httpStatus;
	}
}


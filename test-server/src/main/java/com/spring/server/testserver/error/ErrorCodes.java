package com.spring.server.testserver.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCodes {


	UNKNOWN_ERROR("000000", "unknown_error", HttpStatus.BAD_REQUEST),


	FILE_NOT_FOUND("001001", "error.NOT_FOUND", HttpStatus.BAD_REQUEST),
	FILE_NAME_IS_INVALID("001002", "error.NAME_IS_INVALID", HttpStatus.BAD_REQUEST),
	FILE_COULD_NOT_STORE("001003", "error.COULD_NOT_STORE", HttpStatus.BAD_REQUEST),
	FILE_ALREADY_EXISTS("001004", "error.FILE_ALREADY_EXISTS", HttpStatus.BAD_REQUEST);





	private final String errorCode;
	private final String message;
	private final HttpStatus httpStatus;
}

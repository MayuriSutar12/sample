package com.example.demo.resp_dto;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public class ApiResponse {

	private HttpStatus httpStatusCode;
	private String message;
	private Object result;

	public HttpStatus getHttpStatusCode() {
		return httpStatusCode;
	}

	public void setHttpStatusCode(HttpStatus httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public ApiResponse(HttpStatus httpStatusCode, String message, Object result) {
		super();
		this.httpStatusCode = httpStatusCode;
		this.message = message;
		this.result = result;
	}

	public ApiResponse() {
		super();
		// TODO Auto-generated constructor stub
	}

}

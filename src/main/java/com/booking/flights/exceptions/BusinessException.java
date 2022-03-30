package com.booking.flights.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	private String businessErrorCode;
	private String debugMessage;
	
	public BusinessException() {
	}
	
   public BusinessException(String businessErrorCode,String message) {
    	this(businessErrorCode,message,null);
	}
	
    public BusinessException(String businessErrorCode,String message,String debugMessage) {
    	super(message);
    	this.businessErrorCode=businessErrorCode;
    	this.debugMessage=debugMessage;
	}
    
    public BusinessException(String businessErrorCode,Throwable error) {
		super(error.getMessage());
    	this.businessErrorCode=businessErrorCode;
    }
}
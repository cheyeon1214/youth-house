package com.project.exception;

public class PaymentException extends Exception{
	public PaymentException(){
		this("결제 도중 문제가 발생했습니다.");
	}
	public PaymentException(String message){
		super(message);
	}
}

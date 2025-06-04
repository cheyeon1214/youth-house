package com.project.exception;

public class InvalidInputException extends Exception{
	public InvalidInputException(){
		this("잘못된 입력입니다. ");
	}
	public InvalidInputException(String message){
		super(message);
	}
}

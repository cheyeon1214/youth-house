package com.project.exception;

public class DMLException extends Exception{
	public DMLException(){
		this("문제가 발생했습니다.");
	}
	public DMLException(String message){
		super(message);
	}
}

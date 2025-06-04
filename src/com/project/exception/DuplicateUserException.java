package com.project.exception;

public class DuplicateUserException extends Exception{
	public DuplicateUserException(){
		this("이미 있는 회원입니다. ");
	}
	public DuplicateUserException(String message){
		super(message);
	}
}

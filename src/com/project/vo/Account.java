package com.project.vo;

public class Account {
	private String account;
	private String bankName;
	
	public Account(String account, String bankName) {
		super();
		this.account = account;
		this.bankName = bankName;
	}
	
	public String getAccount() {
		return account;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	@Override
	public String toString() {
		return "Account [account=" + account + ", bankName=" + bankName + "]";
	}
	
	
}

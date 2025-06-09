package com.project.vo;

import java.util.ArrayList;

public class Guest {
	private String id;
	private String name;
	private String pass;
	private String phone; //phonenumber
	private String gender;
	private String depositeAccount; //deposite_account
	private double depositeBalance; //deposite_balance
	
	ArrayList<Reservation> reservations = new ArrayList<>();
	
	ArrayList<Account> accounts = new ArrayList<>();

	public Guest() {}
	public Guest( String id, String name, String pass, String phone, String gender,
			String depositeAccount) {
		super();
		this.id = id;
		this.name = name;
		this.pass = pass;
		this.phone = phone;
		this.gender = gender;
		this.depositeAccount = depositeAccount;
	}
	public Guest( String id, String name, String pass, String phone, String gender,
			String depositeAccount, double depositeBalance) {
		super();
		this.id = id;
		this.name = name;
		this.pass = pass;
		this.phone = phone;
		this.gender = gender;
		this.depositeAccount = depositeAccount;
		this.depositeBalance = depositeBalance;
	}
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getDepositeAccount() {
		return depositeAccount;
	}
	public void setDepositeAccount(String depositeAccount) {
		this.depositeAccount = depositeAccount;
	}
	public double getDepositeBalance() {
		return depositeBalance;
	}
	public void setDepositeBalance(double depositeBalance) {
		this.depositeBalance = depositeBalance;
	}
	public ArrayList<Reservation> getReservations() {
		return reservations;
	}
	public void setReservations(ArrayList<Reservation> reservations) {
		this.reservations = reservations;
	}
	public ArrayList<Account> getAccounts() {
		return accounts;
	}
	public void setAccounts(ArrayList<Account> accounts) {
		this.accounts = accounts;
	}
	
	@Override
	public String toString() {
		return "Guest [id=" + id + ", name=" + name + ", pass=" + pass + ", phone=" + phone + ", gender=" + gender
				+ ", depositeAccount=" + depositeAccount + ", depositeBalance=" + depositeBalance + ", reservations="
				+ reservations + ", accounts=" + accounts + "]";
	}
	
	
	
}


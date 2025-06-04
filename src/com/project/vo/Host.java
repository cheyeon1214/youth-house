package com.project.vo;

import java.util.ArrayList;

public class Host {
	private String hostID;
	private String pass;
	private String name;
	private String account;
	
	ArrayList<GuestHouse> ghs = new ArrayList<>();

	public Host() {}
	public Host(String hostID, String pass, String name, String account) {
		super();
		this.hostID = hostID;
		this.pass = pass;
		this.name = name;
		this.account = account;
	}

	public String getHostID() {
		return hostID;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public ArrayList<GuestHouse> getGhs() {
		return ghs;
	}

	public void setGhs(ArrayList<GuestHouse> ghs) {
		this.ghs = ghs;
	}

	@Override
	public String toString() {
		return "Host [hostID=" + hostID + ", pass=" + pass + ", name=" + name + ", account=" + account + ", ghs=" + ghs
				+ "]";
	}
	
	
}

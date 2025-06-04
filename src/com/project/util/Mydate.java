package com.project.util;


public class Mydate {
	int yaer;
	int month;
	int day;
	
	public Mydate() {}
	public Mydate(int yaer, int month, int day) {
		super();
		this.yaer = yaer;
		this.month = month;
		this.day = day;
	}
	public int getYaer() {
		return yaer;
	}
	public void setYaer(int yaer) {
		this.yaer = yaer;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	@Override
	public String toString() {
		return "Mydate [yaer=" + yaer + ", month=" + month + ", day=" + day + "]";
	}
	
	
	
}

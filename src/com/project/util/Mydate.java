package com.project.util;


public class Mydate {
	int year;
	int month;
	int day;
	
	public Mydate() {}
	public Mydate(int year, int month, int day) {
		super();
		this.year = year;
		this.month = month;
		this.day = day;
	}
	public int getYaer() {
		return year;
	}
	public void setYaer(int year) {
		this.year = year;
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
		return "Mydate [yaer=" + year + ", month=" + month + ", day=" + day + "]";
	}
	
	
	
}

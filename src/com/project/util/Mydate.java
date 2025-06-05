package com.project.util;

import java.time.LocalDate;

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
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
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
		return "Mydate [year=" + year + ", month=" + month + ", day=" + day + "]";
	}
	public LocalDate toLocalDate() {
	    return LocalDate.of(this.year, this.month, this.day);
	}
	
	
}

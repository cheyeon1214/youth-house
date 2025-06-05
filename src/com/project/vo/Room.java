package com.project.vo;

public class Room {
	private String roomno;
	private String gender;
	private int capacity;
	private double price;
	private String overview;
	
	public Room() {}
	public Room(String roomno, String gender, int capacity, double price, String overview) {
		super();
		this.roomno = roomno;
		this.gender = gender;
		this.capacity = capacity;
		this.price = price;
		this.overview = overview;
	}
	public Room(String roomno, int price) {
		this.roomno = roomno;
		this.price = price;
	}
	public String getRoomno() {
		return roomno;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getOverview() {
		return overview;
	}
	public void setOverview(String overview) {
		this.overview = overview;
	}
	@Override
	public String toString() {
		return "Room [roomno=" + roomno + ", gender=" + gender + ", capacity=" + capacity + ", price=" + price
				+ ", overview=" + overview + "]";
	}

	
}

package com.project.vo;

import java.sql.Date;
import java.util.ArrayList;

import com.project.enums.PaymentType;
import com.project.util.Mydate;

public class Reservation {
	private String reservationID; //reservatioin_id
	private int headCount; //head_count
	private double price;
	private Mydate checkinDate; //checkin_date
	private Mydate checkoutDate; //checkout_date
	private PaymentType paymentType; //payment_type
	private String roomno;
	private String ghcode;
	
	ArrayList<Review> reviews = new ArrayList<>();

	public Reservation() {}
	public Reservation(String reservationID, int headCount, double price, Mydate checkinDate, Mydate checkoutDate,
			PaymentType paymentType, String roomno, String ghcode) {
		super();
		this.reservationID = reservationID;
		this.headCount = headCount;
		this.price = price;
		this.checkinDate = checkinDate;
		this.checkoutDate = checkoutDate;
		this.paymentType = paymentType;
		this.roomno = roomno;
		this.ghcode = ghcode;
	}

	public String getReservationID() {
		return reservationID;
	}
	
	public int getHaedCount() {
		return headCount;
	}

	public void setHaedCount(int headCount) {
		this.headCount = headCount;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Mydate getCheckinDate() {
		return checkinDate;
	}

	public void setCheckinDate(Mydate checkinDate) {
		this.checkinDate = checkinDate;
	}

	public Mydate getCheckoutDate() {
		return checkoutDate;
	}

	public void setCheckoutDate(Mydate checkoutDate) {
		this.checkoutDate = checkoutDate;
	}

	public PaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	public String getRoomno() {
		return roomno;
	}

	public void setRoomno(String roomno) {
		this.roomno = roomno;
	}

	public String getGhcode() {
		return ghcode;
	}
	
//	게하 자체를 바꾸고 싶으면 삭제 후 다시 예약
//	public void setGhcode(String ghcode) {
//		this.ghcode = ghcode;
//	}

	public ArrayList<Review> getReviews() {
		return reviews;
	}

	public void setReviews(ArrayList<Review> reviews) {
		this.reviews = reviews;
	}

	@Override
	public String toString() {
		return "Reservation [reservationID=" + reservationID + ", haedCount=" + headCount + ", price=" + price
				+ ", checkinDate=" + checkinDate + ", checkoutDate=" + checkoutDate + ", paymentType=" + paymentType
				+ ", roomno=" + roomno + ", ghcode=" + ghcode + ", reviews=" + reviews + "]";
	}
	

}

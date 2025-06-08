package com.project.vo;

import java.util.ArrayList;

import com.project.enums.PaymentType;
import com.project.util.Mydate;

public class Reservation {
	private String guest_id; // user_id
	private String reservationID; //reservatioin_id
	private int headCount; //head_count
	private double price;
	private Mydate checkinDate; //checkin_date
	private Mydate checkoutDate; //checkout_date
	private PaymentType paymentType; //payment_type
	private String roomno;
	private String ghcode;
	
	//ArrayList<Review> reviews = new ArrayList<>();
	Review review = new Review();

	public Reservation() {}
	public Reservation(String reservationID, int headCount, double price, Mydate checkinDate, Mydate checkoutDate,
			PaymentType paymentType, String roomno, String ghcode, String guest_id) {
		super();
		this.reservationID = reservationID;
		this.headCount = headCount;
		this.price = price;
		this.checkinDate = checkinDate;
		this.checkoutDate = checkoutDate;
		this.paymentType = paymentType;
		this.roomno = roomno;
		this.ghcode = ghcode;
		this.guest_id = guest_id;
	}
	public Reservation(int headCount, double price, Mydate checkinDate, Mydate checkoutDate,
			PaymentType paymentType, String roomno, String ghcode, String guest_id) {
		super();
		this.headCount = headCount;
		this.price = price;
		this.checkinDate = checkinDate;
		this.checkoutDate = checkoutDate;
		this.paymentType = paymentType;
		this.roomno = roomno;
		this.ghcode = ghcode;
		this.guest_id = guest_id;
	}

	public String getGuestId() {
		return guest_id;
	}
	
	public String getReservationID() {
		return reservationID;
	}
	
	public int getHeadCount() {
		return headCount;
	}

	public void setHeadCount(int headCount) {
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

	public Review getReviews() {
		return review;
	}

	public void setReviews(Review review) {
		this.review = review;
	}

	@Override
	public String toString() {
		return "Reservation [reservationID=" + reservationID + ", haedCount=" + headCount + ", price=" + price
				+ ", checkinDate=" + checkinDate + ", checkoutDate=" + checkoutDate + ", paymentType=" + paymentType
				+ ", roomno=" + roomno + ", ghcode=" + ghcode + ", reviews=" + review + "]";
	}
	

}

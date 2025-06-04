package com.project.vo;

public class Review {
	private String reviewID; //review_id
	private String text;
	private String starRating; //star_rating
	private String reservationID;
	public Review() {}
	public Review(String reviewID, String text, String starRating, String reservationID) {
		super();
		this.reviewID = reviewID;
		this.text = text;
		this.starRating = starRating;
		this.reservationID = reservationID;
	}
	public String getReviewID() {
		return reviewID;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getStarRating() {
		return starRating;
	}
	public void setStarRating(String starRating) {
		this.starRating = starRating;
	}
	public String getReservationID() {
		return reservationID;
	}
	public void setReservationID(String reservationID) {
		this.reservationID = reservationID;
	}
	@Override
	public String toString() {
		return "Review [reviewID=" + reviewID + ", text=" + text + ", starRating=" + starRating + ", reservationID="
				+ reservationID + "]";
	}
	
	
}

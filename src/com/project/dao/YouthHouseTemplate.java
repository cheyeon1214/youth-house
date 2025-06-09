package com.project.dao;

import java.sql.Connection;
import java.util.ArrayList;

import com.project.exception.DMLException;
import com.project.exception.DuplicateUserException;
import com.project.exception.InvalidInputException;
import com.project.exception.RecordNotFoundException;
import com.project.util.Mydate;
import com.project.vo.Account;
import com.project.vo.Guest;
import com.project.vo.GuestHouse;
import com.project.vo.Host;
import com.project.vo.Reservation;
import com.project.vo.Review;
import com.project.vo.Room;

public interface YouthHouseTemplate {
	
	//예약 관련
	ArrayList<Reservation> getClosedReservations(Guest guest) throws DMLException;
	void addReservation(Reservation reservation, String gender) throws DMLException, InvalidInputException, RecordNotFoundException; //채니채니 
	ArrayList<Reservation> getAllReservations(String guestID) throws DMLException; //라니라니
	ArrayList<Reservation> getAllReservations(String ghcode, Mydate date) throws DMLException; //여리여리
	ArrayList<Reservation> getAllReservations(String ghcode, int num) throws DMLException; //혀니혀니
	Reservation getAReservation(String reserID) throws DMLException;
	void updateReservation(Reservation reservation,  Mydate startDate, Mydate endDate, String roomno, int headCount, String gender) throws DMLException, RecordNotFoundException; //여리여리, 채니채니 
	Review getReview(Guest guest, String reservationID) throws DMLException;
	void deleteReservation(String reservationID) throws DMLException, RecordNotFoundException; //혀니혀니

	//정렬 조회
	ArrayList<GuestHouse> sortGHsByCount(String dong) throws DMLException ; //혀니혀니
	ArrayList<GuestHouse> sortGHsByStar(String sigungu, String dong) throws DMLException; //채니채니
	ArrayList<GuestHouse>  sortGHs(int min, int max) throws DMLException,InvalidInputException; //사잇값 라니라니
	ArrayList<GuestHouse> sortGHs(int limit) throws DMLException; //num개 최신 게하 혀니혀니
	ArrayList<GuestHouse>sortGHs() throws DMLException; //추천순 여리여리 
	
}

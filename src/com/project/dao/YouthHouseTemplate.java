package com.project.dao;

import java.util.ArrayList;
import java.util.HashMap;

import com.project.exception.DMLException;
import com.project.exception.DuplicateUserException;
import com.project.exception.InvalidInputException;
import com.project.exception.PaymentException;
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
	
	
	void addUser(Guest guest) throws DMLException,DuplicateUserException; //라니라니
	void addUser(Host host) throws DMLException, DuplicateUserException; //혀니혀니
	Guest loginGuest(String id, String pass) throws RecordNotFoundException, DMLException; //여리여리
	Host loginHost(String id, String pass) throws DMLException,RecordNotFoundException; //관리자 전화 승인 채니채니 
	
	void addReservation(Reservation reservation) throws DMLException, PaymentException; //채니채니 
	public ArrayList<Reservation> getAllReservations(String guestID) throws DMLException; //라니라니
	ArrayList<Reservation> getAllReservations(String ghcode, Mydate date) throws DMLException; //여리여리
	public ArrayList<Reservation> getAllReservations(String ghcode, int num) throws DMLException; //혀니혀니
	ArrayList<GuestHouse> getAllGHs(String hostID) throws DMLException; //라니라니
	
	boolean isPossibleReservation(String ghcode, String roomno, Mydate checkIn, Mydate checkOut, int num) throws RecordNotFoundException, DMLException;
	Reservation getAReservation(String reserID) throws DMLException;
	void updateReservation(String reservationID, Mydate startDate, Mydate endDate) throws DMLException, RecordNotFoundException; //여리여리, 채니채니
	void updateReservatioin(String reservationID, String roomno, int headCount) throws DMLException, RecordNotFoundException; //여리여리, 채니채니 
	
	void deleteReservation(String reservationID) throws DMLException, RecordNotFoundException; //혀니혀니
	void updateUser(Guest guest) throws DMLException,RecordNotFoundException; //혀니혀니 
	void updateUser(Host host) throws DMLException,RecordNotFoundException; //라니라니
	
	void addAccount(String id, String account, String bankName) throws DMLException,DuplicateUserException; //라니라니
	ArrayList<Account> getAllAccounts(String id) throws DMLException; //혀니혀니 
	void depositYouthCard(String id, double price) throws DMLException, RecordNotFoundException; //혀니혀니
	
	ArrayList<GuestHouse> sortGHsByCount(String dong) throws DMLException ; //혀니혀니
	ArrayList<GuestHouse> sortGHsByStar(String sigungu, String dong) throws DMLException; //채니채니
	ArrayList<GuestHouse>  sortGHs(int min, int max) throws DMLException,InvalidInputException; //사잇값 라니라니
	ArrayList<GuestHouse> sortGHs(int limit) throws DMLException; //num개 최신 게하 혀니혀니
	ArrayList<GuestHouse>sortGHs() throws DMLException; //추천순 여리여리 
	
	void writeReview(Review review) throws DMLException; //여리여리
	
	GuestHouse getGH(String ghcode) throws DMLException; //라니라니
	
	void addGH(GuestHouse gh) throws DMLException; //채니채니 
	
	public void deleteGH(String ghcode)throws DMLException,RecordNotFoundException; //관리자꺼 라니라니
	void deleteRoom(String ghcode, String roomno) throws RecordNotFoundException,DMLException; //혀니혀니 
	void addRoom(String ghcode, Room room) throws DMLException; //여리여리
	void updateRoom(String ghcode, String roomno, int capacity) throws RecordNotFoundException, DMLException; //채니채니 
	void updateRoom(String ghcode, String roomno, String gender)throws DMLException,RecordNotFoundException; //라니라니
	
	double getRevenue(int year, int month, String ghcode) throws InvalidInputException, DMLException; //채니 
	double getRevenue(String ghcode, int year) throws DMLException; // 여리 
	double getRevenue(int year, int month, int day) throws DMLException; //혀니 
	double getRevenue(int year, String quater) throws DMLException; //라니라니
	double getRevenue(int year, String quater, String ghcode) throws DMLException;
	
	ArrayList<Room> getAllRooms(String ghcode) throws DMLException; //라니라니
	Room getARoom(String ghcode, String roomno) throws DMLException,RecordNotFoundException; //채니채니
	
}

package com.project.dao;

import java.util.ArrayList;

import com.project.util.Mydate;
import com.project.vo.Guest;
import com.project.vo.GuestHouse;
import com.project.vo.Host;
import com.project.vo.Reservation;
import com.project.vo.Review;
import com.project.vo.Room;

public interface YouthHouseTemplate {
	
	
	void addUser(Guest guest); //라니라니
	void addUser(Host host); //혀니혀니
	Guest loginGuest(String id, String pass); //여리여리
	Host loginHost(String id, String pass); //관리자 전화 승인 채니채니 
	
	void addReservation(Reservation reservation); //채니채니 
	ArrayList<Reservation> getAllReservations(String guestID); //라니라니
	ArrayList<Reservation> getAllReservations(String ghcode, Mydate date); //여리여리
	ArrayList<Reservation> getAllReservations(String ghcode, int num); //혀니혀니
	ArrayList<GuestHouse> getAllGHs(String hostID); //라니라니
	
	void updateReservation(String reservationID, Mydate startDate, Mydate endDate); //여리여리, 채니채니
	void updateReservatioin(String reservationID, String roomno, int headCount); //여리여리, 채니채니 
	
	void deleteReservation(String reservationID); //혀니혀니
	void updateUser(Guest guest); //혀니혀니 
	void updateUser(Host host); //라니라니
	
	void addAccount(String id, String account, String bankName); //라니라니
	void getAllAccounts(String id); //혀니혀니 
	void depositYouthCard(String id, double price); //혀니혀니
	
	ArrayList<GuestHouse> sortGHsByCount(String dong); //혀니혀니
	ArrayList<GuestHouse> sortGHsByStar(String dong); //채니채니
	ArrayList<GuestHouse> sortGHs(int min, int max); //사잇값 라니라니
	ArrayList<GuestHouse> sortGHs(int num); //num개 최신 게하 혀니혀니
	ArrayList<GuestHouse> sortGHs(); //추천순 여리여리 
	
	void writeReview(Review review); //여리여리
	
	GuestHouse getGH(String ghcode); //라니라니
	
	void addGH(GuestHouse gh); //채니채니 
	
	void deleteGH(String ghcode); //관리자꺼 라니라니
	void deleteRoom(String ghcode, String roomno); //혀니혀니 
	void addRoom(String ghcode, Room room); //여리여리
	void updateRoom(String ghcode, String roomno, int capacity); //채니채니 
	void updateRoom(String ghcode, String roomno, String gender); //라니라니
	
	double getRevenue(int year, int month); //채니 
	double getRevenue(int year); // 여리 
	double getRevenue(int year, int month, int day); //혀니 
	double getRevenue(int year, String quater); //라니라니
	
	ArrayList<Room> getAllRooms(String ghcode); //라니라니
	Room getARoom(String ghcode, String roomno); //채니채니
	
}

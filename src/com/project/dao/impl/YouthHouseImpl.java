package com.project.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.project.config.ServerInfo;
import com.project.dao.YouthHouseTemplate;
import com.project.util.Mydate;
import com.project.vo.Guest;
import com.project.vo.GuestHouse;
import com.project.vo.Host;
import com.project.vo.Reservation;
import com.project.vo.Review;
import com.project.vo.Room;

public class YouthHouseImpl implements YouthHouseTemplate{
	
	private static YouthHouseImpl yhdao = new YouthHouseImpl();

	private YouthHouseImpl() {};
	public YouthHouseImpl getInstance() {
		return yhdao;
	};
	//공통로직
	public Connection getConnect() throws SQLException {
		Connection conn = DriverManager.getConnection(ServerInfo.URL, ServerInfo.USER, ServerInfo.PASS);
		System.out.println("디비연결...");
		return conn;
	}
	
	public void closeAll(PreparedStatement ps, Connection conn)throws SQLException{
		if(ps!= null) ps.close();
		if(conn != null) conn.close();
	}
	
	public void closeAll(ResultSet rs,PreparedStatement ps, Connection conn)throws SQLException{
		if(rs != null) rs.close();
		closeAll(ps,conn);
	}
	
	
	@Override
	public void addUser(Guest guest) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addUser(Host host) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Guest loginGuest(String id, String pass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Host loginHost(String id, String pass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addReservation(Reservation reservation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<Reservation> getAllReservations(String guestID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Reservation> getAllReservations(String ghcode, Mydate date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Reservation> getAllReservations(String ghcode, int num) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<GuestHouse> getAllGHs(String hostID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateReservation(String reservationID, Mydate startDate, Mydate endDate) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateReservatioin(String reservationID, String roomno, int headCount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteReservation(String reservationID) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateUser(Guest guest) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateUser(Host host) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addAccount(String id, String account, String bankName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void getAllAccounts(String id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void depositYouthCard(String id, double price) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<GuestHouse> sortGHsByCount(String dong) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<GuestHouse> sortGHsByStar(String dong) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<GuestHouse> sortGHs(int min, int max) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<GuestHouse> sortGHs(int num) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<GuestHouse> sortGHs() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void writeReview(Review review) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public GuestHouse getGH(String ghcode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addGH(GuestHouse gh) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteGH(String ghcode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteRoom(String ghcode, String roomno) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addRoom(String ghcode, Room room) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateRoom(String ghcode, String roomno, int capacity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateRoom(String ghcode, String roomno, String gender) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getRevenue(int year, int month) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getRevenue(int year) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getRevenue(int year, int month, int day) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getRevenue(int year, String quater) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public ArrayList<Room> getAllRooms(String ghcode) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Room getARoom(String ghcode, String roomno) {
		// TODO Auto-generated method stub
		return null;
	}
}

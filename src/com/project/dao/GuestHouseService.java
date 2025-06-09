package com.project.dao;

import java.util.ArrayList;

import com.project.exception.DMLException;
import com.project.exception.InvalidInputException;
import com.project.exception.RecordNotFoundException;
import com.project.vo.GuestHouse;
import com.project.vo.Room;

public interface GuestHouseService extends YouthHouseTemplate{
		//게스트하우스 관련
		GuestHouse getGH(String ghcode) throws DMLException; //라니라니
		ArrayList<GuestHouse> getAllGHs(String hostID) throws DMLException; //라니라니
		void addGH(GuestHouse gh) throws DMLException; //채니채니 
		public void deleteGH(String ghcode)throws DMLException,RecordNotFoundException; //관리자꺼 라니라니
		void deleteRoom(String ghcode, String roomno) throws RecordNotFoundException,DMLException; //혀니혀니 
		void addRoom(String ghcode, Room room) throws DMLException; //여리여리
		void updateRoom(String ghcode, String roomno, int capacity) throws RecordNotFoundException, DMLException; //채니채니 
		void updateRoom(String ghcode, String roomno, String gender)throws DMLException,RecordNotFoundException; //라니라니
		ArrayList<Room> getAllRooms(String ghcode) throws DMLException; //라니라니
		Room getARoom(String ghcode, String roomno) throws DMLException,RecordNotFoundException; //채니채니
		
		//매출 조회 관련
		double getRevenue(int year, int month, String ghcode) throws InvalidInputException, DMLException; //채니 
		double getRevenue(String ghcode, int year) throws DMLException; // 여리 
		double getRevenue(int year, int month, int day, String ghcode) throws DMLException; //혀니 
		double getRevenue(int year, String quater, String ghcode) throws DMLException;
		
}

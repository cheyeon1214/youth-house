package com.project.dao;

import java.util.ArrayList;

import com.project.exception.DMLException;
import com.project.exception.DuplicateUserException;
import com.project.exception.RecordNotFoundException;
import com.project.vo.Account;
import com.project.vo.Guest;
import com.project.vo.Host;
import com.project.vo.Reservation;
import com.project.vo.Review;

public interface UserService extends YouthHouseTemplate{
		//User 관련
		void addUser(Guest guest) throws DMLException,DuplicateUserException; //라니라니
		void addUser(Host host) throws DMLException, DuplicateUserException; //혀니혀니
		Guest loginGuest(String id, String pass) throws RecordNotFoundException, DMLException; //여리여리
		Host loginHost(String id, String pass) throws DMLException,RecordNotFoundException; //채니채니 
		void updateUser(Guest guest) throws DMLException,RecordNotFoundException; //혀니혀니 
		void updateUser(Host host) throws DMLException,RecordNotFoundException; //라니라니
		void addAccount(String id, String account, String bankName) throws DMLException,DuplicateUserException; //라니라니
		ArrayList<Account> getAllAccounts(String id) throws DMLException; //혀니혀니 
		void depositYouthCard(String id, double price) throws DMLException, RecordNotFoundException; //혀니혀니
		void writeReview(Review review, Guest guest, Reservation reservation) throws DMLException; //여리여리
		
		
}

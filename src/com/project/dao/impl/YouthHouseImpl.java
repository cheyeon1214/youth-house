package com.project.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.project.config.ServerInfo;
import com.project.dao.YouthHouseTemplate;
import com.project.util.GuestHouseScore;
import com.project.util.RawGHData;
import com.project.enums.PaymentType;
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

public class YouthHouseImpl implements YouthHouseTemplate {

   private static YouthHouseImpl yhdao = new YouthHouseImpl();

   private YouthHouseImpl() {
   };

   public static YouthHouseImpl getInstance() {
      return yhdao;
   };

   // 공통로직
   public Connection getConnect() throws SQLException {
      Connection conn = DriverManager.getConnection(ServerInfo.URL, ServerInfo.USER, ServerInfo.PASS);
      System.out.println("디비연결...");
      return conn;
   }

//   public void closeAll(PreparedStatement ps, Connection conn) throws SQLException {
//      if (ps != null)
//         ps.close();
//      if (conn != null)
//         conn.close();
//   }
//
//   public void closeAll(ResultSet rs, PreparedStatement ps, Connection conn) throws SQLException {
//      if (rs != null)
//         rs.close();
//      closeAll(ps, conn);
//   }

   public boolean isExistGuest(String id, Connection conn) throws SQLException {
      String query = "SELECT id FROM guest WHERE id=?";
      PreparedStatement ps = conn.prepareStatement(query);
      ps.setString(1, id);
      ResultSet rs = ps.executeQuery();

      return rs.next();// ssn이 있으면 true |없으면 false
   }

   public boolean isExistHost(String id, Connection conn) throws SQLException {
      String query = "SELECT host_id FROM host WHERE host_id=?";
      PreparedStatement ps = conn.prepareStatement(query);
      ps.setString(1, id);
      ResultSet rs = ps.executeQuery();

      return rs.next();// ssn이 있으면 true |없으면 false
   }

   @Override
   public void addUser(Guest guest) throws DMLException, DuplicateUserException {// 라니라니
      String query = "INSERT INTO guest(id, name, pass, phonenumber , gender, deposite_account, deposite_balance) "
            + "VALUES(?, ?, ?, ?, ?, ?, ?)";
      try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query)) {
         if (!isExistGuest(guest.getId(), conn)) {
            ps.setString(1, guest.getId());
            ps.setString(2, guest.getName());
            ps.setString(3, guest.getPass());
            ps.setString(4, guest.getPhone());
            ps.setString(5, guest.getGender());
            ps.setString(6, guest.getDepositeAccount());
            ps.setDouble(7, guest.getDepositeBalance());

            System.out.println(ps.executeUpdate() + " 명의 게스트가 등록되었습니다. ");
         } else {
            throw new DuplicateUserException("이미 등록된 고객입니다.");
         }
      } catch (SQLException e) {
         throw new DMLException("게스트를 등록하던 중 문제가 발생했습니다.");
      }

   }

   @Override
   public void addUser(Host host) throws DMLException, DuplicateUserException {
      String query = "INSERT INTO host (host_id, pass, name, account) VALUES(?,?,?,?)";
      try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query);) {
         if (!isExistHost(host.getHostID(), conn)) {// 추가하려는 ssn이 없다면
            ps.setString(1, host.getHostID());
            ps.setString(2, host.getPass());
            ps.setString(3, host.getName());
            ps.setString(4, host.getAccount().getAccount());

            System.out.println(ps.executeUpdate() + " 명 INSERT 성공..");
         } else {
            throw new DuplicateUserException("추가하려는 고객은 이미 등록된 상태입니다.");
         }
      } catch (SQLException e) {
         throw new DMLException("회원가입을 하던 중 오류가 발생했습니다.");
      }
   }

   @Override
   public Guest loginGuest(String id, String pass) throws RecordNotFoundException, DMLException {
      String query = "SELECT id,name,pass,phonenumber,gender,deposite_account FROM guest WHERE id = ? and pass = ?";
      Guest guest = null;
      ResultSet rs = null;
      try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query);) {
         ps.setString(1, id);
         ps.setString(2, pass);
         rs = ps.executeQuery();
         if (rs.next()) {
            guest = new Guest(rs.getString("id"), rs.getString("name"), rs.getString("pass"),
                  rs.getString("phonenumber"), rs.getString("gender"), rs.getString("deposite_account"));
         }

      } catch (SQLException e) {
         throw new DMLException("책을 등록하는 중에 문제가 발생했습니다.");
      }

      if (guest == null)
         throw new RecordNotFoundException("비밀번호가 잘못 되었습니다.");

      return guest;
   }

   @Override
   public Host loginHost(String id, String pass) throws DMLException, RecordNotFoundException {
      Host host = null;
      String query = "SELECT * FROM host WHERE host_id = ? and pass = ?";
      try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query);) {
         ps.setString(1, id);
         ps.setString(2, pass);
         try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
               host = new Host(rs.getString("host_id"), rs.getString("pass"), rs.getString("name"),
                     new Account(rs.getString("account")));
            }
         }
      } catch (SQLException e) {
         e.printStackTrace();
         throw new DMLException("로그인에 실패하였습니다.");
      }

      if (host == null)
         throw new RecordNotFoundException("해당하는 호스트가 존재하지 않습니다");
      return host;
   }
   
   public void checkRoom(String ghcode,Mydate checkIn, Mydate checkOut) throws DMLException {
		LocalDate start = LocalDate.of(checkIn.getYear(), checkIn.getMonth(), checkIn.getDay());
	    LocalDate end = LocalDate.of(checkOut.getYear(), checkOut.getMonth(), checkOut.getDay());
	    for (LocalDate date = start; date.isBefore(end); date = date.plusDays(1)) {
	    	Mydate mydate = new Mydate(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
	    	HashMap<Room,Integer> status = getCheckCountRoom(ghcode, mydate);
	    	
	    	System.out.println("[" + mydate + "] 예약 현황:");
	        for (Map.Entry<Room, Integer> entry : status.entrySet()) {
	            Room room = entry.getKey();
	            int reserved = entry.getValue();
	            int capacity = room.getCapacity();

	            System.out.println("방번호: " + room.getRoomno() + ", 예약 수용률: " + reserved + " / " + capacity);
	        }
	        System.out.println(); 
	    	
	    }
	}
   
   @Override
   public ArrayList<Reservation> getClosedReservations(Guest guest) throws DMLException{
	   String query = "SELECT * FROM reservation WHERE checkin_date < curdate() AND user_id = ?";
	   ArrayList<Reservation> reservs = new ArrayList<>();
	   try(Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query)){
		   ps.setString(1, guest.getId());
		   try(ResultSet rs = ps.executeQuery();){
			   while(rs.next()) {
				   reservs.add(new Reservation(
			                  rs.getString("reservation_id"),
			                   rs.getInt("head_count"),
			                   rs.getDouble("price"),
			                   new Mydate(rs.getDate("checkin_date").toLocalDate().getYear(),
			                              rs.getDate("checkin_date").toLocalDate().getMonthValue(),
			                              rs.getDate("checkin_date").toLocalDate().getDayOfMonth()),

			                   new Mydate(rs.getDate("checkout_date").toLocalDate().getYear(),
			                              rs.getDate("checkout_date").toLocalDate().getMonthValue(),
			                              rs.getDate("checkout_date").toLocalDate().getDayOfMonth()),
			                   PaymentType.valueOf(rs.getString("payment_type")), 
			                   rs.getString("roomno"),
			                   rs.getString("ghcode"), rs.getString("user_id")
			                  ));
			   }
		   }
	   }catch(SQLException e) {
		   throw new DMLException("문제 발생");
	   }
	   
	   return reservs;
   }

   @Override
   public void addReservation(Reservation reservation, String gender) throws DMLException, InvalidInputException, RecordNotFoundException {
      String query = "INSERT INTO reservation (head_count, price, checkin_date, checkout_date, payment_type, roomno, ghcode, user_id) VALUES (?,?,?,?,?,?,?,?)";
      double balance = 0;
      try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query);) {
         
			try {
				if(!isPossibleReservation(conn, reservation.getGhcode(), reservation.getRoomno(), reservation.getCheckinDate(), reservation.getCheckoutDate(), reservation.getHeadCount(), gender)) {
					 throw new InvalidInputException("예약이 불가합니다. 인원수나 성별을 체크해주세요");
				 }
			} catch (RecordNotFoundException e) {
				throw new RecordNotFoundException("방이 없습니다.");
			} catch (DMLException e) {
				throw new DMLException(e.getMessage());
			} 
		// 예치금 잔액처리
         if (reservation.getPaymentType().toString().equals("deposit")) {
            String querySelect = "SELECT deposite_balance FROM guest WHERE id = ?";
            String queryUpdate = "UPDATE guest SET deposite_balance = deposite_balance - ? WHERE id = ?";
            PreparedStatement ps2 = conn.prepareStatement(querySelect);
            ps2.setString(1, reservation.getGuestId());

            try (ResultSet rs = ps2.executeQuery();) {
               if (rs.next()) {
                  balance = rs.getDouble("deposite_balance");
               }
            }
            if (balance < reservation.getPrice())
               throw new RecordNotFoundException("예치금 잔액이 부족합니다.");
            else {
               PreparedStatement ps3 = conn.prepareStatement(queryUpdate);
               ps3.setDouble(1, reservation.getPrice());
               ps3.setString(2, reservation.getGuestId());
               System.out.println(ps3.executeUpdate() == 1 ? "예치금 결제가 완료되었습니다." : "예치금 결제를 실패하였습니다.");
            }

         } else {
            String querySelect = "SELECT * FROM account WHERE user_id = ?";
            PreparedStatement ps2 = conn.prepareStatement(querySelect);
            ps2.setString(1, reservation.getGuestId());
            try (ResultSet rs = ps2.executeQuery();) {
               if (rs.next()) {
                  // 계좌가 있다고 간주함
                  System.out.println("결제할 계좌가 존재합니다. 결제를 진행합니다.. ");
               } else {
                  throw new RecordNotFoundException("결제할 계좌가 없습니다. ");
               }
            }
         }
         ps.setInt(1, reservation.getHeadCount());
         ps.setDouble(2, reservation.getPrice());
         ps.setDate(3, java.sql.Date.valueOf(reservation.getCheckinDate().toLocalDate()));
         ps.setDate(4, java.sql.Date.valueOf(reservation.getCheckoutDate().toLocalDate()));
         ps.setString(5, reservation.getPaymentType().toString());
         ps.setString(6, reservation.getRoomno());
         ps.setString(7, reservation.getGhcode());
         ps.setString(8, reservation.getGuestId());
         System.out.println(ps.executeUpdate() == 1 ? "예약이 완료되었습니다." : "예약을 실패하였습니다.");

      } catch (SQLException e) {
         throw new DMLException("예약 도중 문제가 발생하였습니다.");
      }

   }

   @Override
   public ArrayList<Reservation> getAllReservations(String guestID) throws DMLException {// 라니라니
      ArrayList<Reservation> temp = new ArrayList<Reservation>();
      String query = "SELECT * from reservation where user_id=?";
      try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query);) {
         ps.setString(1, guestID);

         try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
               java.sql.Date sqlCheckIn = rs.getDate("checkin_date");
               java.sql.Date sqlCheckOut = rs.getDate("checkout_date");

               Mydate checkin = null;
               Mydate checkout = null;
               if (sqlCheckIn != null) {
                  int y = sqlCheckIn.toLocalDate().getYear();
                  int m = sqlCheckIn.toLocalDate().getMonthValue();
                  int d = sqlCheckIn.toLocalDate().getDayOfMonth();
                  checkin = new Mydate(y, m, d);
               }

               if (sqlCheckOut != null) {
                  int y2 = sqlCheckOut.toLocalDate().getYear();
                  int m2 = sqlCheckOut.toLocalDate().getMonthValue();
                  int d2 = sqlCheckOut.toLocalDate().getDayOfMonth();
                  checkout = new Mydate(y2, m2, d2);
               }

               String raw = rs.getString("payment_type");
               PaymentType pt = null;
               if (raw != null) {
                  pt = PaymentType.valueOf(raw);
               }

               Reservation reservation = new Reservation(rs.getString("reservation_id"), rs.getInt("head_count"),
                     rs.getDouble("price"), checkin, checkout, pt, rs.getString("roomno"),
                     rs.getString("ghcode"), rs.getString("user_id"));
               temp.add(reservation);
            }
         }
      } catch (SQLException e) {
         throw new DMLException("예약하는 도중 문제가 발생했습니다.");
      }
      return temp;
   }

   // 특정 게스트하우스(ghcode), 특정 방(roomno) 에 대해서 checkin 부터 checkout-1 까지
   // 수용인원 num 명에 대해서 가능한 방인지를 boolean으로 체크합니다.
   private boolean isPossibleReservation(Connection conn, String ghcode, String roomno, Mydate checkIn, Mydate checkOut, int num, String gender)
	         throws RecordNotFoundException, DMLException {
	   
	   
	      Room targetRoom = null;
	      ResultSet rs = null;
	      String query = "SELECT * from room where ghcode =? and roomno = ?";
	      try (PreparedStatement ps = conn.prepareStatement(query);) {
	         ps.setString(1, ghcode);
	         ps.setString(2, roomno);
	         rs = ps.executeQuery();
	         if (rs.next()) {
	            targetRoom = new Room(rs.getString("roomno"), rs.getString("gender"), rs.getInt("capacity"),
	                  rs.getDouble("price"), rs.getString("overview"));
	         }
	      } catch (SQLException e) {
	         throw new DMLException("예약 하는 중에 문제가 발생하였습니다.");
	      }

	      if (targetRoom == null) {
	         throw new RecordNotFoundException("해당 방을 찾을 수 없습니다."); // 방 정보 없으면 예약 불가
	      }

	      String rGender = targetRoom.getGender();
	      int capacity = targetRoom.getCapacity();
	      
	      if (!rGender.equals(gender)) {
	         return false; 
	      }

	      LocalDate start = LocalDate.of(checkIn.getYear(), checkIn.getMonth(), checkIn.getDay());
	      LocalDate end = LocalDate.of(checkOut.getYear(), checkOut.getMonth(), checkOut.getDay());

	      for (LocalDate date = start; date.isBefore(end); date = date.plusDays(1)) {
	         Mydate currentDate = new Mydate(date.getYear(), date.getMonthValue(), date.getDayOfMonth());

	         ArrayList<Reservation> reservations = getAllReservations(ghcode, currentDate); 
	         int totalReserved = 0;

	         for (Reservation r : reservations) {
	            if (r.getRoomno().equals(roomno)) {
	               totalReserved += r.getHeadCount();
	            }
	         }

	         if (totalReserved + num > capacity) {
	            return false; 
	         }
	      }

	      return true; 
	   }

//  특정 게스트하우스(ghcode)의 특정 날짜(date)에 대해서
//  모든 Room의 정보(gender,capacity) 와 현재 예약 인원 수를 HashMap으로 반환
   public HashMap<Room, Integer> getCheckCountRoom(String ghcode, Mydate date) throws DMLException {
      ArrayList<Room> rooms = new ArrayList<Room>();
      ArrayList<Reservation> currentR = getAllReservations(ghcode, date);

      // 1. roomno 기준 현재 예약된 인원 누적
      HashMap<String, Integer> currentMap = new HashMap<>();
      for (Reservation r : currentR) {
         currentMap.put(r.getRoomno(), currentMap.getOrDefault(r.getRoomno(), 0) + r.getHeadCount());
      }

      // 2. 전체 Room 정보 조회 후 Room → 예약 인원 수로 구성된 Map 생성
      String query = "SELECT roomno, gender, capacity, price, overview, ghcode FROM room WHERE ghcode = ?";
      HashMap<Room, Integer> currMap = new HashMap<>();

      try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query)) {

         ps.setString(1, ghcode);
         ResultSet rs = ps.executeQuery();

         while (rs.next()) {
            String roomno = rs.getString("roomno");
            String gender = rs.getString("gender");
            int capacity = rs.getInt("capacity");
            double price = rs.getDouble("price");
            String overview = rs.getString("overview");

            // Room 객체 생성
            Room room = new Room(roomno, gender, capacity, price, overview);

            // 예약 인원 수: currentMap에 없으면 0
            int reservedCount = currentMap.getOrDefault(roomno, 0);

            currMap.put(room, reservedCount);
         }

      } catch (SQLException e) {
         throw new DMLException("예약하는 도중 오류가 발생하였습니다.");
      }
      return currMap;
   }

   @Override
   public ArrayList<Reservation> getAllReservations(String ghcode, Mydate date) throws DMLException {
      // TODO Auto-generated method stub
      String query = "SELECT * FROM reservation WHERE ghcode = ? AND ? BETWEEN checkin_date AND checkout_date";
      ArrayList<Reservation> reservations = new ArrayList<Reservation>();
      ResultSet rs = null;
      try(Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query); ) {
         ps.setString(1, ghcode);
         String dateFormat = date.getYear() + "-" + date.getMonth() + "-" + date.getDay();
         ps.setString(2, dateFormat);
         rs = ps.executeQuery();
         while(rs.next()) {
            reservations.add(new Reservation(
                  rs.getString("reservation_id"),
                   rs.getInt("head_count"),
                   rs.getDouble("price"),
                   new Mydate(rs.getDate("checkin_date").toLocalDate().getYear(),
                              rs.getDate("checkin_date").toLocalDate().getMonthValue(),
                              rs.getDate("checkin_date").toLocalDate().getDayOfMonth()),

                   new Mydate(rs.getDate("checkout_date").toLocalDate().getYear(),
                              rs.getDate("checkout_date").toLocalDate().getMonthValue(),
                              rs.getDate("checkout_date").toLocalDate().getDayOfMonth()),
                   PaymentType.valueOf(rs.getString("payment_type")), 
                   rs.getString("roomno"),
                   rs.getString("ghcode"), rs.getString("user_id")
                  ));
                        
         } 
         
      } catch(SQLException e) {
         throw new DMLException("예약하는 도중 오류가 발생하였습니다.1");
      }
      return reservations;
   }

   @Override
   public ArrayList<Reservation> getAllReservations(String ghcode, int num) throws DMLException {
      String query = "SELECT * FROM reservation WHERE ghcode =? ORDER BY checkin_date DESC LIMIT ?";
      ArrayList<Reservation> reservations = new ArrayList<>();

      try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query)) {
         ps.setString(1, ghcode);
         ps.setInt(2, num);

         try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
               Reservation reservation = new Reservation(rs.getString("reservation_id"), rs.getInt("head_count"),
                     rs.getDouble("price"),
                     new Mydate(rs.getDate("checkin_date").toLocalDate().getYear(),
                           rs.getDate("checkin_date").toLocalDate().getMonthValue(),
                           rs.getDate("checkin_date").toLocalDate().getDayOfMonth()),

                     new Mydate(rs.getDate("checkout_date").toLocalDate().getYear(),
                           rs.getDate("checkout_date").toLocalDate().getMonthValue(),
                           rs.getDate("checkout_date").toLocalDate().getDayOfMonth()),
                     PaymentType.valueOf(rs.getString("payment_type")), rs.getString("roomno"),
                     rs.getString("ghcode"), rs.getString("user_id"));
               reservations.add(reservation);
            }
         }
      } catch (SQLException e) {
         throw new DMLException("조회 도중 문제가 발생하였습니다...");
      }
      return reservations;
   }

   @Override
   public ArrayList<GuestHouse> getAllGHs(String hostID) throws DMLException {// 라니라니
      ArrayList<GuestHouse> temp = new ArrayList<GuestHouse>();
      String query = "SELECT * from guesthouse where host_id=?";
      try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query)) {
         ps.setString(1, hostID);
         try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
               GuestHouse gh = new GuestHouse(rs.getString("ghcode"), rs.getString("businessnum"),
                     rs.getString("name"), rs.getString("sido"), rs.getString("sigungu"), rs.getString("dong"),
                     rs.getString("detail_address"),hostID);
               temp.add(gh);
            }
         }

      } catch (SQLException e) {
         throw new DMLException("게스트 하우스를 검색하던 중 문제가 발생했습니다.");
      }
      return temp;
   }
   
   @Override
   public Reservation getAReservation(String reserID) throws DMLException{
      Reservation reservation = null;
      String query = "SELECT * FROM reservation where reservation_id = ?";
      ResultSet rs = null;
      try(Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query);) {
         ps.setString(1, reserID);
         rs = ps.executeQuery();
         if(rs.next()) 
         {
            reservation = new Reservation(
                  rs.getString("reservation_id"),
                   rs.getInt("head_count"),
                   rs.getDouble("price"),
                   new Mydate(rs.getDate("checkin_date").toLocalDate().getYear(),
                              rs.getDate("checkin_date").toLocalDate().getMonthValue(),
                              rs.getDate("checkin_date").toLocalDate().getDayOfMonth()),

                   new Mydate(rs.getDate("checkout_date").toLocalDate().getYear(),
                              rs.getDate("checkout_date").toLocalDate().getMonthValue(),
                              rs.getDate("checkout_date").toLocalDate().getDayOfMonth()),
                   PaymentType.valueOf(rs.getString("payment_type")), 
                   rs.getString("roomno"),
                   rs.getString("ghcode"),
                   rs.getString("user_id")
                  );
         }
      } catch (SQLException e) {
         throw new DMLException("예약이 없습니다.");
      }
      return reservation;
   }

   @Override
   public void updateReservation(Reservation reservation, Mydate startDate, Mydate endDate, String gender) throws DMLException, RecordNotFoundException {
      String query = "Delete from reservation where reservation_id = ?";
      String query2 = "Insert into reservation (reservation_id,head_count,price,checkin_date,checkout_date,payment_type,roomno,ghcode,user_id) VALUES (?,?,?,?,?,?,?,?)";
      try(Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query);) {
         ps.setString(1, reservation.getReservationID());
         System.out.println(ps.executeUpdate() +"개 성공");
         
         if(isPossibleReservation(conn, reservation.getGhcode(), reservation.getRoomno(), startDate, endDate, reservation.getHeadCount(), gender)) {
            reservation.setCheckinDate(startDate);
            reservation.setCheckoutDate(endDate);
            
         } 
         try(PreparedStatement ps2 = conn.prepareStatement(query2);) { 
         ps2.setString(1, reservation.getReservationID());
         ps2.setInt(2, reservation.getHeadCount());
         ps2.setDouble(3, reservation.getPrice());
         String startStr = startDate.getYear() + "-" + startDate.getMonth() + "-" + startDate.getDay();
         java.sql.Date startSqlDate = java.sql.Date.valueOf(startStr);
         ps2.setDate(4, startSqlDate);
         String endStr = endDate.getYear() + "-" + endDate.getMonth() + "-" + endDate.getDay();
         java.sql.Date endSqlDate = java.sql.Date.valueOf(endStr);
         ps2.setDate(5, endSqlDate);
         ps2.setString(6, reservation.getPaymentType().toString());
         ps2.setString(7, reservation.getRoomno());
         ps2.setString(8,reservation.getGuestId());

         }
      } catch (SQLException e) {
         throw new DMLException("예약이 없습니다.");
      }
      
   }

	@Override
	public void updateReservatioin(String reservationID, String roomno, int headCount) throws DMLException, RecordNotFoundException {
		Reservation reservation = getAReservation(reservationID);
			String query = "SELECT gender FROM RESERVATION r join Guest g on r.user_id = g.id WHERE g.id = ?";
		String gGender = "";
		boolean flag = true;
		// 게스트 성별을 가져온다.
		try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query);) {
			ps.setString(1, reservation.getGuestId());
			try (ResultSet rs = ps.executeQuery();) {
				if (rs.next()) {
					gGender = rs.getString("gender");
				}
			}

//			if (!(isPossibleReservation(reservation.getGhcode(), roomno, gGender))) {
//				flag = false;
//
//			}
		if(roomno.equals(reservation.getRoomno())) {
			if(!(isPossibleReservation(conn, reservation.getGhcode(), roomno, reservation.getCheckinDate(), reservation.getCheckoutDate(), headCount-reservation.getHeadCount(),gGender)))
			{
				flag = false;
			}
		}
		else if(!(isPossibleReservation(conn, reservation.getGhcode(), roomno, reservation.getCheckinDate(), reservation.getCheckoutDate(), headCount, gGender)))
		{
			flag = false;
		}
		
		
		if(flag == true)
		{
			query = "Update reservation SET roomno = ? ,head_count = ? WHERE reservation_id = ?";
			try(PreparedStatement ps2 = conn.prepareStatement(query)){
				ps2.setString(1, roomno);
				ps2.setInt(2, headCount);
				ps2.setString(3, reservation.getReservationID());
				System.out.println(ps2.executeUpdate() + "개 변경 성공"); 
				
			} catch (SQLException e) {
				throw new DMLException("예약 변경 중 오류 발생하였습니다.");
			}
		} else {
			System.out.println("해당 방 혹은 해당 인원수로 변경하실 수 없습니다.");
		}
		} catch (SQLException e) {
			throw new DMLException("예약 변경 중 오류 발생하였습니다.");
		}

	}

   @Override
   public void deleteReservation(String reservationID) throws DMLException, RecordNotFoundException {
      String query = "DELETE FROM reservation WHERE reservation_id=?";
      try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query);) {
         ps.setString(1, reservationID);
         if (ps.executeUpdate() == 0)
            throw new RecordNotFoundException(reservationID + " 의 예약이 존재하지 않습니다.");
      } catch (SQLException e) {
         throw new DMLException("삭제 진행 도중 문제가 발생하였습니다...");
      }
   }

   @Override
   public void updateUser(Guest guest) throws DMLException, RecordNotFoundException {
      String query = "UPDATE guest SET name=?,pass=?,phonenumber=?,gender=?,deposite_account=? WHERE id=?";
      try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query);) {
    	  ps.setString(1, guest.getName());
    	  ps.setString(2, guest.getPass());
    	  ps.setString(3, guest.getPhone());
    	  ps.setString(4, guest.getGender());
    	  ps.setString(5, guest.getDepositeAccount());
    	  ps.setString(6, guest.getId());
         if (ps.executeUpdate() == 0)
            throw new RecordNotFoundException(guest.getName() + " 게스트님이 존재하지 않습니다.");
      } catch (SQLException e) {
         throw new DMLException("삭제 진행 도중 문제가 발생하였습니다...");
      }
   }

   @Override
   public void updateUser(Host host) throws DMLException, RecordNotFoundException {// 라니라니
      String query = "UPDATE host SET pass=?,name=?,account=? where host_id=?";
      try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query)) {
         if (!isExistHost(host.getHostID(), conn)) {
            ps.setString(1, host.getPass());
            ps.setString(2, host.getName());
            ps.setString(3, host.getAccount().getAccount());
            System.out.println(ps.executeUpdate() + "개의 정보가 변경되었습니다.");
         } else
            throw new RecordNotFoundException("등록된 호스트가 없습니다.");
      } catch (SQLException e) {
         throw new DMLException("정보를 변경하던 중 문제가 발생했습니다. ");
      }
   }

   public boolean isExistAccount(String id, Connection conn) throws SQLException {
      String query = "SELECT user_id from account where user_id=?";
      PreparedStatement ps = conn.prepareStatement(query);
      ps.setString(1, id);
      ResultSet rs = ps.executeQuery();
      return rs.next();
   }

   @Override
   public void addAccount(String id, String account, String bankName) throws DMLException, DuplicateUserException {// 라니라니
      String query = "INSERT into account(account,bankname,user_id) values (?,?,?)";
      try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query)) {
         if (!isExistAccount(id, conn)) {
            ps.setString(1, account);
            ps.setString(2, bankName);
            ps.setString(3, id);
            System.out.println(ps.executeUpdate() + " 개의 계좌가 등록되었습니다.");
         } else
            throw new DuplicateUserException("이미 등록된 계좌입니다.");
      } catch (SQLException e) {
         throw new DMLException("계좌를 등록하던 중 문제가 발생했습니다.");
      }
   }

   @Override
   public ArrayList<Account> getAllAccounts(String id) throws DMLException {
      ArrayList<Account> accounts = new ArrayList<Account>();
      String query = "SELECT account,bankname,user_id FROM account WHERE user_id=?";
      try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query);) {
         ps.setString(1, id);
         try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
               Account account = new Account(rs.getString("account"), rs.getString("bankname"));
               accounts.add(account);
            }
         }
      } catch (SQLException e) {
         throw new DMLException("조회 도중 문제가 생겼습니다.");
      }
      return accounts;
   }

   @Override
   public void depositYouthCard(String id, double price) throws DMLException, RecordNotFoundException {
      String query = "UPDATE guest SET deposite_balance = deposite_balance + ? WHERE id = ?";
      try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query)) {
         ps.setDouble(1, price);
         ps.setString(2, id);
         if (ps.executeUpdate() == 0) {
            throw new RecordNotFoundException(id + " 게스트가 존재하지 않습니다.");
         }
      } catch (SQLException e) {
         throw new DMLException("충전 도중 문제가 생겼습니다.");

      }
   }

   @Override
   public ArrayList<GuestHouse> sortGHsByCount(String dong) throws DMLException {
      ArrayList<GuestHouse> guestHouses = new ArrayList<GuestHouse>();
      String query = "SELECT * FROM guesthouse WHERE dong = ? AND ghcode IN "
            + "(SELECT ghcode FROM reservation WHERE checkin_date BETWEEN DATE_SUB(CURDATE(), INTERVAL 1 YEAR) AND CURDATE() GROUP BY ghcode) "
            + "ORDER BY (SELECT COUNT(*) FROM reservation WHERE reservation.ghcode = guesthouse.ghcode AND checkin_date BETWEEN DATE_SUB(CURDATE(), INTERVAL 1 YEAR) AND CURDATE()) DESC";
      try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query)) {
         ps.setString(1, dong);
         try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
               GuestHouse guestHouse = new GuestHouse(rs.getString("ghcode"), rs.getString("businessnum"),
                     rs.getString("name"), rs.getString("sido"), rs.getString("sigungu"), rs.getString("dong"),
                     rs.getString("detail_address"),rs.getString("host_id"));
               guestHouses.add(guestHouse);
            }
         }
      } catch (SQLException e) {
         throw new DMLException("조회 도중 문제가 생겼습니다.");
      }
      return guestHouses;
   }

   // 리뷰넣고 테스트 필요
//   @Override
//   public HashMap<GuestHouse, Double> sortGHsByStar(String sigungu, String dong) throws DMLException {
//      String query = "SELECT g.ghcode, g.businessnum, g.name, g.sido, g.sigungu, g.dong, g.detail_address, g.host_id, AVG(r.star_rating) AS star\n"
//            + "FROM guesthouse g INNER JOIN review r ON g.ghcode = r.ghcode WHERE g.sigungu = ? AND g.dong = ?\n"
//            + "GROUP BY g.ghcode, g.businessnum, g.name, g.sido, g.sigungu, g.dong, g.detail_address, g.host_id\n"
//            + "ORDER BY star DESC";
//      HashMap<GuestHouse, Double> map = new HashMap<>();
//      try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query);) {
//         ps.setString(1, sigungu);
//         ps.setString(2, dong);
//         try (ResultSet rs = ps.executeQuery()) {
//            while (rs.next()) {
//               map.put(new GuestHouse(rs.getString("g.ghcode"), rs.getString("g.businessnum"),
//                     rs.getString("g.name"), rs.getString("g.sido"), rs.getString("g.sigungu"),
//                     rs.getString("g.dong"), rs.getString("g.detail_address"), rs.getString("g.host_id")),
//                     rs.getDouble("star"));
//            }
//
//         }
//      } catch (SQLException e) {
//         throw new DMLException("문!제!발!생!");
//      }
//
//      return map;
//   }
   
   @Override
   public ArrayList<GuestHouse> sortGHsByStar(String sigungu, String dong) throws DMLException {
      String query = "SELECT g.ghcode, g.businessnum, g.name, g.sido, g.sigungu, g.dong, g.detail_address, g.host_id, AVG(r.star_rating) AS star\n"
            + "FROM guesthouse g INNER JOIN review r ON g.ghcode = r.ghcode WHERE g.sigungu = ? AND g.dong = ?\n"
            + "GROUP BY g.ghcode, g.businessnum, g.name, g.sido, g.sigungu, g.dong, g.detail_address, g.host_id\n"
            + "ORDER BY star DESC";
      ArrayList<GuestHouse> arr = new ArrayList<>();
      try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query);) {
         ps.setString(1, sigungu);
         ps.setString(2, dong);
         try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
               arr.add(new GuestHouse(rs.getString("g.ghcode"), rs.getString("g.businessnum"),
                     rs.getString("g.name"), rs.getString("g.sido"), rs.getString("g.sigungu"),
                     rs.getString("g.dong"), rs.getString("g.detail_address"), rs.getString("g.host_id")));
            }

         }
      } catch (SQLException e) {
         throw new DMLException("문!제!발!생!");
      }

      return arr;
   }

   @Override
   public ArrayList<GuestHouse> sortGHs(int min, int max) throws DMLException, InvalidInputException {
       ArrayList<GuestHouse> guestHouses = new ArrayList<>();

       if (min > max)
           throw new InvalidInputException("최대값이 최소값보다 작습니다.");
       if (max < 0 || min < 0)
           throw new InvalidInputException("음수는 입력할 수 없습니다.");

       String query = 
           "SELECT DISTINCT g.ghcode, g.businessnum, g.name, g.sido, g.sigungu, g.dong, g.detail_address, g.host_id, r.price " +
           "FROM guesthouse g " +
           "JOIN room r ON g.ghcode = r.ghcode " +
           "WHERE r.price BETWEEN ? AND ? " +
           "ORDER BY r.price";

       try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query)) {
           ps.setInt(1, min);
           ps.setInt(2, max);

           try (ResultSet rs = ps.executeQuery();) {
        	   
        	   while (rs.next()) {
                   guestHouses.add(new GuestHouse(rs.getString("ghcode"), rs.getString("businessnum"),
                         rs.getString("name"), rs.getString("sido"), rs.getString("sigungu"),
                         rs.getString("dong"), rs.getString("detail_address"), rs.getString("host_id")));
                }
           }
       } catch (SQLException e) {
           throw new DMLException("게스트하우스를 찾는 중 문제가 발생했습니다.");
       }

       return guestHouses;
   }

   @Override
   public ArrayList<GuestHouse> sortGHs(int limit) throws DMLException {
       ArrayList<GuestHouse> guestHouses = new ArrayList<>();
       String query = "SELECT * FROM guesthouse ORDER BY record_date DESC LIMIT ?";

       try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query)) {
           ps.setInt(1, limit);
           try (ResultSet rs = ps.executeQuery()) {
               while (rs.next()) {
                   GuestHouse guestHouse = new GuestHouse(
                       rs.getString("ghcode"),
                       rs.getString("businessnum"),
                       rs.getString("name"),
                       rs.getString("sido"),
                       rs.getString("sigungu"),
                       rs.getString("dong"),
                       rs.getString("detail_address"),
                       rs.getString("host_id")
                   );
                   guestHouses.add(guestHouse);
               }
           }
       } catch (SQLException e) {
           throw new DMLException("조회 도중 문제가 생겼습니다.");
       }

       return guestHouses;
   }

   @Override
   public ArrayList<GuestHouse> sortGHs() throws DMLException {
      ArrayList<GuestHouse> guestHouses = new ArrayList<>();

       String query = "SELECT g.ghcode as ghcodes, AVG(v.star_rating) AS avg_rating, COUNT(*) AS total_sales, g.name as gname, g.businessnum as gbusinessnum, g.sido as gsido, g.sigungu as gsigungu, g.dong as gdong, g.detail_address as gdetailaddress, g.host_id as ghostid "
             + "FROM (reservation r JOIN review v ON r.reservation_id = v.reservation_id) "
             + "JOIN GuestHouse g on r.ghcode = g.ghcode "
             + "GROUP BY g.ghcode";
       ResultSet rs = null;

       // HashMap<String, Double> scoreMap = new HashMap<>();
       ArrayList<GuestHouseScore> scores = new ArrayList<GuestHouseScore>();
       ArrayList<RawGHData> rawList = new ArrayList<RawGHData>();
       double maxPrice = 0, maxRating = 0, maxSales = 0;
        double minPrice = Double.MAX_VALUE, minRating = Double.MAX_VALUE, minSales = Double.MAX_VALUE;
        GuestHouse guesthouse = null;
       
       try (Connection conn = getConnect();
            PreparedStatement ps = conn.prepareStatement(query);) {
          
          rs = ps.executeQuery();

           // 1. raw data 수집 및 최대값 계산
           while (rs.next()) {
              // (String ghcode, String businessNum, String name, String sido, String sigungu, String dong, String detailAddress, String hostID)
              guesthouse = new GuestHouse(rs.getString("ghcodes"),rs.getString("gbusinessnum"),rs.getString("gname"),rs.getString("gsido"),rs.getString("gsigungu"),rs.getString("gdong"),rs.getString("gdetailaddress"),rs.getString("ghostid"));
               double rating = rs.getDouble("avg_rating");
               double sales = rs.getDouble("total_sales");

               maxRating = Math.max(maxRating, rating);
               maxSales = Math.max(maxSales, sales);
               
               minRating = Math.min(minRating, rating);
               minSales = Math.min(minSales, sales);
               
               

               rawList.add(new RawGHData(guesthouse, rating, sales));
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }
       rs = null;
       query = "SELECT ghcode,AVG(price) as avg_price FROM ROOM GROUP BY ghcode";
       try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query);) {
          rs = ps.executeQuery();
          while(rs.next()) {
             String ghcode = rs.getString("ghcode");
             double price = rs.getDouble("avg_price");
             
             maxPrice = Math.max(maxPrice, price);
             minPrice = Math.min(minPrice, price);
             
             for (RawGHData raw : rawList) {
                   if (raw.getGuesthouse().getGhcode().equals(ghcode)) {
                       raw.setPrice(price);
                       break;
                   }
               }
          }
          
       } catch (SQLException e) {
           e.printStackTrace();
       }

      // 2. 정규화 및 점수 계산
      for (RawGHData raw : rawList) {
         // (값 - 최소값) / (최대값 - 최소값)
         
         double normPrice = maxPrice == minPrice ? 0 : (raw.price - minPrice) / (maxPrice - minPrice);
         double normRating = maxRating == minRating ? 0 : (raw.rating - minRating) / (maxRating - minRating);
         double normSales = maxSales == minSales ? 0 : (raw.sales - minSales) / (maxSales - minSales);

         double score = (1 - normPrice) * 0.1 + normRating * 0.4 + normSales * 0.5;
         scores.add(new GuestHouseScore(raw.getGuesthouse(), score));
      }
      

      // 3. 점수 순 정렬
      scores.sort((a, b) -> Double.compare(b.score, a.score));
      String query001 = "SELECT * from GuestHouse WHERE ";
      String end001 = "(";
      for(GuestHouseScore s : scores) {
         query001 += "ghcode = ? or";
         end001 += "?,";
      }

       for (GuestHouseScore s : scores) {
           GuestHouse gh = s.getGuesthouse();
          if (gh != null) {
                  guestHouses.add(gh);
              }
          }

          return guestHouses;
   }



   /*
    *       this.roomno = roomno;
      this.gender = gender;
      this.capacity = capacity;
      this.price = price;
      this.overview = overview;
    */
   
   public Room getRoomByCode(String ghcode,String roomno) {
      String query = "SELECT * FROM ROOM WHERE ghcode = ? AND roomno = ?";
      try(Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query)) {
         ps.setString(1, ghcode);
         ps.setString(2, roomno);
         try(ResultSet rs = ps.executeQuery();) {
            if(rs.next()) {
               return new Room(
                     rs.getString("roomno"),
                     rs.getString("gender"),
                     rs.getInt("capacity"),
                     rs.getDouble("price"),
                     rs.getString("overview")
                     );
            }
         }
      } catch(SQLException e) {
         e.printStackTrace();
      }
      return null;
   }
   
   @Override
   public void writeReview(Review review, Guest guest, Reservation reservation) throws DMLException {
      /*
       * public Review(String reviewID, String text, String starRating, String
       * reservationID) { super(); this.reviewID = reviewID; this.text = text;
       * this.starRating = starRating; this.reservationID = reservationID; }
       */
      String query = "INSERT INTO review (text, star_rating, user_id, reservation_id, roomno, ghcode) VALUES (?,?,?,?,?,?)";
      try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query);) {
         ps.setString(1, review.getText());
         ps.setInt(2, review.getStarRating());
         ps.setString(3, guest.getId());
         ps.setString(4, review.getReservationID());
         ps.setString(5, reservation.getRoomno());
         ps.setString(6, reservation.getGhcode());
         System.out.println(ps.executeUpdate() + "개 리뷰 작성");
      } catch (SQLException e) {
         throw new DMLException("리뷰를 추가할 수 없습니다..");
      } 

   }

   @Override
   public GuestHouse getGH(String ghcode) throws DMLException {// 라니라니
      String query = "SELECT * from guesthouse where ghcode=?";
      GuestHouse gh = null;
      try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query)) {
         ps.setString(1, ghcode);
         try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
               gh = new GuestHouse(rs.getString("ghcode"), rs.getString("businessnum"), rs.getString("name"),
                     rs.getString("sido"), rs.getString("sigungu"), rs.getString("dong"),
                     rs.getString("detail_address"),rs.getString("host_id"));
            }
         }
      } catch (SQLException e) {
         throw new DMLException("게스트 하우스를 찾는 중 문제가 발생했습니다.");
      }
      return gh;
   }

   @Override
   public void addGH(GuestHouse gh) throws DMLException {
      String query = "INSERT INTO GuestHouse (ghcode, businessnum, name, sido, sigungu, dong, detail_address, host_id) "
            + "SELECT CONCAT(sc.region_code, RIGHT(?, 5)), ?, ?, ?, ?, ?, ?, ? "
            + "FROM sido_code sc WHERE sc.sido_name = ?";

      try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query)) {
         ps.setString(1, gh.getBusinessNum()); // RIGHT(?, 5)
         ps.setString(2, gh.getBusinessNum());
         ps.setString(3, gh.getName());
         ps.setString(4, gh.getSido());
         ps.setString(5, gh.getSigungu());
         ps.setString(6, gh.getDong());
         ps.setString(7, gh.getDetailAddress());
         ps.setString(8, gh.getHostID());
         ps.setString(9, gh.getSido()); // WHERE sc.sido_name = ?

         int result = ps.executeUpdate();
         System.out.println(result == 1 ? "게스트하우스 등록 성공!" : "게스트하우스 등록 실패! 시/도명을 확인하세요.");
      } catch (SQLException e) {
         throw new DMLException("게스트하우스 등록 중 오류 발생");
      }
   }

//   @Override
//   public void addGH(GuestHouse gh) throws DMLException{
//       String query = "INSERT INTO GuestHouse (ghcode, businessnum, name, sido, sigungu, dong, detail_address, host_id) VALUES(?,?,?,?,?,?,?,?)";
//
//       try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query)) {
//
//           // 지역코드 매핑
//           String regionCode = SIDO_CODE_MAP.getOrDefault(gh.getSido(), "00");
//
//           // 사업자번호에서 숫자만 추출 후 마지막 5자리
//           String businessNum = gh.getBusinessNum().replaceAll("-", "");
//           String bizTail = businessNum.length() >= 5 ?
//                            businessNum.substring(businessNum.length() - 5) : businessNum;
//
//           // ghcode 조합
//           String ghcode = regionCode + bizTail;
//
//           // SQL 세팅
//           ps.setString(1, ghcode);
//           ps.setString(2, gh.getBusinessNum());
//           ps.setString(3, gh.getName());
//           ps.setString(4, gh.getSido());
//           ps.setString(5, gh.getSigungu());
//           ps.setString(6, gh.getDong());
//           ps.setString(7, gh.getDetailAddress());
//           ps.setString(8, gh.getHostID());
//
//           System.out.println(ps.executeUpdate() + "개 게스트하우스 INSERT 성공....");
//       } catch (SQLException e) {
//           throw new DMLException("회원가입을 하던 중 오류가 발생했습니다.");
//       }
//   }

   @Override
   public void deleteGH(String ghcode) throws DMLException, RecordNotFoundException {// 라니라니
      System.out.println("관리자에게 전화하십시오~~~~~");
      int result = 0;
      String query = "DELETE FROM Guesthouse where ghcode=?";
      try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query)) {
         ps.setString(1, ghcode);
         result = ps.executeUpdate();
         if (result != 0)
            System.out.println("삭제 성공!!");
         else
            throw new RecordNotFoundException("삭제하려는 게스트하우스가 없습니다.");
      } catch (SQLException e) {
         throw new DMLException("게스트하우스를 삭제하던중 문제가 발생했습니다. ");
      }

   }

   @Override
   public void deleteRoom(String ghcode, String roomno) throws RecordNotFoundException, DMLException {
      String query = "DELETE FROM room WHERE roomno=? AND ghcode=?";
      try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query);) {
         ps.setString(1, roomno);
         ps.setString(2, ghcode);
         if (ps.executeUpdate() == 0)
            throw new RecordNotFoundException("해당 방이 존재하지 않습니다.");
      } catch (SQLException e) {
         throw new DMLException("삭제 진행 도중 문제가 발생하였습니다...");
      }
   }

   @Override
   public void addRoom(String ghcode, Room room) throws DMLException {
      // TODO Auto-generated method stub
      String query = "INSERT INTO ROOM (roomno,gender,capacity,price,overview,ghcode) VALUES (?,?,?,?,?,?)";
      try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query)) {
         ps.setString(1, room.getRoomno());
         ps.setString(2, room.getGender());
         ps.setInt(3, room.getCapacity());
         ps.setDouble(4, room.getPrice());
         ps.setString(5, room.getOverview());
         ps.setString(6, ghcode);
         System.out.println(ps.executeUpdate() + "개 룸 생성");
      } catch (SQLException e) {
         throw new DMLException("예약하는 도중 오류가 발생하였습니다.");
      }

   }

   public boolean isExistRoom(String ghcode, String roomno, Connection conn) throws SQLException { // 방 존재
      String query = "SELECT ghcode,roomno from room where ghcode=? and roomno=?";
      try (PreparedStatement ps = conn.prepareStatement(query)) {
         ps.setString(1, ghcode);
         ps.setString(2, roomno);
         try (ResultSet rs = ps.executeQuery()) {
            return rs.next();
         }
      }
   }

   @Override
   public void updateRoom(String ghcode, String roomno, int capacity) throws RecordNotFoundException, DMLException {
      String query = "UPDATE room SET capacity=? where roomno=? AND ghcode=?";
      try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query)) {
         if (isExistRoom(ghcode, roomno, conn)) {
            ps.setInt(1, capacity);
            ps.setString(2, roomno);
            ps.setString(3, ghcode);
            System.out.println(ps.executeUpdate() + "개의 방의 정보가 변경되었습니다. ");
         } else
            throw new RecordNotFoundException("등록된 방이 없습니다.");
      } catch (SQLException e) {
         throw new DMLException("정보 수정 중 문제 발생.");
      }
   }

   @Override
   public void updateRoom(String ghcode, String roomno, String gender) throws DMLException, RecordNotFoundException {// 라니라
      String query = "UPDATE room set gender=? where roomno=? and ghcode=?";
      try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query)) {
         if (isExistRoom(ghcode, roomno, conn)) {
            ps.setString(1, gender);
            ps.setString(2, roomno);
            ps.setString(3, ghcode);
            System.out.println(ps.executeUpdate() + "개의 방의 정보가 변경되었습니다. ");
         } else
            throw new RecordNotFoundException("등록된 방이 없습니다.");
      } catch (SQLException e) {
         throw new DMLException("정보를 수정하던 중 문제가 발생했습니다.");
      }
   }

   @Override
   public double getRevenue(int year, int month, String ghcode) throws InvalidInputException, DMLException {
      // 특정 게하의 해당 월의 매출 출력
      String query = "SELECT SUM(price) total_price FROM reservation WHERE ghcode = ? AND year(checkin_date) = ? AND month(checkin_date) = ?";
      try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query)) {
         if (year > LocalDate.now().getYear() || year < 0 || month < 1 || month > 12) {
            throw new InvalidInputException("유효하지 않은 연도 또는 월입니다.");
         }
         ps.setString(1, ghcode);
         ps.setInt(2, year);
         ps.setInt(3, month);
         try (ResultSet rs = ps.executeQuery();) {
            if (rs.next())
               return rs.getDouble("total_price");
         }

      } catch (SQLException e) {
         throw new DMLException("매출 집계 도중 문제가 발생했습니다. ");
      }
      return 0;
   }

   @Override
   public double getRevenue(String ghcode, int year) throws DMLException{
      // TODO Auto-generated method stub
      String query = "SELECT g.name as GHName, sum(r.price) as revenue FROM reservation r join guesthouse g on r.ghcode = g.ghcode WHERE year(r.checkin_date) = '2024' AND r.ghcode = ?";
      ResultSet rs = null;
      double sum = 0;
      try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query)) {
         ps.setInt(1, year);
         ps.setString(2, ghcode);
         rs = ps.executeQuery();
         while(rs.next()) {
            sum += rs.getDouble("revenue");
         }
      } catch (SQLException e) {
         throw new DMLException("예약하는 도중 오류가 발생하였습니다.");
      }
      
      return sum;
   }
   
   @Override
   public double getRevenue(int year, int month, int day, String ghcode) throws DMLException {
         double totalRevenue = 0.0;
          String query = "SELECT SUM(price) as daily_sales FROM reservation WHERE checkin_date = ? AND ghcode = ?";

          try (
              Connection conn = getConnect();
              PreparedStatement ps = conn.prepareStatement(query)
          ) {
              LocalDate localDate = LocalDate.of(year, month, day);
              ps.setDate(1, Date.valueOf(localDate));
              ps.setString(2, ghcode);

              try (ResultSet rs = ps.executeQuery()) {
                  if (rs.next()) {
                      totalRevenue = rs.getDouble("daily_sales");
                  }
              }
          } catch (SQLException e) {
              throw new DMLException("매출 조회 중 오류가 발생했습니다.");
          }

          return totalRevenue;
      }

   @Override
   public double getRevenue(int year, String quater) throws DMLException{//라니라니
      double sales=0;
      String query="select r.ghcode,g.name,year(r.checkin_date) year,Quarter(r.checkin_date) quarter, "
            + "sum(r.price) total_revenue "
            + "from reservation r "
            + "join guesthouse g "
            + "on r.ghcode=g.ghcode "
            + "where year(r.checkin_date)=? and quarter(r.checkin_date)=? "
            + "group by r.ghcode,g.name,YEAR(r.checkin_date),QUARTER(r.checkin_date) "
            + "ORDER BY total_revenue DESC";
      try(Connection conn=getConnect();PreparedStatement ps=conn.prepareStatement(query)) {
         ps.setInt(1, year);
         ps.setString(2, quater);
         
         try(ResultSet rs=ps.executeQuery()){
              while (rs.next()) {
                      double total = rs.getDouble("total_revenue");
                      sales += total;
                  }
         }
      } catch (SQLException e) {
         throw new DMLException("매출 통계중 문제가 발생했습니다.");
      }
      return sales;
   }
   
   @Override
   public double getRevenue(int year, String quater, String ghcode) throws DMLException{
      double sales=0;
      String query = "SELECT year(checkin_date) year, Quarter(checkin_date) quarter, sum(price) total_revenue "
            + "from reservation "
            + "where ghcode=? "
            + "AND year(checkin_date)=? "
            + "AND quarter(checkin_date)=? "
            + "GROUP BY YEAR(checkin_date),quarter(checkin_date)";
      try(Connection conn=getConnect();PreparedStatement ps=conn.prepareStatement(query)){
         ps.setString(1, ghcode);
         ps.setInt(2, year);
         ps.setString(3, quater);
         
         try(ResultSet rs=ps.executeQuery()){
            while(rs.next()) {
                   double total = rs.getDouble("total_revenue");
                   sales += total;
            }
         }
      } catch (SQLException e) {
         throw new DMLException("정보를 가져오던 중 오류가 발생했습니다~");
      }
      return sales;
   }

   @Override
   public ArrayList<Room> getAllRooms(String ghcode) throws DMLException{//라니라니
      ArrayList<Room> temp=new ArrayList<Room>();
      String query="SELECT * from room where ghcode = ?";
      try(Connection conn=getConnect();PreparedStatement ps=conn.prepareStatement(query)) {
         ps.setString(1, ghcode);
         try(ResultSet rs=ps.executeQuery()){
            while(rs.next()) {
               Room room = new Room(rs.getString("roomno"), rs.getString("gender"), 
                     rs.getInt("capacity"), rs.getDouble("price"), rs.getString("overview"));
               temp.add(room);
            }
         }
      } catch (SQLException e) {
         throw new DMLException("방을 검색하던 중 문제가 발생했습니다. ");
      }
      return temp;
   }

   @Override
   public Room getARoom(String ghcode, String roomno) throws DMLException, RecordNotFoundException {
      String query = "SELECT * FROM room WHERE roomno = ? AND ghcode = ?";
      // 특정 방 정보를 담은 객체를 반환
      try (Connection conn = getConnect(); PreparedStatement ps = conn.prepareStatement(query);) {
         if (isExistRoom(ghcode, roomno, conn)) {
            ps.setString(1, roomno);
            ps.setString(2, ghcode);
            try (ResultSet rs = ps.executeQuery();) {
               if (rs.next()) {
                  return new Room(roomno, rs.getString("gender"), rs.getInt("capacity"), rs.getDouble("price"),
                        rs.getString("overview"));
               }
            }
         } else
            throw new RecordNotFoundException("등록된 방이 없습니다");
      } catch (SQLException e) {
         throw new DMLException("방 정보를 가져오는 도중 문제가 발생했습니다. ");
      }
      return null;
   }

//   private static final Map<String, String> SIDO_CODE_MAP = Map.ofEntries(
//          Map.entry("서울특별시", "11"),
//          Map.entry("부산광역시", "26"),
//          Map.entry("대구광역시", "27"),
//          Map.entry("인천광역시", "28"),
//          Map.entry("광주광역시", "29"),
//          Map.entry("대전광역시", "30"),
//          Map.entry("울산광역시", "31"),
//          Map.entry("세종특별자치시", "36"),
//          Map.entry("경기도", "41"),
//          Map.entry("강원도", "42"),
//          Map.entry("충청북도", "43"),
//          Map.entry("충청남도", "44"),
//          Map.entry("전라북도", "45"),
//          Map.entry("전라남도", "46"),
//          Map.entry("경상북도", "47"),
//          Map.entry("경상남도", "48"),
//          Map.entry("제주특별자치도", "50")
//      );

}

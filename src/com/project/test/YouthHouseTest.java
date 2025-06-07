package com.project.test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

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
import com.project.vo.Room;
import com.project.dao.YouthHouseTemplate;
import com.project.dao.impl.YouthHouseImpl;
import com.project.enums.PaymentType;

public class YouthHouseTest {

	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		YouthHouseImpl service = YouthHouseImpl.getInstance();
		boolean running = true;

		while (running) {
		    System.out.println("\n━━━━━━━━━━━━━━━━━━━━━━");
		    System.out.println("   🏠 유스하우스 플랫폼");
		    System.out.println("━━━━━━━━━━━━━━━━━━━━━━");
		    System.out.println("1. 로그인");
		    System.out.println("2. 회원가입");
		    System.out.println("3. 종료");
		    System.out.print("메뉴를 선택하세요 👉 ");
		    int choice = sc.nextInt();

			switch (choice) {
			case 1:
				System.out.println("1. 👤게스트 로그인");
				System.out.println("2. 🧑‍💼호스트 로그인");
				System.out.print("선택 👉 ");
				int loginType = sc.nextInt();
				if (loginType == 1) {
					guestService(service);
				} else if (loginType == 2) {
					System.out.print("호스트 ID를 입력하세요: ");
					String id = sc.next();
					System.out.print("비밀번호를 입력하세요: ");
					String password = sc.next();
					try {
						Host host = service.loginHost(id, password);
						System.out.println("호스트 로그인 시도: " + id);
						hostService(service, host); // 호스트 로그인 메소드 호출
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				} else {
					System.out.println("잘못된 입력입니다.");
				}
				break;

			case 2:
				System.out.println("1. 게스트 회원가입");
				System.out.println("2. 호스트 회원가입");
				System.out.print("선택: ");
				int signInType = sc.nextInt();
				if (signInType == 1) {
					System.out.println("\n아이디 입력: ");
					String id = sc.next();
					System.out.println("비밀번호 입력: ");
					String pw = sc.next();
					System.out.println("이름 입력: ");
					String name = sc.next();
					System.out.println("전화번호 입력: ");
					String phone = sc.next();
					System.out.println("성별 입력(M/F): ");
					String gender = sc.next();
					Random rand = new Random();
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < 9; i++) {
						sb.append(rand.nextInt(10)); // 0~9 랜덤 숫자 계좌 생성
					}
					try {
						service.addUser(new Guest(id, name, pw, phone, gender, sb.toString()));
					} catch (DMLException e) {
						System.out.println(e.getMessage());
					} catch (DuplicateUserException e) {
						System.out.println(e.getMessage());
					}
				} else if (signInType == 2) {
					// 호스트 회원가입 필요
				} else {
					System.out.println("잘못된 입력입니다.");
				}

				System.out.println("회원 가입에 성공하셨습니다.");
				break;

			case 3:
				System.out.println("프로그램을 종료합니다.");
				running = false;
				break;

			default:
				System.out.println("잘못된 입력입니다. 다시 선택해주세요.");
				break;
			}
		}

		sc.close();
	}

	public static void guestService(YouthHouseImpl yh) {
		Scanner sc = new Scanner(System.in);
		String id;
		Guest g = null;
		System.out.println("닉네임 입력: ");
		id = sc.next();
		System.out.println("비밀번호 입력: ");
		String pw = sc.next();
		try {
			g = yh.loginGuest(id, pw);
		} catch (RecordNotFoundException e) {
			System.out.println(e.getMessage());
			System.out.println("메인 화면으로 돌아갑니다");
			return;
		} catch (DMLException e) {
			System.out.println(e.getMessage());
		}
		boolean flag = true;
		while (flag) {
			System.out.println("✨ 청춘 게스트하우스에 오신 걸 환영합니다! ✨  \n"
					+ "      codus님, 반가워요! 😊  \n"
					+ "🏡 원하는 숙소를 골라 편하게 예약하세요!\n\n"
					+ "1. 예약 내역 확인\n"
					+ "2. 게스트하우스 목록 확인\n" 
					+ "3. 계좌 추가\n" 
					+ "4. 예치금 입금\n" 
					+ "5. 회원 정보 변경\n" 
					+ "6. 메인 화면으로\n");
			switch (sc.next()) {
			case "1":
				showMyReserve(sc, yh, g);
				break;
			case "2":
				showGHs(sc, yh, g);
				break;
			case "3":
				addAccount(sc, yh, g);
				break;
			case "4":
				depositAccount(sc, yh, g);
				break;
			case "5":
				updateMyInfo(sc, yh, g);
				break;
			case "6":
				flag = false;
				break;
			default:
				System.out.println("다시 입력해주세요");

			}

		}
	}

	private static void updateMyInfo(Scanner sc, YouthHouseImpl yh, Guest g) {
	    System.out.println("\n🔧 회원 정보 수정");
	    System.out.println("--------------------------");

	    try {
	        System.out.print("새 이름을 입력하세요 (현재: " + g.getName() + "): ");
	        String name = sc.next();
	        System.out.print("새 비밀번호를 입력하세요: ");
	        String pass = sc.next();
	        System.out.print("새 전화번호를 입력하세요 (현재: " + g.getPhone() + "): ");
	        String phone = sc.next();
	        System.out.print("새 성별을 입력하세요 (M/F): ");
	        String gender = sc.next();

	        // Guest 객체 업데이트
	        g.setName(name);
	        g.setPass(pass);
	        g.setPhone(phone);
	        g.setGender(gender);

	        yh.updateUser(g);
	        System.out.println("✅ 회원 정보가 성공적으로 수정되었습니다.");
	    } catch (RecordNotFoundException e) {
	        System.out.println("❌ " + e.getMessage());
	    } catch (DMLException e) {
	        System.out.println("⚠️ 수정 중 오류가 발생했습니다: " + e.getMessage());
	    }
	}

	private static void depositAccount(Scanner sc, YouthHouseImpl yh, Guest g) {
	    System.out.println("\n💰 예치금 충전 메뉴");
	    System.out.println("──────────────────────────────");

	    try {
	        System.out.print("충전할 금액을 입력하세요 (단위: 원): ");
	        double amount = sc.nextDouble();

	        if (amount <= 0) {
	            System.out.println("0원 이하의 금액은 충전할 수 없습니다.");
	            return;
	        }

	        yh.depositYouthCard(g.getId(), amount);
	        System.out.println("✅ " + String.format("%,.0f", amount) + "원이 성공적으로 충전되었습니다!");
	    } catch (RecordNotFoundException e) {
	        System.out.println("⚠️ " + e.getMessage());
	    } catch (DMLException e) {
	        System.out.println("⚠️ 충전 중 문제가 발생했습니다: " + e.getMessage());
	    }
	}

	private static void addAccount(Scanner sc, YouthHouseImpl yh, Guest g) {
		try {
			ArrayList<Account> accounts = yh.getAllAccounts(g.getId());
			if(accounts != null) {
				System.out.println("📒회원님의 계좌 목록\n");
				for(Account a : accounts) {
					System.out.println("["+a.getAccount()+" "+a.getBankName()+"은행]");
				}
			}else {
				System.out.println("등록된 계좌가 없습니다");
			}
			System.out.println("계좌를 추가하시겠습니까?(네/아니요)");
			String answer = sc.next();
			if(answer.equals("네")) {
				System.out.println("은행명을 입력하세요.");
				String bankName = sc.next();
				System.out.println("계좌를 입력하세요.");
				String account = sc.next();
				try {
					yh.addAccount(g.getId(), account, bankName);
				} catch (DuplicateUserException e) {
					System.out.println(e.getMessage());
				}
			}else {
				System.out.println("이전으로 돌아갑니다.");
			}
		} catch (DMLException e) {
			System.out.println(e.getMessage());
		}
		
	}

	private static void showGHs(Scanner sc, YouthHouseImpl yh, Guest g) {
		// 게스트 하우스 목록 보기
		boolean flag = true;
		String sigungu = "";
		String dong = "";
		ArrayList<GuestHouse> gh = null;
		while (flag) {
			System.out.println("원하는 게스트 하우스 정렬을 선택해주세요\n\n" 
					+ "1. 지역별 인기순(예약 건수 많은 순)\n"
					+ "2. 지역별 인기순(별점⭐ 높은 순)\n" 
					+ "3. 최근에 등록된 지점 순\n" 
//					+ "4. 방이 가장 저렴한 순\n" 
					+ "4. 원하는 가격 범위의 게스트 하우스 검색\n"
					+ "5. 추천순✨\n"
					+ "6. 뒤로가기");
			switch (sc.next()) {
			case "1":
				System.out.println("원하는 시/군/구를 작성해주세요: \n");
				sigungu = sc.next();
				System.out.println("원하는 동을 작성해주세요: \n");
				dong = sc.next();
				try {
					gh= yh.sortGHsByCount(dong);
				} catch (DMLException e) {
					System.out.println(e.getMessage());
					break;
				}
				if(gh == null) {
					System.out.println("해당 지역의 게스트 하우스가 없습니다. ");
				}else {
					viewGHList(gh, sc, yh, g);
				}
				break;
			case "2":
				System.out.println("원하는 시/군/구를 작성해주세요: ");
				sigungu = sc.next();
				System.out.println("원하는 동을 작성해주세요: ");
				dong = sc.next();
				try {
					gh = yh.sortGHsByStar(sigungu,dong);
				} catch (DMLException e) {
					System.out.println(e.getMessage());
					break;
				}
				if(gh == null) {
					System.out.println("해당 지역의 게스트 하우스가 없습니다. ");
				}else {
					viewGHList(gh, sc, yh, g);
				}
				break;
			case "3":
			    System.out.println("✨ 최근에 등록된 게스트하우스 5 ✨");
			    ArrayList<GuestHouse> recentGHs = null;
			    try {
			        recentGHs = yh.sortGHs(5);
			    } catch (DMLException e) {
			        System.out.println(e.getMessage());
			        break;
			    }

			    if (recentGHs == null || recentGHs.isEmpty()) {
			        System.out.println("최근 등록된 게스트하우스가 없습니다.");
			        break;
			    }

			    for (int i = 0; i < recentGHs.size(); i++) {
			        GuestHouse gh1 = recentGHs.get(i);
			        System.out.printf("%d. %s (%s %s %s)\n", i + 1, gh1.getName(), gh1.getSido(), gh1.getSigungu(), gh1.getDong());
			    }

			    System.out.println("\n상세히 보고 싶은 번호를 입력하세요 (b: 뒤로가기)");
			    System.out.print("👉 선택: ");
			    String input = sc.next();

			    if (input.equalsIgnoreCase("b")) break;
			    try {
			        int sel = Integer.parseInt(input) - 1;
			        if (sel >= 0 && sel < recentGHs.size()) {
			            viewGHDetail(recentGHs.get(sel), sc, yh, g);
			        } else {
			            System.out.println("잘못된 번호입니다.");
			        }
			    } catch (NumberFormatException e) {
			        System.out.println("숫자를 정확히 입력하세요.");
			    }

			    break;
			case "4":
				System.out.print("원하는 최소 가격: \n");
				int min = sc.nextInt();
				System.out.print("원하는 최대 가격: \n");
				int max = sc.nextInt();
				System.out.print(min+ "원 ~ "+max+"원 까지의 방을 가진 게스트하우스입니다.\n");
				try {
					gh = yh.sortGHs(min, max);
				} catch (DMLException | InvalidInputException e) {
					System.out.println(e.getMessage());
				} 
				if(gh == null) {
					System.out.println("해당 지역의 게스트 하우스가 없습니다. ");
				}else {
					viewGHList(gh, sc, yh, g);
				}
				break;
			case "5":
				System.out.println("✨플랫폼 추천 순✨\n"
						+"판매량, 별점순, 가격(낮은)순을 종합 ");
				try {
					gh = yh.sortGHs();
				} catch (DMLException e) {
					System.out.println(e.getMessage());
				}
				if(gh == null) {
					System.out.println("해당 지역의 게스트 하우스가 없습니다. ");
				}else {
					viewGHList(gh, sc, yh, g);
				}
				break;
			case "6":
				flag = false;
				break;
			default :
				System.out.println("다시 입력해주세요");
			}
		}
	}
	private static void showMyReserve(Scanner sc, YouthHouseImpl yh, Guest g) {
	    System.out.println(g.getName() + "님의 예약 내역입니다.^^");

	    try {
	        ArrayList<Reservation> reservs = yh.getAllReservations(g.getId());

	        if (reservs.isEmpty()) {
	            System.out.println("예약 내역이 없습니다.");
	            return;
	        }

	        for (int i = 0; i < reservs.size(); i++) {
	            Reservation rs = reservs.get(i);
	            System.out.println((i + 1) + ". " + rs.getGhcode()+" " + rs.getRoomno() + " | " 
	                + rs.getCheckinDate() + " ~ " + rs.getCheckoutDate() 
	                + " | 인원: " + rs.getHeadCount() + "명 | 결제: " + rs.getPaymentType());
	        }

	        System.out.print("\n상세히 보고 싶은 번호를 입력하세요 (b: 뒤로가기)\n 👉 선택: ");
	        String cmd = sc.next();
	        if (cmd.equalsIgnoreCase("b")) return;

	        int sel = Integer.parseInt(cmd) - 1;
	        if (sel < 0 || sel >= reservs.size()) {
	            System.out.println("잘못된 번호입니다.");
	            return;
	        }

	        Reservation target = reservs.get(sel);
	        System.out.println("\n--- 예약 상세 정보 ---");
	        System.out.println("지점코드: " + target.getGhcode());
	        System.out.println("방번호: " + target.getRoomno());
	        System.out.println("인원: " + target.getHeadCount());
	        System.out.println("가격: " + target.getPrice());
	        System.out.println("체크인: " + target.getCheckinDate());
	        System.out.println("체크아웃: " + target.getCheckoutDate());
	        System.out.println("결제방식: " + target.getPaymentType());

	        System.out.println("\n1. 예약 수정 / 2. 예약 취소 / [b] 뒤로가기");
	        String action = sc.next();
	        if (action.equals("1")) {
	        	System.out.println("\n1. 날짜 변경 / 2. 방 및 인원수 변경 / [b] 뒤로가기");
	        	String updateType = sc.next();
	        	if(updateType.equals("1")) {
	        		System.out.println("새 체크인 날짜 (yyyy mm dd): ");
		            int y1 = sc.nextInt(), m1 = sc.nextInt(), d1 = sc.nextInt();
		            System.out.println("새 체크아웃 날짜 (yyyy mm dd): ");
		            int y2 = sc.nextInt(), m2 = sc.nextInt(), d2 = sc.nextInt();
		            //target.setCheckinDate(new Mydate(y1, m1, d1));
		            //target.setCheckoutDate(new Mydate(y2, m2, d2));
		            try {
						yh.updateReservation(target.getReservationID(), new Mydate(y1, m1, d1), new Mydate(y2, m2, d2));
					} catch (RecordNotFoundException e) {
						System.out.println(e.getMessage());
					}
		            
	        	} else if(updateType.equals("2")) {
	        	    try {
	        	        System.out.println("\n--- 변경 가능한 방 목록 ---");
	        	        ArrayList<Room> rooms = yh.getAllRooms(target.getGhcode());
	        	        for (int i = 0; i < rooms.size(); i++) {
	        	            Room r = rooms.get(i);
	        	            System.out.printf("%d. 방번호: %s | 인원: %d명 | 가격: %.0f원\n", i + 1, r.getRoomno(), r.getCapacity(), r.getPrice());
	        	        }

	        	        System.out.print("\n변경할 방 번호(roomno)를 입력하세요: \n");
	        	        String newRoomno = sc.next();

	        	        System.out.print("변경할 인원수를 입력하세요: \n");
	        	        int newHead = sc.nextInt();

	        	        boolean canBook = yh.isPossibleReservation(
	        	            target.getGhcode(), newRoomno, 
	        	            target.getCheckinDate(), target.getCheckoutDate(), newHead
	        	        );

	        	        if (!canBook) {
	        	            System.out.println("⚠️ 선택하신 방은 해당 기간 동안 인원이 초과되어 예약이 불가능합니다.");
	        	            return;
	        	        }

	        	        // 실제 업데이트
	        	        yh.updateReservatioin(target.getReservationID(), newRoomno, newHead);
	        	        System.out.println("✅ 예약 정보가 성공적으로 변경되었습니다.");

	        	    } catch (DMLException | RecordNotFoundException e) {
	        	        System.out.println("변경 중 오류 발생: " + e.getMessage());
	        	    }
	        	}

	        } else if (action.equals("2")) {
	                try {
						yh.deleteReservation(target.getReservationID());
					} catch (RecordNotFoundException e) {
						System.out.println(e.getMessage());
					} 
	                System.out.println("✅ 예약이 취소되었습니다.");
	        }

	    } catch (DMLException e) {
	        System.out.println("조회 중 오류: " + e.getMessage());
	    }
	}
	
	public static void viewGHList(ArrayList<GuestHouse> list, Scanner sc, YouthHouseImpl yh, Guest g) {
	    int pageSize = 5;
	    int total = list.size();
	    int page = 0;

	    while (true) {
	        int start = page * pageSize;
	        int end = Math.min(start + pageSize, total);

	        System.out.printf("\n--- %d페이지 / 총 %d페이지 ---\n", page + 1, (total + pageSize - 1) / pageSize);
	        for (int i = start; i < end; i++) {
	            GuestHouse gh = list.get(i);
	            System.out.println((i + 1) + ". " + gh.getName() + " - " + gh.getSido() + " " + gh.getSigungu());
	        }

	        System.out.println("\n상세보기: 번호 입력\n"
	        		+ "다음 페이지: n / 이전 페이지: p / 종료: q\n"
	        		+ "👉 선택:  ");
	        String cmd = sc.next();

	        if (cmd.equalsIgnoreCase("n") && end < total) {
	            page++;
	        } else if (cmd.equalsIgnoreCase("p") && page > 0) {
	            page--;
	        } else if (cmd.equalsIgnoreCase("q")) {
	            break;
	        } else {
	            try {
	                int sel = Integer.parseInt(cmd);
	                if (sel >= 1 && sel <= total) {
	                    GuestHouse selectedGH = list.get(sel - 1);
	                    viewGHDetail(selectedGH, sc, yh, g); // 상세보기
	                }
	            } catch (Exception e) {
	                System.out.println("올바른 번호를 입력해주세요.");
	            }
	        }
	    }
	}

	public static void viewGHDetail(GuestHouse gh, Scanner sc, YouthHouseImpl yh, Guest g) {
	    System.out.println("\n--- 게스트하우스 상세보기 ---");
	    System.out.println("이름: " + gh.getName());
	    System.out.println("위치: " + gh.getSido() + " " + gh.getSigungu() + " " + gh.getDong());
	    System.out.println("상세주소: " + gh.getDetailAddress());
	    //System.out.println("사업자번호: " + gh.getBusinessNum());
	    System.out.println("게하코드: "+gh.getGhcode());

	    System.out.println("\n1. 방 목록 보기 및 예약\n2. 뒤로가기\n 👉 선택: ");
	    String cmd = sc.next();

	    if (cmd.equals("1")) {
            try {
            	ArrayList<Room> rooms = yh.getAllRooms(gh.getGhcode());
                System.out.println("\n--- 방 목록 ---");
                for (Room r : rooms) {
                    System.out.println("방번호: " + r.getRoomno());
                    System.out.println("수용 인원: " + r.getCapacity());
                    System.out.println("가격: " + r.getPrice());
                    System.out.println("설명: " + r.getOverview());
                    System.out.println("가격: " + r.getPrice());
                    System.out.println("--------------------");
                }
                System.out.println("예약할 방 번호(roomno)를 입력하세요:");
                String roomno = sc.next();

                // 방 정보 가져오기
                Room selectedRoom = null;
                for (Room r : rooms) {
                    if (r.getRoomno().equals(roomno)) {
                        selectedRoom = r;
                        break;
                }
                }
                if (selectedRoom == null) {
                    System.out.println("해당 방번호가 존재하지 않습니다.");
                    return;
                }

                System.out.println("체크인 날짜 (yyyy mm dd):");
                int y1 = sc.nextInt(), m1 = sc.nextInt(), d1 = sc.nextInt();

                System.out.println("체크아웃 날짜 (yyyy mm dd):");
                int y2 = sc.nextInt(), m2 = sc.nextInt(), d2 = sc.nextInt();

                System.out.println("인원수:");
                int head = sc.nextInt();

                System.out.println("결제 방식(account/deposit):");
                String pay = sc.next();

                double price = selectedRoom.getPrice() * head;

                Reservation res = new Reservation(
                    head, price, new Mydate(y1, m1, d1), new Mydate(y2, m2, d2),
                    PaymentType.valueOf(pay), roomno, gh.getGhcode(), g.getId()
                );

                try {
                    yh.addReservation(res);
                    System.out.println("예약이 완료되었습니다.");
                } catch (PaymentException e) {
                    System.out.println("결제 실패: " + e.getMessage());
                }
            } catch (DMLException e) {
                System.out.println(e.getMessage());
            }
        }
	}

	public static void hostService(YouthHouseImpl service, Host host) {
		Scanner sc = new Scanner(System.in);
		boolean running = true;

		while (running) {
			System.out.println("======= Host 메뉴 =======");
			System.out.println("1. 게스트하우스 추가");
			System.out.println("2. 게스트하우스 삭제 요청");
			System.out.println("3. 게스트하우스 관리");
			System.out.println("4. 지점 정보 확인");
			System.out.println("5. 종료");
			System.out.print("메뉴를 선택하세요: ");

			int choice = sc.nextInt();

			switch (choice) {
			case 1:
				System.out.println("추가하실 게스트하우스 정보(사업자 번호, 게스트하우스 이름, 시/도 , 시/군/구, 동, 상세 주소)를 입력해주세요.");
				sc.nextLine(); // 버퍼 클리어

				String input = sc.nextLine();

				// 공백 기준으로 분리
				String[] tokens = input.split(" ", 6);

				if (tokens.length < 6) {
					System.out.println("입력 형식이 잘못되었습니다. 다시 입력해주세요.");
				} else {
					String businessNum = tokens[0];
					String name = tokens[1];
					String sido = tokens[2];
					String sigungu = tokens[3];
					String dong = tokens[4];
					String detailAddress = tokens[5];
					GuestHouse guesthouse = new GuestHouse(businessNum, name, sido, sigungu, dong, detailAddress,
							host.getHostID());
					try {
						service.addGH(guesthouse);
						System.out.println(name + "게스트하우스 생성이 완료되었습니다.");
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				}

				break;

			case 2:
				System.out.println("어떤 게스트하우스를 삭제하시겠습니까?");
				// hostID에 모든 게스트하우스 출력
				ArrayList<GuestHouse> guesthouses = new ArrayList<GuestHouse>();
				try {
					guesthouses = service.getAllGHs(host.getHostID());
					guesthouses.stream().forEach(System.out::println);
					sc.nextLine();
					String ghcode = sc.nextLine();
					GuestHouse findGH = service.getGH(ghcode);
					System.out.println(findGH.getGhcode() + ", " + findGH.getName() + " 삭제를 요청하였습니다. 담당 직원이 연락드리겠습니다.");
					System.out.println(findGH.getGhcode() + ", " + findGH.getName() + " 삭제 처리가 완료되었습니다.");
					break;

				} catch (Exception e) {
					System.out.println(e.getMessage());
				}

			case 3:
				System.out.println("게스트하우스 관리 메뉴를 선택하셨습니다. 관리하실 게스트하우스 지점코드를 입력해주세요");

				try {
					guesthouses = service.getAllGHs(host.getHostID());
					guesthouses.stream().forEach(System.out::println);
					sc.nextLine();
					String ghcode02 = sc.nextLine();
					GuestHouse findGH02 = service.getGH(ghcode02);
					gHService(service, host, findGH02);

				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
				break;

			case 4:
				System.out.println("지점 정보 확인 메뉴를 선택하셨습니다. 정보를 확인하실 게스트하우스 지점코드를 입력해주세요.");
				try {
					guesthouses = service.getAllGHs(host.getHostID());
					guesthouses.stream().forEach(System.out::println);
					sc.nextLine();
					String ghcode02 = sc.nextLine();
					GuestHouse findGH02 = service.getGH(ghcode02);
					infoService(service, host, findGH02);

				} catch (Exception e) {
					System.out.println(e.getMessage());
				}

				break;
			case 5:
				running = false;
				break;

			default:
				System.out.println("잘못된 입력입니다. 다시 선택해주세요.");
				break;
			}

		}

	}

	public static void gHService(YouthHouseImpl service, Host host, GuestHouse guesthouse) {
		Scanner sc = new Scanner(System.in);
		boolean running = true;

		while (running) {
			System.out.println("======= [" + guesthouse.getName() + "] 방 메뉴 =======");
			System.out.println("1. 방 추가");
			System.out.println("2. 방 삭제");
			System.out.println("3. 방 관리");
			System.out.println("4. 종료");
			System.out.print("메뉴를 선택하세요: ");

			int choice = sc.nextInt();

			switch (choice) {
			case 1:
				System.out.println("추가하실 방 정보(방 호수, 방필요 성별,방 인원 수,방 가격, 방 설명)를 입력해주세요.");
				sc.nextLine(); // 버퍼 클리어

				String input = sc.nextLine();

				// 공백 기준으로 분리
				String[] tokens = input.split(" ", 5);

				if (tokens.length < 5) {
					System.out.println("입력 형식이 잘못되었습니다. 다시 입력해주세요.");
				} else {
					try {
						String roomno = tokens[0];
						String gender = tokens[1];
						int capacity = Integer.parseInt(tokens[2]);
						double price = Double.parseDouble(tokens[3]);
						String overview = tokens[4];
						service.addRoom(guesthouse.getGhcode(), new Room(roomno, gender, capacity, price, overview));
						System.out.println(roomno + " 방 생성이 완료되었습니다.");
					} catch (Exception e) {
						System.out.println(e.getMessage());
					}
				}

				break;

			case 2:
				try {
					System.out.println("어떤 방을 삭제하시겠습니까?");
					ArrayList<Room> rooms = service.getAllRooms(guesthouse.getGhcode());
					rooms.stream().forEach(System.out::println);
					sc.nextLine();
					String roomNO = sc.nextLine();
					service.deleteRoom(guesthouse.getGhcode(), roomNO);
					System.out.println(guesthouse.getGhcode() + " ," + roomNO + " 방이 삭제되었습니다");

				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
				break;

			case 3:

				try {
					System.out.println("어떤 방을 변경하시겠습니까?");
					ArrayList<Room> rooms02 = service.getAllRooms(guesthouse.getGhcode());
					rooms02.stream().forEach(System.out::println);
					sc.nextLine();
					String roomNO02 = sc.nextLine();
					boolean flag = true;
					while (flag) {
						System.out.println(roomNO02 + " 선택하였습니다. 어떤 것을 수정하시겠습니까");
						System.out.println("1. 성별");
						System.out.println("2. 인원 수");
						System.out.println("3. 종료");
						// roomNO02와 ghcode로 Room 객체를 받아올 함수가 필요하다.

						int choice02 = sc.nextInt();
						sc.nextLine();

						Room room = service.getRoomByCode(guesthouse.getGhcode(), roomNO02);

						switch (choice02) {
						case 1:

							System.out.println("현재 방 필요 성별은 " + room.getGender() + "입니다. 수정할 성별을 입력하세요. (남자/여자): ");
							// 1. 남자 2. 여자 3. 종료 나오도록 while문을 작성해야할까? 해야할까?
							String inputGender = sc.nextLine();
							service.updateRoom(guesthouse.getGhcode(), room.getRoomno(), inputGender);

							System.out.println(
									room.getRoomno() + "," + inputGender + ", " + room.getCapacity() + "로 변경되었습니다");
							break;

						case 2:
							System.out.println("현재 방 인원 수는 " + room.getCapacity() + "입니다. 수정하실 인원을 작성해주세요.");
							int inputCapacity = sc.nextInt();
							sc.nextLine();
							service.updateRoom(guesthouse.getGhcode(), room.getRoomno(), inputCapacity);

							System.out.println(
									room.getRoomno() + ", " + room.getGender() + ", " + inputCapacity + "로 변경되었습니다");

							break;

						case 3:
							System.out.println("프로그램을 종료합니다.");
							flag = false;
							break;

						default:
							System.out.println("잘못된 입력입니다. 다시 선택해주세요.");
							break;
						}

					}

				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
				break;

			case 4:
				System.out.println("프로그램을 종료합니다.");
				running = false;
				break;

			default:
				System.out.println("잘못된 입력입니다. 다시 선택해주세요.");
				break;
			}

		}

	}

	public static void infoService(YouthHouseImpl service, Host host, GuestHouse guesthouse) {

		Scanner sc = new Scanner(System.in);
		boolean running = true;

		while (running) {
			System.out.println("======= [" + guesthouse.getName() + "] 방 메뉴 =======");
			System.out.println("1. 매출 정보");
			System.out.println("2. 예약 정보");
			System.out.println("3. 방 정보");
			System.out.println("4. 종료");
			System.out.print("메뉴를 선택하세요: ");

			int choice = sc.nextInt();

			switch (choice) {
			case 1:
				boolean running02 = true;
				while (running02) {
					System.out.println("매출 정보를 선택하셨습니다.");
					System.out.println("1. 연 매출 정보");
					System.out.println("2. 분기 매출 정보");
					System.out.println("3. 월 매출 정보");
					System.out.println("4. 일 매출 정보");
					System.out.println("5. 종료");
					System.out.print("원하시는 옵션을 선택하세요 : ");

					int choice02 = sc.nextInt();
					try {
						int year, month, day;
						switch (choice02) {
						case 1:
							System.out.print("조회할 연도를 입력하세요: ");
							year = sc.nextInt();
							double yearlyRevenue = service.getRevenue(guesthouse.getGhcode(), year);
							System.out.println(year + "년 연 매출: " + yearlyRevenue + "원");
							break;

						case 2:
							System.out.print("조회할 연도를 입력하세요: ");
							year = sc.nextInt();
							System.out.print("조회할 분기(1~4)를 입력하세요: ");
							String quarter = sc.next();
							double quarterlyRevenue = service.getRevenue(year, quarter, guesthouse.getGhcode());
							System.out.println(year + "년 " + quarter + "분기 매출: " + quarterlyRevenue + "원");
							break;

						case 3:
							System.out.print("조회할 연도를 입력하세요: ");
							year = sc.nextInt();
							System.out.print("조회할 월(1~12)을 입력하세요: ");
							month = sc.nextInt();
							double monthlyRevenue = service.getRevenue(year, month, guesthouse.getGhcode());
							System.out.println(year + "년 " + month + "월 매출: " + monthlyRevenue + "원");
							break;

						case 4:
							System.out.print("조회할 연도를 입력하세요: ");
							year = sc.nextInt();
							System.out.print("조회할 월(1~12)을 입력하세요: ");
							month = sc.nextInt();
							System.out.print("조회할 일(1~31)을 입력하세요: ");
							day = sc.nextInt();
							double dailyRevenue = service.getRevenue(year, month, day, guesthouse.getGhcode());
							System.out.println(year + "년 " + month + "월 " + day + "일 매출: " + dailyRevenue + "원");
							break;

						case 5:
							running02 = false;
							break;

						default:
							System.out.println("잘못된 입력입니다. 다시 선택해주세요.");
							break;
						}
					} catch (Exception e) {
						System.out.println("매출 정보를 조회하는 중 오류가 발생했습니다: " + e.getMessage());
					}
				}
				break;

			case 2:
				try {
					System.out.println("예약 정보를 선택하셨습니다.");
					System.out.print("조회할 날짜(yyyy-mm-dd)를 입력하세요: ");
					String inputDate = sc.next(); // 예: 2025-06-07
					String[] parts = inputDate.split("-");
					int year = Integer.parseInt(parts[0]);
					int month = Integer.parseInt(parts[1]);
					int day = Integer.parseInt(parts[2]);

					Mydate targetDate = new Mydate(year, month, day);

					ArrayList<Reservation> reservations = service.getAllReservations(guesthouse.getGhcode(),
							targetDate);

					if (reservations.isEmpty()) {
						System.out.println("해당 날짜에 예약된 정보가 없습니다.");
					} else {
						System.out.println("===== 예약 목록 =====");
						for (Reservation r : reservations) {
							System.out.println(r);
						}
					}
				} catch (Exception e) {
					System.out.println("예약 정보를 조회하는 중 오류가 발생했습니다: " + e.getMessage());
				}

				break;

			case 3:
				try {
					System.out.println("방 정보를 선택하셨습니다.");
					System.out.print("조회할 날짜(yyyy-mm-dd)를 입력하세요: ");
					String inputDate = sc.next();
					String[] parts = inputDate.split("-");
					int year = Integer.parseInt(parts[0]);
					int month = Integer.parseInt(parts[1]);
					int day = Integer.parseInt(parts[2]);
					Mydate targetDate = new Mydate(year, month, day);

					// 방 + 해당 날짜 기준 예약 인원 조회
					HashMap<Room, Integer> roomStatusMap = service.getCheckCountRoom(guesthouse.getGhcode(),
							targetDate);

					System.out.println("===== [" + guesthouse.getName() + "] 방 정보 및 예약 현황 =====");
					for (Room room : roomStatusMap.keySet()) {
						int reserved = roomStatusMap.get(room);
						int available = room.getCapacity() - reserved;
						double expectedRevenue = reserved * room.getPrice();

						System.out.printf(
								"방 번호: %s | 성별: %s | 정원: %d | 예약 인원: %d | 잔여 인원: %d | 방 가격: %.2f원 | 예상 수입: %.2f원\n",
								room.getRoomno(), room.getGender(), room.getCapacity(), reserved, available,
								room.getPrice(), expectedRevenue);
					}

				} catch (Exception e) {
					System.out.println("방 정보를 불러오는 중 오류가 발생했습니다: " + e.getMessage());
				}
				break;

			case 4:
				System.out.println("프로그램을 종료합니다.");
				running = false;
				break;

			default:
				System.out.println("잘못된 입력입니다. 다시 선택해주세요.");
				break;
			}

		}

	}

}

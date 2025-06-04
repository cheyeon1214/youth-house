package com.project.vo;

import java.util.ArrayList;

public class GuestHouse {
	private String ghcode;
	private String businessNum;
	private String name;
	private String sido;
	private String sigungu;
	private String dong;
	private String detailAddress;
	
	ArrayList<Room> rooms = new ArrayList<>();

	public GuestHouse() {}
	public GuestHouse(String ghcode, String businessNum, String name, String sido, String sigungu, String dong,
			String detailAddress) {
		super();
		this.ghcode = ghcode;
		this.businessNum = businessNum;
		this.name = name;
		this.sido = sido;
		this.sigungu = sigungu;
		this.dong = dong;
		this.detailAddress = detailAddress;
	}
	public String getGhcode() {
		return ghcode;
	}
	public String getBusinessNum() {
		return businessNum;
	}
//	public void setBusinessNum(String businessNum) {
//		this.businessNum = businessNum;
//	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSido() {
		return sido;
	}
	public void setSido(String sido) {
		this.sido = sido;
	}
	public String getSigungu() {
		return sigungu;
	}
	public void setSigungu(String sigungu) {
		this.sigungu = sigungu;
	}
	public String getDong() {
		return dong;
	}
	public void setDong(String dong) {
		this.dong = dong;
	}
	public String getDetailAddress() {
		return detailAddress;
	}
	public void setDetailAddress(String detailAddress) {
		this.detailAddress = detailAddress;
	}
	public ArrayList<Room> getRooms() {
		return rooms;
	}
	public void setRooms(ArrayList<Room> rooms) {
		this.rooms = rooms;
	}
	@Override
	public String toString() {
		return "GuestHouse [ghcode=" + ghcode + ", businessNum=" + businessNum + ", name=" + name + ", sido=" + sido
				+ ", sigungu=" + sigungu + ", dong=" + dong + ", detailAddress=" + detailAddress + ", rooms=" + rooms
				+ "]";
	}
	
	
}

package com.zz.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class UserAddress {
		@Id
	    @Column(length = 100)
	    private String id;
	 	private String userid;
	 	private String reciever;
	 	private String phone;
	 	private String mainaddress;
	 	private String detailaddress;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getUserid() {
			return userid;
		}
		public void setUserid(String userid) {
			this.userid = userid;
		}
		public String getReciever() {
			return reciever;
		}
		public void setReciever(String reciever) {
			this.reciever = reciever;
		}
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		public String getMainaddress() {
			return mainaddress;
		}
		public void setMainaddress(String mainaddress) {
			this.mainaddress = mainaddress;
		}
		public String getDetailaddress() {
			return detailaddress;
		}
		public void setDetailaddress(String detailaddress) {
			this.detailaddress = detailaddress;
		}
	 	
	 	
}

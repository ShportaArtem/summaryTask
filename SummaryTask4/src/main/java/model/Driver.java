package model;

import java.io.Serializable;
/**
 * Driver entity.
 * 
 * @author A.Shporta
 * 
 */
public class Driver implements Serializable{

	private static final long serialVersionUID = -232773389738031756L;
	
	private int  userId;
	
	private String passport;
	
	private String phone;

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getPassport() {
		return passport;
	}

	public void setPassport(String passport) {
		this.passport = passport;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
}

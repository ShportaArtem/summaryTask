package model;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User implements Serializable{
	private static final long serialVersionUID = -7351729135012380019L;
	
	private int id;
	
	private int roleId;
	
	private String login;
	
	private String password;
	
	private String passwordView;
	
	public String getPasswordView() {
		return passwordView;
	}

	private String name;

	private String surname;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) throws NoSuchAlgorithmException {
		this.passwordView = password;
		this.password = new String(getSHA(password));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	public static byte[] getSHA(String input) throws NoSuchAlgorithmException 
	    {  
	        MessageDigest md = MessageDigest.getInstance("SHA-256");  
	        return md.digest(input.getBytes(StandardCharsets.UTF_8));  
	    } 
	
	
	
}

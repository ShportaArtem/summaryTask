package model;

import java.io.Serializable;

public class Firm implements Serializable{

	private static final long serialVersionUID = 2582997534345540024L;
	
	private int id;
	
	private String name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}

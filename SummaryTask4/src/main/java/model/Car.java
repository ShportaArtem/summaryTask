package model;

import java.io.Serializable;

public class Car implements Serializable{

	private static final long serialVersionUID = 5376181047481846206L;
	
	private int id;
	
	private String model;
	
	private int carryingCapacity;
	
	private int passangersCapacity;
	
	private int status;
	
	private int firmId;
	
	private String vehicleCondition;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	public int getCarryinfCapacity() {
		return carryingCapacity;
	}

	public void setCarryinfCapacity(int carryinfCapacity) {
		this.carryingCapacity = carryinfCapacity;
	}

	public int getPassangersCapacity() {
		return passangersCapacity;
	}

	public void setPassangersCapacity(int passangersCapacity) {
		this.passangersCapacity = passangersCapacity;
	}

	public int getFirmId() {
		return firmId;
	}

	public void setFirmId(int firmId) {
		this.firmId = firmId;
	}

	public String getVehicleCondition() {
		return vehicleCondition;
	}

	public void setVehicleCondition(String vehicleCondition) {
		this.vehicleCondition = vehicleCondition;
	}
}

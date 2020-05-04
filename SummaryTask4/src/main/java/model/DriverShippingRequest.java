package model;

import java.io.Serializable;
/**
 * DriverShippingRequest entity.
 * 
 * @author A.Shporta
 * 
 */
public class DriverShippingRequest implements Serializable{

	private static final long serialVersionUID = 1906440099054051803L;

	private int id;
	
	private int driverId;
	
	private int carryinfCapacity;
	
	private int passangersCapacity;
	
	private String vehicleCondition;
	
	private int shippingId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDriverId() {
		return driverId;
	}

	public void setDriverId(int driverId) {
		this.driverId = driverId;
	}

	public int getCarryinfCapacity() {
		return carryinfCapacity;
	}

	public void setCarryinfCapacity(int carryinfCapacity) {
		this.carryinfCapacity = carryinfCapacity;
	}

	public int getPassangersCapacity() {
		return passangersCapacity;
	}

	public void setPassangersCapacity(int passangersCapacity) {
		this.passangersCapacity = passangersCapacity;
	}

	public String getVehicleCondition() {
		return vehicleCondition;
	}

	public void setVehicleCondition(String vehicleCondition) {
		this.vehicleCondition = vehicleCondition;
	}

	public int getShippingId() {
		return shippingId;
	}

	public void setShippingId(int shippingId) {
		this.shippingId = shippingId;
	}
}

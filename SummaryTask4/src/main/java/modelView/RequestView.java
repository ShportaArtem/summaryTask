package modelView;
/**
 * View for driverShippingRequest entity.
 * 
 * @author A.Shporta
 * 
 */
public class RequestView {
	
	private int id;
	
	private String driverLogin;
	
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

	public String getDriverLogin() {
		return driverLogin;
	}

	public void setDriverLogin(String driverName) {
		this.driverLogin = driverName;
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

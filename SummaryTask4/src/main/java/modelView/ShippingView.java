package modelView;

import java.sql.Date;
import java.sql.Timestamp;

public class ShippingView {

	private int id;
	
	private String dispatcherLogin;
	
	private String status;
	
	private String driverShippngRequestId;
	
	private String carId;
	
	private String arrivalCity;
	
	private String departureCity;
	
	private Timestamp creationTimestamp;
	
	private Date departureTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDispatcherLogin() {
		return dispatcherLogin;
	}

	public void setDispatcherLogin(String dispatcherLogin) {
		this.dispatcherLogin = dispatcherLogin;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDriverShippngRequestId() {
		return driverShippngRequestId;
	}

	public void setDriverShippngRequestId(String driverShippngRequestId) {
		this.driverShippngRequestId = driverShippngRequestId;
	}

	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	public String getArrivalCity() {
		return arrivalCity;
	}

	public void setArrivalCity(String arrivalCity) {
		this.arrivalCity = arrivalCity;
	}

	public String getDepartureCity() {
		return departureCity;
	}

	public void setDepartureCity(String departureCity) {
		this.departureCity = departureCity;
	}

	public Timestamp getCreationTimestamp() {
		return creationTimestamp;
	}

	public void setCreationTimestamp(Timestamp creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}

	public Date getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(Date departureTime) {
		this.departureTime = departureTime;
	}
}

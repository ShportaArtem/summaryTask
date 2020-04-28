package model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class Shipping implements Serializable{

	private static final long serialVersionUID = -8838361035691691947L;

	private int id;
	
	private Integer dispathcerId;
	
	private String status;
	
	private Integer driverShippngRequestId;
	
	private Integer carId;
	
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

	public int getDispathcerId() {
		return dispathcerId;
	}

	public void setDispathcerId(int dispathcerId) {
		this.dispathcerId = dispathcerId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getDriverShippngRequestId() {
		return driverShippngRequestId;
	}

	public void setDriverShippngRequestId(Integer driverShippngRequestId) {
		this.driverShippngRequestId = driverShippngRequestId;
	}

	public Integer getCarId() {
		return carId;
	}

	public void setCarId(Integer carId) {
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((arrivalCity == null) ? 0 : arrivalCity.hashCode());
		result = prime * result + ((carId == null) ? 0 : carId.hashCode());
		result = prime * result + ((creationTimestamp == null) ? 0 : creationTimestamp.hashCode());
		result = prime * result + ((departureCity == null) ? 0 : departureCity.hashCode());
		result = prime * result + ((departureTime == null) ? 0 : departureTime.hashCode());
		result = prime * result + ((dispathcerId == null) ? 0 : dispathcerId.hashCode());
		result = prime * result + ((driverShippngRequestId == null) ? 0 : driverShippngRequestId.hashCode());
		result = prime * result + id;
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Shipping other = (Shipping) obj;
		if (arrivalCity == null) {
			if (other.arrivalCity != null)
				return false;
		} else if (!arrivalCity.equals(other.arrivalCity))
			return false;
		if (carId == null) {
			if (other.carId != null)
				return false;
		} else if (!carId.equals(other.carId))
			return false;
		if (creationTimestamp == null) {
			if (other.creationTimestamp != null)
				return false;
		} else if (!creationTimestamp.equals(other.creationTimestamp))
			return false;
		if (departureCity == null) {
			if (other.departureCity != null)
				return false;
		} else if (!departureCity.equals(other.departureCity))
			return false;
		if (departureTime == null) {
			if (other.departureTime != null)
				return false;
		} else if (!departureTime.equals(other.departureTime))
			return false;
		if (dispathcerId == null) {
			if (other.dispathcerId != null)
				return false;
		} else if (!dispathcerId.equals(other.dispathcerId))
			return false;
		if (driverShippngRequestId == null) {
			if (other.driverShippngRequestId != null)
				return false;
		} else if (!driverShippngRequestId.equals(other.driverShippngRequestId))
			return false;
		if (id != other.id)
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		return true;
	}
	
}

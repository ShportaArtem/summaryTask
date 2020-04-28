package db.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.utils.DBUtils;
import model.Car;

public class CarRep {
	
	private static final String SQL_CREATE_CAR = "INSERT INTO car VALUES (DEFAULT, ?, ?, ?, ?, ?, ?)";
	private static final String SQL_FIND_ALL_CARS="SELECT * FROM car ";
	private static final String SQL_FIND_ALL_CARS_NOT_IN_TRIP="SELECT * FROM car WHERE statusCar=0";
	private static final String SQL_DELETE_CAR = "DELETE FROM car WHERE id=?";
	private static final String SQL_FIND_CAR_BY_MODEL_AND_STATUS_CAR = "SELECT * FROM car WHERE statusCar=? AND car_model=?";
	private static final String SQL_FIND_CAR_BY_ID = "SELECT * FROM car WHERE id=?";
	private static final String SQL_UPDATE_CAR_BY_ID = "UPDATE car SET car_model=?, carrying_capacity=?, passengers_capacity=?, car_firm_id=?, statusCar=?,vehicle_condition=? WHERE id=?";  
	private static final String SQL_UPDATE_CAR_VEHICLE_CONDITION_BY_ID = "UPDATE car SET statusCar=?,vehicle_condition=? WHERE id=?";  
	
	private static CarRep instance;
	public static synchronized CarRep getInstance() {
		if (instance == null) {
			instance = new CarRep();
		}
		return instance;
	}

	private CarRep() {
	}
	
	public List<Car> findAllCars(Connection con) throws SQLException{
		List<Car> users = new ArrayList<>();

		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(SQL_FIND_ALL_CARS);

			while (rs.next()) {
				users.add(extractCar(rs));
			}
		} finally {
			DBUtils.close(rs);
			DBUtils.close(stmt);
		}
		return users;
	}
	
	public boolean updateCar(Connection con, Car car) throws SQLException { 
		boolean res = false;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = con.prepareStatement(SQL_UPDATE_CAR_BY_ID, Statement.RETURN_GENERATED_KEYS);

			int k = 1;
			pstmt.setString(k++, car.getModel());
			pstmt.setInt(k++, car.getCarryinfCapacity());
			pstmt.setInt(k++, car.getPassangersCapacity());
			pstmt.setInt(k++, car.getFirmId());
			pstmt.setInt(k++, car.getStatus());
			pstmt.setString(k++, car.getVehicleCondition());
			pstmt.setInt(k++, car.getId());
			
			if (pstmt.executeUpdate() > 0) {
				rs = pstmt.getGeneratedKeys();
				if (rs.next()) {
					res = true;
				}
			}
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
		}

		return res;
	}
	
	public boolean finishTrip(Connection con, String vehicle, Integer status, Integer id) throws SQLException { 
		boolean res = false;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = con.prepareStatement(SQL_UPDATE_CAR_VEHICLE_CONDITION_BY_ID, Statement.RETURN_GENERATED_KEYS);

			int k = 1;
			pstmt.setInt(k++, status);
			pstmt.setString(k++, vehicle);
			pstmt.setInt(k++, id);
			
			if (pstmt.executeUpdate() > 0) {
				rs = pstmt.getGeneratedKeys();
				if (rs.next()) {
					res = true;
				}
			}
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
		}

		return res;
	}
	
	public List<Car> findAllCarsNotInTrip(Connection con) throws SQLException{
		List<Car> users = new ArrayList<>();

		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(SQL_FIND_ALL_CARS_NOT_IN_TRIP);

			while (rs.next()) {
				users.add(extractCar(rs));
			}
		} finally {
			DBUtils.close(rs);
			DBUtils.close(stmt);
		}
		return users;
	}
	
	public Car findCarByModelAndStatus(Connection con,String model, Integer status) throws SQLException {
		Car car = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = con.prepareStatement(SQL_FIND_CAR_BY_MODEL_AND_STATUS_CAR);
			pstmt.setString(1, model);
			pstmt.setInt(2, status);
			rs = pstmt.executeQuery();

			if(rs.next()) {
				car = extractCar(rs);
			}
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
		}
		return car;
	}
	
	public Car findCarById(Connection con, Integer id) throws SQLException {
		Car car = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = con.prepareStatement(SQL_FIND_CAR_BY_ID);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();

			if(rs.next()) {
				car = extractCar(rs);
			}
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
		}
		return car;
	}
	
	private Car extractCar(ResultSet rs) throws SQLException {
		Car car = new Car();
		car.setId(rs.getInt("id"));
		car.setModel(rs.getString("car_model"));
		car.setCarryinfCapacity(rs.getInt("carrying_capacity"));
		car.setStatus(rs.getInt("statusCar"));
		car.setPassangersCapacity(rs.getInt("passengers_capacity"));
		car.setFirmId(rs.getInt("car_firm_id"));
		car.setVehicleCondition(rs.getString("vehicle_condition"));
		return car;
	}
	
	
	public boolean insertCar(Connection con, Car car) throws SQLException { 
		boolean res = false;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = con.prepareStatement(SQL_CREATE_CAR, Statement.RETURN_GENERATED_KEYS);

			int k = 1;
			pstmt.setString(k++, car.getModel());
			pstmt.setInt(k++, car.getCarryinfCapacity());
			pstmt.setInt(k++, car.getPassangersCapacity());
			pstmt.setInt(k++, car.getFirmId());
			pstmt.setInt(k++, car.getStatus());
			pstmt.setString(k++, car.getVehicleCondition());

			if (pstmt.executeUpdate() > 0) {
				rs = pstmt.getGeneratedKeys();
				if (rs.next()) {
					int carId = rs.getInt(1);
					car.setId(carId);
					res = true;
				}
			}
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
		}

		return res;
	}

	

	public boolean deleteCar(Connection con, int carId) throws SQLException {
		PreparedStatement pstmt = null;

		try {
			pstmt = con.prepareStatement(SQL_DELETE_CAR);

			int k = 1;
			pstmt.setInt(k++, carId);

			return pstmt.executeUpdate() > 0;
		} finally {
			DBUtils.close(pstmt);
		}
	}
}

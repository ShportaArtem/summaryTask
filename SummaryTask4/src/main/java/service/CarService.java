package service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import db.exception.AppException;
import db.exception.DBException;
import db.exception.Messages;
import db.repository.CarRep;
import db.repository.FirmRep;
import db.utils.DBUtils;
import model.Car;
import model.Firm;

public class CarService {

	private static final Logger LOG = Logger.getLogger(CarService.class);

	
	private DataSource ds;
	private static CarService instance;

	public static synchronized CarService getInstance() throws DBException {
		if (instance == null) {
			instance = new CarService();
		}
		return instance;
	}
	
	private CarService() throws DBException{
		try {
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			ds = (DataSource) envContext.lookup("jdbc/autobasedb");
			LOG.trace("Data source ==> " + ds);
		} catch (NamingException ex) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_DATA_SOURCE, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_DATA_SOURCE, ex);
		}
	}
	
	public void deleteCartById(Integer carId) throws AppException {
		Connection con = null;
		CarRep carRep = CarRep.getInstance();
		try {
			con = getConnection();
			con.setAutoCommit(true);
			carRep.deleteCar(con, carId);	
		}catch (SQLException ex ) {
			LOG.error(Messages.ERR_CANNOT_DELETE_CAR, ex);
			throw new DBException(Messages.ERR_CANNOT_DELETE_CAR, ex);
		}finally {
			DBUtils.close(con);
		}
		
	}
	
	public Firm findFirmById(Integer firmId) throws AppException {
		Connection con = null;
		Firm firm =null;
		FirmRep firmRep = FirmRep.getInstance();
		try {
			con = getConnection();
			con.setAutoCommit(true);
			firm = firmRep.findFirmById(con, firmId);	
		}catch (SQLException ex ) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_FIRM, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_FIRM, ex);
		}finally {
			DBUtils.close(con);
		}
		return firm;
	}
	
	public Car findCarById(Integer carId) throws AppException {
		Connection con = null;
		Car car =null;
		CarRep carRep = CarRep.getInstance();
		try {
			con = getConnection();
			con.setAutoCommit(true);
			car = carRep.findCarById(con, carId);	
		}catch (SQLException ex ) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CAR, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_CAR, ex);
		}finally {
			DBUtils.close(con);
		}
		return car;
	}
	
	public Firm findFirmByName(String name) throws AppException {
		Connection con = null;
		Firm firm =null;
		FirmRep firmRep = FirmRep.getInstance();
		try {
			con = getConnection();
			con.setAutoCommit(true);
			firm = firmRep.findFirmByName(con, name);	
		}catch (SQLException ex ) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_FIRM, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_FIRM, ex);
		}finally {
			DBUtils.close(con);
		}
		return firm;
	}
	
	public void updateCar(Car car) throws AppException {
		Connection con = null;
		CarRep carRep = CarRep.getInstance();
		try {
			con = getConnection();
			con.setAutoCommit(true);
			carRep.updateCar(con, car);
		} catch (SQLException ex ) {
			LOG.error(Messages.ERR_CANNOT_UPDATE_CAR, ex);
			throw new DBException(Messages.ERR_CANNOT_UPDATE_CAR, ex);
		} finally {
			DBUtils.close(con);
		}
		
	}
	
	public void insertCar(String model, Integer carryingCapacity, Integer passengersCapacity, Integer firmId, Integer statusCar, String vehicleCondition) throws AppException {
		Car car= new Car();
		car.setCarryinfCapacity(carryingCapacity);
		car.setFirmId(firmId);
		car.setModel(model);
		car.setPassangersCapacity(passengersCapacity);
		car.setStatus(statusCar);
		car.setVehicleCondition(vehicleCondition);
		Connection con = null;
		CarRep carRep = CarRep.getInstance();
		try {
			con = getConnection();
			con.setAutoCommit(true);
			carRep.insertCar(con, car);
		} catch (SQLException ex ) {
			LOG.error(Messages.ERR_CANNOT_INSERT_CAR, ex);
			throw new DBException(Messages.ERR_CANNOT_INSERT_CAR, ex);
		} finally {
			DBUtils.close(con);
		}
		
	}
	
	public Connection getConnection() throws DBException {
		Connection con = null;
		try {
			con = ds.getConnection();
		} catch (SQLException ex) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CONNECTION, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_CONNECTION, ex);
		}
		return con;
	}
	
	public List<Car> findAllCars() throws AppException{
		Connection con = null;
		List<Car> cars= null;
		CarRep carRep = CarRep.getInstance();
		try {
			con = getConnection();
			cars= carRep.findAllCars(con);
		}catch(SQLException ex ) {
			DBUtils.rollback(con);
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CARS, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_CARS, ex);
		} finally {
			DBUtils.close(con);
		}
		return cars;
	}
	
	public List<Firm> findAllFrims() throws AppException{
		Connection con = null;
		List<Firm> firms= null;
		FirmRep firmRep = FirmRep.getInstance();
		try {
			con = getConnection();
			firms= firmRep.findAllFirms(con);
		}catch(SQLException ex ) {
			DBUtils.rollback(con);
			LOG.error(Messages.ERR_CANNOT_OBTAIN_FIRMS, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_FIRMS, ex);
		} finally {
			DBUtils.close(con);
		}
		return firms;
	}
	
	public List<Car> findAllCarsNotInTrip() throws AppException{
		Connection con = null;
		List<Car> cars= null;
		CarRep carRep = CarRep.getInstance();
		try {
			con = getConnection();
			cars= carRep.findAllCarsNotInTrip(con);
		}catch(SQLException ex ) {
			DBUtils.rollback(con);
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CARS, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_CARS, ex);
		} finally {
			DBUtils.close(con);
		}
		return cars;
	}
}

package service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import db.DBManager;
import db.exception.AppException;
import db.exception.DBException;
import db.exception.Messages;
import db.repository.CarRep;
import db.repository.FirmRep;
import db.utils.DBUtils;
import model.Car;
import model.Firm;

/**
 * Car service. Works with DBManager and repositories. 
 * 
 * @author A.Shporta
 * 
 */
public class CarService {

	private static final Logger LOG = Logger.getLogger(CarService.class);

	
	private DBManager dbManager;
	private Connection con;
	private CarRep carRep; 
	private FirmRep firmRep; 
	
	public CarService(DBManager dbManager, CarRep carRep, FirmRep firmRep) {
	this.carRep = carRep;
	this.dbManager = dbManager;
	this.firmRep = firmRep;
	}
	
	
	/**
	 * Delete car by id
	 * @param carId
	 * 		Id car that will be delete.	
	 * @throws AppException
	 */
	public void deleteCartById(Integer carId) throws AppException {
		try {
			con = dbManager.getConnection();
			con.setAutoCommit(true);
			carRep.deleteCar(con, carId);	
		}catch (SQLException ex ) {
			LOG.error(Messages.ERR_CANNOT_DELETE_CAR, ex);
			throw new DBException(Messages.ERR_CANNOT_DELETE_CAR, ex);
		}finally {
			DBUtils.close(con);
		}
		
	}
	
	/**
	 * Find firm by id
	 * 
	 * @param firmId
	 * 			Id firm that will be find.		
	 * @return firm model
	 *
	 * @throws AppException
	 */
	public Firm findFirmById(Integer firmId) throws AppException {
		Firm firm =null;
		try {
			con = dbManager.getConnection();
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
	
	/**
	 * Find car by id
	 * 
	 * @param carId
	 * 			Id car that will be find.
	 * @return car model
	 * @throws AppException
	 */
	public Car findCarById(Integer carId) throws AppException {
		Car car =null;
		try {
			con = dbManager.getConnection();
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
	
	/**
	 * Find firm by name
	 * 
	 * @param name
	 * 		Name firm that will be find
	 * @return firm model
	 * 
	 * @throws AppException
	 */
	public Firm findFirmByName(String name) throws AppException {
		Firm firm =null;
		try {
			con = dbManager.getConnection();
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
	
	/**
	 * Update car
	 * 
	 * @param car
	 * 		Car that will be update
	 * 
	 * @throws AppException
	 */
	public void updateCar(Car car) throws AppException {
		try {
			con = dbManager.getConnection();
			con.setAutoCommit(true);
			carRep.updateCar(con, car);
		} catch (SQLException ex ) {
			LOG.error(Messages.ERR_CANNOT_UPDATE_CAR, ex);
			throw new DBException(Messages.ERR_CANNOT_UPDATE_CAR, ex);
		} finally {
			DBUtils.close(con);
		}
		
	}
	
	/**
	 * Insert car in DB
	 * 
	 * @throws AppException
	 */
	public void insertCar(String model, Integer carryingCapacity, Integer passengersCapacity, Integer firmId, Integer statusCar, String vehicleCondition) throws AppException {
		Car car= new Car();
		car.setCarryinfCapacity(carryingCapacity);
		car.setFirmId(firmId);
		car.setModel(model);
		car.setPassangersCapacity(passengersCapacity);
		car.setStatus(statusCar);
		car.setVehicleCondition(vehicleCondition);
		try {
			con = dbManager.getConnection();
			con.setAutoCommit(true);
			carRep.insertCar(con, car);
		} catch (SQLException ex ) {
			LOG.error(Messages.ERR_CANNOT_INSERT_CAR, ex);
			throw new DBException(Messages.ERR_CANNOT_INSERT_CAR, ex);
		} finally {
			DBUtils.close(con);
		}
		
	}
	
	
	/**
	 * Returns all cars.
	 * 
	 * @return List of car models.
	 *
	 * @throws AppException
	 */
	public List<Car> findAllCars() throws AppException{
		List<Car> cars= null;
		try {
			con = dbManager.getConnection();
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
	
	/**
	  * Returns all firms .
	 * 
	 * @return List of firm models.
	 * 
	 * @throws AppException
	 */
	public List<Firm> findAllFrims() throws AppException{
		List<Firm> firms= null;
		try {
			con = dbManager.getConnection();
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
	
	/**
	  * Returns all cars that not in trip.
	 * 
	 * @return List of car models.
	 * 
	 * @throws AppException
	 */
	public List<Car> findAllCarsNotInTrip() throws AppException{
		List<Car> cars= null;
		try {
			con = dbManager.getConnection();
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

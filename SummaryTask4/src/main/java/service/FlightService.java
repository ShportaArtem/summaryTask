package service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import db.DBManager;
import db.exception.AppException;
import db.exception.DBException;
import db.exception.Messages;
import db.repository.CarRep;
import db.repository.DriverShippingRequestRep;
import db.repository.ShippingRep;
import db.repository.UserRep;
import db.utils.DBUtils;
import model.Car;
import model.DriverShippingRequest;
import model.Shipping;
import model.User;

/**
 * Flight service. Works with DBManager and repositories. 
 * 
 * @author A.Shporta
 * 
 */
public class FlightService {

	private static final Logger LOG = Logger.getLogger(FlightService.class);

	private DBManager dbManager;
	private UserRep userRep;
	private CarRep carRep;
	private DriverShippingRequestRep requestRep;
	private ShippingRep shipRep;

	public FlightService(DBManager dbManager, UserRep userRep, CarRep carRep, DriverShippingRequestRep requestRep,
			ShippingRep shipRep) {
		this.dbManager = dbManager;
		this.carRep = carRep;
		this.requestRep = requestRep;
		this.shipRep = shipRep;
		this.userRep = userRep;
	}

	/**
	 * Find user by shippingDriverRequest id
	 * 
	 * @param id
	 * 			Id request that will used for find user.		
	 * @return user model
	 *
	 * @throws AppException
	 */
	public User findUserByShippingRequestId(int id) throws AppException {
		User user = null;
		DriverShippingRequest request = null;
		Connection con=null;
		try {
			con = dbManager.getConnection();
			request = requestRep.findDriverShippingRequestById(con, id);
			user = userRep.findUserById(con, request.getDriverId());
			con.commit();
		} catch (SQLException ex) {
			DBUtils.rollback(con);
			LOG.error(Messages.ERR_CANNOT_OBTAIN_USER_BY_ID, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_ID, ex);
		} finally {
			DBUtils.close(con);
		}
		return user;
	}
	
	/**
	 * * Cancel shipping by id.
	 * 
	 * @param flightId
	 * 		Id shipping that will be cancel.
	 * @throws AppException
	 */
	public void cancelFlightById(Integer flightId) throws AppException {
		Connection con=null;
		try {
			con = dbManager.getConnection();
			con.setAutoCommit(true);
			shipRep.cancelShipping(con, flightId);
		} catch (SQLException ex) {
			LOG.error(Messages.ERR_CANNOT_DELETE_FLIGHT_BY_ID, ex);
			throw new DBException(Messages.ERR_CANNOT_DELETE_FLIGHT_BY_ID, ex);
		} finally {
			DBUtils.close(con);
		}

	}
	/**
	 * Returns all shipping.
	 * 
	 * @return List of shipping models.
	 *
	 * @throws AppException
	 */
	public List<Shipping> findAllShips() throws AppException {
		List<Shipping> shippings = null;
		Connection con=null;
		try {
			con = dbManager.getConnection();
			shippings = shipRep.findAllShips(con);
		} catch (SQLException ex) {
			DBUtils.rollback(con);
			LOG.error(Messages.ERR_CANNOT_OBTAIN_SHIPPINGS, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_SHIPPINGS, ex);
		} finally {
			DBUtils.close(con);
		}
		return shippings;
	}
	
	/**
	 * Find shippings by status
	 * 
	 * @param status
	 * 		Status shippings that will be find
	 * @return List of shipping models
	 * 
	 * @throws AppException
	 */
	public List<Shipping> findShippingsByStatus(String status) throws AppException {
		List<Shipping> shippings = null;
		Connection con=null;
		try {
			con = dbManager.getConnection();
			shippings = shipRep.findShippingsByStatus(con, status);
		} catch (SQLException ex) {
			DBUtils.rollback(con);
			LOG.error(Messages.ERR_CANNOT_OBTAIN_SHIPPINGS, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_SHIPPINGS, ex);
		} finally {
			DBUtils.close(con);
		}
		return shippings;
	}
	/**
	 * Find shipping by id
	 * 
	 * @param flightId
	 * 		Id shipping that will be find
	 * @return shipping model
	 * 
	 * @throws AppException
	 */
	public Shipping findFlightById(Integer flightId) throws AppException {
		Shipping ship = null;
		Connection con=null;
		try {
			con = dbManager.getConnection();
			con.setAutoCommit(true);
			ship = shipRep.findShippingById(con, flightId);
		} catch (SQLException ex) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_FLIGHT_BY_ID, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_FLIGHT_BY_ID, ex);
		} finally {
			DBUtils.close(con);
		}
		return ship;
	}
	
	/**
	 * Insert shipping in DB
	 * 
	 * @throws AppException
	 */
	public void insertShipping(Integer dispathcerId, String arrivalCity, String departureCity, Date departureTime)
			throws AppException {
		Connection con=null;
		Shipping ship = new Shipping();
		ship.setArrivalCity(arrivalCity);
		ship.setDepartureCity(departureCity);
		ship.setDepartureTime(departureTime);
		ship.setCarId(null);
		ship.setCreationTimestamp(new Timestamp(System.currentTimeMillis()));
		ship.setDispathcerId(dispathcerId);
		ship.setStatus("Open");
		ship.setDriverShippngRequestId(null);
		try {
			con = dbManager.getConnection();
			con.setAutoCommit(true);
			shipRep.insertShipping(con, ship);
		} catch (SQLException ex) {
			LOG.error(Messages.ERR_CANNOT_INSERT_SHIPPING, ex);
			throw new DBException(Messages.ERR_CANNOT_INSERT_SHIPPING, ex);
		} finally {
			DBUtils.close(con);
		}

	}
	
	
	/**
	 * Find driverShippingRequest by driver login and shipping id
	 * @param shippingId
	 * 		Id shipping that will use for find driverShippingRequest.
	 * @param loginDriver
	 * 		Login driver that will be find.
	 * @return driverShippingRequest model
	 * @throws AppException
	 */
	public DriverShippingRequest findRequestByDriverLoginAndShippingId(Integer shippingId, String loginDriver)
			throws AppException {
		Connection con=null;
		DriverShippingRequest req = null;
		User driver = null;

		try {
			con = dbManager.getConnection();
			driver = userRep.findUserByLogin(con, loginDriver);
			req = requestRep.findRequestByShippingIdAndDriverId(con, shippingId, driver.getId());
			con.commit();
		} catch (SQLException ex) {
			DBUtils.rollback(con);
			LOG.error(Messages.ERR_CANNOT_OBTAIN_REQUEST, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_REQUEST, ex);
		} finally {
			DBUtils.close(con);
		}

		return req;
	}

	/**
	 * * Returns all shippings that not have this driver.
	 * 
	 * @param driverId
	 * @return List of shipping models.
	 * @throws AppException
	 */
	public List<Shipping> findShippingsByNotDriverId(Integer driverId) throws AppException {
		List<Shipping> shippings = null;
		Connection con=null;
		try {
			con = dbManager.getConnection();
			shippings = shipRep.findAllShips(con);

			for (int i = 0; i < shippings.size(); i++) {
				Shipping sh = shippings.get(i);
				System.out.println("Id= " + sh.getId());
				if (requestRep.findRequestByShippingIdAndDriverId(con, sh.getId(), driverId) != null) {
					DriverShippingRequest a = requestRep.findRequestByShippingIdAndDriverId(con, sh.getId(), driverId);
					System.out.println("Id= " + a.getId() + " shipping id = " + a.getShippingId() + " driver = "
							+ a.getDriverId());
					shippings.remove(i--);
				}
			}
			con.commit();
		} catch (SQLException ex) {
			DBUtils.rollback(con);
			LOG.error(Messages.ERR_CANNOT_OBTAIN_SHIPPINGS, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_SHIPPINGS, ex);
		} finally {
			DBUtils.close(con);
		}

		return shippings;
	}

	/**
	 * Find car by model and status
	 * @param model
	 * 			Model car that will be find
	 * @param status
	 * 			Status car that will be find
	 * @return car model
	 * @throws AppException
	 */
	public Car findCarByModelAndStatus(String model, Integer status) throws AppException {
		Car car = null;
		Connection con=null;
		try {
			con = dbManager.getConnection();
			con.setAutoCommit(true);
			carRep.findCarByModelAndStatus(con, model, status);
		} catch (SQLException ex) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CAR, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_CAR, ex);
		} finally {
			DBUtils.close(con);
		}

		return car;
	}
	
	/**
	 * Update shipping
	 * 
	 * @throws AppException
	 */
	public void updateShipping(Integer dispathcerId, String status, Integer request, Integer carId, String arrivalCity,
			String departureCity, Date departureTime, Integer id) throws AppException {
		Connection con=null;
		Shipping ship = new Shipping();
		ship.setId(id);
		ship.setArrivalCity(arrivalCity);
		ship.setDepartureCity(departureCity);
		ship.setDepartureTime(departureTime);
		ship.setCarId(carId);
		ship.setCreationTimestamp(new Timestamp(System.currentTimeMillis()));
		ship.setDispathcerId(dispathcerId);
		ship.setStatus(status);
		ship.setDriverShippngRequestId(request);
		try {
			con = dbManager.getConnection();
			con.setAutoCommit(true);
			shipRep.updateShipping(con, ship);
		} catch (SQLException ex) {
			LOG.error(Messages.ERR_CANNOT_UPDATE_SHIPPING, ex);
			throw new DBException(Messages.ERR_CANNOT_UPDATE_SHIPPING, ex);
		} finally {
			DBUtils.close(con);
		}

	}
	
	/**
	 * Finish shipping with mar vehicle condition of car.
	 * 
	 * @param vehicle
	 * @param finishedRequest
	 * @throws AppException
	 */
	public void finishShipping(String vehicle, DriverShippingRequest finishedRequest) throws AppException {
		Connection con=null;
		try {
			con = dbManager.getConnection();
			shipRep.finishShipping(con, finishedRequest.getShippingId());
			Shipping ship = shipRep.findShippingById(con, finishedRequest.getShippingId());
			carRep.finishTrip(con, vehicle, 0, ship.getCarId());
			con.commit();
		} catch (SQLException ex) {
			DBUtils.rollback(con);
			LOG.error(Messages.ERR_CANNOT_FINISH_SHIPPING, ex);
			throw new DBException(Messages.ERR_CANNOT_FINISH_SHIPPING, ex);
		} finally {
			DBUtils.close(con);
		}

	}
	
	/**
	 * Returns all driver by shipping id.
	 * 
	 * @return List of user models.
	 *
	 * @throws AppException
	 */
	public List<User> findUsersByShippingId(Integer shippingId) throws AppException {
		List<User> users = new ArrayList<>();
		Connection con=null;
		try {
			con = dbManager.getConnection();
			List<DriverShippingRequest> requests = requestRep.findDriverShippingRequestsByShippingId(con, shippingId);
			for (DriverShippingRequest req : requests) {
				users.add(userRep.findUserById(con, req.getDriverId()));
			}
			con.commit();
		} catch (SQLException ex) {
			DBUtils.rollback(con);
			LOG.error(Messages.ERR_CANNOT_OBTAIN_FLIGHT_BY_ID, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_FLIGHT_BY_ID, ex);
		} finally {
			DBUtils.close(con);
		}

		return users;
	}
}

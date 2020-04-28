package service;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
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
import db.repository.DriverShippingRequestRep;
import db.repository.ShippingRep;
import db.repository.UserRep;
import db.utils.DBUtils;
import model.Car;
import model.DriverShippingRequest;
import model.Shipping;
import model.User;

public class FlightService {

	private static final Logger LOG = Logger.getLogger(FlightService.class);

	private DataSource ds;
	private static FlightService instance;

	public static synchronized FlightService getInstance() throws DBException {
		if (instance == null) {
			instance = new FlightService();
		}
		return instance;
	}

	private FlightService() throws DBException {
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

	public User findUserByShippingRequestId(int id) throws AppException {
		User user = null;
		DriverShippingRequest request = null;
		Connection con = null;
		UserRep userRep = UserRep.getInstance();
		DriverShippingRequestRep requestRep = DriverShippingRequestRep.getInstance();

		try {
			con = getConnection();
			request = requestRep.findDriverShippingRequestById(con, id);
			user = userRep.findUserById(con, request.getDriverId());
			con.commit();
		} catch (SQLException | NoSuchAlgorithmException ex) {
			DBUtils.rollback(con);
			LOG.error(Messages.ERR_CANNOT_OBTAIN_USER_BY_ID, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_ID, ex);
		} finally {
			DBUtils.close(con);
		}
		return user;
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

	public void cancelFlightById(Integer flightId) throws AppException {
		Connection con = null;
		ShippingRep shipRep = ShippingRep.getInstance();
		try {
			con = getConnection();
			con.setAutoCommit(true);
			shipRep.cancelShipping(con, flightId);
		} catch (SQLException ex) {
			LOG.error(Messages.ERR_CANNOT_DELETE_FLIGHT_BY_ID, ex);
			throw new DBException(Messages.ERR_CANNOT_DELETE_FLIGHT_BY_ID, ex);
		} finally {
			DBUtils.close(con);
		}

	}

	public List<Shipping> findAllShips() throws AppException {
		Connection con = null;
		List<Shipping> shippings = null;
		ShippingRep shippingRep = ShippingRep.getInstance();
		try {
			con = getConnection();
			shippings = shippingRep.findAllShips(con);
		} catch (SQLException ex) {
			DBUtils.rollback(con);
			LOG.error(Messages.ERR_CANNOT_OBTAIN_SHIPPINGS, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_SHIPPINGS, ex);
		} finally {
			DBUtils.close(con);
		}
		return shippings;
	}

	public List<Shipping> findShippingsByStatus(String status) throws AppException {
		Connection con = null;
		List<Shipping> shippings = null;
		ShippingRep shippingRep = ShippingRep.getInstance();
		try {
			con = getConnection();
			shippings = shippingRep.findShippingsByStatus(con, status);
		} catch (SQLException ex) {
			DBUtils.rollback(con);
			LOG.error(Messages.ERR_CANNOT_OBTAIN_SHIPPINGS, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_SHIPPINGS, ex);
		} finally {
			DBUtils.close(con);
		}
		return shippings;
	}

	public Shipping findFlightById(Integer flightId) throws AppException {
		Connection con = null;
		Shipping ship = null;
		ShippingRep shipRep = ShippingRep.getInstance();
		try {
			con = getConnection();
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

	public void insertShipping(Integer dispathcerId, String arrivalCity, String departureCity, Date departureTime)
			throws AppException {
		Shipping ship = new Shipping();
		ship.setArrivalCity(arrivalCity);
		ship.setDepartureCity(departureCity);
		ship.setDepartureTime(departureTime);
		ship.setCarId(null);
		ship.setCreationTimestamp(new Timestamp(System.currentTimeMillis()));
		ship.setDispathcerId(dispathcerId);
		ship.setStatus("Open");
		ship.setDriverShippngRequestId(null);
		Connection con = null;
		ShippingRep shipRep = ShippingRep.getInstance();
		try {
			con = getConnection();
			con.setAutoCommit(true);
			shipRep.insertShipping(con, ship);
		} catch (SQLException ex) {
			LOG.error(Messages.ERR_CANNOT_INSERT_SHIPPING, ex);
			throw new DBException(Messages.ERR_CANNOT_INSERT_SHIPPING, ex);
		} finally {
			DBUtils.close(con);
		}

	}

	public DriverShippingRequest findRequestByDriverLoginAndShippingId(Integer shippingId, String loginDriver)
			throws AppException {
		DriverShippingRequest req = null;
		Connection con = null;
		UserRep userRep = UserRep.getInstance();
		DriverShippingRequestRep reqRep = DriverShippingRequestRep.getInstance();
		User driver = null;

		try {
			con = getConnection();
			driver = userRep.findUserByLogin(con, loginDriver);
			req = reqRep.findRequestByShippingIdAndDriverId(con, shippingId, driver.getId());
			con.commit();
		} catch (SQLException | NoSuchAlgorithmException ex) {
			DBUtils.rollback(con);
			LOG.error(Messages.ERR_CANNOT_OBTAIN_REQUEST, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_REQUEST, ex);
		} finally {
			DBUtils.close(con);
		}

		return req;
	}

	public List<Shipping> findShippingsByNotDriverId(Integer driverId) throws AppException {
		Connection con = null;
		List<Shipping> shippings = null;
		DriverShippingRequestRep reqRep = DriverShippingRequestRep.getInstance();
		ShippingRep shipRep = ShippingRep.getInstance();
		try {
			con = getConnection();
			shippings = shipRep.findAllShips(con);

				for (int i=0;i<shippings.size();i++) {
					Shipping sh = shippings.get(i);
					System.out.println("Id= " +sh.getId());
					if(reqRep.findRequestByShippingIdAndDriverId(con, sh.getId(), driverId)!=null) {
						DriverShippingRequest a =reqRep.findRequestByShippingIdAndDriverId(con, sh.getId(), driverId);
						System.out.println("Id= " +a.getId()+ " shipping id = "+ a.getShippingId()+ " driver = "+ a.getDriverId());
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

	public Car findCarByModelAndStatus(String model, Integer status) throws AppException {
		Car car = null;
		Connection con = null;
		CarRep carRep = CarRep.getInstance();

		try {
			con = getConnection();
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

	public void updateShipping(Integer dispathcerId, String status, Integer request, Integer carId, String arrivalCity,
			String departureCity, Date departureTime, Integer id) throws AppException {
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
		Connection con = null;
		ShippingRep shipRep = ShippingRep.getInstance();
		try {
			con = getConnection();
			con.setAutoCommit(true);
			shipRep.updateShipping(con, ship);
		} catch (SQLException ex) {
			LOG.error(Messages.ERR_CANNOT_UPDATE_SHIPPING, ex);
			throw new DBException(Messages.ERR_CANNOT_UPDATE_SHIPPING, ex);
		} finally {
			DBUtils.close(con);
		}

	}

	public void finishShipping(String vehicle, DriverShippingRequest finishedRequest) throws AppException {
		Connection con = null;
		ShippingRep shipRep = ShippingRep.getInstance();
		CarRep carRep = CarRep.getInstance();
		try {
			con = getConnection();
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
	
	public List<User> findUsersByShippingId(Integer shippingId) throws AppException, NoSuchAlgorithmException {
		Connection con = null;
		List<User> users = new ArrayList<>();
		UserRep userRep = UserRep.getInstance();
		DriverShippingRequestRep reqRep = DriverShippingRequestRep.getInstance();
		try {
			con = getConnection();
			List<DriverShippingRequest> requests = reqRep.findDriverShippingRequestsByShippingId(con, shippingId);
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

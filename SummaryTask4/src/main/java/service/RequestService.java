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
import db.repository.DriverShippingRequestRep;
import db.repository.FirmRep;
import db.repository.ShippingRep;
import db.utils.DBUtils;
import model.Car;
import model.DriverShippingRequest;
import model.Firm;
import model.Shipping;

public class RequestService {

	private static final Logger LOG = Logger.getLogger(RequestService.class);

	private DataSource ds;
	private static RequestService instance;

	public static synchronized RequestService getInstance() throws DBException {
		if (instance == null) {
			instance = new RequestService();
		}
		return instance;
	}
	
	private RequestService() throws DBException{
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
	
	public void deleteRequestById(Integer requestId) throws AppException {
		Connection con = null;
		DriverShippingRequestRep reqRep = DriverShippingRequestRep.getInstance();
		try {
			con = getConnection();
			con.setAutoCommit(true);
			reqRep.deleteRequest(con, requestId);
		}catch (SQLException ex ) {
			LOG.error(Messages.ERR_CANNOT_DELETE_REQUEST, ex);
			throw new DBException(Messages.ERR_CANNOT_DELETE_REQUEST, ex);
		}finally {
			DBUtils.close(con);
		}
		
	}
	
	public void insertRequest(DriverShippingRequest request) throws AppException {
		
		Connection con = null;
		DriverShippingRequestRep reqRep = DriverShippingRequestRep.getInstance();
		try {
			con = getConnection();
			con.setAutoCommit(true);
			reqRep.insertRequest(con, request);
		} catch (SQLException ex ) {
			LOG.error(Messages.ERR_CANNOT_INSERT_REQUEST, ex);
			throw new DBException(Messages.ERR_CANNOT_INSERT_REQUEST, ex);
		} finally {
			DBUtils.close(con);
		}
		
	}
	
	

	public List<DriverShippingRequest> findAllRequest() throws AppException{
		Connection con = null;
		List<DriverShippingRequest> requests= null;
		DriverShippingRequestRep requestRep = DriverShippingRequestRep.getInstance();
		try {
			con = getConnection();
			requests= requestRep.findAllRequests(con);
		}catch(SQLException ex ) {
			DBUtils.rollback(con);
			LOG.error(Messages.ERR_CANNOT_OBTAIN_REQUESTS, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_REQUESTS, ex);
		} finally {
			DBUtils.close(con);
		}
		return requests;
	}
	
	public List<DriverShippingRequest> findRequestsByDriverId(Integer driverId) throws AppException{
		Connection con = null;
		List<DriverShippingRequest> requests= null;
		DriverShippingRequestRep requestRep = DriverShippingRequestRep.getInstance();
		try {
			con = getConnection();
			con.setAutoCommit(true);
			requests= requestRep.findDriverShippingRequestsByDriverId(con, driverId);
		}catch(SQLException ex ) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_REQUESTS, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_REQUESTS, ex);
		} finally {
			DBUtils.close(con);
		}
		return requests;
	}
	
	public DriverShippingRequest findRequestInProcessByDriverId(Integer driverId) throws AppException{
		Connection con = null;
		DriverShippingRequest request= null;
		ShippingRep shipRep = ShippingRep.getInstance();
		DriverShippingRequestRep requestRep = DriverShippingRequestRep.getInstance();
		try {
			con = getConnection();
			List<Shipping> shippings = shipRep.findAllShipsInProcess(con);
			List<DriverShippingRequest> requests = requestRep.findDriverShippingRequestsByDriverId(con, driverId);
			boolean k=false;
			for(Shipping sh : shippings) {
				for(DriverShippingRequest req : requests) {
					if(sh.getDriverShippngRequestId()==req.getId()) {
						request=req;
						k=true;
						break;
					}
				}
				if(k) {
					break;
				}
			}
			con.commit();
		}catch(SQLException ex ) {
			DBUtils.rollback(con);
			LOG.error(Messages.ERR_CANNOT_OBTAIN_REQUESTS, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_REQUESTS, ex);
		} finally {
			DBUtils.close(con);
		}
		return request;
	}
	
	public void chooseRequestByRequestId(Integer requestId) throws AppException{
		Connection con = null;
		DriverShippingRequest request= null;
		DriverShippingRequestRep requestRep = DriverShippingRequestRep.getInstance();
		ShippingRep shipRep = ShippingRep.getInstance();
		try {
			con = getConnection();
			request = requestRep.findDriverShippingRequestById(con, requestId);
			shipRep.chooseRequestForShippingById(con, request.getShippingId(), requestId);
			con.commit();
		}catch(SQLException ex ) {
			DBUtils.rollback(con);
			LOG.error(Messages.ERR_CANNOT_OBTAIN_REQUESTS, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_REQUESTS, ex);
		} finally {
			DBUtils.close(con);
		}
	}
	
	public void chooseCarForShipping(Integer carId, Integer shippingId) throws AppException{
		Connection con = null;
		ShippingRep shipRep = ShippingRep.getInstance();
		try {
			con = getConnection();
			con.setAutoCommit(true);
			shipRep.chooseCarForShippingById(con, carId, shippingId);
		}catch(SQLException ex ) {
			LOG.error(Messages.ERR_CANNOT_CHOOSE_CAR, ex);
			throw new DBException(Messages.ERR_CANNOT_CHOOSE_CAR, ex);
		} finally {
			DBUtils.close(con);
		}
	}
	
	public List<DriverShippingRequest> findRequestByShippingId(Integer shippingId) throws AppException{
		Connection con = null;
		List<DriverShippingRequest> requests= null;
		DriverShippingRequestRep requestRep = DriverShippingRequestRep.getInstance();
		try {
			con = getConnection();
			con.setAutoCommit(true);
			requests= requestRep.findDriverShippingRequestsByShippingId(con, shippingId);
		}catch(SQLException ex ) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_REQUESTS, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_REQUESTS, ex);
		} finally {
			DBUtils.close(con);
		}
		return requests;
	}
/////////////////
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

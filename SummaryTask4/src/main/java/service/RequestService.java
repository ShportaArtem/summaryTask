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
import db.repository.DriverShippingRequestRep;
import db.repository.FirmRep;
import db.repository.ShippingRep;
import db.utils.DBUtils;
import model.Car;
import model.DriverShippingRequest;
import model.Firm;
import model.Shipping;

/**
 * Request service. Works with DBManager and repositories. 
 * 
 * @author A.Shporta
 * 
 */
public class RequestService {

	private static final Logger LOG = Logger.getLogger(RequestService.class);

	private DBManager dbManager;
	private DriverShippingRequestRep reqRep;
	private ShippingRep shipRep;
	private FirmRep firmRep;
	private CarRep carRep;
	
	public RequestService(DBManager dbManager, DriverShippingRequestRep reqRep, ShippingRep shipRep, FirmRep firmRep, CarRep carRep) {
	this.carRep = carRep;
	this.dbManager = dbManager;
	this.firmRep = firmRep;
	this.reqRep = reqRep;
	this.shipRep = shipRep;
	}
	
	/**
	 * Delete request by id
	 * @param requestId
	 * 		Id request that will be delete.	
	 * @throws AppException
	 */
	public void deleteRequestById(Integer requestId) throws AppException {
		Connection con=null;
		try {
			con = dbManager.getConnection();
			con.setAutoCommit(true);
			reqRep.deleteRequest(con, requestId);
		}catch (SQLException ex ) {
			LOG.error(Messages.ERR_CANNOT_DELETE_REQUEST, ex);
			throw new DBException(Messages.ERR_CANNOT_DELETE_REQUEST, ex);
		}finally {
			DBUtils.close(con);
		}
		
	}
	
	/**
	 * Insert request in DB
	 * @param request
	 * 			request that will be insert
	 * @throws AppException
	 */
	public void insertRequest(DriverShippingRequest request) throws AppException {
		Connection con=null;
		try {
			con = dbManager.getConnection();
			con.setAutoCommit(true);
			reqRep.insertRequest(con, request);
		} catch (SQLException ex ) {
			LOG.error(Messages.ERR_CANNOT_INSERT_REQUEST, ex);
			throw new DBException(Messages.ERR_CANNOT_INSERT_REQUEST, ex);
		} finally {
			DBUtils.close(con);
		}
		
	}
	
	
	/**
	 * Returns all requests.
	 * 
	 * @return List of driverShippingRequest models.
	 *
	 * @throws AppException
	 */
	public List<DriverShippingRequest> findAllRequest() throws AppException{
		List<DriverShippingRequest> requests= null;
		Connection con=null;
		try {
			con = dbManager.getConnection();
			requests= reqRep.findAllRequests(con);
		}catch(SQLException ex ) {
			DBUtils.rollback(con);
			LOG.error(Messages.ERR_CANNOT_OBTAIN_REQUESTS, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_REQUESTS, ex);
		} finally {
			DBUtils.close(con);
		}
		return requests;
	}
	
	/**
	 *Returns all requests by driver id.
	 *  
	 * @param driverId
	 * @return List of driverShippingRequest models.
	 * @throws AppException
	 */
	public List<DriverShippingRequest> findRequestsByDriverId(Integer driverId) throws AppException{
		List<DriverShippingRequest> requests= null;
		Connection con=null;
		try {
			con = dbManager.getConnection();
			con.setAutoCommit(true);
			requests= reqRep.findDriverShippingRequestsByDriverId(con, driverId);
		}catch(SQLException ex ) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_REQUESTS, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_REQUESTS, ex);
		} finally {
			DBUtils.close(con);
		}
		return requests;
	}
	
	/**
	 * Find request that in progress by driver id
	 * 
	 * @param driverId
	 * 			Id driver who create request that will be find
	 * @return driverShippingRequest model
	 * @throws AppException
	 */
	public DriverShippingRequest findRequestInProcessByDriverId(Integer driverId) throws AppException{
		DriverShippingRequest request= null;
		Connection con=null;
		try {
			con = dbManager.getConnection();
			List<Shipping> shippings = shipRep.findAllShipsInProcess(con);
			List<DriverShippingRequest> requests = reqRep.findDriverShippingRequestsByDriverId(con, driverId);
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
	
	/**
	 * Choose request by id
	 * @param requestId
	 * 			Id request that will  be choose
	 * @throws AppException
	 */
	public void chooseRequestByRequestId(Integer requestId) throws AppException{
		Connection con = null;
		DriverShippingRequest request= null;
		try {
			con = dbManager.getConnection();
			request = reqRep.findDriverShippingRequestById(con, requestId);
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
	
	/**
	 * Choose car for shipping
	 * 
	 * @param carId
	 * 		Id car that will be choose
	 * @param shippingId
	 * 		Id shipping for which will be find car
	 * @throws AppException
	 */
	public void chooseCarForShipping(Integer carId, Integer shippingId) throws AppException{
		Connection con=null;
		try {
			con = dbManager.getConnection();
			con.setAutoCommit(true);
			shipRep.chooseCarForShippingById(con, carId, shippingId);
		}catch(SQLException ex ) {
			LOG.error(Messages.ERR_CANNOT_CHOOSE_CAR, ex);
			throw new DBException(Messages.ERR_CANNOT_CHOOSE_CAR, ex);
		} finally {
			DBUtils.close(con);
		}
	}
	
	/**
	 * Find requests by shipping id
	 * 
	 * @param shippingId
	 * @return list of driverShippingRequest models
	 * @throws AppException
	 */
	public List<DriverShippingRequest> findRequestByShippingId(Integer shippingId) throws AppException{
		Connection con=null;
		List<DriverShippingRequest> requests= null;
		try {
			con = dbManager.getConnection();
			con.setAutoCommit(true);
			requests= reqRep.findDriverShippingRequestsByShippingId(con, shippingId);
		}catch(SQLException ex ) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_REQUESTS, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_REQUESTS, ex);
		} finally {
			DBUtils.close(con);
		}
		return requests;
	}

	/**
	 * Find firm by id
	 * @param firmId
	 * @return firm model
	 * @throws AppException
	 */
	public Firm findFirmById(Integer firmId) throws AppException {
		Connection con=null;
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
	 * @param carId
	 * @return car model
	 * @throws AppException
	 */
	public Car findCarById(Integer carId) throws AppException {
		Connection con=null;
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
	 * @param name
	 * @return firm model
	 * @throws AppException
	 */
	public Firm findFirmByName(String name) throws AppException {
		Connection con=null;
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
	
}

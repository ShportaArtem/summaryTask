package service;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import db.DBManager;
import db.exception.AppException;
import db.exception.DBException;
import db.exception.Messages;
import db.repository.UserRep;
import db.utils.DBUtils;
import model.User;

/**
 * Dispatcher service. Works with DBManager and repositories. 
 * 
 * @author A.Shporta
 * 
 */
public class DispatcherService {

	private static final Logger LOG = Logger.getLogger(DispatcherService.class);
	
	private DBManager dbManager;
	private UserRep userRep;
	private Connection con;
	
	public DispatcherService(DBManager dbManager, UserRep userRep) {
		this.dbManager = dbManager;
		this.userRep= userRep;
	}
	
	
	/**
	 * Delete dispatcher by id
	 * @param dispatcherId
	 * 		Id dispatcher that will be delete.	
	 * @throws AppException
	 */
	public void deleteDispatcherById(Integer dispatcherId) throws AppException {
		try {
			con = dbManager.getConnection();
			con.setAutoCommit(true);
			userRep.deleteUser(con, dispatcherId);	
		}catch (SQLException ex ) {
			LOG.error(Messages.ERR_CANNOT_DELETE_DISPATCHER, ex);
			throw new DBException(Messages.ERR_CANNOT_DELETE_DISPATCHER, ex);
		}finally {
			DBUtils.close(con);
		}
		
	}
	/**
	 * Update dispatcher
	 * 
	 * @param user
	 * 		Dispatcher that will be update
	 * 
	 * @throws AppException
	 */
	public void updateDriver(User user) throws AppException {
		try {
			con = dbManager.getConnection();
			userRep.updateUserById(con, user);
			con.commit();
		} catch (SQLException ex ) {
			DBUtils.rollback(con);
			LOG.error(Messages.ERR_CANNOT_UPDATE_DISPATCHER, ex);
			throw new DBException(Messages.ERR_CANNOT_UPDATE_DISPATCHER, ex);
		} finally {
			DBUtils.close(con);
		}
		
	}
	
	/**
	 * Insert dispatcher in DB
	 * 
	 * @throws AppException
	 */
	public void insertDispatcher(User dispatcher) throws AppException {
		try {
			con = dbManager.getConnection();
			con.setAutoCommit(true);
			userRep.insertUser(con, dispatcher);
		} catch (SQLException ex ) {
			LOG.error(Messages.ERR_CANNOT_INSERT_DISPATCHER, ex);
			throw new DBException(Messages.ERR_CANNOT_INSERT_DISPATCHER, ex);
		} finally {
			DBUtils.close(con);
		}
		
	}
}

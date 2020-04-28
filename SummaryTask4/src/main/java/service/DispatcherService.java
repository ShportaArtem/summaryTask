package service;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import db.exception.AppException;
import db.exception.DBException;
import db.exception.Messages;
import db.repository.UserRep;
import db.utils.DBUtils;
import model.User;

public class DispatcherService {

	private static final Logger LOG = Logger.getLogger(DispatcherService.class);

	private DataSource ds;
	private static DispatcherService instance;

	public static synchronized DispatcherService getInstance() throws DBException {
		if (instance == null) {
			instance = new DispatcherService();
		}
		return instance;
	}
	
	private DispatcherService() throws DBException{
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
	
	
	public void deleteDispatcherById(Integer dispatcherId) throws AppException {
		Connection con = null;
		UserRep userRep = UserRep.getInstance();
		try {
			con = getConnection();
			con.setAutoCommit(true);
			userRep.deleteUser(con, dispatcherId);	
		}catch (SQLException ex ) {
			LOG.error(Messages.ERR_CANNOT_DELETE_DISPATCHER, ex);
			throw new DBException(Messages.ERR_CANNOT_DELETE_DISPATCHER, ex);
		}finally {
			DBUtils.close(con);
		}
		
	}
	
	public void updateDriver(User user) throws AppException {
		Connection con = null;
		UserRep userRep = UserRep.getInstance();
		try {
			con = getConnection();
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
	
	public void insertDispatcher(User dispatcher) throws AppException {
		Connection con = null;
		UserRep userRep = UserRep.getInstance();
		try {
			con = getConnection();
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

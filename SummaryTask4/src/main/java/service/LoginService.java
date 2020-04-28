package service;

import java.security.NoSuchAlgorithmException;
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


public class LoginService {

	private static final Logger LOG = Logger.getLogger(LoginService.class);

	private DataSource ds;
	private static LoginService instance;

	public static synchronized LoginService getInstance() throws DBException {
		if (instance == null) {
			instance = new LoginService();
		}
		return instance;
	}
	
	private LoginService() throws DBException{
		try {
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			ds = (DataSource) envContext.lookup("jdbc/autobasedb");
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
	
	public User findUserByLogin(String login) throws AppException {
		User user = null;
		Connection con = null;
		UserRep userRep = UserRep.getInstance();
		try {
			con = getConnection();
			user =userRep.findUserByLogin(con, login);
			con.commit();
		} catch (SQLException | NoSuchAlgorithmException ex ) {
			DBUtils.rollback(con);
			LOG.error(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
		} finally {
			DBUtils.close(con);
		}
		return user;
	}
	
	
	
	public User findUserById(int id) throws AppException {
		User user = null;
		Connection con = null;
		UserRep userRep = UserRep.getInstance();
		try {
			con = getConnection();
			user =userRep.findUserById(con, id);
			con.commit();
		} catch (SQLException | NoSuchAlgorithmException ex ) {
			DBUtils.rollback(con);
			LOG.error(Messages.ERR_CANNOT_OBTAIN_USER_BY_ID, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_ID, ex);
		} finally {
			DBUtils.close(con);
		}
		return user;
	}
	
	
}

package service;

import java.security.NoSuchAlgorithmException;
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
import db.repository.DriverRep;
import db.repository.UserRep;
import db.utils.DBUtils;
import model.Driver;
import model.User;

public class DriverService {

	private static final Logger LOG = Logger.getLogger(DriverService.class);

	private DataSource ds;
	private static DriverService instance;

	public static synchronized DriverService getInstance() throws DBException {
		if (instance == null) {
			instance = new DriverService();
		}
		return instance;
	}
	
	private DriverService() throws DBException{
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
	
	public List<User> findAllUsersByRoleId(Integer roleId) throws AppException
	{
		List<User> users = null;
		Connection con=null;
		UserRep userRep = UserRep.getInstance();
		
		
		try {
			con = getConnection();
			con.setAutoCommit(true);
		users = userRep.findAllUsersByRoleId(con, roleId);
		}catch(SQLException|NoSuchAlgorithmException ex) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_USERS, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_USERS, ex);
		} finally {
			DBUtils.close(con);
		}
		return users;
	}
	
	public void deleteDriverById(Integer driverId) throws AppException {
		Connection con = null;
		UserRep userRep = UserRep.getInstance();
		try {
			con = getConnection();
			con.setAutoCommit(true);
			System.out.println(driverId);
			userRep.deleteUser(con, driverId);
		}catch (SQLException ex ) {
			LOG.error(Messages.ERR_CANNOT_DELETE_DRIVER, ex);
			throw new DBException(Messages.ERR_CANNOT_DELETE_DRIVER, ex);
		}finally {
			DBUtils.close(con);
		}
		
	}
	
	public void updateDriver(User user, Driver driver) throws AppException {
		Connection con = null;
		UserRep userRep = UserRep.getInstance();
		DriverRep driverRep = DriverRep.getInstance();
		try {
			con = getConnection();
			userRep.updateUserById(con, user);
			driverRep.updateDriverByUserId(con, driver);
			con.commit();
		} catch (SQLException ex ) {
			DBUtils.rollback(con);
			LOG.error(Messages.ERR_CANNOT_UPDATE_CAR, ex);
			throw new DBException(Messages.ERR_CANNOT_UPDATE_CAR, ex);
		} finally {
			DBUtils.close(con);
		}
		
	}
	
	public void insertDriver(String login, String password, String name, String surname, String passport, String phone) throws AppException {
		User user= new User();
		Driver driver = new Driver();
		user.setLogin(login);
		user.setName(name);
		try {
			user.setPassword(password);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		user.setRoleId(2);
		user.setSurname(surname);
		driver.setPassport(passport);
		driver.setPhone(phone);
		Connection con = null;
		UserRep userRep = UserRep.getInstance();
		DriverRep driverRep = DriverRep.getInstance();
		try {
			con = getConnection();
			user = userRep.insertUser(con, user);
			driver.setUserId(user.getId());
			driverRep.insertDriver(con, driver);
			con.commit();
		} catch (SQLException ex ) {
			DBUtils.rollback(con);
			LOG.error(Messages.ERR_CANNOT_INSERT_DRIVER, ex);
			throw new DBException(Messages.ERR_CANNOT_INSERT_DRIVER, ex);
		} finally {
			DBUtils.close(con);
		}
		
	}
	public Driver findDriverByUserId(Integer userId) throws AppException {
		Driver driver = null;
		Connection con = null;
		DriverRep driverRep = DriverRep.getInstance();
		try {
			con = getConnection();
			con.setAutoCommit(true);
			driver =driverRep.findDriverByUserId(con, userId);
		} catch (SQLException ex ) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
			throw new DBException(Messages.ERR_CANNOT_OBTAIN_USER_BY_LOGIN, ex);
		} finally {
			DBUtils.close(con);
		}
		return driver;
	}
}

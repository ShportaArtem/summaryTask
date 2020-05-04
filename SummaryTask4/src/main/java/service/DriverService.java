package service;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.Logger;

import db.DBManager;
import db.exception.AppException;
import db.exception.DBException;
import db.exception.Messages;
import db.repository.DriverRep;
import db.repository.UserRep;
import db.utils.DBUtils;
import model.Driver;
import model.User;
import utils.HashUtil;

/**
 * Driver service. Works with DBManager and repositories. 
 * 
 * @author A.Shporta
 * 
 */
public class DriverService {

	private static final Logger LOG = Logger.getLogger(DriverService.class);

	private DBManager dbManager;
	private Connection con;
	private UserRep userRep; 
	private DriverRep driverRep;
	
	public DriverService(DBManager dbManager, UserRep userRep, DriverRep driverRep) {
		this.dbManager = dbManager;
		this.driverRep = driverRep;
		this.userRep = userRep;
	}
	
	/**
	 * Returns all user by role.
	 * 
	 * @return List of user models.
	 *
	 * @throws AppException
	 */
	public List<User> findAllUsersByRoleId(Integer roleId) throws AppException
	{
		List<User> users = null;
		
		try {
			con = dbManager.getConnection();
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

	/**
	 * Delete driver by id
	 * @param driverId
	 * 		Id driver that will be delete.	
	 * @throws AppException
	 */
	public void deleteDriverById(Integer driverId) throws AppException {
		try {
			con = dbManager.getConnection();
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
	
	/**
	 * Update driver
	 * 
	 * @param driver
	 * 		Driver that will be update
	 * 
	 * @throws AppException
	 */
	public void updateDriver(User user, Driver driver) throws AppException {
		try {
			con = dbManager.getConnection();
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
	
	/**
	 * Insert driver in DB
	 * 
	 * @throws AppException
	 */
	public void insertDriver(String login, String password, String name, String surname, String passport, String phone) throws AppException {
		User user= new User();
		Driver driver = new Driver();
		user.setLogin(login);
		user.setName(name);
			try {
				user.setPassword(new String(HashUtil.getSHA(password)));
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		user.setRoleId(2);
		user.setSurname(surname);
		driver.setPassport(passport);
		driver.setPhone(phone);
		try {
			con = dbManager.getConnection();
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
	
	/**
	 * Find driver by id
	 * 
	 * @param userId
	 * 		Id driver that will be find
	 * @return driver model
	 * 
	 * @throws AppException
	 */
	public Driver findDriverByUserId(Integer userId) throws AppException {
		Driver driver = null;
		try {
			con = dbManager.getConnection();
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

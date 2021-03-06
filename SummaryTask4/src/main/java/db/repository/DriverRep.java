package db.repository;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.utils.DBUtils;
import model.Driver;
/**
 * Driver repository. Works with table driver in db. 
 * 
 * @author A.Shporta
 *
 */
public class DriverRep {
	
	private static final String SQL_CREATE_DRIVER = "INSERT INTO driver VALUES (?, ?, ?)";
	private static final String SQL_DELETE_DRIVER = "DELETE FROM driver WHERE id=?";
	private static final String SQL_FIND_ALL_DRIVERS= "SELECT * FROM driver";
	private static final String SQL_FIND_DRIVER_BY_USER_ID= "SELECT * FROM driver WHERE user_id=?";
	private static final String SQL_UPDATE_DRIVER_BY_USER_ID = "UPDATE driver SET passport=?, telephone_number=? WHERE user_id=?";

	/**
	 * Returns all drivers.
	 * 
	 * @return List of driver models.
	 */
	public List<Driver> findAllDrivers(Connection con) throws SQLException, NoSuchAlgorithmException {
		List<Driver> drivers = new ArrayList<>();

		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(SQL_FIND_ALL_DRIVERS);

			while (rs.next()) {
				drivers.add(extractDriver(rs));
			}
		} finally {
			DBUtils.close(rs);
			DBUtils.close(stmt);
		}
		return drivers;
	}
	
	/**
	 * Extracts a driver model from the result set.
	 * 
	 * @param rs
	 *            Result set from which a driver model will be extracted.
 	 * @return Driver model
	 */
	private Driver extractDriver(ResultSet rs) throws SQLException {
		Driver driver = new Driver();
		driver.setUserId(rs.getInt("user_id"));
		driver.setPassport(rs.getString("passport"));
		driver.setPhone(rs.getString("telephone_number"));
		
		return driver;
	}
	
	/**
	 * Update driver.
	 * 
	 * @param driver
	 *            driver to update.
	 * @throws SQLException
	 */
	public boolean updateDriverByUserId(Connection con, Driver driver) throws SQLException { 
		boolean res = false;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = con.prepareStatement(SQL_UPDATE_DRIVER_BY_USER_ID, Statement.RETURN_GENERATED_KEYS);

			int k = 1;
			pstmt.setString(k++, driver.getPassport());
			pstmt.setString(k++, driver.getPhone());
			pstmt.setInt(k++, driver.getUserId());
			
			if (pstmt.executeUpdate() > 0) {
				rs = pstmt.getGeneratedKeys();
				if (rs.next()) {
					res = true;
				}
			}
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
		}

		return res;
	}
	
	/**
	 * Insert a driver to db.
	 * 
	 * @param driver
	 *            The driver that will be insert.
	 */
	public boolean insertDriver(Connection con, Driver driver) throws SQLException { 
		boolean res = false;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = con.prepareStatement(SQL_CREATE_DRIVER, Statement.RETURN_GENERATED_KEYS);

			int k = 1;
			pstmt.setInt(k++, driver.getUserId());
			pstmt.setString(k++, driver.getPassport());
			pstmt.setString(k++, driver.getPhone());
			
			if (pstmt.executeUpdate() > 0) {
				rs = pstmt.getGeneratedKeys();
				if (rs.next()) {
					res = true;
				}
			}
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
		}

		return res;
	}

	
	/**
	 * Delete a driver from db.
	 * 
	 * @param driver
	 *            The driver that will be delete.
	 */
	public boolean deleteDriver(Connection con, int userId) throws SQLException {
		PreparedStatement pstmt = null;

		try {
			pstmt = con.prepareStatement(SQL_DELETE_DRIVER);

			int k = 1;
			pstmt.setInt(k++, userId);

			return pstmt.executeUpdate() > 0;
		} finally {
			DBUtils.close(pstmt);
		}
	}
	/**
	 * Find driver by id.
	 * 
	 * @return driver model.
	 */
	public Driver findDriverByUserId(Connection con, int userId) throws SQLException {
		Driver driver = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
			pstmt = con.prepareStatement(SQL_FIND_DRIVER_BY_USER_ID);
			pstmt.setInt(1, userId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				driver = extractDriver(rs);
			}
		return driver;
	}
}

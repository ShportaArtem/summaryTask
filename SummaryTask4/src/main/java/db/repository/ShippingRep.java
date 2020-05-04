package db.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import db.utils.DBUtils;
import model.Shipping;
/**
 * Shipping repository. Works with table shipping in db. 
 * 
 * @author A.Shporta
 *
 */
public class ShippingRep {
	
	private static final String SQL_CREATE_SHIPPING = "INSERT INTO shipping VALUES (DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?)";
	private static final String SQL_FIND_ALL_SHIPPINGS="SELECT * FROM shipping";
	private static final String SQL_CANCEl_SHIPPING = "UPDATE shipping SET status =? WHERE id=?";
	private static final String SQL_FIND_SHIPPING_BY_ID = "SELECT * FROM shipping WHERE id=?";
	private static final String SQL_FIND_SHIPPING_BY_REQUEST_ID = "SELECT * FROM shipping WHERE suitable_driver_shipping_request_id=?";
	private static final String SQL_FIND_SHIPPINGS_BY_STATUS = "SELECT * FROM shipping WHERE status=?";
	private static final String SQL_FIND_SHIPPINGS_IN_PROCESS= "SELECT * FROM shipping WHERE status='In progress'";
	private static final String SQL_UPDATE_SHIPPING_BY_ID = "UPDATE shipping SET dispatcher_id =?, status =?, suitable_driver_shipping_request_id=?, car_id=?, arrival_city=?, departure_city=?, creation_timestamp=?, departure_time=? WHERE id=?";
	private static final String SQL_FINISH_SHIPPING_BY_ID = "UPDATE shipping SET  status ='Close' WHERE id=?";
	private static final String SQL_CHOOSE_REQUEST_FOR_SHIPPING_BY_ID = "UPDATE shipping SET  status =?, suitable_driver_shipping_request_id=? WHERE id=?";
	private static final String SQL_CHOOSE_CAR_FOR_SHIPPING_BY_ID = "UPDATE shipping SET car_id=? WHERE id=?";
	
	/**
	 * Returns all shippings.
	 * 
	 * @return List of shipping models.
	 */
	public List<Shipping> findAllShips(Connection con) throws SQLException {
		List<Shipping> shippings = new ArrayList<>();

		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(SQL_FIND_ALL_SHIPPINGS);

			while (rs.next()) {
				shippings.add(extractShipping(rs));
			}
		} finally {
			DBUtils.close(rs);
			DBUtils.close(stmt);
		}
		return shippings;
	}
	
	/**
	 * Returns all shippings that in progress.
	 * 
	 * @return List of shipping models.
	 */
	public List<Shipping> findAllShipsInProcess(Connection con) throws SQLException {
		List<Shipping> shippings = new ArrayList<>();

		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(SQL_FIND_SHIPPINGS_IN_PROCESS);

			while (rs.next()) {
				shippings.add(extractShipping(rs));
			}
		} finally {
			DBUtils.close(rs);
			DBUtils.close(stmt);
		}
		return shippings;
	}
	/**
	 * Returns all shippings by status.
	 * 
	 * @return List of shipping models.
	 */
	public List<Shipping> findShippingsByStatus(Connection con, String status) throws SQLException {
		List<Shipping> shippings = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(SQL_FIND_SHIPPINGS_BY_STATUS);
			pstmt.setString(1, status);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				shippings.add(extractShipping(rs));
			}
			}finally {
				DBUtils.close(rs);
				DBUtils.close(pstmt);
			}
		
		return shippings;
	}
	
	/**
	 * Extracts a shipping model from the result set.
	 * 
	 * @param rs
	 *            Result set from which a shipping model will be extracted.
	 * @return Shipping model
	 */
	private Shipping extractShipping(ResultSet rs) throws SQLException {
		Shipping shipping = new Shipping();
		shipping.setId(rs.getInt("id"));
		shipping.setDispathcerId(rs.getInt("dispatcher_id"));
		shipping.setStatus(rs.getString("status"));
		shipping.setDriverShippngRequestId(rs.getInt("suitable_driver_shipping_request_id"));
		shipping.setCarId(rs.getInt("car_id"));
		shipping.setArrivalCity(rs.getString("arrival_city"));
		shipping.setDepartureCity(rs.getString("departure_city"));
		shipping.setCreationTimestamp(rs.getTimestamp("creation_timestamp"));
		shipping.setDepartureTime(rs.getDate("departure_time"));
		return shipping;
	}
	
	/**
	 * Insert a shipping to db.
	 * 
	 * @param shipping
	 *            The shipping that will be insert.
	 */
	public boolean insertShipping(Connection con, Shipping shipping) throws SQLException { 
		boolean res = false;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = con.prepareStatement(SQL_CREATE_SHIPPING, Statement.RETURN_GENERATED_KEYS);

			int k = 1;
			pstmt.setInt(k++, shipping.getDispathcerId());
			pstmt.setString(k++, shipping.getStatus());
			pstmt.setNull(k++, Types.INTEGER);
			pstmt.setNull(k++, Types.INTEGER);
			pstmt.setString(k++, shipping.getArrivalCity());
			pstmt.setString(k++, shipping.getDepartureCity());
			pstmt.setTimestamp(k++, shipping.getCreationTimestamp());
			pstmt.setDate(k++, shipping.getDepartureTime());

			if (pstmt.executeUpdate() > 0) {
				rs = pstmt.getGeneratedKeys();
				if (rs.next()) {
					int shippingId = rs.getInt(1);
					shipping.setId(shippingId);
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
	 * Choose a shippingDriverRequest for shipping.
	 * 
	 * @param shipping id and request id
	 */
	public boolean chooseRequestForShippingById(Connection con, Integer shippingId, Integer requestId) throws SQLException { 
		boolean res = false;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = con.prepareStatement(SQL_CHOOSE_REQUEST_FOR_SHIPPING_BY_ID, Statement.RETURN_GENERATED_KEYS);

			int k = 1;
			pstmt.setString(k++, "In progress");
			pstmt.setInt(k++, requestId);
			pstmt.setInt(k++, shippingId);
			
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
	 * Choose a car for shipping.
	 * 
	 * @param shipping id and car id
	 */
	public boolean chooseCarForShippingById(Connection con, Integer carId, Integer shippingId) throws SQLException { 
		boolean res = false;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = con.prepareStatement(SQL_CHOOSE_CAR_FOR_SHIPPING_BY_ID, Statement.RETURN_GENERATED_KEYS);

			int k = 1;
			pstmt.setInt(k++, carId);
			pstmt.setInt(k++, shippingId);
			
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
	 * Update shipping.
	 * 
	 * @param shipping
	 *            Shipping to update.
	 * @throws SQLException
	 */
	public boolean updateShipping(Connection con, Shipping shipping) throws SQLException { 
		boolean res = false;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = con.prepareStatement(SQL_UPDATE_SHIPPING_BY_ID, Statement.RETURN_GENERATED_KEYS);

			int k = 1;
			pstmt.setInt(k++, shipping.getDispathcerId());
			pstmt.setString(k++, shipping.getStatus());
			if(shipping.getDriverShippngRequestId()!=null && shipping.getDriverShippngRequestId()!=0) {
			pstmt.setInt(k++, shipping.getDriverShippngRequestId());
			}else {
				pstmt.setNull(k++, Types.INTEGER);
			}
			if(shipping.getCarId()==null && shipping.getCarId()!=0) {
			pstmt.setInt(k++, shipping.getCarId());
			}else {
				pstmt.setNull(k++, Types.INTEGER);
			}
			pstmt.setString(k++, shipping.getArrivalCity());
			pstmt.setString(k++, shipping.getDepartureCity());
			pstmt.setTimestamp(k++, shipping.getCreationTimestamp());
			pstmt.setDate(k++, shipping.getDepartureTime());
			pstmt.setInt(k++, shipping.getId());
			
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
	 * Finish a shipping by id.
	 * 
	 * @param shipping id 
	 */
	public boolean finishShipping(Connection con,Integer id) throws SQLException { 
		boolean res = false;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = con.prepareStatement(SQL_FINISH_SHIPPING_BY_ID, Statement.RETURN_GENERATED_KEYS);

			int k = 1;
			pstmt.setInt(k++, id);
			
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
	 * Returns shipping by id.
	 * 
	 * @return shipping model.
	 */
	public Shipping findShippingById(Connection con, Integer id) throws SQLException {
		Shipping ship = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = con.prepareStatement(SQL_FIND_SHIPPING_BY_ID);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();

			if(rs.next()) {
				ship = extractShipping(rs);
			}
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
		}
		return ship;
	}
	/**
	 * Returns shipping by request id.
	 * 
	 * @return shipping model.
	 */
	public Shipping findShippingByRequestId(Connection con, Integer requestId) throws SQLException {
		Shipping ship = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = con.prepareStatement(SQL_FIND_SHIPPING_BY_REQUEST_ID);
			pstmt.setInt(1, requestId);
			rs = pstmt.executeQuery();

			if(rs.next()) {
				ship = extractShipping(rs);
			}
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
		}
		return ship;
	}
	/**
	 * Cancel shipping by id.
	 */
	public boolean cancelShipping(Connection con, int shippingId) throws SQLException {
		PreparedStatement pstmt = null;

		try {
			pstmt = con.prepareStatement(SQL_CANCEl_SHIPPING);

			int k = 1;
			pstmt.setInt(k++, shippingId);

			return pstmt.executeUpdate() > 0;
		} finally {
			DBUtils.close(pstmt);
		}
	}
}

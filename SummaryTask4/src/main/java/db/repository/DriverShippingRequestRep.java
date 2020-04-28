package db.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.utils.DBUtils;
import model.DriverShippingRequest;

public class DriverShippingRequestRep {
	private static final String SQL_CREATE_REQUEST = "INSERT INTO driver_shipping_request VALUES (DEFAULT, ?, ?, ?, ?, ?)";
	private static final String SQL_FIND_ALL_REQUESTS="SELECT * FROM driver_shipping_request";
	private static final String SQL_DELETE_REQUEST = "DELETE FROM driver_shipping_request WHERE id=?";
	private static final String SQL_FIND_REQUEST_BY_ID = "SELECT * FROM driver_shipping_request WHERE id=?";
	private static final String SQL_FIND_REQUESTS_BY_SHIPPING_ID = "SELECT * FROM driver_shipping_request WHERE shipping_id=?";
	private static final String SQL_FIND_REQUEST_BY_SHIPPING_ID_AND_DRIVER_ID = "SELECT * FROM driver_shipping_request WHERE shipping_id=? and driver_id=?";
	private static final String SQL_FIND_REQUESTS_BY_DRIVER_ID = "SELECT * FROM driver_shipping_request WHERE driver_id=?";
	
	private static DriverShippingRequestRep instance;
	public static synchronized DriverShippingRequestRep getInstance() {
		if (instance == null) {
			instance = new DriverShippingRequestRep();
		}
		return instance;
	}

	private DriverShippingRequestRep() {
	}
	
	public List<DriverShippingRequest> findAllRequests(Connection con) throws SQLException {
		List<DriverShippingRequest> requests = new ArrayList<>();

		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(SQL_FIND_ALL_REQUESTS);

			while (rs.next()) {
				requests.add(extractDriverShippingRequest(rs));
			}
		} finally {
			DBUtils.close(rs);
			DBUtils.close(stmt);
		}
		return requests;
	}
	
	public List<DriverShippingRequest> findDriverShippingRequestsByShippingId(Connection con, int shippingId) throws SQLException {
		List<DriverShippingRequest> requests = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(SQL_FIND_REQUESTS_BY_SHIPPING_ID );
			pstmt.setInt(1, shippingId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				requests.add(extractDriverShippingRequest(rs));
			}
			}finally {
				DBUtils.close(rs);
				DBUtils.close(pstmt);
			}
		
		return requests;
	}
	
	public List<DriverShippingRequest> findDriverShippingRequestsByDriverId(Connection con, int driverId) throws SQLException {
		List<DriverShippingRequest> requests = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(SQL_FIND_REQUESTS_BY_DRIVER_ID);
			pstmt.setInt(1, driverId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				requests.add(extractDriverShippingRequest(rs));
			}
			}finally {
				DBUtils.close(rs);
				DBUtils.close(pstmt);
			}
		
		return requests;
	}
	
	public List<Integer> findRequestsIdByDriverId(Connection con, int driverId) throws SQLException {
		List<Integer> requests = new ArrayList<>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = con.prepareStatement(SQL_FIND_REQUESTS_BY_DRIVER_ID);
			pstmt.setInt(1, driverId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				requests.add(rs.getInt("id"));
			}
			}finally {
				DBUtils.close(rs);
				DBUtils.close(pstmt);
			}
		
		return requests;
	}
	
	public DriverShippingRequest findDriverShippingRequestById(Connection con, int id) throws SQLException {
		DriverShippingRequest request = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
			pstmt = con.prepareStatement(SQL_FIND_REQUEST_BY_ID);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				request = extractDriverShippingRequest(rs);
			}
		return request;
	}
	
	public DriverShippingRequest findRequestByShippingIdAndDriverId(Connection con, Integer shippingId, Integer driverId) throws SQLException {
		DriverShippingRequest request = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = con.prepareStatement(SQL_FIND_REQUEST_BY_SHIPPING_ID_AND_DRIVER_ID);
			pstmt.setInt(1, shippingId);
			pstmt.setInt(2, driverId);
			
			rs = pstmt.executeQuery();

			if(rs.next()) {
				request = extractDriverShippingRequest(rs);
			}
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
		}
		return request;
	}
	
	private DriverShippingRequest extractDriverShippingRequest(ResultSet rs) throws SQLException {
		DriverShippingRequest request = new DriverShippingRequest();
		request.setId(rs.getInt("id"));
		request.setDriverId(rs.getInt("driver_id"));
		request.setCarryinfCapacity(rs.getInt("carrying_capacity"));
		request.setPassangersCapacity(rs.getInt("passengers_capaity"));
		request.setVehicleCondition(rs.getString("vehicle_condition"));
		request.setShippingId(rs.getInt("shipping_id"));
		return request;
	}
	
	
	public boolean insertRequest(Connection con, DriverShippingRequest request) throws SQLException { 
		boolean res = false;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = con.prepareStatement(SQL_CREATE_REQUEST, Statement.RETURN_GENERATED_KEYS);

			int k = 1;
			pstmt.setInt(k++, request.getDriverId());
			pstmt.setInt(k++, request.getCarryinfCapacity());
			pstmt.setInt(k++, request.getPassangersCapacity());
			pstmt.setString(k++, request.getVehicleCondition());
			pstmt.setInt(k++, request.getShippingId());

			if (pstmt.executeUpdate() > 0) {
				rs = pstmt.getGeneratedKeys();
				if (rs.next()) {
					int requestId = rs.getInt(1);
					request.setId(requestId);
					res = true;
				}
			}
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
		}

		return res;
	}

	public boolean deleteRequest(Connection con, int requestId) throws SQLException {
		PreparedStatement pstmt = null;

		try {
			pstmt = con.prepareStatement(SQL_DELETE_REQUEST);

			int k = 1;
			pstmt.setInt(k++, requestId);

			return pstmt.executeUpdate() > 0;
		} finally {
			DBUtils.close(pstmt);
		}
	}
}

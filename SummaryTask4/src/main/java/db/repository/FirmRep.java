package db.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.utils.DBUtils;
import model.Firm;
/**
 * Firm repository. Works with table firm in db. 
 * 
 * @author A.Shporta
 *
 */
public class FirmRep {
	private static final String SQL_CREATE_FIRM = "INSERT INTO firm VALUES (DEFAULT, ?)";
	private static final String SQL_FIND_ALL_FIRMS="SELECT * FROM firm";
	private static final String SQL_FIND_FIRM_BY_ID="SELECT * FROM firm WHERE id=?";
	private static final String SQL_FIND_FIRM_BY_NAME="SELECT * FROM firm WHERE name=?";
	
	/**
	 * Returns all firms.
	 * 
	 * @return List of firm models.
	 */
	public List<Firm> findAllFirms(Connection con) throws SQLException {
		List<Firm> firms = new ArrayList<>();

		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(SQL_FIND_ALL_FIRMS);

			while (rs.next()) {
				firms.add(extractFirm(rs));
			}
		} finally {
			DBUtils.close(rs);
			DBUtils.close(stmt);
		}
		return firms;
	}
	
	
	/**
	 * Extracts a firm model from the result set.
	 * 
	 * @param rs
	 *            Result set from which a firm model will be extracted.
	 * @return Firm model
	 */
	private Firm extractFirm(ResultSet rs) throws SQLException {
		Firm firm = new Firm();
		firm.setId(rs.getInt("id"));
		firm.setName(rs.getString("name"));
		return firm;
	}
	/**
	 * Find firm by id.
	 * 
	 * @return firm model.
	 */
	public Firm findFirmById(Connection con, int id) throws SQLException {
		Firm firm= null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
			pstmt = con.prepareStatement(SQL_FIND_FIRM_BY_ID);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				firm = extractFirm(rs);
			}
		return firm;
	}
	/**
	 * Find firm by name.
	 * 
	 * @return firm model.
	 */
	public Firm findFirmByName(Connection con, String name) throws SQLException {
		Firm firm= null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
			pstmt = con.prepareStatement(SQL_FIND_FIRM_BY_NAME);
			pstmt.setString(1, name);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				firm = extractFirm(rs);
			}
		return firm;
	}
	/**
	 * Insert a firm to db.
	 * 
	 * @param firm
	 *            The firm that will be insert.
	 */
	public boolean insertFirm(Connection con, Firm firm) throws SQLException { 
		boolean res = false;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = con.prepareStatement(SQL_CREATE_FIRM, Statement.RETURN_GENERATED_KEYS);

			int k = 1;
			pstmt.setInt(k++, firm.getId());
			pstmt.setString(k, firm.getName());

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
}

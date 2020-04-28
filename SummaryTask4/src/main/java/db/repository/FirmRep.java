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

public class FirmRep {
	private static final String SQL_CREATE_FIRM = "INSERT INTO firm VALUES (DEFAULT, ?)";
	private static final String SQL_FIND_ALL_FIRMS="SELECT * FROM firm";
	private static final String SQL_FIND_FIRM_BY_ID="SELECT * FROM firm WHERE id=?";
	private static final String SQL_FIND_FIRM_BY_NAME="SELECT * FROM firm WHERE name=?";
	
	private static FirmRep instance;
	public static synchronized FirmRep getInstance() {
		if (instance == null) {
			instance = new FirmRep();
		}
		return instance;
	}

	private FirmRep() {
	}
	
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
	
	
	
	private Firm extractFirm(ResultSet rs) throws SQLException {
		Firm firm = new Firm();
		firm.setId(rs.getInt("id"));
		firm.setName(rs.getString("name"));
		return firm;
	}
	
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

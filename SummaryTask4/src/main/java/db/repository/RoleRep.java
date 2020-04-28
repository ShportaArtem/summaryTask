package db.repository;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.utils.DBUtils;
import model.Role;

public class RoleRep {
	
	private static final String SQL_FIND_ROLE_BY_ID = "SELECT * FROM role WHERE id = ?";
	
	private static RoleRep instance;
	public static synchronized RoleRep getInstance() {
		if (instance == null) {
			instance = new RoleRep();
		}
		return instance;
	}

	private RoleRep() {
	}
	
	
	public Role findRole(Connection con, int roleId) throws SQLException{ 
		Role role = null;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = con.prepareStatement(SQL_FIND_ROLE_BY_ID);
			pstmt.setInt(1, roleId);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				role = extractRole(rs);
			}
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
		}
		return role;
	}

	
	private Role extractRole(ResultSet rs) throws SQLException {
		Role role= new Role();
		role.setId(rs.getInt("id"));
		role.setRole(rs.getString("role"));
		return role;
	}

	
	
}
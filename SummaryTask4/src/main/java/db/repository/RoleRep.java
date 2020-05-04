package db.repository;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import db.utils.DBUtils;
import model.Role;
/**
 * Role repository. Works with table role in db. 
 * 
 * @author A.Shporta
 *
 */
public class RoleRep {
	
	private static final String SQL_FIND_ROLE_BY_ID = "SELECT * FROM role WHERE id = ?";
	
	/**
	 * Find role by id.
	 * 
	 * @return role model.
	 */
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

	/**
	 * Extracts a role model from the result set.
	 * 
	 * @param rs
	 *            Result set from which a role model will be extracted.
	 * @return Role model
	 */
	private Role extractRole(ResultSet rs) throws SQLException {
		Role role= new Role();
		role.setId(rs.getInt("id"));
		role.setRole(rs.getString("role"));
		return role;
	}

	
	
}
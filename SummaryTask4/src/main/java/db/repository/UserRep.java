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
import model.User;


public class UserRep {
	
	private static final String SQL_CREATE_USER = "INSERT INTO users VALUES (DEFAULT, ?, ?, ?, ?, ?)";
	private static final String SQL_FIND_ALL_USERS="SELECT * FROM users";
	private static final String SQL_FIND_ALL_USERS_BY_ROLE_ID="SELECT * FROM users WHERE role_id=?";
	private static final String SQL_DELETE_USER = "DELETE FROM users WHERE id=?";
	private static final String SQL_FIND_USER_BY_LOGIN = "SELECT * FROM users WHERE login=?";
	private static final String SQL_FIND_USER_BY_ID = "SELECT * FROM users WHERE id=?";
	private static final String SQL_UPDATE_USER_BY_ID = "UPDATE users SET login=?, password=?, name=?, surname=? WHERE id=?";
	
	private static UserRep instance;
	public static synchronized UserRep getInstance() {
		if (instance == null) {
			instance = new UserRep();
		}
		return instance;
	}

	private UserRep() {
	}
	
	public List<User> findAllUsers(Connection con) throws SQLException, NoSuchAlgorithmException {
		List<User> users = new ArrayList<>();

		Statement stmt = null;
		ResultSet rs = null;

		try {
			stmt = con.createStatement();
			rs = stmt.executeQuery(SQL_FIND_ALL_USERS);

			while (rs.next()) {
				users.add(extractUser(rs));
			}
		} finally {
			DBUtils.close(rs);
			DBUtils.close(stmt);
		}
		return users;
	}
	
	public boolean updateUserById(Connection con, User user) throws SQLException { 
		boolean res = false;

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = con.prepareStatement(SQL_UPDATE_USER_BY_ID, Statement.RETURN_GENERATED_KEYS);

			int k = 1;
			pstmt.setString(k++, user.getLogin());
			pstmt.setString(k++, user.getPasswordView());
			pstmt.setString(k++, user.getName());
			pstmt.setString(k++, user.getSurname());
			pstmt.setInt(k++, user.getId());
			
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
	
	public List<User> findAllUsersByRoleId(Connection con, Integer roleId) throws SQLException, NoSuchAlgorithmException {
		List<User> users = new ArrayList<>();

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = con.prepareStatement(SQL_FIND_ALL_USERS_BY_ROLE_ID, Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, roleId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				users.add(extractUser(rs));
			}
			}finally {
				DBUtils.close(rs);
				DBUtils.close(pstmt);
			}
		return users;
	}
	
	public User findUserById(Connection con, int id) throws SQLException, NoSuchAlgorithmException {
		User user = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
			pstmt = con.prepareStatement(SQL_FIND_USER_BY_ID);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				user = extractUser(rs);
			}
		return user;
	}
	
	public User findUserByLogin(Connection con,String login) throws SQLException, NoSuchAlgorithmException {
		User user = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
			pstmt = con.prepareStatement(SQL_FIND_USER_BY_LOGIN);
			pstmt.setString(1, login);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				user = extractUser(rs);
			}
		return user;
	}
	
	private User extractUser(ResultSet rs) throws SQLException, NoSuchAlgorithmException {
		User user = new User();
		user.setId(rs.getInt("id"));
		user.setRoleId(rs.getInt("role_id"));
		user.setLogin(rs.getString("login"));
		user.setPassword(rs.getString("password"));
		user.setName(rs.getString("name"));
		user.setSurname(rs.getString("surname"));
		return user;
	}
	
	
	public User insertUser(Connection con, User user) throws SQLException { 

		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			pstmt = con.prepareStatement(SQL_CREATE_USER, Statement.RETURN_GENERATED_KEYS);

			int k = 1;
			pstmt.setInt(k++, user.getRoleId());
			pstmt.setString(k++, user.getLogin());
			pstmt.setString(k++, user.getPasswordView());
			pstmt.setString(k++, user.getName());
			pstmt.setString(k++, user.getSurname());

			if (pstmt.executeUpdate() > 0) {
				rs = pstmt.getGeneratedKeys();
				if (rs.next()) {
					int userId = rs.getInt(1);
					user.setId(userId);
				}
			}
		} finally {
			DBUtils.close(rs);
			DBUtils.close(pstmt);
		}

		return user;
	}

	

	public boolean deleteUser(Connection con, int userId) throws SQLException {
		PreparedStatement pstmt = null;

		try {
			pstmt = con.prepareStatement(SQL_DELETE_USER);

			int k = 1;
			pstmt.setInt(k++, userId);

			return pstmt.executeUpdate() > 0;
		} finally {
			DBUtils.close(pstmt);
		}
	}
}

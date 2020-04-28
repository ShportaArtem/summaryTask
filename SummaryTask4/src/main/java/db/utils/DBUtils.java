package db.utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class DBUtils {
	
	private static Logger LOG = Logger.getLogger(DBUtils.class);
	
	public static void close(Connection con) {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				LOG.error("Cannot close connection", e);
			}
		}
	}

	public static void close(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				LOG.error("Cannot close statement", e);
			}
		}
	}

	public static void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				LOG.error("Cannot close result set", e);
			}
		}
	}

	public static void rollback(Connection con) {
		if (con != null) {
			try {
				con.rollback();
			} catch (SQLException e) {
				LOG.error("Cannot rollback transaction", e);
			}
		}
	}
	
	

}

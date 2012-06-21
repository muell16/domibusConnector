package org.holodeck.common.logging.jdbc;

import java.sql.SQLException;

import org.apache.log4j.jdbc.JDBCAppender;


public class JDBCAppender_new extends JDBCAppender {
	@Override
	protected void execute(String sql) throws SQLException {
		System.out.println("Statement before: " + sql);
		
		
		
		System.out.println("Statement after: " + sql);
		super.execute(sql);
	}
}
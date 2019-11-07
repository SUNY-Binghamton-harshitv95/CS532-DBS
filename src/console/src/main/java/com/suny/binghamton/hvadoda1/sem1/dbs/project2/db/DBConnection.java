package com.suny.binghamton.hvadoda1.sem1.dbs.project2.db;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import oracle.jdbc.pool.OracleDataSource;

public class DBConnection implements Closeable {
	private OracleDataSource ds;
	public static Properties config;
	private Connection con;
	private static DBConnection instance = null;
	
	private DBConnection() throws SQLException {
		ds = new OracleDataSource();
        ds.setURL(config.getProperty("db_url"));
        con = ds.getConnection(config.getProperty("username"), config.getProperty("password"));
	}
	
	public static DBConnection factory() throws SQLException {
		if (config == null) throw new RuntimeException("Database config is not initialized");
		if (instance == null)
			instance = new DBConnection();
		return instance;
	}
	
	public Connection getConnection() {
		return con;
	}
	
	@Override
	public void close() {
		try {
			this.con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.con = null;
		instance = null;
	}
	@Override
	protected void finalize() throws Throwable {
		this.close();
		super.finalize();
	}
	
	
}

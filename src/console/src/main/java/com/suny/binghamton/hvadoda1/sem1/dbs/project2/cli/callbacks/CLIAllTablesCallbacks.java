package com.suny.binghamton.hvadoda1.sem1.dbs.project2.cli.callbacks;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;

import com.suny.binghamton.hvadoda1.sem1.dbs.project2.db.DBConnection;

import oracle.jdbc.internal.OracleTypes;

public class CLIAllTablesCallbacks {
	enum Tables {
		Students, Courses, Classes, Enrollments, Prerequisites, Logs;

		public static Tables[] getValues() {
			return new Tables[] {
				Students, Courses, Classes, Enrollments, Prerequisites, Logs
			};
		}
	}
	
	public CLICallback showTable(final Tables table) {
		return new CLICallback() {
			@Override
			public void execute(Map<String, ?> config) {
				System.out.println("Showing data in table : " + table.name());
				try {
					Connection con = DBConnection.factory().getConnection();
			        CallableStatement cs = con.prepareCall(
			        		"begin ? := srs.get_"+table.name().toLowerCase()+"(); end;"
			        		);
			        cs.registerOutParameter(1, OracleTypes.CURSOR);
			        cs.execute();
			        ResultSet rs = (ResultSet) cs.getObject(1);
			        ResultSetMetaData rsmd = rs.getMetaData();
			        int numColumns = rsmd.getColumnCount();
			        for (int i=1; i<=numColumns; i++)
			        	System.out.print(rsmd.getColumnName(i) + "\t");
			        System.out.println();
			        while (rs.next()) {
				        for (int i=1; i<=numColumns; i++)
				        	System.out.print(rs.getString(i) + "\t");
				        System.out.println();
			        }
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		};
	}
	
}

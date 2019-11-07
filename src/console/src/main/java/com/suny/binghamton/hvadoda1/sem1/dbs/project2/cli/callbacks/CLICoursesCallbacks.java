package com.suny.binghamton.hvadoda1.sem1.dbs.project2.cli.callbacks;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;

import com.suny.binghamton.hvadoda1.sem1.dbs.project2.db.DBConnection;

import oracle.jdbc.internal.OracleTypes;

public class CLICoursesCallbacks {

	public CLICallback prerequisiteCallback() {
		return new CLICallback() {
			
			@Override
			public void execute(Map<String, ?> config) {
				try {
					Connection con = DBConnection.factory().getConnection();
					CallableStatement cs = con.prepareCall("begin ? := srs.get_prerequisites(?, ?); end;");
					cs.registerOutParameter(1, OracleTypes.CURSOR);
					cs.setString(2, (String) config.get("DeptCode"));
					cs.setInt(3, (Integer) config.get("CourseNo"));
					cs.execute();
					ResultSet rs = (ResultSet) cs.getObject(1);
					ResultSetMetaData md = rs.getMetaData();
					int colCount = md.getColumnCount();
					for (int i=1; i<=colCount; i++)
						System.out.print(md.getColumnName(i) + "\t");
					System.out.println();
					while (rs.next()) {
						for (int i=1; i<=colCount; i++)
							System.out.print(rs.getString(i) + "\t");
						System.out.println();
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
		};
	}
	
	public CLICallback getStudentsInClass() {
		return new CLICallback() {
			
			@Override
			public void execute(Map<String, ?> config) {
				try {
					Connection con = DBConnection.factory().getConnection();
					CallableStatement cs = con.prepareCall("begin ? := srs.get_class_students(?); end;");
					cs.registerOutParameter(1, OracleTypes.CURSOR);
					cs.setString(2, (String) config.get("classid"));
					cs.execute();
					ResultSet rs = (ResultSet) cs.getObject(1);
					ResultSetMetaData rsmd = rs.getMetaData();
					int colCount = rsmd.getColumnCount();
					int sidColNum = -1;
					for (int i=1; i<=colCount; i++) {
						String colName = rsmd.getColumnName(i);
						System.out.print(colName + "\t");
						if (colName.trim().toLowerCase().equals("sid")) sidColNum = i;
					}
					System.out.println();
					int rowCount = 0;
					while (rs.next()) {
						rowCount++;
						if (rowCount == 1 && rs.getString(sidColNum) == null) {
							System.out.println("No student is enrolled in the class");
							break;
						}
						for (int i=1; i<=colCount; i++)
							System.out.print(rs.getString(i) + "\t");
						System.out.println();
					}
					if (rowCount == 0)
						System.out.println("The cid is invalid");
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		};
	}
}

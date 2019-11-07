package com.suny.binghamton.hvadoda1.sem1.dbs.project2.cli.callbacks;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Map;

import com.suny.binghamton.hvadoda1.sem1.dbs.project2.db.DBConnection;

import oracle.jdbc.internal.OracleTypes;

public class CLIStudentsCallbacks {
	public CLICallback addStudentsCallback() {
		return this.studentActionCallback(false);
	}
	
	public CLICallback deleteStudentsCallback() {
		return this.studentActionCallback(true);
	}
	
	private CLICallback studentActionCallback(final boolean delete) {
		return new CLICallback() {
			
			@Override
			public void execute(Map<String, ?> params) {
				Connection con;
				try {
					con = DBConnection.factory().getConnection();
					CallableStatement cs;
					int outputParamIndex = -1;
					if (!delete) {
						cs = con.prepareCall("begin srs.add_student(?, ?, ?, ?, ?, ?, ?); end;");
						cs.setString(1, (String) params.get("sid"));
						cs.setString(2, (String) params.get("firstname"));
						cs.setString(3, (String) params.get("lastname"));
						cs.setString(4, (String) params.get("status"));
						cs.setDouble(5, (Double) params.get("gpa"));
						cs.setString(6, (String) params.get("email"));
						cs.registerOutParameter(7, OracleTypes.VARCHAR);
						outputParamIndex = 7;
					} else {
						cs = con.prepareCall("begin srs.delete_student(?, ?); end;");
						cs.setString(1, (String) params.get("sid"));
						cs.registerOutParameter(2, OracleTypes.VARCHAR);
						outputParamIndex = 2;
					}
					cs.execute();
					String output = cs.getString(outputParamIndex);
					System.out.println(output);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		};
	}
	
	public CLICallback changeEnrollmentStatus(final boolean enroll) {
		return new CLICallback() {
			
			@Override
			public void execute(Map<String, ?> config) {
				try {
					Connection con = DBConnection.factory().getConnection();
					CallableStatement cs;
					String procName = enroll ? "enroll_student" : "drop_class";
					cs = con.prepareCall("begin srs." + procName + "(?, ?, ?); end;");
					cs.setString(1, (String) config.get("sid"));
					cs.setString(2, (String) config.get("classid"));
					cs.registerOutParameter(3, OracleTypes.VARCHAR);
					cs.execute();
					System.out.println(cs.getString(3));
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
		};
	}
	
	public CLICallback studentEnrollmentDetails() {
		return new CLICallback() {
			@Override
			public void execute(Map<String, ?> config) {
				try {
					Connection con = DBConnection.factory().getConnection();
					CallableStatement cs = con.prepareCall("begin ? := srs.get_enrollment_details(?); end;");
					cs.registerOutParameter(1, OracleTypes.CURSOR);
					cs.setString(2, (String) config.get("sid"));
					cs.execute();
					ResultSet rs = (ResultSet) cs.getObject(1);
					ResultSetMetaData md = rs.getMetaData();
					int colCount = md.getColumnCount();
					int classidIdx = -1;
					for (int i=1; i<=colCount; i++) {
						String colName = md.getColumnName(i);
						if (colName.trim().toLowerCase().equals("classid"))
							classidIdx = i;
						System.out.print(colName + "\t");
					}
					System.out.println();
					int rowCount = 0;
					while (rs.next()) {
						rowCount++;
						if (rowCount == 1 && rs.getString(classidIdx) == null) {
							System.out.println("The student has not taken any course\n");
							break;
						}
						for (int i=1; i<=colCount; i++)
							System.out.print(rs.getString(i) + "\t");
						System.out.println();
					}
					if (rowCount == 0)
						System.out.println("The sid is invalid\n");
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		};
	}
}

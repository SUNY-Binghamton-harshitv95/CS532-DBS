package com.suny.binghamton.hvadoda1.sem1.dbs.project2;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.suny.binghamton.hvadoda1.sem1.dbs.project2.cli.callbacks.CLIMainMenuCallbacks;
import com.suny.binghamton.hvadoda1.sem1.dbs.project2.db.DBConnection;
import com.suny.binghamton.hvadoda1.sem1.dbs.project2.util.CLIAction;
import com.suny.binghamton.hvadoda1.sem1.dbs.project2.util.InputType;

public class Initializer {
	private CLIAction[] actions;
	public void initMainMenu() {
		System.out.println("******* Student Registration System *******");
		CLIMainMenuCallbacks callbacks = new CLIMainMenuCallbacks();
		actions = new CLIAction[] {
			new CLIAction("Show all data from tables", callbacks.allTablesCallback()),
			new CLIAction("Create new/Delete existing Student", callbacks.studentActionCallback()),
			new CLIAction("Show Prerequisites of a course", callbacks.prerequisiteCallback(), getPrerequisitesParams()),
			new CLIAction("Show student's enrollment details", callbacks.enrollmentDetailsCallback(), getEnrollmentsDetailsParams()),
			new CLIAction("Modify a student's enrollment status for a class", callbacks.enrollCallback()),
			new CLIAction("Show students enrolled in a class", callbacks.studentsInClassCallback(), getStudentsInClassParams())
		};
		new CommandLineInterface("Choose any of the below options", actions, "Exit", true).initialize();
	}
	
	
	public static Map<String, InputType> getEnrollmentsDetailsParams() {
		Map<String, InputType> enrollmentsParams = new HashMap<String, InputType>();
		enrollmentsParams.put("sid", InputType.String);
		return enrollmentsParams;
	}
	public static Map<String, InputType> getPrerequisitesParams() {
		Map<String, InputType> prereqParams = new HashMap<String, InputType>();
		prereqParams.put("CourseNo", InputType.Integer);
		prereqParams.put("DeptCode", InputType.String);
		return prereqParams;
	}
	public static Map<String, InputType> getChangeEnrollmentParams() {
		Map<String, InputType> enrollmentsParams = new HashMap<String, InputType>();
		enrollmentsParams.put("sid", InputType.String);
		enrollmentsParams.put("classid", InputType.String);
		return enrollmentsParams;
	}
	public static Map<String, InputType> getStudentsInClassParams() {
		Map<String, InputType> studentsInClassParams = new HashMap<String, InputType>();
		studentsInClassParams.put("classid", InputType.String);
		return studentsInClassParams;
	}

	public static void main(String args[]) throws FileNotFoundException, IOException {
		if (args.length == 0) throw new RuntimeException("Require DB config file location as Jar execution params");
		Properties config = new Properties();
		config.load(new FileReader(args[0]));
		System.out.println("Config found for DB host : ["+ config.getProperty("db_url") +"] \nand username :[" + config.get("username") + "]");
		DBConnection.config = config;
		new Initializer().initMainMenu();
	}
}

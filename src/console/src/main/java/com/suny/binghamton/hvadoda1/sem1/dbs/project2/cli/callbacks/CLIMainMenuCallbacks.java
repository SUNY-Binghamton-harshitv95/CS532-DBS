package com.suny.binghamton.hvadoda1.sem1.dbs.project2.cli.callbacks;

import java.util.HashMap;
import java.util.Map;

import com.suny.binghamton.hvadoda1.sem1.dbs.project2.CommandLineInterface;
import com.suny.binghamton.hvadoda1.sem1.dbs.project2.Initializer;
import com.suny.binghamton.hvadoda1.sem1.dbs.project2.cli.callbacks.CLIAllTablesCallbacks.Tables;
import com.suny.binghamton.hvadoda1.sem1.dbs.project2.util.CLIAction;
import com.suny.binghamton.hvadoda1.sem1.dbs.project2.util.InputType;

public class CLIMainMenuCallbacks {
	CLIAllTablesCallbacks allCallbacks = new CLIAllTablesCallbacks();
	CLIStudentsCallbacks studentCallbacks = new CLIStudentsCallbacks();
	CLICoursesCallbacks coursesCallbacks = new CLICoursesCallbacks();
	
	public CLICallback allTablesCallback() {
		return new CLICallback() {
			@Override
			public void execute(Map<String, ?> config) {
				Tables[] tables = Tables.getValues();
				CLIAction[] actions = new CLIAction[tables.length];
				int i = 0;
				for (Tables table : tables)
					actions[i++] = new CLIAction(table.name(), allCallbacks.showTable(table));
				new CommandLineInterface(
					"Select the table who's values you want to show",
					actions,
					"Return to main menu",
					false
				).initialize();
			}
		};
	}
	
	public CLICallback studentActionCallback() {
		return new CLICallback() {
			@Override
			public void execute(Map<String, ?> config) {
				Map<String, InputType> addInputParams = new HashMap<String, InputType>();
				addInputParams.put("sid", InputType.String);
				addInputParams.put("firstname", InputType.String);
				addInputParams.put("lastname", InputType.String);
				addInputParams.put("status", InputType.String);
				addInputParams.put("gpa", InputType.Double);
				addInputParams.put("email", InputType.String);

				Map<String, InputType> delInputParams = new HashMap<String, InputType>();
				delInputParams.put("sid", InputType.String);
				
				CLIAction[] actions = new CLIAction[]{
					new CLIAction("Create Student", studentCallbacks.addStudentsCallback(), addInputParams),
					new CLIAction("Delete Student", studentCallbacks.deleteStudentsCallback(), delInputParams)
				};
				new CommandLineInterface(
						"Choose the appropriate option",
						actions,
						"Return to main menu",
						false
						).initialize();
			}
		};
	}
	
	public CLICallback enrollmentDetailsCallback() {
		return studentCallbacks.studentEnrollmentDetails();
	}
	
	public CLICallback prerequisiteCallback() {
		return coursesCallbacks.prerequisiteCallback();
	}
	
	public CLICallback enrollCallback() {
		return new CLICallback() {
			@Override
			public void execute(Map<String, ?> config) {
				Map<String, InputType> params = Initializer.getChangeEnrollmentParams();
				CLIAction[] actions = new CLIAction[]{
					new CLIAction("Enroll student into a class", studentCallbacks.changeEnrollmentStatus(true), params),
					new CLIAction("Drop student from a class", studentCallbacks.changeEnrollmentStatus(false), params)
				};
				new CommandLineInterface("Choose the appropriate option", actions, "Return to main menu", false).initialize();
			}
		};
	}
	
	public CLICallback studentsInClassCallback() {
		return coursesCallbacks.getStudentsInClass();
	}
}

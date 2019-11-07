package com.suny.binghamton.hvadoda1.sem1.dbs.project2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.suny.binghamton.hvadoda1.sem1.dbs.project2.util.CLIAction;
import com.suny.binghamton.hvadoda1.sem1.dbs.project2.util.InputType;

public class CommandLineInterface {
	private CLIAction[] actions;
	private String title;
	private String exitText;
	private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	private boolean loop = true;
	
	private CommandLineInterface() {}
	
	public CommandLineInterface(CLIAction[] entries) {
		this("Choose any one option", entries, "Exit", true);
	}
	
	public CommandLineInterface(String title, CLIAction[] actions, String exitText, boolean loop) {
		this.title = title;
		this.actions = actions;
		this.exitText = exitText;
		this.loop = loop;
	}
	
	public void initialize() {
		int len = actions.length;
		if (actions == null || len == 0) return;
		int inputOption = 0;
		System.out.println(this.title);
		while ((inputOption = userInput()) >= 0) {
			boolean error = false;
			if (inputOption >= len)
				System.out.println("\nPlease choose a valid option");
			else {
				CLIAction entry = actions[inputOption];
				Map<String, InputType> inputParamKeys = entry.getInputParamKeys();
				Map<String, Object> inputParamValues = new HashMap<String, Object>();
				if (inputParamKeys != null) {
					for (Entry<String, InputType> paramKey : inputParamKeys.entrySet()) {
						String param = paramKey.getKey();
						InputType type = paramKey.getValue();
						System.out.println(String.format("Enter value for [%s]", param));
						String inp = null;
						try {
							inp = reader.readLine();
							inputParamValues.put(param, type.parse(inp));
						} catch (NumberFormatException e) {
							error = true;
							System.err.println("ERROR: Invalid Number : ["+inp+"]");
							break;
						} catch (Exception e) {
							error = true;
							e.printStackTrace();
							break;
						}
					}
				}
				if (!error)
					entry.getCallback().execute(inputParamValues);
				if (!this.loop) break;
			}
			System.out.println("\n" + this.title);
		}
	}
	
	private int userInput() {
		for (int i=0, l=actions.length; i<l; i++) {
			System.out.println((i+1) + ".\t" + actions[i]);
		}
		if (this.exitText != null)
			System.out.println("0.\t" + this.exitText);
		try {
			String inp = reader.readLine();
			return Integer.parseInt(inp.trim()) - 1;
		} catch (NumberFormatException e) {
			return this.actions.length + 1;
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
	}
}

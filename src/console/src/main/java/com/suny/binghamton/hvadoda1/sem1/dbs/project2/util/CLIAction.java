package com.suny.binghamton.hvadoda1.sem1.dbs.project2.util;

import java.util.Map;

import com.suny.binghamton.hvadoda1.sem1.dbs.project2.cli.callbacks.CLICallback;

public class CLIAction {
	private String displayText;
	private CLICallback callback;
	Map<String, InputType> inputParams;
	private CLIAction() {}
	public CLIAction(String displayText, CLICallback callback) {
		this(displayText, callback, null);
	}
	public CLIAction(String displayText, CLICallback callback, Map<String, InputType> inputParams) {
		this.displayText = displayText;
		this.callback = callback;
		this.inputParams = inputParams == null ?
				null : (inputParams.isEmpty() ? null : inputParams);
	}
	public String getDisplayText() {
		return displayText;
	}
	public CLICallback getCallback() {
		return this.callback;
	}
	public Map<String, InputType> getInputParamKeys() {
		return this.inputParams;
	}
	
	@Override
	public String toString() {
		return this.getDisplayText();
	}
	
}

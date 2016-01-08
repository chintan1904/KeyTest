package beans;

import java.util.ArrayList;
import java.util.List;
import common.Result;

public class Test {
	
	private String testName;
	private String testDescription;
	private Result testResult;
	private StringBuilder testLog = new StringBuilder();
	//public TestType type;
	public List<Action> actions;
	private boolean runMode;
	
	public Test(String testName, String testDescription, boolean runMode) {
		setTestName(testName);
		setTestDescription(testDescription);
		actions = new ArrayList<Action>();
		//this.type = type;
		setRunMode(runMode);
	}
	
	public void execute() {
		boolean executionComplete = true;
		for (Action action : actions) {
			action.execute();
			appendTestLog(action.getMessage());
			appendTestLog("<br>");
			if(action.getResult().equals(Result.FAIL)) {
				executionComplete = false;
				break;
			}
		}
		if(executionComplete) 
			setTestResult(Result.PASS);
		else
			setTestResult(Result.FAIL);
		
	}

	public Result getTestResult() {
		return testResult;
	}

	public void setTestResult(Result testResult) {
		this.testResult = testResult;
	}

	public boolean isRunMode() {
		return runMode;
	}

	public void setRunMode(boolean runMode) {
		this.runMode = runMode;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public StringBuilder getTestLog() {
		return testLog;
	}

	public void appendTestLog(String testLog) {
		this.testLog.append(testLog);
	}

	public String getTestDescription() {
		return testDescription;
	}

	public void setTestDescription(String testDescription) {
		this.testDescription = testDescription;
	}
	
	
	
}

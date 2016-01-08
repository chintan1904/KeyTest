package beans;

import java.util.ArrayList;
import java.util.List;

import common.Result;

public class TestSuite {

	public Test beforeSuite;
	public Test afterSuite;
	public Test beforeTest;
	public Test afterTest;
	public List<Test> tests = new ArrayList<Test>();

	public void execute() {

		// Execute Before Test - If before suite is not configured to run then
		// mark it as Pass for further execution.
		if (beforeSuite.isRunMode())
			beforeSuite.execute();
		else
			beforeSuite.setTestResult(Result.PASS);

		// TODO : Check Execution result for BeforeSuite - if it is not passed
		// then mark all test as skipped
		if (beforeSuite.getTestResult().equals(Result.PASS)) {
			for (Test test : tests) {
				// Execute Before Test - if before test is not configured for
				// run then mark it as Pass for further execution.
				if (beforeTest.isRunMode())
					beforeTest.execute();
				else
					beforeTest.setTestResult(Result.PASS);

				// Execute Test method is result is pass - else mark test method
				// as skipped
				if (beforeTest.getTestResult().equals(Result.PASS)) {
					test.execute();
					// Execute After Test
					if (afterTest.isRunMode())
						afterTest.execute();
					else
						afterTest.setTestResult(Result.PASS);
				} else {
					test.setTestResult(Result.SKIPPED);
				}
			}

			// Execute After Suite
			if (afterSuite.isRunMode())
				afterSuite.execute();

		} else {
			// Mark status of all tests as skipped, as before suite has failed.
			for (Test test : tests) {
				test.setTestResult(Result.SKIPPED);
			}
		}
	}
}

package execution;

import java.util.Properties;

import beans.TestSuite;
import utility.PropertyFile;
import utility.ReadExcel;

public class DriverScript {

	public static Properties CONFIG;
	public static TestSuite suite;

	public static void main(String[] args) {

		CONFIG = PropertyFile.load("src/main/resources/config.properties");
		String excelFilePath = CONFIG.getProperty("TEST_SUITE_PATH");
		suite = ReadExcel.createTestSuiteFromExcel(excelFilePath);
		suite.execute();
	}

}

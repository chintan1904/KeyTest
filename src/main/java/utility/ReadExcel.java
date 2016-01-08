package utility;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import beans.Action;
import beans.Test;
import beans.TestSuite;
import common.Constants;
import static execution.DriverScript.CONFIG;

public class ReadExcel {

	public static XSSFWorkbook workbook;
	public static XSSFSheet sheet;
	public static XSSFSheet summarySheet;
	public static DataFormatter formatter = new DataFormatter();

	/**
	 * Method to load excel workbook and initialize static workbook variable for
	 * ReadExcel class.
	 * 
	 * @author chintan.shah
	 * @param filePath
	 */
	private static void loadExcelWorkbook(String filePath) {

		try {
			FileInputStream fin = new FileInputStream(new File(filePath));
			workbook = new XSSFWorkbook(fin);
		} catch (Exception e) {

			// TODO : Logging
			System.exit(1);
		}
	}

	/**
	 * Method to create Test object by reading sheet from excel. If sheet is not
	 * found in excel, then runmode for that test is marked as "false" and it
	 * will not be executed.
	 * 
	 * @author chintan.shah
	 * @param sheetName
	 * @param testDescription
	 * @param runMode
	 * @return
	 */
	private static Test mapSheetToTestObject(String sheetName, String testDescription, boolean runMode) {

		Test test = new Test(sheetName, testDescription, runMode);
		if (runMode) {
			sheet = workbook.getSheet(sheetName);

			if (sheet != null) {
				int lastRow = sheet.getLastRowNum();
				for (int i = 1; i <= lastRow; i++) {
					String keyword = formatter.formatCellValue(sheet.getRow(i).getCell(Constants.COL_KEYWORD));
					String data = formatter.formatCellValue(sheet.getRow(i).getCell(Constants.COL_DATA));
					String sourceElement = formatter.formatCellValue(sheet.getRow(i).getCell(Constants.COL_SRCELEMENT));
					String stepId = formatter.formatCellValue(sheet.getRow(i).getCell(Constants.COL_STEPID));

					Action action = new Action(keyword, sourceElement, data, stepId);
					test.actions.add(action);
				}
			} else {
				test.setRunMode(false);
				test.appendTestLog("Could not find the sheet in excel file, Sheet name : +" + sheetName);
			}
		}

		return test;
	}

	/**
	 * Method to create Test suite object reading excel file.
	 * 
	 * @author chintan.shah
	 * @param excelFilePath
	 * @return
	 */
	public static TestSuite createTestSuiteFromExcel(String excelFilePath) {

		loadExcelWorkbook(excelFilePath);
		TestSuite suite = new TestSuite();

		String beforeSuiteSheetName = CONFIG.getProperty("BEFORE_SUITE_SHEET_NAME");
		String afterSuiteSheetName = CONFIG.getProperty("AFTER_SUITE_SHEET_NAME");
		String beforeTestSheetName = CONFIG.getProperty("BEFORE_TEST_SHEET_NAME");
		String afterTestSheetName = CONFIG.getProperty("AFTER_TEST_SHEET_NAME");

		suite.beforeSuite = StringUtils.isNotBlank(beforeSuiteSheetName)
				? mapSheetToTestObject(beforeSuiteSheetName, "Before Suite", true)
				: mapSheetToTestObject("BEFORE_SUITE", "Before Suite", false);

		suite.afterSuite = StringUtils.isNoneBlank(afterSuiteSheetName)
				? mapSheetToTestObject(afterSuiteSheetName, "After Suite", true)
				: mapSheetToTestObject("AFTER_SUITE", "After Suite", false);

		suite.beforeTest = StringUtils.isNotBlank(beforeTestSheetName)
				? mapSheetToTestObject(beforeTestSheetName, "Before Test", true)
				: mapSheetToTestObject("BEFORE_TEST", "Before Test", false);

		suite.afterTest = StringUtils.isNoneBlank(afterTestSheetName)
				? mapSheetToTestObject(afterTestSheetName, "After Test", true)
				: mapSheetToTestObject("AFTER_TEST", "After Test", false);

		summarySheet = workbook.getSheet("Test Cases"); //TODO: Make sheet name configurable.
		int lastRow = summarySheet.getLastRowNum();
		for (int i = 1; i <= lastRow; i++) {
			String testName = formatter.formatCellValue(summarySheet.getRow(i).getCell(Constants.COL_TESTCASENAME));
			String testDescription = formatter
					.formatCellValue(summarySheet.getRow(i).getCell(Constants.COL_TESTCASEDESCRIPTION));
			boolean testRunMode = formatter.formatCellValue(summarySheet.getRow(i).getCell(Constants.COL_TESTRUNMODE))
					.equalsIgnoreCase("Yes") ? true : false;

			suite.tests.add(mapSheetToTestObject(testName, testDescription, testRunMode));
		}
		return suite;
	}

}

package common;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import utility.PropertyFile;

public class Operation {

	private static WebDriver driver;
	private static Properties OR = PropertyFile.load("src/main/resources/OR.properties");

	public static void openBrowser(String sourceElement, String data) throws Exception {

		if (StringUtils.isBlank(data))
			data = "FIREFOX";

		BrowserType browser = BrowserType.valueOf(data);

		switch (browser) {
		case FIREFOX:
			driver = new FirefoxDriver();
			break;
		case INTERNET_EXPLORER:
			driver = new InternetExplorerDriver();
			break;
		case CHROME:
			driver = new ChromeDriver();
			break;
		default:
			throw new Exception(
					"Invalid browser selection, browser name should be any of the following : FIREFOX, CHROME, INTERNET_EXPLORER");
		}
	}

	public static void navigateTo(String sourceElement, String data) throws Exception {
		driver.navigate().to(data);
	}

	public static void setImpliciteWait(String sourceElement, String data) throws Exception {
		int time = Integer.parseInt(data);
		driver.manage().timeouts().implicitlyWait(time, TimeUnit.SECONDS);
	}

	public static void maximizeWindow(String sourceElement, String data) throws Exception {
		driver.manage().window().maximize();
	}

	public static void enterText(String sourceElement, String data) throws Exception {
		locateElement(sourceElement).sendKeys(data);
	}

	public static void click(String sourceElement, String data) throws Exception {
		locateElement(sourceElement).click();
	}

	public static void waitFor(String sourceElement, String data) throws Exception {

		if (!StringUtils.isBlank(data))
			data = "2";
		int time = Integer.parseInt(data) * 1000;
		Thread.sleep(time);
	}

	public static void close(String sourceElement, String data) throws Exception {
		driver.quit();
	}
	
	public static void SelectDropDownByValue(String sourceElement, String data) throws Exception {
		Select select = new Select(locateElement(sourceElement));
		select.selectByValue(data);
	}
	
	public static void SelectDropDownByIndex(String sourceElement, String data) throws Exception {
		Select select = new Select(locateElement(sourceElement));
		select.selectByIndex(Integer.parseInt(data));
	}
	
	public static void WaitForPresenceOfElement(String sourceElement, String data) throws Exception {

		if (!StringUtils.isBlank(data))
			data = "10";
		int time = Integer.parseInt(data) * 1000;
		WebDriverWait wait = new WebDriverWait(driver, time);
		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator(sourceElement)));
	}
	
	private static By locator(String sourceElement) throws Exception{

		String locator[] = OR.getProperty(sourceElement).split("\\$\\$");
		Locator locatorType = Locator.valueOf(locator[0]);
		String locatorValue = locator[1];

		By by = null;
		switch (locatorType) {
		case XPATH:
			by = By.xpath(locatorValue);
			break;
		case CSS:
			by = By.cssSelector(locatorValue);
			break;
		case ID:
			by = By.id(locatorValue);
			break;
		case NAME:
			by = By.name(locatorValue);
			break;
		default:
			throw new Exception("Invalid by locator provided for source element : "+sourceElement);
		}

		return by;
	}
	
	

	private static WebElement locateElement(String sourceElement) throws Exception{

		WebElement webelement = null;
		By by = locator(sourceElement);
		webelement = driver.findElement(by);
		if(webelement == null)
			throw new Exception("WebElement not found : "+sourceElement+" By locator is : "+by.toString());
		return webelement;
	}

}

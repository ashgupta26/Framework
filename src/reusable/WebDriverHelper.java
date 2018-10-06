package reusable;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import atu.testng.reports.ATUReports;
import atu.testng.reports.logging.LogAs;
import atu.testng.selenium.reports.CaptureScreen;
import atu.testng.selenium.reports.CaptureScreen.ScreenshotOf;

public class WebDriverHelper {
	static WebDriver driver=null;
	static WebDriverWait wait;
	public static void initialize(String browser)
	{
		switch(browser.toLowerCase())
		{
		case "gc": case "chrome":
			System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
			driver = new ChromeDriver();
			break;
		case "ff": case "mozilla": case "firefox":
			System.setProperty("webdriver.gecko.driver", "drivers/geckodriver.exe");
			driver = new FirefoxDriver();
			break;
		case "ie": case "explorer" : 
			System.setProperty("webdriver.ie.driver", "driver/IEDriverServer.exe");
			driver = new InternetExplorerDriver();
			break;
		default:
			System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
			driver = new ChromeDriver();
			break;
		}
		wait = new WebDriverWait(driver,60);
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		ATUReports.setWebDriver(driver);
		ATUReports.add("Initialized browser",browser, LogAs.INFO, new CaptureScreen(
                ScreenshotOf.BROWSER_PAGE));
	}
	public static void navigate(String url)
	{
		driver.get(url);
		ATUReports.add("Navigate to URL", driver.getCurrentUrl(), LogAs.PASSED, new CaptureScreen(
                ScreenshotOf.BROWSER_PAGE));
	}
	static int i=0;
	public static void click(By loc)
	{
		i++;
		if(i<3)
		{
		try
		{
		wait.until(ExpectedConditions.presenceOfElementLocated(loc));
		wait.until(ExpectedConditions.visibilityOfElementLocated(loc));
		wait.until(ExpectedConditions.elementToBeClickable(loc));
		driver.findElement(loc).click();
		ATUReports.add("Click on "+loc.toString(), LogAs.PASSED, new CaptureScreen(
                ScreenshotOf.BROWSER_PAGE));
		}
		catch(NoSuchElementException e)
		{
			driver.manage().deleteAllCookies();
			driver.navigate().refresh();
			click(loc);
		}
		catch(ElementNotInteractableException e)
		{
			/*Actions act = new Actions(driver);
			act.click(driver.findElement(loc)).build().perform();*/
			JavascriptExecutor js =(JavascriptExecutor) driver;
			js.executeScript("document.getElementsByClassName(\"fa-user\")[0].click()");
		}
		}
		else
		{
		ATUReports.add("Click on "+loc.toString(), LogAs.FAILED, new CaptureScreen(
	                ScreenshotOf.BROWSER_PAGE));
		}
	}
	public static void type(By loc, String val)
	{
		wait.until(ExpectedConditions.presenceOfElementLocated(loc));
		wait.until(ExpectedConditions.visibilityOfElementLocated(loc));
		driver.findElement(loc).sendKeys(val);
		ATUReports.add("Input text into "+loc.toString(),val, LogAs.PASSED, new CaptureScreen(
                ScreenshotOf.BROWSER_PAGE));
	}
	public static void assertText(By loc, String val)
	{
		wait.until(ExpectedConditions.presenceOfElementLocated(loc));
		wait.until(ExpectedConditions.visibilityOfElementLocated(loc));
		Assert.assertEquals(driver.findElement(loc).getText(), val);
		ATUReports.add("Assert Text at "+loc.toString(),val,driver.findElement(loc).getText(), LogAs.PASSED, new CaptureScreen(
                ScreenshotOf.BROWSER_PAGE));
	}
	public static void quit()
	{
		driver.quit();
		ATUReports.add("Quit WebDrive", false);
	}
}

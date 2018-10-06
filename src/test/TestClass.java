package test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.By;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import atu.testng.reports.ATUReports;
import atu.testng.reports.listeners.ATUReportsListener;
import atu.testng.reports.listeners.ConfigurationListener;
import atu.testng.reports.listeners.MethodListener;
import atu.testng.reports.logging.LogAs;
import atu.testng.selenium.reports.CaptureScreen;
import atu.testng.selenium.reports.CaptureScreen.ScreenshotOf;
import po.HomePage;
import po.LoginPage;
import po.RegisterPage;
import po.UserPage;
import reusable.WebDriverHelper;
import utility.ExcelUtil;


@Listeners({ ATUReportsListener.class, ConfigurationListener.class,
	  MethodListener.class })
public class TestClass {
	Properties p = new Properties();
	{
        System.setProperty("atu.reporter.config", "atu.properties");
     }
	
	@BeforeSuite
	public void bs() throws FileNotFoundException, IOException {
		p.load(new FileInputStream("project.properties"));
	}
	
	//@Parameters({"browser"})
	@BeforeClass
	public void bc() {
		WebDriverHelper.initialize(p.getProperty("browser"));
	}
	
	@BeforeMethod
	public void bm() {
		WebDriverHelper.navigate(p.getProperty("url"));
		WebDriverHelper.click(HomePage.user_icon);
	}
	
	@Test(priority=1)
	public void register() {
		WebDriverHelper.click(LoginPage.reg_btn);
		WebDriverHelper.type(RegisterPage.fname_ip, "Natarajan");
		WebDriverHelper.type(RegisterPage.lname_ip, "Ramanathan");
		WebDriverHelper.type(RegisterPage.email_ip, "test@gmail.com");
	}
	
	@Test(priority=2)
	public void login() {
		WebDriverHelper.type(LoginPage.email_ip, p.getProperty("user"));
		WebDriverHelper.type(LoginPage.pwd_ip, p.getProperty("password", "Welcome123"));
		WebDriverHelper.click(LoginPage.login_btn);
		WebDriverHelper.assertText(UserPage.mag_id, "ID: MAG003417822");
		WebDriverHelper.click(UserPage.logout_btn);
	}
	
	@AfterClass
	public void ac() {
		WebDriverHelper.quit();
	}
	
	@DataProvider
	public Object[][] login_data() throws FileNotFoundException, IOException
	{
		return ExcelUtil.excel_data("resources/Data.xlsx", "Login");
	}
}

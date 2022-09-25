package com.orangehrm.qa.pages;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class LoginPage {
	Properties prop;
	public LoginPage() throws IOException {
		try{
			prop = new Properties();
			FileInputStream ip = new FileInputStream("C:\\Users\\91772\\eclipse-workspace\\orangehrmtest\\src\\main\\java\\com\\orangehrm\\qa\\configuration\\config.properties");
			
			prop.load(ip);
		}catch(FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	public static WebDriver driver;
	
	@BeforeMethod
	public static void setUpBrowser(){
		System.setProperty("webdriver.chrome.driver", "C:\\\\Users\\\\91772\\\\eclipse-workspace\\\\SeleniumBasics\\\\chromedriver.exe");
		driver = new ChromeDriver();
		
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
	}
	
	@Test(priority=1)
	public void checkLandingPageTitleTest() {
		Assert.assertEquals("OrangeHRM", driver.getTitle());
	}
	
	@Test(priority=2)
	public void checkloginTest() {
		driver.findElement(By.xpath("//input[@name='username']")).sendKeys(prop.getProperty("username"));
		driver.findElement(By.xpath("//input[@name='password']")).sendKeys(prop.getProperty("password"));
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		if(prop.getProperty("username").equals("Admin") && prop.getProperty("password").equals("admin123")) {
			String headertext = driver.findElement(By.xpath("//h6[@class='oxd-text oxd-text--h6 oxd-topbar-header-breadcrumb-module']")).getText();
			Assert.assertEquals("PIM", headertext);
		}
		else {
			boolean alert = driver.findElement(By.xpath("//i[@class='oxd-icon bi-exclamation-circle oxd-alert-content-icon']")).isDisplayed();
			Assert.assertTrue(alert);
		}
		
	}
	
	@Test(priority=3)
	public void emptypasswordSpaceLoginTest() {
		driver.findElement(By.xpath("//input[@name='username']")).sendKeys(prop.getProperty("username"));
		WebElement password = driver.findElement(By.xpath("//input[@name='password']"));
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		if(password.getText().equals(null)) {
			boolean errormsg = driver.findElement(By.xpath("//span[@class='oxd-text oxd-text--span oxd-input-field-error-message oxd-input-group__message']")).isDisplayed();
			Assert.assertTrue(errormsg);
		}
	}
	
	@Test(priority=4)
	public void emptyusernameSpaceLoginTest() {
		WebElement username = driver.findElement(By.xpath("//input[@name='username']"));
		driver.findElement(By.xpath("//input[@name='password']"));
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		if(username.getText().equals(null)) {
			boolean errormsg = driver.findElement(By.xpath("//span[@class='oxd-text oxd-text--span oxd-input-field-error-message oxd-input-group__message']")).isDisplayed();
			Assert.assertTrue(errormsg);
		}
	}
	
	@Test(priority=5)
	public void emptyUserPassSpaceTest() {
		WebElement username = driver.findElement(By.xpath("//input[@name='username']"));
		WebElement password = driver.findElement(By.xpath("//input[@name='password']"));
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		if(username.getText().equals(null) && password.getText().equals(null)) {
			List<WebElement> elements = driver.findElements(By.xpath("//span[@class='oxd-text oxd-text--span oxd-input-field-error-message oxd-input-group__message']"));
			Assert.assertEquals(2,elements.size());
		}
	}
	
	@Test(priority=6)
	public void checkLogoutTest() {
		driver.findElement(By.xpath("//input[@name='username']")).sendKeys(prop.getProperty("username"));
		driver.findElement(By.xpath("//input[@name='password']")).sendKeys(prop.getProperty("password"));
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		WebElement dropdown = driver.findElement(By.xpath("//ul//li//i[@class='oxd-icon bi-caret-down-fill oxd-userdropdown-icon']"));
		new WebDriverWait(driver,Duration.ofSeconds(5)).until(ExpectedConditions.elementToBeClickable(dropdown));
		dropdown.click();
		driver.findElement(By.xpath("//ul//li//a[@class='oxd-userdropdown-link' and contains(text(),'Logout')]")).click();
		Assert.assertEquals(driver.getTitle(), "OrangeHRM");
	}
	
	@Test(priority=7)
	public void checkLinkedinShareTest() throws InterruptedException {
		driver.findElement(By.xpath("//div[@class='orangehrm-login-footer-sm']//a[@href='https://www.linkedin.com/company/orangehrm/mycompany/']")).click();
		Thread.sleep(2000);
		Set<String> windowhandles = driver.getWindowHandles();
		System.out.println(windowhandles.size());
		Iterator<String> itr = windowhandles.iterator();
		String childwindow=null;
		while(itr.hasNext()) {
			childwindow = itr.next();
			driver.switchTo().window(childwindow);
		}
		boolean title = driver.getTitle().contains("Sign In | LinkedIn");
		if(title){
			Assert.assertTrue(driver.getTitle().contains("Sign In | LinkedIn"));
		}
		else {
			
			//Assert.assertEquals(windowhandles.size(), 2, "Verify window handles");
			driver.findElement(By.xpath("//div[@class='top-level-modal-container']//div//div//section//button//icon[@class='contextual-sign-in-modal__modal-dismiss-icon lazy-loaded']")).click();
			//driver.switchTo().window()
			WebElement header = driver.findElement(By.xpath("//h1[@class='top-card-layout__title font-sans text-lg papabear:text-xl font-bold leading-open text-color-text mb-0']"));
			Assert.assertTrue(header.getText().equals("OrangeHRM"));
		}
	}
	
	@Test(priority=8)
	public void checkFacebookShareTest() throws InterruptedException {
		driver.findElement(By.xpath("//div[@class='orangehrm-login-footer-sm']//a[@href='https://www.facebook.com/OrangeHRM/']")).click();
		Thread.sleep(2000);
		Set<String> windowhandles = driver.getWindowHandles();
		Iterator<String> itr = windowhandles.iterator();
		while(itr.hasNext()) {
			driver.switchTo().window(itr.next());
		}
		WebElement header = driver.findElement(By.xpath("//h1[@class='jxuftiz4 jwegzro5 hl4rid49 icdlwmnq']"));
		boolean checkheader = header.getText().contains("OrangeHRM - World's Most Popular Opensource HRIS");
		Assert.assertTrue(checkheader);
	}
	
	@Test(priority=9)
	public void checkTwitterShareTest() throws InterruptedException {
		driver.findElement(By.xpath("//div[@class='orangehrm-login-footer-sm']//a[@href='https://twitter.com/orangehrm?lang=en']")).click();
		Thread.sleep(2000);
		Set<String> windowhandles = driver.getWindowHandles();
		Iterator<String> itr = windowhandles.iterator();
		while(itr.hasNext()) {
			driver.switchTo().window(itr.next());
		}
		//driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(3));
		new WebDriverWait(driver,Duration.ofSeconds(10)).until(ExpectedConditions.titleContains("OrangeHRM (@orangehrm) / Twitter"));
		String title = driver.getTitle();
		boolean checktitle = title.contains("OrangeHRM (@orangehrm) / Twitter");
		System.out.println(title);
		Assert.assertTrue(checktitle);
	}
	
	@Test(priority=10)
	public void checkYoutubeShareTest() throws InterruptedException {
		driver.findElement(By.xpath("//div[@class='orangehrm-login-footer-sm']//a[@href='https://www.youtube.com/c/OrangeHRMInc']")).click();
		//Thread.sleep(2000);
		Set<String> windowhandles = driver.getWindowHandles();
		Iterator<String> itr = windowhandles.iterator();
		while(itr.hasNext()) {
			driver.switchTo().window(itr.next());
		}
		new WebDriverWait(driver,Duration.ofSeconds(10)).until(ExpectedConditions.titleContains("OrangeHRM Inc - YouTube"));
		String title = driver.getTitle();
		boolean checktitle = title.contains("OrangeHRM Inc - YouTube");
		System.out.println(title);
		Assert.assertTrue(checktitle);
	}
	
	@AfterMethod
	public void endSetUp() {
		driver.quit();
	}
	
	
	
}
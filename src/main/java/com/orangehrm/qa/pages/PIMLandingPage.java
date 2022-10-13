package com.orangehrm.qa.pages;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class PIMLandingPage {
	static WebDriver driver;
	LoginPage login;
	Properties prop;
	
	public PIMLandingPage() throws IOException, FileNotFoundException {
		prop = new Properties();
		FileInputStream ip = new FileInputStream("C:\\Users\\91772\\eclipse-workspace\\orangehrmtest\\src\\main\\java\\com\\orangehrm\\qa\\configuration\\config.properties");
		prop.load(ip);
	}
	
	@BeforeMethod
	public void setUp() {
		System.setProperty("webdriver.chrome.driver", "C:\\\\\\\\Users\\\\\\\\91772\\\\\\\\eclipse-workspace\\\\\\\\SeleniumBasics\\\\\\\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	}
	
	@Test
	public void addEmployeeTest() throws InterruptedException {
		driver.findElement(By.xpath("//input[@name='username']")).sendKeys(prop.getProperty("username"));
		driver.findElement(By.xpath("//input[@name='password']")).sendKeys(prop.getProperty("password"));
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		WebElement addelement = driver.findElement(By.xpath("//button[@class='oxd-button oxd-button--medium oxd-button--secondary']"));
		new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.elementToBeClickable(addelement)).click();
		
		String firstname = prop.getProperty("firstname");
		String middlename = prop.getProperty("middlename");
		String lastname = prop.getProperty("lastname");
		
		WebElement firstnameInput = driver.findElement(By.xpath("//input[@name='firstName']"));
		firstnameInput.sendKeys(firstname);
		driver.findElement(By.xpath("//input[@name='middleName']")).sendKeys(middlename);
		driver.findElement(By.xpath("//input[@name='lastName']")).sendKeys(lastname);
		driver.findElement(By.xpath("//button[@class='oxd-button oxd-button--medium oxd-button--secondary orangehrm-left-space']")).click();
		
		new WebDriverWait(driver,Duration.ofSeconds(5)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='oxd-toast-content oxd-toast-content--success']")));
		String s = driver.findElement(By.xpath("//div[@class='oxd-toast-content oxd-toast-content--success']")).getText();
		System.out.println(s);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		
		driver.findElement(By.xpath("//a[@class='oxd-topbar-body-nav-tab-item' and text()='Employee List']")).click();
		List<WebElement> list = driver.findElements(By.xpath("//div[@class='oxd-table-body']//div[@class='oxd-table-card']//div[3]"));
		for(int i=0; i<list.size(); i++) {
			String recordname = list.get(i).getText();
			if(recordname.equals(firstname+" "+middlename)) {
				Assert.assertEquals(recordname, firstname+" "+middlename);	
			}		
		}
	}
	
	@Test
	public void deleteEmployeeTest() {
		driver.findElement(By.xpath("//input[@name='username']")).sendKeys(prop.getProperty("username"));
		driver.findElement(By.xpath("//input[@name='password']")).sendKeys(prop.getProperty("password"));
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		
		String firstname = prop.getProperty("firstname");
		String middlename = prop.getProperty("middlename");
		List<WebElement> list = driver.findElements(By.xpath("//div[@class='oxd-table-body']//div[@class='oxd-table-card']//div[3]"));
		for(int i=0; i<list.size();i++) {
			String name = list.get(i).getText();
			if(name.equals(firstname + " " + middlename)) {
				WebElement deletebtn = driver.findElement(By.xpath("//div[@class='oxd-table-body']//div[@class='oxd-table-card']//div[contains(text(),'"+firstname+" "+middlename+"')]//parent::div//parent::div//descendant::button//i[@class='oxd-icon bi-trash']"));
				deletebtn.click();
				WebElement deletebin = driver.findElement(By.xpath("//button[@class='oxd-button oxd-button--medium oxd-button--label-danger orangehrm-button-margin']"));
				new WebDriverWait(driver,Duration.ofSeconds(3)).until(ExpectedConditions.elementToBeClickable(deletebin)).click();
				
				String s = driver.findElement(By.xpath("//div[@class='oxd-toast-content oxd-toast-content--success']")).getText();
				boolean bool = s.contains("Successfully Deleted");
				System.out.println(s);
				Assert.assertTrue(bool);
				break;
			}
		}
	}
	
	@AfterMethod
	public void endsetUp() {
		driver.quit();
	}
	
	
	
	
	

}

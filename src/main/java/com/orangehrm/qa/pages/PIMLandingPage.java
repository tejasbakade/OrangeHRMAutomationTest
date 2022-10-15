package com.orangehrm.qa.pages;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
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
		System.setProperty("webdriver.chrome.driver", "C:\\Users\\91772\\eclipse-workspace\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	}
	
	@Test(priority=1)
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
	
	@Test(priority=2) 
	public void editPersonalDetailsTest() throws InterruptedException {
		driver.findElement(By.xpath("//input[@name='username']")).sendKeys(prop.getProperty("username"));
		driver.findElement(By.xpath("//input[@name='password']")).sendKeys(prop.getProperty("password"));
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		
		String firstname = prop.getProperty("firstname");
		String middlename = prop.getProperty("middlename");
		
		List<WebElement> list = driver.findElements(By.xpath("//div[@class='oxd-table-body']//div[@class='oxd-table-card']//div[3]"));
		for(int i=0; i<list.size(); i++) {
			String name = list.get(i).getText();
			if(name.equals(firstname+" "+middlename)) {
				Actions action = new Actions(driver);
				JavascriptExecutor js = (JavascriptExecutor) driver;
				WebElement editbtn = driver.findElement(By.xpath("//div[@class='oxd-table-body']//div[@class='oxd-table-card']//div[contains(text(),'"+firstname+" "+middlename+"')]//parent::div//parent::div//descendant::button[2]"));
				editbtn.click();
				
				//to clear text field
				String del = Keys.chord(Keys.CONTROL, "a") + Keys.DELETE;
				
				//Date input field
				String datexpath1 = "//div[@class='oxd-grid-item oxd-grid-item--gutters']//descendant::label[contains(text(),'";
				String datexpath2 = "')]//parent::div//following-sibling::div//descendant::input";
				
				WebElement dob = driver.findElement(By.xpath(datexpath1+"Date of Birth"+datexpath2));
				
				dob.sendKeys(del+"2002-09-06");
				
				//select dropdown input
				String dropdownxpath1 = "//div[@class='oxd-input-group oxd-input-field-bottom-space']//label[contains(text(),'";
				String dropdownxpath2 = "')]//parent::div//following-sibling::div//i";
				
				WebElement nationality = driver.findElement(By.xpath(dropdownxpath1+"Nationality"+dropdownxpath2));
				WebElement maritalstatus = driver.findElement(By.xpath(dropdownxpath1+"Marital Status"+dropdownxpath2));
				WebElement bloodtype = driver.findElement(By.xpath(dropdownxpath1+"Blood Type"+dropdownxpath2));
				
				String lixpath1 = "//div[@class='oxd-input-group oxd-input-field-bottom-space']//label[contains(text(),'";
				String lixpath2 = "')]//parent::div//following-sibling::div//descendant::div[@role='listbox']//div[@role='option']//span";
				
				js.executeScript("arguments[0].click()", nationality);
				new WebDriverWait(driver,Duration.ofSeconds(5)).until(ExpectedConditions.presenceOfElementLocated(By.xpath(lixpath1+"Nationality"+lixpath2)));
				List<WebElement> listOfNationals = driver.findElements(By.xpath(lixpath1+"Nationality"+lixpath2));
				for(int j=0; j<listOfNationals.size();j++) {
					String x = listOfNationals.get(j).getText();
					if(x.equals("Indian")) {
						listOfNationals.get(j).click();
						Thread.sleep(2000);
						break;
					}
				}
				js.executeScript("arguments[0].click()", maritalstatus);
				new WebDriverWait(driver,Duration.ofSeconds(5)).until(ExpectedConditions.presenceOfElementLocated(By.xpath(lixpath1+"Marital Status"+lixpath2)));
				List<WebElement> listOfMaritalStatus = driver.findElements(By.xpath(lixpath1+"Marital Status"+lixpath2));
				for(int j=0;j<listOfMaritalStatus.size();j++) {
					String x1 = listOfMaritalStatus.get(j).getText();
					System.out.println(x1);
					if(x1.equals("Other")) {
						listOfMaritalStatus.get(j).click();
						break;
					}
				}
//				js.executeScript("arguments[0].click()",bloodtype);
//				new WebDriverWait(driver,Duration.ofSeconds(5)).until(ExpectedConditions.presenceOfElementLocated(By.xpath(lixpath1+"Blood Type"+lixpath2)));
//				List<WebElement> listOfBloodType = driver.findElements(By.xpath(lixpath1+"Blood Type"+lixpath2));
//				for(int j=0; j<listOfBloodType.size();j++) {
//					String x2 = listOfBloodType.get(j).getText();
//					if(x2.equals("O+")) {
//						listOfBloodType.get(j).click();
//						break;
//					}
//				}
				WebElement gender = driver.findElement(By.xpath("//div[@class='--gender-grouped-field']//descendant::label[contains(text()[2],'Male')]//input[@type='radio']"));
				js.executeScript("arguments[0].click()", gender);
				
				WebElement savebtn = driver.findElement(By.xpath("//div[@class='oxd-form-actions']//p//following-sibling::button"));
				new WebDriverWait(driver,Duration.ofSeconds(5)).until(ExpectedConditions.elementToBeClickable(savebtn));
				savebtn.click();
				
				Thread.sleep(3000);
				String s = driver.findElement(By.xpath("//div[@class='oxd-toast-content oxd-toast-content--success']")).getText();
				System.out.println(s);
				boolean updatesuccessful = s.contains("Successfully Updated");
				Assert.assertTrue(updatesuccessful);				
				break;
			}
		}
	}
	
	@Test(priority=3)
	public void editContactDetailsTest() throws InterruptedException {
		driver.findElement(By.xpath("//input[@name='username']")).sendKeys(prop.getProperty("username"));
		driver.findElement(By.xpath("//input[@name='password']")).sendKeys(prop.getProperty("password"));
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		
		String firstname = prop.getProperty("firstname");
		String middlename = prop.getProperty("middlename");
		
		List<WebElement> list = driver.findElements(By.xpath("//div[@class='oxd-table-body']//div[@class='oxd-table-card']//div[3]"));
		for(int i=0;i<list.size();i++) {
			String name = list.get(i).getText();
			if(name.equals(firstname+" "+middlename)) {
				Actions action = new Actions(driver);
				JavascriptExecutor js = (JavascriptExecutor) driver;
				WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(5));
				WebElement editbtn = driver.findElement(By.xpath("//div[@class='oxd-table-body']//div[@class='oxd-table-card']//div[contains(text(),'"+firstname+" "+middlename+"')]//parent::div//parent::div//descendant::button[2]"));
				editbtn.click();
				
				WebElement contacttab = driver.findElement(By.xpath("//a[@class='orangehrm-tabs-item' and contains(text(),'Contact Details')]"));
				js.executeScript("arguments[0].click()", contacttab);
				
				String xpath1 = "//label[@class='oxd-label' and contains(text(),'";
				String xpath2 = "')]//parent::div//following-sibling::div//descendant::input";
				String del = Keys.chord(Keys.CONTROL + "a") + Keys.DELETE;
				driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
				
				//Address
				WebElement street1 = driver.findElement(By.xpath(xpath1+"Street 1"+xpath2));
				WebElement street2 = driver.findElement(By.xpath(xpath1+"Street 2"+xpath2));
				WebElement city = driver.findElement(By.xpath(xpath1+"City"+xpath2));
				WebElement state = driver.findElement(By.xpath(xpath1+"State/Province"+xpath2));
				WebElement postalcode = driver.findElement(By.xpath(xpath1+"Zip/Postal Code"+xpath2));
				WebElement country = driver.findElement(By.xpath("//label[@class='oxd-label' and contains(text(),'Country')]//parent::div//following-sibling::div//descendant::i"));
				Thread.sleep(2000);
				wait.until(ExpectedConditions.elementToBeClickable(street1));
				street1.sendKeys(del+"Street 2");
				wait.until(ExpectedConditions.elementToBeClickable(street2));
				street2.sendKeys(del + "ABC road");
				wait.until(ExpectedConditions.elementToBeClickable(city));
				city.sendKeys(del + "Poona");
				wait.until(ExpectedConditions.elementToBeClickable(state));
				state.sendKeys(del + "Maharashtra");
				wait.until(ExpectedConditions.elementToBeClickable(postalcode));
				postalcode.sendKeys(del + "543453");
				//country.sendKeys("India");
				js.executeScript("arguments[0].click()", country);
				String dropdownxpath = "//label[@class='oxd-label' and contains(text(),'Country')]//parent::div//following-sibling::div//descendant::div[@role='listbox']//span";
				new WebDriverWait(driver,Duration.ofSeconds(3)).until(ExpectedConditions.presenceOfElementLocated(By.xpath(dropdownxpath)));
				List<WebElement> listOfCountry = driver.findElements(By.xpath(dropdownxpath));
				for(int j=0;j<listOfCountry.size();j++) {
					String x = listOfCountry.get(j).getText();
					if(x.equals("Iceland")) {
						action.moveToElement(listOfCountry.get(j)).click().build().perform();
						break;
					}
				}
				
				//Telephone
				WebElement home = driver.findElement(By.xpath(xpath1+"Home"+xpath2));
				WebElement mobile = driver.findElement(By.xpath(xpath1+"Mobile"+xpath2));
				WebElement work = driver.findElement(By.xpath(xpath1+"Work"+xpath2));
				
				wait.until(ExpectedConditions.elementToBeClickable(home));
				home.sendKeys(del + "9876543345");
				wait.until(ExpectedConditions.elementToBeClickable(mobile));
				mobile.sendKeys(del+ "7584935433");
				wait.until(ExpectedConditions.elementToBeClickable(work));
				work.sendKeys(del + "8759358532");
				
				//Email
				WebElement workemail = driver.findElement(By.xpath(xpath1+"Work Email"+xpath2));
				WebElement otheremail = driver.findElement(By.xpath(xpath1+"Other Email"+xpath2));
				
				wait.until(ExpectedConditions.elementToBeClickable(workemail));
				workemail.sendKeys(del + "hgfaf@gmail.com");
				wait.until(ExpectedConditions.elementToBeClickable(otheremail));
				otheremail.sendKeys(del + "asdf@hotmail.com");
				Thread.sleep(4000);
				WebElement savebtn = driver.findElement(By.xpath("//div[@class='oxd-form-actions']//p//following-sibling::button"));
				new WebDriverWait(driver,Duration.ofSeconds(5)).until(ExpectedConditions.elementToBeClickable(savebtn));
				savebtn.click();
				
				Thread.sleep(3000);
				String s = driver.findElement(By.xpath("//div[@class='oxd-toast-content oxd-toast-content--success']")).getText();
				System.out.println(s);
				boolean updatesuccessful = s.contains("Successfully Updated");
				Assert.assertTrue(updatesuccessful);
				break;
			}
		}
	}
	
	@Test(priority=4)
	public void editJobDetailsTest() throws InterruptedException {
		driver.findElement(By.xpath("//input[@name='username']")).sendKeys(prop.getProperty("username"));
		driver.findElement(By.xpath("//input[@name='password']")).sendKeys(prop.getProperty("password"));
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		
		String firstname = prop.getProperty("firstname");
		String middlename = prop.getProperty("middlename");
		System.out.println(firstname+" "+middlename);
		List<WebElement> list = driver.findElements(By.xpath("//div[@class='oxd-table-body']//div[@class='oxd-table-card']//div[3]"));
		for(int i=0;i<list.size();i++) {
			String name = list.get(i).getText();
			System.out.println(name);
			if(name.equals(firstname+" "+middlename)) {
				Actions action = new Actions(driver);
				JavascriptExecutor js = (JavascriptExecutor) driver;
				WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(5));
				WebElement editbtn = driver.findElement(By.xpath("//div[@class='oxd-table-body']//div[@class='oxd-table-card']//div[contains(text(),'"+firstname+" "+middlename+"')]//parent::div//parent::div//descendant::button[2]"));
				editbtn.click();
				
				WebElement jobtab = driver.findElement(By.xpath("//div[@class='orangehrm-tabs-wrapper']//a[contains(text(),'Job')]"));
				jobtab.click();
				driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
				
				WebElement joineddate = driver.findElement(By.xpath("//div[@class='oxd-form-row']//descendant::label[contains(text(),'Joined Date')]//parent::div//following-sibling::div//descendant::input"));
				String dropdownxpath1 = "//div[@class='oxd-form-row']//descendant::label[contains(text(),'";
				String dropdownxpath2 = "')]//parent::div//following-sibling::div//descendant::i";
				String listboxxpath = "')]//parent::div//following-sibling::div//descendant::div[@role='listbox']//span";
				
				WebElement jobtitle = driver.findElement(By.xpath(dropdownxpath1+"Job Title"+dropdownxpath2));
				WebElement jobcategory = driver.findElement(By.xpath(dropdownxpath1+"Job Category"+dropdownxpath2));
				WebElement subunit = driver.findElement(By.xpath(dropdownxpath1+"Sub Unit"+dropdownxpath2));
				WebElement location = driver.findElement(By.xpath(dropdownxpath1+"Location"+dropdownxpath2));
				WebElement empstatus = driver.findElement(By.xpath(dropdownxpath1+"Employment Status"+dropdownxpath2));
				
				//pass data
				String x1 = "2022-09-11";
				js.executeScript("arguments[0].value='"+x1+"'", joineddate);
				js.executeScript("arguments[0].click()", jobtitle);
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath(dropdownxpath1+"Job Title"+listboxxpath)));
				List<WebElement> listOfJob = driver.findElements(By.xpath(dropdownxpath1+"Job Title"+listboxxpath));
				for(int j=0;j<listOfJob.size();j++) {
					String x= listOfJob.get(j).getText();
					if(x.equals("Account Assistant")) {
						listOfJob.get(j).click();
						break;
					}
				}
				js.executeScript("arguments[0].click()", jobcategory);
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath(dropdownxpath1+"Job Category"+listboxxpath)));
				List<WebElement> listOfJobCategory = driver.findElements(By.xpath(dropdownxpath1+"Job Category"+listboxxpath));
				for(int j=0;j<listOfJobCategory.size();j++) {
					String x= listOfJobCategory.get(j).getText();
					if(x.equals("Professionals")) {
						listOfJobCategory.get(j).click();
						break;
					}
				}
				js.executeScript("arguments[0].click()", subunit);
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath(dropdownxpath1+"Sub Unit"+listboxxpath)));
				List<WebElement> listOfSubUnit = driver.findElements(By.xpath(dropdownxpath1+"Sub Unit"+listboxxpath));
				for(int j=0;j<listOfSubUnit.size();j++) {
					String x= listOfSubUnit.get(j).getText();
					if(x.equals("Finance")) {
						listOfSubUnit.get(j).click();
						break;
					}
				}
				js.executeScript("arguments[0].click()", location);
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath(dropdownxpath1+"Location"+listboxxpath)));
				List<WebElement> listOfLoc = driver.findElements(By.xpath(dropdownxpath1+"Location"+listboxxpath));
				for(int j=0;j<listOfLoc.size();j++) {
					String x= listOfLoc.get(j).getText();
					if(x.equals("Texas R&D")) {
						listOfLoc.get(j).click();
						break;
					}
				}
				js.executeScript("arguments[0].click()", empstatus);
				wait.until(ExpectedConditions.elementToBeClickable(By.xpath(dropdownxpath1+"Employment Status"+listboxxpath)));
				List<WebElement> listOfEmpStatus = driver.findElements(By.xpath(dropdownxpath1+"Employment Status"+listboxxpath));
				for(int j=0;j<listOfEmpStatus.size();j++) {
					String x= listOfEmpStatus.get(j).getText();
					if(x.equals("Full-Time Probation")) {
						listOfEmpStatus.get(j).click();
						break;
					}
				}
				WebElement savebtn = driver.findElement(By.xpath("//div[@class='oxd-form-actions']//button[@type='submit']"));
				savebtn.click();
				break;
			}
		}
	}


//	@Test
//	public void deleteEmployeeTest() {
//		driver.findElement(By.xpath("//input[@name='username']")).sendKeys(prop.getProperty("username"));
//		driver.findElement(By.xpath("//input[@name='password']")).sendKeys(prop.getProperty("password"));
//		driver.findElement(By.xpath("//button[@type='submit']")).click();
//		
//		String firstname = prop.getProperty("firstname");
//		String middlename = prop.getProperty("middlename");
//		List<WebElement> list = driver.findElements(By.xpath("//div[@class='oxd-table-body']//div[@class='oxd-table-card']//div[3]"));
//		for(int i=0; i<list.size();i++) {
//			String name = list.get(i).getText();
//			if(name.equals(firstname + " " + middlename)) {
//				WebElement deletebtn = driver.findElement(By.xpath("//div[@class='oxd-table-body']//div[@class='oxd-table-card']//div[contains(text(),'"+firstname+" "+middlename+"')]//parent::div//parent::div//descendant::button//i[@class='oxd-icon bi-trash']"));
//				deletebtn.click();
//				WebElement deletebin = driver.findElement(By.xpath("//button[@class='oxd-button oxd-button--medium oxd-button--label-danger orangehrm-button-margin']"));
//				new WebDriverWait(driver,Duration.ofSeconds(3)).until(ExpectedConditions.elementToBeClickable(deletebin)).click();
//				
//				String s = driver.findElement(By.xpath("//div[@class='oxd-toast-content oxd-toast-content--success']")).getText();
//				boolean bool = s.contains("Successfully Deleted");
//				System.out.println(s);
//				Assert.assertTrue(bool);
//				break;
//			}
//		}
//	}
	
	@AfterMethod
	public void endsetUp() {
		driver.quit();
	}
	
	
	
	
	

}

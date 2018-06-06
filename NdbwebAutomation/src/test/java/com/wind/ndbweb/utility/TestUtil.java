package com.wind.ndbweb.utility;

import org.testng.AssertJUnit;

import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ById;
import org.openqa.selenium.By.ByXPath;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.openqa.selenium.ie.InternetExplorerDriver;




import com.thoughtworks.selenium.webdriven.commands.KeyEvent;

import java.awt.AWTException;



public class TestUtil {
	public static Logger log;
	private static boolean isBrowserOpened = false;
	private static List<WebElement> refList = null;
	private static List<WebElement> dateList = null;
	private static List<WebElement> Actions = null;
	public static List<WebElement> radiobuttons = null;
	public static Set<String> winIds=null;
	public static Iterator<String> it=null;
	public static String reference;
	public static WebDriver driver = null;
	private Properties CONFIG = null;
	private Properties uiMapper = null;
	private Properties SQL = null;
	
	
	HSSFWorkbook workbook;
	HSSFSheet sheet;
	Map<String, Object[]> TestNGResults;
	private DesiredCapabilities ieCapbalities;
	private String[] handlewindows;
	
	public TestUtil() {
		try {

			log = Logger.getLogger("devpinoyLogger");
			// Read config
			CONFIG = new Properties();
			FileInputStream fs = new FileInputStream(
					System.getProperty("user.dir")
							+ "\\src\\test\\resources\\com\\ndbweb\\properties\\config.properties");
			CONFIG.load(fs);
			//  read UIMapper
			uiMapper = new Properties();
			fs = new FileInputStream(
					System.getProperty("user.dir")
							+ "\\src\\test\\resources\\com\\ndbweb\\properties\\UIMapper.properties");
			uiMapper.load(fs);

			SQL = new Properties();
			fs = new FileInputStream(
					System.getProperty("user.dir")
							+ "\\src\\test\\resources\\com\\ndbweb\\properties\\sql.properties");
			SQL.load(fs);
			

		} catch (Exception e) {
			// Error is found
			e.printStackTrace();
		}
	}
	
	  public String openBrowser(String browserType) {
		log.debug("Executing openBrowser");
		try {
			if (!isBrowserOpened) {
				if (CONFIG.getProperty("browserType").equals("Mozilla"))
					driver = new FirefoxDriver();
				else if (CONFIG.getProperty("browserType").equals("IE")){
					System.setProperty("webdriver.ie.driver", System.getProperty("user.dir")+"\\src\\test\\resources\\com\\ndbweb\\driver\\IEDriverServer.exe");
					driver = new InternetExplorerDriver();
					
					
				}else if (CONFIG.getProperty("browserType").equals("CHROME")){
					System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"\\src\\test\\resources\\com\\ndbweb\\driver\\chromedriver.exe");
					driver = new ChromeDriver();
				}
			isBrowserOpened = true;

				driver.manage().window().maximize();
				
				final String waitTime = CONFIG
						.getProperty("default_implicitWait");
				driver.manage()
						.timeouts()
						.implicitlyWait(Long.parseLong(waitTime),
								TimeUnit.SECONDS);
				
			}
		} catch (Throwable e) {
			log.debug("Unable to open the Browser");
			
			ErrorUtil.addVerificationFailure(e);
			return "Fail";
		}
		log.debug("Pass");
		isBrowserOpened = true;
		return "Pass";
	}
	
	/**
	 * navigate to the given URLKey
	 * 
	 * @param URLKey
	 * @return Pass/Fail
	 */
	public String navigate(String URLKey) {
		log.debug("Executing navigate");
		try {
			driver.get(CONFIG.getProperty(URLKey));
		} catch (Throwable e) {
			log.debug("Unable to navigate");
			ErrorUtil.addVerificationFailure(e); 
			return "Fail - not able to navigate";
			
		}
		log.debug("Pass");
		return "Pass";
	}

	
	public String inputfromConfigurations(String identifier, String data)
			throws NoSuchElementException {
		log.debug("Executing inputfromConfigurations");
		try {
			if (identifier.endsWith("_xpath"))
				driver.findElement(By.xpath(uiMapper.getProperty(identifier)))
						.sendKeys(CONFIG.getProperty(data));
			else if (identifier.endsWith("_id"))
				driver.findElement(By.id(uiMapper.getProperty(identifier))).sendKeys(
						CONFIG.getProperty(data));
			else if (identifier.endsWith("_name"))
				driver.findElement(By.name(uiMapper.getProperty(identifier)))
						.sendKeys(CONFIG.getProperty(data));
			else if (identifier.endsWith("_css"))
				driver.findElement(By.cssSelector(uiMapper.getProperty(identifier))).click();
		} catch (Throwable t) {
			log.debug("Element not found - " + identifier);
			ErrorUtil.addVerificationFailure(t);
			
			return "Fail";
		}
		log.debug("Pass");
		
		return "Pass";
	}
	
	
	/**
	 * Clicks the identifier
	 * 
	 * @param identifier
	 * @return Pass/Fail
	 * @throws NoSuchElementException
	 */
	public String click(String identifier) throws NoSuchElementException {
		log.debug("Executing click");
		try {
			if (identifier.endsWith("_xpath")) 
				driver.findElement(By.xpath(uiMapper.getProperty(identifier))).click();
			else if (identifier.endsWith("_id"))
				driver.findElement(By.id(uiMapper.getProperty(identifier))).click();
			else if (identifier.endsWith("_name"))
				driver.findElement(By.name(uiMapper.getProperty(identifier))).click();
			else if (identifier.endsWith("_linktext"))
				driver.findElement(By.linkText(uiMapper.getProperty(identifier))).click();
			else if (identifier.endsWith("_css"))
				driver.findElement(By.cssSelector(uiMapper.getProperty(identifier))).click();
			else if (identifier.endsWith("_class")) 
		driver.findElement(By.className(uiMapper.getProperty(identifier))).click();;
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		} catch (Throwable e) {
			log.debug(e.getMessage());
			
			log.debug("Element not found");
			ErrorUtil.addVerificationFailure(e);
			log.debug(e.getMessage());
			
			return "Fail";
		}
		log.debug("Pass");
		
		return "Pass";
	}
	
	/**
	 * Clicks the identifier
	 * 
	 * @param identifier
	 * @return Pass/Fail
	 * @throws NoSuchElementException
	 */
	
	 
	 //#selectedEntityName
	 //*[@id="selectedEntityName"]
	public String radioforpopup(String data,String identifier)throws NoSuchElementException{
try{
		final String mainWindowHandle = driver.getWindowHandle();
		System.out.println(driver.getCurrentUrl());
		 for (String activeHandle : driver.getWindowHandles()) {
		        	if(!mainWindowHandle.equals(activeHandle)){   
		        	driver.switchTo().window(activeHandle);
		            if(!driver.getTitle().equals("Wizard Lov")){
		            	driver.switchTo().window(mainWindowHandle);
		            	
		            }
		            else{
		            
		          
						  List <WebElement> oRadioButton = driver.findElements(By.name("flagSelezionato"));
		        
		         
		            	
		            	oRadioButton.get(4).click();
		            	driver.findElement(By.xpath("/html/body/table/tbody/tr/td[2]/form/table/tbody/tr[2]/td/a/img")).click();
		            	driver.switchTo().window(mainWindowHandle);
		            	System.out.println(driver.getCurrentUrl());
		            	driver.findElement(By.xpath("/html/body/table/tbody/tr[1]/td[2]/form/table/tbody/tr[6]/td/a/img")).click();
		            	System.out.println(driver.getCurrentUrl());
		            	
		          
		          
		            	
		           }
		            }
		 }
		        	
		 
		    }catch (Throwable e) {
				log.debug(e.getMessage());
				
				log.debug("Element not found");
				ErrorUtil.addVerificationFailure(e);
				log.debug(e.getMessage());
				
				return "Fail";
			}
			log.debug("Pass");
			
			return "Pass";
		}
	public String lovSelection(String data,String identifier,Set<String> windowids)throws NoSuchElementException{
		try{
			driver.findElement(By.xpath("/html/body/table/tbody/tr[4]/td/form/table/tbody/tr/td/table/tbody/tr[1]/td[3]/a[1]/img")).click();	
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			windowids=driver.getWindowHandles();
			Iterator<String> iter = windowids.iterator();
			String mainWindowId=iter.next();
			System.out.println("Main window handle:"+mainWindowId);
			System.out.println(driver.getCurrentUrl());
			final String mainWindowHandle = driver.getWindowHandle(); 
			for (String activeHandle : driver.getWindowHandles()) {
			        
				if (!activeHandle.equals(mainWindowHandle)) {
			            driver.switchTo().window(activeHandle);
				            
				          
								  List <WebElement> oRadioButton = driver.findElements(By.name("keyModelSelected"));
				            	oRadioButton.get(0).click();
				            	driver.findElement(By.cssSelector("body > table > tbody > tr > td:nth-child(2) > form > table > tbody > tr:nth-child(2) > td > img")).click();
				            	
				            	
				            	
				            	driver.switchTo().window(mainWindowId);
				            	driver.switchTo().frame("right-bttom");


				            	/*System.out.println(driver.getCurrentUrl());*/
//				            	driver.findElement(By.xpath("/html/body/table/tbody/tr[1]/td[2]/form/table/tbody/tr[6]/td/a/img")).click();
//				            	System.out.println(driver.getCurrentUrl());
				            	
				           }
				            }
				 
				        	
				 
				    }catch (Throwable e) {
						log.debug(e.getMessage());
						
						log.debug("Element not found");
						ErrorUtil.addVerificationFailure(e);
						log.debug(e.getMessage());
						
						return "Fail";
					}
					log.debug("Pass");
					
					return "Pass";
				}
	
	
			 
	public String popupClick(String identifier) throws NoSuchElementException {
		log.debug("Executing click");
		try {
			
			final String mainWindowHandle = driver.getWindowHandle(); 
			for (String activeHandle : driver.getWindowHandles()) {
			        
				if (!activeHandle.equals(mainWindowHandle)) {
			            driver.switchTo().window(activeHandle);
			           
			        }
				  
			}
			 System.out.println(driver.getCurrentUrl());
			 Select select = new Select(driver.findElement(By.id("selectedEntityName")));
			 select.selectByValue("REPORT MAN");
			 //driver.switchTo().window(mainWindowHandle);  
			 System.out.println(driver.getCurrentUrl());
			 
			//*[@id="selectedEntityName"]

		} catch (Throwable e) {
			log.debug(e.getMessage());
			
			log.debug("Element not found");
			ErrorUtil.addVerificationFailure(e);
			log.debug(e.getMessage());
			
			return "Fail";
		}
		log.debug("Pass");
		
		return "Pass";
	}

	public String reportSelection(String identifier) throws NoSuchElementException {
		log.debug("Executing click");
		try {
			
			final String mainWindowHandle = driver.getWindowHandle(); 
			for (String activeHandle : driver.getWindowHandles()) {
			        
				if (!activeHandle.equals(mainWindowHandle)) {
			            driver.switchTo().window(activeHandle);
			           
			        }
				  
			}
			 System.out.println(driver.getCurrentUrl());
			 Select select = new Select(driver.findElement(By.id("selectedEntityName")));
			 select.selectByValue("REPORT OCCUPAZIONE POP FTTH");
			 //driver.switchTo().window(mainWindowHandle);  
			 System.out.println(driver.getCurrentUrl());
			 
			//*[@id="selectedEntityName"]

		} catch (Throwable e) {
			log.debug(e.getMessage());
			
			log.debug("Element not found");
			ErrorUtil.addVerificationFailure(e);
			log.debug(e.getMessage());
			
			return "Fail";
		}
		log.debug("Pass");
		
		return "Pass";
	}
	public String wizardPeip(String identifier,String data,Set<String> windowids) throws NoSuchElementException {
		log.debug("Executing click");
		try {
			
			driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
			windowids=driver.getWindowHandles();
			Iterator<String> iter = windowids.iterator();
			String mainWindowId=iter.next();
			
			System.out.println("Main window handle:"+mainWindowId);
			System.out.println(driver.getCurrentUrl());
			final String mainWindowHandle = driver.getWindowHandle(); 
			for (String activeHandle : driver.getWindowHandles()) {
			        
				if (!activeHandle.equals(mainWindowHandle)) {
			            driver.switchTo().window(activeHandle);
			 Select select = new Select(driver.findElement(By.cssSelector("#categoryReturn")));
			 select.selectByIndex(72);
			// System.out.println(driver.getCurrentUrl());
			 driver.switchTo().window(mainWindowId);
			
				}
	}	 
		

		} catch (Throwable e) {
			log.debug(e.getMessage());
			
			log.debug("Element not found");
			ErrorUtil.addVerificationFailure(e);
			log.debug(e.getMessage());
			
			return "Fail";
		}
		log.debug("Pass");
		
		return "Pass";
	}
	public String findButtonSelection(String identifier,String data) throws NoSuchElementException {
		log.debug("Executing click");
		try {
			
			final String mainWindowHandle = driver.getWindowHandle(); 
			for (String activeHandle : driver.getWindowHandles()) {
			        
				if (!activeHandle.equals(mainWindowHandle)) {
			            driver.switchTo().window(activeHandle);
			           
			        }
				  
			}
			 System.out.println(driver.getCurrentUrl());
			 Select select = new Select(driver.findElement(By.id("selectedEntityName")));
			 select.selectByValue("LOCATION");
			 
			 System.out.println(driver.getCurrentUrl());
			 
			//*[@id="selectedEntityName"]

		} catch (Throwable e) {
			log.debug(e.getMessage());
			
			log.debug("Element not found");
			ErrorUtil.addVerificationFailure(e);
			log.debug(e.getMessage());
			
			return "Fail";
		}
		log.debug("Pass");
		
		return "Pass";
	}
	public String findbutton(String identifier){
		log.debug("find click");
		try {
			if (identifier.endsWith("_xpath")) 
				driver.findElement(By.xpath(uiMapper.getProperty(identifier))).click();
			else if (identifier.endsWith("_id"))
				driver.findElement(By.id(uiMapper.getProperty(identifier))).click();
			else if (identifier.endsWith("_name"))
				driver.findElement(By.name(uiMapper.getProperty(identifier))).click();
			else if (identifier.endsWith("_linktext"))
				driver.findElement(By.linkText(uiMapper.getProperty(identifier))).click();
			else if (identifier.endsWith("_css"))
				driver.findElement(By.cssSelector(uiMapper.getProperty(identifier))).click();
			System.out.println(driver.getCurrentUrl());
			String main=driver.getWindowHandle();
			Set<String> sub= driver.getWindowHandles();
			System.out.println(sub);
			for(String active: driver.getWindowHandles()){
			driver.switchTo().window(active);
			System.out.println(driver.getCurrentUrl());
			}
			Thread.sleep(10000);
			if(driver.getCurrentUrl().equalsIgnoreCase("http://assur-oss-a-04-tp.wind.root.it:7777/ndb@web/checklogin")){
				inputfromConfigurations("loginusername_xpath","username");
				findbutton("loginbutton_xpath");
			}
			Select dropdown=new Select(driver.findElement(By.id("selectedEntityName")));
			dropdown.selectByValue("TRAIL");
			
			driver.switchTo().window(main);
			System.out.println(driver.getCurrentUrl());
		} catch (Throwable e) {
			log.debug(e.getMessage());
			
			log.debug("find not found");
			ErrorUtil.addVerificationFailure(e);
			log.debug(e.getMessage());
			return "Fail";
		}
		log.debug("Pass");
		return "Pass";
	
	}
	public String Iframeselection(String identifier,String data) throws NoSuchElementException{
		try{
		
			driver.switchTo().frame("right-bttom");
		    /*driver.findElement(By.cssSelector("body > div.wrraper > div.main-container > div.right > div.wizard-container.box > div.box-header > div.partly-section > a.part-minimize-app.part-minimize-app-4.tips")).click();*/
		    
		}catch (Throwable e) {
			log.debug(e.getMessage());
			
			log.debug("Element not found");
			ErrorUtil.addVerificationFailure(e);
			log.debug(e.getMessage());
			
			return "Fail";
		}
		log.debug("Pass");
		
		return "Pass";
			
		
	}
	
	public String Findbuttonpage1(String identifier,String data) throws NoSuchElementException{
		try{
			driver.findElement(By.xpath("/html/body/table/tbody/tr[4]/td/form/table/tbody/tr/td/table/tbody/tr[3]/td[5]/a/img")).click();
			
			System.out.println(driver.getCurrentUrl());
			final String mainWindowHandle = driver.getWindowHandle(); 
			for (String activeHandle : driver.getWindowHandles()) {
			        
				if (!activeHandle.equals(mainWindowHandle)) {
			            driver.switchTo().window(activeHandle);
			            System.out.println(driver.getCurrentUrl());
			            driver.findElement(By.cssSelector("#idSearch")).click();
						driver.manage().timeouts().implicitlyWait(32, TimeUnit.SECONDS);
						
						  List <WebElement> oRadioButton = driver.findElements(By.name("currentKeySelected"));
						  oRadioButton.get(0).click();   
						  if(driver.findElement(By.cssSelector("body.stileSfondoBlu > form > table.stileSfondoBlu > tbody > tr > td > input")).isDisplayed())
						  {
							  System.out.println("Find button is avaliable");
                          driver.findElement(By.cssSelector("body.stileSfondoBlu > form > table.stileSfondoBlu > tbody > tr > td > input")).click();
							  }else{

							  System.out.println("Find button is not avaliable");

							  }
						  /*driver.findElement(By.xpath("/html/body/table/tbody/tr/td[2]/form/table/tbody/tr[2]/td/a/img")).click();*/
						  driver.manage().timeouts().implicitlyWait(32, TimeUnit.SECONDS);
						  driver.switchTo().window(mainWindowHandle);
						  driver.switchTo().frame("right-bttom");
						  
						  
				}
				  
			}
			
			   
			//ABBIATEGRASS/L0T
		}catch(Throwable e){
            log.debug(e.getMessage());
			
			log.debug("Element not found");
			ErrorUtil.addVerificationFailure(e);
			log.debug(e.getMessage());
			
			return "Fail";
		}
	return "Pass";
	}
	public String Findbuttonpage2(String identifier,String data) throws NoSuchElementException{
		try{
			driver.findElement(By.xpath("/html/body/table/tbody/tr[4]/td/form/table/tbody/tr/td/table/tbody/tr[4]/td[5]/a/img")).click();
			/*By.xpath("/table[@class='tableWizard']/tbody/tr[3]/td[5]/a/img[@alt='Find']")*/
			
			System.out.println(driver.getCurrentUrl());
			final String mainWindowHandle = driver.getWindowHandle(); 
			for (String activeHandle : driver.getWindowHandles()) {
			        
				if (!activeHandle.equals(mainWindowHandle)) {
			            driver.switchTo().window(activeHandle);
			            System.out.println(driver.getCurrentUrl());
			            driver.findElement(By.cssSelector("#idSearch")).click();
						driver.manage().timeouts().implicitlyWait(32, TimeUnit.SECONDS);
						
						  List <WebElement> oRadioButton = driver.findElements(By.name("currentKeySelected"));
						  oRadioButton.get(0).click();   
						  driver.findElement(By.cssSelector("body.stileSfondoBlu > form > table.stileSfondoBlu > tbody > tr > td > input")).click(); 
						  driver.manage().timeouts().implicitlyWait(32, TimeUnit.SECONDS);
						  driver.switchTo().window(mainWindowHandle);
						  driver.switchTo().frame("right-bttom");
						  
						  
				}
				  
			}
			
			   
			//ABBIATEGRASS/L0T
		}catch(Throwable e){
            log.debug(e.getMessage());
			
			log.debug("Element not found");
			ErrorUtil.addVerificationFailure(e);
			log.debug(e.getMessage());
			
			return "Fail";
		}
	return "Pass";
	}
	public String dailyReport(String identifier,String data) throws NoSuchElementException {
		log.debug("Executing click");
		driver.findElement(By.xpath("/html/body/div[3]/div/div[1]/div[2]/a[2]/img")).click();
		
		try {
			
			final String mainWindowHandle = driver.getWindowHandle(); 
			for (String activeHandle : driver.getWindowHandles()) {
			        
				if (!activeHandle.equals(mainWindowHandle)) {
			            driver.switchTo().window(activeHandle);
			           
			        }
				  
			}
			 System.out.println(driver.getCurrentUrl());
			 Select select = new Select(driver.findElement(By.id("selectedEntityName")));
			 select.selectByValue("NETWORK RESOURCES DAILY REPORT");
			 
			 System.out.println(driver.getCurrentUrl());
			 
			//*[@id="selectedEntityName"]

		} catch (Throwable e) {
			log.debug(e.getMessage());
			
			log.debug("Element not found");
			ErrorUtil.addVerificationFailure(e);
			log.debug(e.getMessage());
			
			return "Fail";
		}
		log.debug("Pass");
		
		return "Pass";
	}
	
	
	public String validationPopup()throws NoSuchElementException{
		try{
			driver.findElement(By.xpath("/html/body/table/tbody/tr[1]/td[2]/form/table/tbody/tr[2]/td/a/img")).click();
			 driver.switchTo().alert().accept();	
			driver.findElement(By.xpath("/html/body/table/tbody/tr[1]/td[2]/form/table/tbody/tr[6]/td/a/img")).click();
			 Alert alert=driver.switchTo().alert();
			System.out.println(alert.getText());
			System.out.println("Enter the details");
			alert.accept();
	} catch (Throwable e){
		
		return "Fail";
	}
		return "Pass";
	}
	public String reportValiadationPopup(String data,String identifier)throws NoSuchElementException{
		try{
				
			 driver.findElement(By.xpath("/html/body/table/tbody/tr[1]/td[2]/form/table/tbody/tr[2]/td/a/img")).click();
			 driver.switchTo().alert().accept();
			 driver.findElement(By.xpath("/html/body/table/tbody/tr[1]/td[2]/form/table/tbody/tr[6]/td/a/img")).click();
			 Alert alert=driver.switchTo().alert();
			System.out.println(alert.getText());
			System.out.println("Enter the details");
			alert.accept();
	} catch (Throwable e){
		return "Fail";
	}
		return "Pass";
	}
	
	public String ReportNetworkValidationPopup(String data,String identifier)throws NoSuchElementException{
		try{
				
			 driver.findElement(By.xpath("/html/body/table/tbody/tr[1]/td[2]/form/table/tbody/tr[2]/td/a/img")).click();
			 driver.switchTo().alert().accept();
			 driver.findElement(By.xpath("/html/body/table/tbody/tr[1]/td[2]/form/table/tbody/tr[6]/td/a/img")).click();
			 Alert alert=driver.switchTo().alert();
			System.out.println(alert.getText());
			System.out.println("Enter the details");
			alert.accept();
	} catch (Throwable e){
		return "Fail";
	}
		return "Pass";
	}
	/**
	 * Compares the Page Title with the given Title.
	 * 
	 * @param expectedVal
	 * @return Pass/Fail
	 */
	public String findParameters()throws NoSuchElementException{
		try{
			 String fileName = "tokenftth.jpg";
			File scrFile = ((TakesScreenshot) TestUtil.driver)
					.getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile,
					new File(System.getProperty("user.dir")
							+ "//screenshots//" + fileName));
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);
			log.debug("Titles do not match");
			log.debug("Actual Value is =" + driver.getTitle());
			return "Fail";
		}
		log.debug("Pass");
		return "Pass";
	}
	public String filterPage()throws NoSuchElementException{
		try{
			driver.findElement(By.xpath("/html/body/table/tbody/tr[1]/td[2]/form/table/tbody/tr[6]/td/a/img")).click();
			System.out.println(driver.getCurrentUrl());
			if(driver.findElement(By.name("tutti")).isDisplayed())
			{
			System.out.println(driver.getCurrentUrl());
			driver.findElement(By.name("tutti")).click();
			driver.findElement(By.xpath("/html/body/table/tbody/tr[1]/td[2]/form/table/tbody/tr[3]/td/a/img")).click();
			}
			else{
			
			}
				
		}
		catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);
			log.debug("Titles do not match");
			log.debug("Actual Value is =" + driver.getTitle());
			return "Fail";
		}
		log.debug("Pass");
		return "Pass";
	}
	
	public String compareTitle(String expectedVal) {
		log.debug("Executing compareTitle Assertion");
		try {
			System.out.println(driver.getTitle());
			System.out.println(uiMapper.getProperty(expectedVal));
			
			
			AssertJUnit.assertEquals(driver.getTitle(), uiMapper.getProperty(expectedVal));
			AssertJUnit.assertEquals(1, 1);
			
		} catch(Throwable t) {
			ErrorUtil.addVerificationFailure(t);
			log.debug("Titles do not match");
			log.debug("Actual Value is =" + driver.getTitle());
			return "Fail";
		}
		log.debug("Pass");
		return "Pass";
	}
	
	
	public String logout(){
		try{
			driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[2]/a")).click();
			driver.switchTo().alert().accept();
		}
		catch (Throwable e) {
			log.debug("Error while loggin out the Browser");
			ErrorUtil.addVerificationFailure(e);
			return "Fail";
		}
		log.debug("Pass");
		return "Pass";
	}
	
	
	
	
	/**
	 * 
	 * Closes the Browser
	 * 
	 * @return Pass/Fail
	 */
	public String closebyBrowser(){
		try{
			Alert alert=driver.switchTo().alert();
			System.out.println(alert.getText());
			alert.accept();
			if (isBrowserOpened) {
				driver.close();
				isBrowserOpened = false;
		}
	}catch (Throwable e) {
		log.debug("Error while closing the Browser");
		ErrorUtil.addVerificationFailure(e);
		return "Fail";
	}
	log.debug("Pass");
	return "Pass";
	}

	public String closeBrowser() {
		log.debug("Executing closeBrowser");
		try {
			if (isBrowserOpened) {
				driver.close();
				isBrowserOpened = false;
			}
		} catch (Throwable e) {
			log.debug("Error while closing the Browser");
			ErrorUtil.addVerificationFailure(e);
			return "Fail";
		}
		log.debug("Pass");
		return "Pass";
	}
	
	


}

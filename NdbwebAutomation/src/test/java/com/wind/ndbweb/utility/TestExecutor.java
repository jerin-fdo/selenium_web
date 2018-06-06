package com.wind.ndbweb.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.InvalidSelectorException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;

/*enum ENXLX{
	openBrowser,navigate,inputfromConfigurations,click,compareTitle,closeBrowser,inputfromXls, popupClick,radioforpopup,dailyReport,validationPopup,reportValiadationPopup,logout,closebyBrowser,findbutton,findoperation,reportSelection,lovSelection,filterPage,findParameters;
}
*/

public class TestExecutor {

	
	@Autowired
	private Xls_Reader xlsReader ;
	
	@Autowired
	private TestUtil testUtil;
	
	public Xls_Reader getXlsReader() {
		return xlsReader;
	}

	public void setXlsReader(Xls_Reader xlsReader) {
		this.xlsReader = xlsReader;
	}

	public TestUtil getTestUtil() {
		return testUtil;
	}

	public void setTestUtil(TestUtil testUtil) {
		this.testUtil = testUtil;
	}

	public static Logger APP_LOGS ;
	
	Hashtable<String, String> data = new Hashtable<String, String>();

	public TestExecutor() {
		System.out.println("Initializing TestExecutor");
		// initialize properties files
		try {

			APP_LOGS = Logger.getLogger("devpinoyLogger");
			

		} catch (Exception e) {
			// Error is found
			e.printStackTrace();
		}
	}

	public void executeKeywords(String testName, Hashtable<String, String> data) {
		System.out.println("Executing - " + testName);
		// find the keywords for the test
		String keyword = null;
		String objectKey = null;
		String dataColVal = null;
		String index=null;
		String tcid = null;
	
		for (int rNum = 0; rNum <= xlsReader.getRowCount("Test Steps"); rNum++) {
			tcid = xlsReader.getCellData("Test Steps", "TCID", rNum);
			if (tcid.equals(testName)) {
 				keyword = xlsReader.getCellData("Test Steps", "Keyword", rNum);
				objectKey = xlsReader.getCellData("Test Steps", "Object", rNum);
				dataColVal = xlsReader.getCellData("Test Steps", "Data", rNum);
				
				//ENXLX enxls = ENXLX.valueOf(keyword);

				String result = null;
				switch(keyword){	
				case "openBrowser" :
					result = testUtil.openBrowser(dataColVal);
					break;
				case "navigate" :
					result = testUtil.navigate(dataColVal);
					break;
					
				case "inputfromConfigurations" :
					result = testUtil.inputfromConfigurations(objectKey,dataColVal);
					break;
				case "click" :
					result = testUtil.click(objectKey);
			      //testUtil.checkforpopup(objectKey,dataColVal);
					break;
					
				case "compareTitle" :
					result = testUtil.compareTitle(dataColVal);
					break;
				case "wizardPeip":
				result= testUtil.wizardPeip(objectKey,dataColVal,null);
					break;
				case "closebyBrowser":
				result=testUtil.closebyBrowser();
				break;
				case "closeBrowser" :
					result = testUtil.closeBrowser();
					break;
					 
				case "inputfromXls" :
					String inputData="";
					System.out.println(inputData);
					//result = testUtil.inputfromXls(objectKey,dataColVal);
					break;
					
				case "popupClick":
					result = testUtil.popupClick(objectKey);
					break;

				case "findButtonSelection":
				result = testUtil.findButtonSelection(dataColVal,objectKey);
				break;
				case "reportSelection":
					result = testUtil.reportSelection(objectKey);
					break;
					
				case "reportValiadationPopup":
					result = testUtil.reportValiadationPopup(dataColVal,objectKey);
					break;
				case "filterPage":
				result = testUtil.filterPage();
				break;
				case "findParameters":
					result= testUtil.findParameters();
				
				case  "radioforpopup":
					result = testUtil.radioforpopup(dataColVal,objectKey);
				break;
				case "findbutton" :
					result = testUtil.findbutton(objectKey);
					break;
				
				case "Iframeselection":
				result=testUtil.Iframeselection(objectKey,dataColVal);
				break;
				
				case "Findbuttonpage1":
				result=testUtil.Findbuttonpage1(objectKey,dataColVal);
				break;
				case "logout":
					result = testUtil.logout();
					break;
				case "lovSelection":
				result = testUtil.lovSelection(dataColVal,objectKey,null);
				break;
				default: 
					log("Keyword not found");
					break;
				}
				
				
				log(result);

				if (!result.equals("Pass")) {
					try {
						// screenshot
						String fileName = tcid + "_" + keyword + ".jpg";
						File scrFile = ((TakesScreenshot) TestUtil.driver)
								.getScreenshotAs(OutputType.FILE);
						FileUtils.copyFile(scrFile,
								new File(System.getProperty("user.dir")
										+ "//screenshots//" + fileName));
					} catch (IOException e) {
						System.out.println("***ERR***");
						log("***ERR***");
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					String proceed = xlsReader.getCellData("Test Steps",
							"Proceedd_On_Fail", rNum);
					if (proceed.equalsIgnoreCase("y")) {
						try {
							// Fail and Continue the Test
							Assert.fail(result);
						} catch (Throwable t) {
							log("***************ERROR*************");
							// listeners
							ErrorUtil.addVerificationFailure(t);
						}
					} else
						// Fail and Stop
						Assert.fail(result);
					     
				}

				System.out.println(tcid + ".........." + keyword + "........."
						+ objectKey + "........." + dataColVal);
				log(tcid + ".........." + keyword + "........." + objectKey
						+ "........." + dataColVal);
			}
		}

	}

	public void log(String msg) {
		APP_LOGS.debug(msg);	
	}
	
	
	
}

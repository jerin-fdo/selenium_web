package com.wind.ndbweb.ndbAutomation;

import org.testng.annotations.Test;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import com.wind.ndbweb.utility.DataReader;
import com.wind.ndbweb.utility.TestExecutor;

@ContextConfiguration("application_context.xml")
public class LoginTest extends AbstractTestNGSpringContextTests{
	
	
	@Autowired
	TestExecutor testExecutor;
	
	@Test(dataProvider = "getVerifyLogin")
	public void verifyReport(Hashtable<String, String> data) {

		if (!DataReader.isTestCaseExecutable("loginTEST", testExecutor.getXlsReader()))
			throw new SkipException("Skipping the test as Runmode is NO");
		if (!data.get("RunMode").equals("Y"))	
			throw new SkipException(
					"Skipping the test as data set Runmode is NO");
		
		
		testExecutor.log("*******Started LoginTest********");
		
		testExecutor.executeKeywords("loginTEST", data);
		
			
		testExecutor.log("******LoginTest Finished******");
	}
	
	
	

	@DataProvider
	public Object[][] getVerifyLogin() {
		return DataReader.getData("loginTEST", testExecutor.getXlsReader());
	}
	
}

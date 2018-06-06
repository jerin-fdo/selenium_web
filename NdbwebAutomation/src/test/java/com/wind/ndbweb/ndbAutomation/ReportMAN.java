package com.wind.ndbweb.ndbAutomation;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.AssertJUnit;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.wind.ndbweb.utility.DataReader;
import com.wind.ndbweb.utility.TestExecutor;
@ContextConfiguration("application_context.xml")


public class ReportMAN extends AbstractTestNGSpringContextTests{
	@Autowired
	TestExecutor testExecutor;
	HSSFWorkbook workbook;
	HSSFSheet sheet;
	Map<String, Object[]> TestNGResults;
	@Test(dataProvider = "checkManreport")
	public void manReporttest(Hashtable<String, String> data){
		try{
		if (!DataReader.isTestCaseExecutable("ReportMAN", testExecutor.getXlsReader()))
			throw new SkipException("Skipping the test as Runmode is NO");
		if (!data.get("RunMode").equals("Y"))	
			throw new SkipException(
					"Skipping the test as data set Runmode is NO");
		
		
		testExecutor.log("*******Started ReportMAN********");
		
		testExecutor.executeKeywords("ReportMAN", data);
		 
			
		testExecutor.log("******ReportMAN Finished******");
			}catch (Exception e){
		}
	}
	@DataProvider
	public Object[][] checkManreport(){
		return DataReader.getData("ReportMAN", testExecutor.getXlsReader());
	
	}	
}

	






package com.wind.ndbweb.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.ITestResult;
import org.testng.Reporter;

public class ErrorUtil {
	private static Map<ITestResult, List> verificationFailuresMap = new HashMap<ITestResult, List>();
	private static Map<ITestResult, List> skipMap = new HashMap<ITestResult, List>();

	public static void addVerificationFailure(final Throwable e) {
		final List verificationFailures = getVerificationFailures();
		verificationFailuresMap.put(Reporter.getCurrentTestResult(),
				verificationFailures);
		verificationFailures.add(e);
	}

	public static List getVerificationFailures() {
		final List verificationFailures = verificationFailuresMap.get(Reporter
				.getCurrentTestResult());
		return verificationFailures == null ? new ArrayList()
				: verificationFailures;
	}

}

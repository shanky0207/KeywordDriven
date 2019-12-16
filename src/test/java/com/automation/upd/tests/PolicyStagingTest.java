package com.automation.upd.tests;

import org.testng.annotations.Test;

import com.automation.upd.ExecutionEngine.KeyWordEngine;

public class PolicyStagingTest {
	
	public KeyWordEngine keyWordEngine;

	@Test
	public void loginTest()
	{
		keyWordEngine = new KeyWordEngine();
		keyWordEngine.startExecution("login");
		keyWordEngine.startExecution("PrimaryProposed");
		
		//System.out.println("Testing the reports");
	}
}

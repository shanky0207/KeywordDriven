package com.automation.upd.base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class Base {

	public WebDriver driver;
	public Properties prop;
	

	@SuppressWarnings("deprecation")
	public WebDriver  init_driver(String browserName)
	{
		
		
		if(browserName.equals("chrome"))
		{
			DesiredCapabilities caps = DesiredCapabilities.chrome();
			ChromeOptions options = new ChromeOptions();
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/chromedriver.exe");
			if(prop.getProperty("headless").equals("yes")){
				//headless mode:
				options.addArguments("--headless");
				driver = new ChromeDriver(options);
			}
			else
			{
				
				options.setExperimentalOption("useAutomationExtension", false);
                options.addArguments("--start-maximized");
                options.addArguments("disable-infobars");
                
                caps.setCapability("browserName", "chrome");
                caps.setCapability("version", "");
                caps.setCapability("platform", "WINDOWS");
                caps.setCapability(ChromeOptions.CAPABILITY, options);

                
				driver = new ChromeDriver(caps);
			}
		}
		else if(browserName.equals("ie"))
			{
			
			DesiredCapabilities caps = DesiredCapabilities.internetExplorer();
			
			caps.setCapability(
                    InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
                    true);
			caps.setCapability("ignoreProtectedModeSettings", true);
            caps.setCapability(CapabilityType.BROWSER_NAME, "internet explorer");
            caps.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR,
                         org.openqa.selenium.UnexpectedAlertBehaviour.ACCEPT);
            caps.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
            
            caps.setCapability(CapabilityType.ELEMENT_SCROLL_BEHAVIOR, true);
            caps.setCapability(
                         CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION,
                         true);
            caps.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);


           System.setProperty("webdriver.ie.driver",
                    System.getProperty("user.dir") + "/IEDriverServer.exe");
				driver = new InternetExplorerDriver(caps);
			
		} 
		return driver;
	

	}

	
	public Properties init_properties(){
		prop = new Properties();
		try {
			FileInputStream ip = new FileInputStream(System.getProperty("user.dir")+"/src/main/java/com/automation/upd/config/config.properties");
			prop.load(ip);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}
}
	


package com.automation.upd.ExecutionEngine;


import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.automation.upd.base.Base;

public class KeyWordEngine {

	public WebDriver driver;
	public Properties prop;

	public static Workbook book;
	public static Sheet sheet;
	public long waitTime = 30;
	public Robot robot;


	public Base base;
	public WebElement element;

	public final String SCENARIO_SHEET_PATH = "C:\\Users\\shashank.g.chavan\\eclipse-workspace\\HybridFramework\\src\\main\\java\\com\\automation\\upd\\UpdScenarios\\Scenario1.xlsx";

	public void startExecution(String sheetName) {

		FileInputStream file = null;
		try {
			file = new FileInputStream(SCENARIO_SHEET_PATH);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			book = WorkbookFactory.create(file);
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		sheet = book.getSheet(sheetName);
		int k = 0;
		for (int i = 0; i < sheet.getLastRowNum(); i++) {
			try {

				String locatorType = sheet.getRow(i + 1).getCell(k + 1).toString().trim();
				String locatorValue = sheet.getRow(i + 1).getCell(k + 2).toString().trim();
				String action = sheet.getRow(i + 1).getCell(k + 3).toString().trim();
				String value = sheet.getRow(i + 1).getCell(k + 4).toString().trim();

				//this switch is for browser related actions
				switch (action) {
				case "open browser":
					base = new Base();
					prop = base.init_properties();
					if (value.isEmpty() || value.equals("NA")) {
						driver = base.init_driver(prop.getProperty("browser"));
					} else {
						driver = base.init_driver(value);
					}
					break;

				case "enter url":
					if (value.isEmpty() || value.equals("NA")) {
						driver.get(prop.getProperty("url"));
					} else {
						driver.get(value);
					}
					break;

				case "quit":
					driver.quit();
					break;

				case "wait":
					driver.manage().timeouts().implicitlyWait(20,TimeUnit.SECONDS);
					break;

				case "sleep":
					Thread.sleep(3000);
					break;

				case "switchToFrame":
					try {
						driver.switchTo().frame(value);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;

				case "switchToMultipleFrame":
					try {
						List<WebElement> frames = driver.findElements(By.tagName("iframe"));
						int size = frames.size();
						for (WebElement frame : frames) {
							if (frame.getAttribute("src").contains(value))
							{
								driver.switchTo().frame(frame);
								break;
							}
						}
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					break;


				case "switchToMainContent":
					driver.switchTo().defaultContent();
					break;

				case "sendKeyTab":
					robot = new Robot();
					robot.keyPress(KeyEvent.VK_TAB);
					break;

				default:
					break;
				}

				//this switch is for locators, can add more locatortypes if required
				switch (locatorType) {
				case "id":
					element = driver.findElement(By.id(locatorValue));
					try {
						if (action.equalsIgnoreCase("sendkeys")) {
							element.clear();
							element.sendKeys(value);
						} else if (action.equalsIgnoreCase("click")) {
							element.click();
						} else if (action.equalsIgnoreCase("isDisplayed")) {
							element.isDisplayed();
						} else if (action.equalsIgnoreCase("getText")) {
							String elementText = element.getText();
							System.out.println("text from element : " + elementText);
						}
						else if (action.equalsIgnoreCase("dropdown")) {
							Select dd = new Select(driver.findElement(By.xpath(locatorValue)));
							dd.selectByVisibleText(value);
						}
						else if (action.equalsIgnoreCase("waitForElementVisibility")) {
							WebDriverWait wait = new WebDriverWait(driver, waitTime);
							wait.until(ExpectedConditions.visibilityOf(element));
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					locatorType = null;
					break;

				case "name":
					element = driver.findElement(By.name(locatorValue));
					try {
						if (action.equalsIgnoreCase("sendkeys")) {
							element.clear();
							element.sendKeys(value);

						} else if (action.equalsIgnoreCase("click")) {
							element.click();
						} else if (action.equalsIgnoreCase("isDisplayed")) {
							element.isDisplayed();
						} else if (action.equalsIgnoreCase("getText")) {
							String elementText = element.getText();
							System.out.println("text from element : " + elementText);
						}
						else if (action.equalsIgnoreCase("dropdown")) {
							Select dd = new Select(element);
							dd.selectByVisibleText(value);
						}
						else if (action.equalsIgnoreCase("waitForElementVisibility")) {
							WebDriverWait wait = new WebDriverWait(driver, waitTime);
							wait.until(ExpectedConditions.visibilityOf(element));
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					locatorType = null;
					break;

				case "xpath":
					element = driver.findElement(By.xpath(locatorValue));

					try {
						if (action.equalsIgnoreCase("sendkeys")) {
							element.clear();
							element.sendKeys(value);
						} else if (action.equalsIgnoreCase("click")) {
							try {
								element.click();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						else if (action.equalsIgnoreCase("JSclick")) {
							try {
								
								JavascriptExecutor executor = (JavascriptExecutor) driver;
								executor.executeScript("arguments[0].click();", element);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						} else if (action.equalsIgnoreCase("isDisplayed")) {
							try {
								element.isDisplayed();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else if (action.equalsIgnoreCase("getText")) {
							String elementText = element.getText();
							System.out.println("text from element : " + elementText);
						}
						else if (action.equalsIgnoreCase("dropdown")) {
							Select dd = new Select(driver.findElement(By.xpath(locatorValue)));
							dd.selectByVisibleText(value);
						}else if (action.equalsIgnoreCase("waitForElementVisibility")) {
							WebDriverWait wait = new WebDriverWait(driver, waitTime);
							wait.until(ExpectedConditions.visibilityOf(element));
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					locatorType = null;
					break;

				case "cssSelector":
					element = driver.findElement(By.cssSelector(locatorValue));
					try {
						if (action.equalsIgnoreCase("sendkeys")) {
							element.clear();
							element.sendKeys(value);
						} else if (action.equalsIgnoreCase("click")) {
							element.click();
						} else if (action.equalsIgnoreCase("isDisplayed")) {
							element.isDisplayed();
						} else if (action.equalsIgnoreCase("getText")) {
							String elementText = element.getText();
							System.out.println("text from element : " + elementText);
						}
						else if (action.equalsIgnoreCase("dropdown")) {
							Select dd = new Select(driver.findElement(By.xpath(locatorValue)));
							dd.selectByVisibleText(value);
						}else if (action.equalsIgnoreCase("waitForElementVisibility")) {
							WebDriverWait wait = new WebDriverWait(driver, waitTime);
							wait.until(ExpectedConditions.visibilityOf(element));
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					locatorType = null;
					break;

				case "className":
					element = driver.findElement(By.className(locatorValue));
					try {
						if (action.equalsIgnoreCase("sendkeys")) {
							element.clear();
							element.sendKeys(value);
						} else if (action.equalsIgnoreCase("click")) {
							element.click();
						} else if (action.equalsIgnoreCase("isDisplayed")) {
							element.isDisplayed();
						} else if (action.equalsIgnoreCase("getText")) {
							String elementText = element.getText();
							System.out.println("text from element : " + elementText);
						}
						else if (action.equalsIgnoreCase("dropdown")) {
							Select dd = new Select(driver.findElement(By.xpath(locatorValue)));
							dd.selectByVisibleText(value);
						}else if (action.equalsIgnoreCase("waitForElementVisibility")) {
							WebDriverWait wait = new WebDriverWait(driver, waitTime);
							wait.until(ExpectedConditions.visibilityOf(element));
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					locatorType = null;
					break;

				case "linkText":
					element = driver.findElement(By.linkText(locatorValue));
					try {
						element.click();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					locatorType = null;
					break;

				case "partialLinkText":
					element = driver.findElement(By.partialLinkText(locatorValue));
					try {
						element.click();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					locatorType = null;
					break;

				default:
					break;
				}

			} catch (Exception e) {

			}

		}

	}
}

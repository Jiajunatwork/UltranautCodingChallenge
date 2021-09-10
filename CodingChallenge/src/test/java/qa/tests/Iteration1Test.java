package qa.tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class Iteration1Test {
	WebDriver driver = null;
	@BeforeClass
	public void startApp() {
		String projectDirectory = System.getProperty("user.dir");
		String htmlFilePath = projectDirectory+"\\src\\main\\webapp\\index.html";
		System.setProperty("webdriver.chrome.driver", "chromedriver.exe");          
		driver = new ChromeDriver(); 
		driver.get(htmlFilePath);
	}
	
	@AfterClass
	public void closeApp() {
		driver.close();
	}
	@Test(dataProvider = "iteration1DataProvider")
	public void iteration1(String password, String result) {
		/*
		 * WebElement inputBox = driver.findElement(By.id("psw"));
		 * inputBox.sendKeys(password); WebElement button =
		 * driver.findElement(By.className("signupbtn")); button.click(); WebElement
		 * judgement = driver.findElement(By.id("msg")); String judgementResult =
		 * judgement.getAttribute("innerHTML").toLowerCase();
		 */
		userEnterPassword(password);
		userClickOnCheckMyPassword();
		String judgementResult = getResult();
		if(result.toUpperCase().equals("F")) {
			System.out.println("password: "+password+" , "+judgementResult+" vs. F");
			Assert.assertTrue(judgementResult.startsWith("invalid password"));
		}else {
			Assert.assertTrue(judgementResult.startsWith("valid password"));
			System.out.println("password: "+password+" , "+judgementResult+" vs. T");
		}
	}

	public String translateTestData(String password) {
		if(password.equals("null")){
			return "";
		}else {
			return password;
		}
	}
	@DataProvider(name = "iteration1DataProvider")
	public String[][] dataProvider() {
		String[][] finalData = null;
		try {
			ArrayList<ArrayList<String>> mydata = new ArrayList<ArrayList<String>>();
			File myObj = new File("src/main/Resources/testdata/iteration1Data.txt");
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine().replace(" ", "").replace("|", "");
				if (data.startsWith("#") == false) {
					ArrayList<String> temp = new ArrayList<>();
					String result = data.substring(data.length() - 1, data.length());
					String password = data.substring(0, data.length() - 1).replace("_", " ");
					temp.add(password);
					temp.add(result);
					mydata.add(temp);
				}
			}
			finalData = new String[mydata.size()][2];
			for (int i = 0; i < mydata.size(); i++) {
				finalData[i][0] = mydata.get(i).get(0);
				finalData[i][1] = mydata.get(i).get(1);
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		return finalData;
	}
	
	public void userEnterPassword(String password) {
		WebElement inputBox = driver.findElement(By.id("psw"));
		inputBox.clear();
		//waitForMilliSecond(500);
		inputBox.sendKeys(password);
		//waitForMilliSecond(500);
	}
	public void userClickOnCheckMyPassword() {
		WebElement button = driver.findElement(By.className("signupbtn"));
		button.click();
		//waitForMilliSecond(500);
	}
	public String getResult() {
		WebElement judgement = driver.findElement(By.id("msg"));
		String judgementResult = judgement.getAttribute("innerHTML").toLowerCase();
		return judgementResult;
	}
	public void waitForMilliSecond(int milliSeconds) {
		try {
			TimeUnit.MILLISECONDS.sleep(milliSeconds);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

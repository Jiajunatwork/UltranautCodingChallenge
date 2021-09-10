package qa.tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

//because of iteration 3 added user role element, you need to change the html file to indexWithAdmin.html
//for each iteration, please make sure passwordVerification.js uses the corresponding password verification method.
//for iteration 3, passwordVerification.js should use iterationThreePasswordVerification(password) method
//because of iteration 3 added user role element, I must create a new set of test data, so use iteration3Data.txt as data file
public class Iteration4Test {
	WebDriver driver = null;
	char[] mySpecialChar = {'!', '@', '#', '$', '%', '^', '&', '*'};
	List<String> mySpecialCharList = Arrays.asList("!", "@", "#", "$", "%", "^", "&", "*");
	@BeforeClass
	public void startApp() {
		String projectDirectory = System.getProperty("user.dir");
		String htmlFilePath = projectDirectory+"\\src\\main\\webapp\\indexWithAdmin.html";
		System.setProperty("webdriver.chrome.driver", "chromedriver.exe");          
		driver = new ChromeDriver(); 
		driver.get(htmlFilePath);
	}
	
	@AfterClass
	public void closeApp() {
		driver.close();
	}
	@Test(dataProvider = "iteration1DataProvider")
	public void iteration4(String password, String result, String role) {
		userEnterPassword(password);
		userSelectRole(role);
		userClickOnCheckMyPassword();
		String judgementResult = getResult();
		ArrayList<String> expectedResult = judgePassword(password, role);
		if(result.toUpperCase().equals("F")) {
			System.out.println("password: "+password+" , "+judgementResult+" vs. F");
			System.out.println(password + " vs. "+judgementResult);
			assert (judgementResult.contains("invalid password")): ""+password + " vs. "+judgementResult;
			//Assert.assertTrue(judgementResult.contains("invalid password"));
			for(String itr: expectedResult) {
				assert (judgementResult.contains(itr)): ""+judgementResult+"  vs. "+itr;
				//Assert.assertTrue(judgementResult.contains(itr));
			}
		}else {
			System.out.println(password + " vs. "+judgementResult);
			assert (judgementResult.matches(".*\\bvalid password\\b.*")): ""+password + " vs. "+judgementResult;
			//Assert.assertTrue(judgementResult.matches(".*\\bvalid password\\b.*"));
			
			System.out.println("password: "+password+" , "+judgementResult+" vs. T");
		}
	}
	public ArrayList<String> judgePassword(String password, String role){
		ArrayList<String> result = new ArrayList<>();
		int numberCount = 0;
		int letterCount = 0;
		int specialCharCount = 0;
		for(char itr: password.toCharArray()) {
			if(Character.isDigit(itr)){
				numberCount++;
			}else if(Character.isLetter(itr)) {
				letterCount++;
			}else if(mySpecialCharList.contains(""+itr)){
				specialCharCount++;
			}
		}
		if(numberCount < 1) {
			result.add("must contains at least 1 number");
		}
		if(letterCount < 1) {
			result.add("must contains at least 1 letter");
		}
		if(role.toLowerCase().equals("normal")) {
			if(password.length() < 10) {
				result.add("must be at least 10 characters");
			}
		}else {
			if(password.length() < 13) {
				result.add("must be at least 13 characters");
			}
			if(specialCharCount < 3) {
				result.add("must contains at least 3 special character");
			}
		}
		return result;
	}
	public String translateTestData(String password) {
		if(password.equals("null")){
			return "";
		}else {
			return password;
		}
	}
	//test data example: [password, expected result, role]
	// role value = normal or admin, expected result = T or F
	//special char = ('!', '@', '#', '$', '%', '^', '&', or '*')
	@DataProvider(name = "iteration1DataProvider")
	public String[][] dataProvider() {
		String[][] finalData = null;
		int iter = 0;
		try {
			int dataCount = 0;
			ArrayList<ArrayList<ArrayList<String>>> mydata = new ArrayList<>();
			File myObj = new File("src/main/Resources/testdata/iteration1Data.txt");
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine().replace(" ", "").replace("|", "");
				if (data.startsWith("#") == false) {
					ArrayList<ArrayList<String>> temp = new ArrayList<>();
					String result = data.substring(data.length() - 1, data.length());
					String password = data.substring(0, data.length() - 1).replace("_", " ")+"  ";
					ArrayList<String> normalUser = new ArrayList<>();
					normalUser.add(password);
					normalUser.add(result);
					normalUser.add("normal");
					temp.add(normalUser); //test data for normal password added
					dataCount+=1;
					
					String adminPasswordWithNoSpecialChar = password + "   ";
					ArrayList<String> adminUser = new ArrayList<>();
					adminUser.add(adminPasswordWithNoSpecialChar);
					adminUser.add("F"); //admin needs 3 special char
					adminUser.add("admin");
					temp.add(adminUser);
					dataCount+=1;
					
					String adminPasswordWithOneSpecialChar = password + "  "+mySpecialChar[(iter++)%mySpecialChar.length];
					adminUser = new ArrayList<>();
					adminUser.add(adminPasswordWithOneSpecialChar);
					adminUser.add("F"); // admin needs 3 special char
					adminUser.add("admin");
					temp.add(adminUser);
					dataCount+=1;
					
					String adminPasswordWithThreeSpecialChar = password + mySpecialChar[(iter++)%mySpecialChar.length]+mySpecialChar[(iter++)%mySpecialChar.length]+mySpecialChar[(iter++)%mySpecialChar.length];
					adminUser = new ArrayList<>();
					adminUser.add(adminPasswordWithThreeSpecialChar);
					adminUser.add(result); 
					adminUser.add("admin");
					temp.add(adminUser);
					dataCount++;
					
					String adminPasswordWithMoreThanThreeSpecialChar = password.replaceFirst(" ", "") +mySpecialChar[(iter++)%mySpecialChar.length]+mySpecialChar[(iter++)%mySpecialChar.length]+mySpecialChar[(iter++)%mySpecialChar.length]+mySpecialChar[(iter++)%mySpecialChar.length];
					adminUser = new ArrayList<>();
					adminUser.add(adminPasswordWithMoreThanThreeSpecialChar);
					adminUser.add(result); 
					adminUser.add("admin");
					temp.add(adminUser);
					dataCount++;
					
					mydata.add(temp);
				}
			}
			finalData = new String[dataCount][3];
			int j = 0;
			for(ArrayList<ArrayList<String>> itr: mydata) {
				for(ArrayList<String> itr2: itr) {
					finalData[j][0] = itr2.get(0);
					finalData[j][1] = itr2.get(1);
					finalData[j][2] = itr2.get(2);
					j++;
				}
			}
			Assert.assertEquals(j, dataCount);
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
	public void userSelectRole(String role) {
		Select roleDropDown = new Select(driver.findElement(By.id("roleSelection")));
		roleDropDown.selectByVisibleText(role.toLowerCase());
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

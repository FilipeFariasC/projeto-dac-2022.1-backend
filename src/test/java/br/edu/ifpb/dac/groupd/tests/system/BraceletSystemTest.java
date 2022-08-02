package br.edu.ifpb.dac.groupd.tests.system;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.annotation.Testable;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;

import br.edu.ifpb.dac.groupd.business.service.interfaces.PasswordEncoderService;
import br.edu.ifpb.dac.groupd.business.service.interfaces.UserService;
import br.edu.ifpb.dac.groupd.model.entities.User;

@Testable
@DisplayName("Bracelet System Tests")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
@ActiveProfiles("test")
@Testcontainers(disabledWithoutDocker = true)
class BraceletSystemTest {

	private WebDriver driver;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoderService passEncoder;
	
	private User user = createUser();
	
	private static final String PREFIX = "http://localhost:3000";
	
	private static String buildUrl(String endpoint) {
		return String.format("%s/%s", PREFIX, endpoint);
	}
	private boolean enter = false;
	
	@BeforeEach
	@EnabledOnOs(value = OS.LINUX)
	void setUp() throws Exception {
		String path = String.format("%s/%s", System.getProperty("user.dir"), "drivers-selenium/linux/chromedriver");
		System.setProperty("webdriver.chrome.driver", path);
		driver = new ChromeDriver();
		
		if(!enter) {
			userService.save(user);
			user = createUser();
			enter=true;
		}
//		login();
	}
	private void login() {
		driver.get(buildUrl("login"));
		
		WebElement emailElement = driver.findElement(By.cssSelector("input[type='email']#inputEmail"));
		WebElement passwordElement = driver.findElement(By.cssSelector("input[type='password']#inputPassword"));
		
		emailElement.sendKeys(user.getEmail());
		passwordElement.sendKeys(user.getPassword());
		
		WebElement botao = driver.findElement(By.cssSelector("button[type='submit']"));
		botao.click();
		
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		Wait<WebDriver> fluentWait = new FluentWait<WebDriver>(driver)
				.withTimeout(Duration.of(1500, ChronoUnit.MILLIS))
			    .ignoring(NoSuchElementException.class); // this defines the exception to ignore 
		fluentWait.until(t -> {
			return t.findElement(By.cssSelector("#bracelet-dropdown"));
		});
	}
	
	private User createUser() {
		User user = new User();
		
		user.setEmail("admin@admin.com");
		user.setName("admin");
		user.setPassword("admin20221");
		
		return user;
	}
	
	@AfterEach
	void tearDown() throws Exception {
		driver.quit();
	}
	@Test
	void testRegisterBraceletInvalid() throws InterruptedException {
		login();
		driver.get(buildUrl("bracelets/create"));
		
		WebElement braceletName = driver.findElement(By.cssSelector("input[type='text'][name='braceletFormName']#braceletFormName"));
		
		WebElement botao = driver.findElement(By.cssSelector("button[type='submit']"));
		botao.click();
		
		Thread.sleep(500000);
	}
}

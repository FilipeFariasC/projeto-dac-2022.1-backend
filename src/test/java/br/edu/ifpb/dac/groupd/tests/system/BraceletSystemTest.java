package br.edu.ifpb.dac.groupd.tests.system;

import static br.edu.ifpb.dac.groupd.tests.utils.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.annotation.Testable;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;

import br.edu.ifpb.dac.groupd.business.exception.UserEmailInUseException;
import br.edu.ifpb.dac.groupd.business.exception.UserNotFoundException;
import br.edu.ifpb.dac.groupd.business.service.BraceletService;
import br.edu.ifpb.dac.groupd.business.service.interfaces.PasswordEncoderService;
import br.edu.ifpb.dac.groupd.business.service.interfaces.UserService;
import br.edu.ifpb.dac.groupd.model.entities.Bracelet;
import br.edu.ifpb.dac.groupd.model.entities.User;
import br.edu.ifpb.dac.groupd.presentation.dto.BraceletRequest;

@Testable
@DisplayName("Bracelet System Tests")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
//@ActiveProfiles("test-local")
@ActiveProfiles("test")
@Testcontainers(disabledWithoutDocker = true)
class BraceletSystemTest {

	private static WebDriver driver;
	
	@Autowired
	private BraceletService braceletService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoderService passEncoder;
	
	private User user = createUser();
	private User created;
	
	private Bracelet bracelet = validBracelet();
	
	private static final String PREFIX = "http://localhost:3000";
	
	private Bracelet validBracelet() {
		Bracelet bracelet = new Bracelet();
		bracelet.setName("ABC");
		
		return bracelet;
	}
	
	private boolean userExists() {
		return getUser() != null;
	}
	
	private User getUser() {
		try {
			return this.userService.findByEmail(user.getEmail());
		} catch (UserNotFoundException e) {
			return null;
		}
	}
	@BeforeAll
	static void setUpClass() throws Exception {
		String path = "";
		
		if(os("Linux")) {
			path = String.format("%s/%s", System.getProperty("user.dir"), "drivers-selenium/linux/chromedriver");
		} else if (os("Windows")) {
			path = String.format("%s/%s", System.getProperty("user.dir"), "drivers-selenium\\windows\\chromedriver.exe");
		}
		System.setProperty("webdriver.chrome.driver", path);
	}
	
	@BeforeEach
	void setUp() throws UserEmailInUseException {
		driver = new ChromeDriver();
		if(!userExists()) {
			this.created= userService.save(user);
		}
		user = createUser();
		bracelet = validBracelet();
//		login();
	}
	private void login() {
		driver.get(buildFrontendUrl("login"));
		
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
	@AfterAll
	static void tearDownClass() throws Exception {
		driver.quit();
	}
	@Test
	void testRegisterBraceletInvalid() throws InterruptedException {
		login();
		driver.get(buildFrontendUrl("bracelets/create"));
		
		WebElement braceletName = driver.findElement(By.cssSelector("input[type='text'][name='braceletFormName']#braceletFormName"));
		
		WebElement botao = driver.findElement(By.cssSelector("button[type='submit']"));
		botao.click();
		
		List<WebElement> toastMessages = new WebDriverWait(driver, Duration.ofMillis(500).toMillis())
				.until(t->t.findElements(By.cssSelector(".toast")));
		
		assertThat(toastMessages).isNotEmpty();
		toastMessages.stream().forEach(t->assertThat(t.getText()).contains("Erro"));
	}
	
	@Test
	void testRegisterBraceletValid() throws InterruptedException {
		login();
		driver.get(buildFrontendUrl("bracelets/create"));
		
		WebElement braceletName = driver.findElement(By.cssSelector("input[type='text'][name='braceletFormName']#braceletFormName"));
		braceletName.sendKeys(bracelet.getName());
		
		WebElement botao = driver.findElement(By.cssSelector("button[type='submit']"));
		botao.click();
		
		WebElement toastSuccess = new WebDriverWait(driver, Duration.ofMillis(500).toMillis())
				.until(t->t.findElement(By.cssSelector(".toast")));
		
		assertThat(toastSuccess.getText()).containsSequence("Sucesso");
		
		assertThat(driver.getCurrentUrl()).isEqualTo(buildFrontendUrl("bracelets"));
	}
	private void registerBracelet() throws UserNotFoundException {
		BraceletRequest bracelet = new BraceletRequest();
		bracelet.setName(this.bracelet.getName());
		this.bracelet = braceletService.createBracelet(getUser().getId(), bracelet);
	}
	
	@Test
	void testListBraceletRegistered() throws InterruptedException, UserNotFoundException {
		login();
		testRegisterBraceletValid();
		
		List<WebElement> braceletDetailsButton = driver.findElements(By.cssSelector("a.bracelets"));
		assertThat(braceletDetailsButton).isNotEmpty();
		
		WebElement lastBracelet = braceletDetailsButton.get(braceletDetailsButton.size()-1);
		
		assertThat(lastBracelet.getText()).isEqualTo(bracelet.getName());
		
		lastBracelet.click();
		
		WebElement braceletNameElement = driver.findElement(By.cssSelector(".bracelet-name"));
		
		assertThat(bracelet.getName()).isEqualTo(braceletNameElement.getText());
	}
	
	@Test
	void testListBraceletAlreadyRegistered() throws InterruptedException, UserNotFoundException {
		login();
		registerBracelet();
		
		driver.get(buildFrontendUrl(String.format("bracelets/%d", bracelet.getId())));
		
		WebElement braceletNameElement = driver.findElement(By.cssSelector(".bracelet-name"));
		
		assertThat(bracelet.getName()).isEqualTo(braceletNameElement.getText());
	}
}

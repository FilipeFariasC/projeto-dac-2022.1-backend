package br.edu.ifpb.dac.groupd.tests.system;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.annotation.Testable;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.html5.LocalStorage;
import org.openqa.selenium.html5.WebStorage;
import org.openqa.selenium.remote.Augmenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;

import br.edu.ifpb.dac.groupd.business.exception.UserNotFoundException;
import br.edu.ifpb.dac.groupd.business.service.interfaces.PasswordEncoderService;
import br.edu.ifpb.dac.groupd.business.service.interfaces.UserService;
import br.edu.ifpb.dac.groupd.model.entities.User;

@Testable
@DisplayName("User System Tests")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
@ActiveProfiles("test")
@Testcontainers(disabledWithoutDocker = true)
class UserSystemTest {

	private WebDriver driver;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoderService passEncoder;
	
	private User user;
	
	private static final String PREFIX = "http://localhost:3000";
	
	private static String buildUrl(String endpoint) {
		return String.format("%s/%s", PREFIX, endpoint);
	}
	
	@BeforeEach
	@EnabledOnOs(value = OS.LINUX)
	void setUp() throws Exception {
		String path = String.format("%s/%s", System.getProperty("user.dir"), "drivers-selenium/linux/chromedriver");
		System.setProperty("webdriver.chrome.driver", path);
		driver = new ChromeDriver();
	}
	
	@AfterEach
	void tearDown() {
		driver.quit();
	}
	@Test
	@Order(1)
	void testInvalidRegister() throws InterruptedException {
		driver.get(buildUrl("signIn"));
		
		WebElement botao = driver.findElement(By.cssSelector("button[type='submit']"));
		botao.click();

		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		
		List<WebElement> toastMessages = driver.findElements(By.cssSelector(".toast"));
		
		assertThat(toastMessages).isNotEmpty();
		toastMessages.stream().forEach(t->assertThat(t.getText()).contains("Erro"));

		driver.manage().timeouts().implicitlyWait(5, TimeUnit.MINUTES);
	}
	@Test
	@Order(2)
	void testValidRegister() throws InterruptedException {
		driver.get(buildUrl("signIn"));
		
		String nome = "ADMIN";
		String email = "admin@admin.com";
		String password = "admin20221";

		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		
		WebElement nomeElement = driver.findElement(By.cssSelector("input[type='text']#inputName"));
		WebElement emailElement = driver.findElement(By.cssSelector("input[type='email']#inputEmail"));
		WebElement passwordElement = driver.findElement(By.cssSelector("input[type='password']#inputPassword"));
		
		nomeElement.sendKeys(nome);
		emailElement.sendKeys(email);
		passwordElement.sendKeys(password);
		

		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		
		WebElement botao = driver.findElement(By.cssSelector("button[type='submit']"));
		botao.click();

		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		
		List<WebElement> toastMessages = driver.findElements(By.cssSelector(".toast"));
		
		assertThat(toastMessages).isNotEmpty();
		toastMessages.stream().forEach(t->assertThat(t.getText()).contains("Sucesso"));
		
		try {
			user = userService.findByEmail(email);
			assertAll(
				()->assertEquals(user.getEmail(), email),
				()->assertEquals(user.getName(), nome),
				()->assertTrue(passEncoder.matches(password, user.getPassword()))
			);
		} catch (UserNotFoundException exception) {
			fail("Usuário não foi cadastrado");
		}
	}
	@Test
	@Order(3)
	void testInvalidLogin() throws InterruptedException {
		driver.get(buildUrl("login"));
		
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		
		WebElement botao = driver.findElement(By.cssSelector("button[type='submit']"));
		botao.click();

		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		
		List<WebElement> toastMessages = driver.findElements(By.cssSelector(".toast"));
		
		assertThat(toastMessages).isNotEmpty();
		
		toastMessages.stream().forEach(t->assertThat(t.getText()).contains("Erro"));

		driver.manage().timeouts().implicitlyWait(5, TimeUnit.MINUTES);
		
		WebStorage storage = (WebStorage) new Augmenter().augment(driver);;
		LocalStorage localStorage = storage.getLocalStorage();
		String token = localStorage.getItem("token");
		assertNull(token);
	}
	
	
	@Test
	@Order(4)
	void testValidLogin() throws InterruptedException {
		String email = "admin@admin.com";
		String password = "admin20221";
		
		try {
			user = userService.findByEmail(email);
		} catch (UserNotFoundException exception) {
			testValidRegister();
		}

		driver.get(buildUrl("login"));
		
		WebElement emailElement = driver.findElement(By.cssSelector("input[type='email']#inputEmail"));
		WebElement passwordElement = driver.findElement(By.cssSelector("input[type='password']#inputPassword"));
		
		emailElement.sendKeys(email);
		passwordElement.sendKeys(password);
		
		driver.manage().timeouts().implicitlyWait(5000, TimeUnit.MILLISECONDS);
		
		WebElement botao = driver.findElement(By.cssSelector("button[type='submit']"));
		botao.click();

		driver.manage().timeouts().implicitlyWait(5000, TimeUnit.MILLISECONDS);
		
		List<WebElement> toastMessages = driver.findElements(By.cssSelector(".toast"));
		
		assertThat(toastMessages).isNotEmpty();
		
		toastMessages.stream().forEach(t->assertThat(t.getText()).contains("Sucesso"));
		
		WebStorage storage = (WebStorage) new Augmenter().augment(driver);;
		LocalStorage localStorage = storage.getLocalStorage();
		String token = localStorage.getItem("token");
		assertNotNull(token);
	}
	
	@Test
	@Order(5)
	void testLogout() throws InterruptedException {
		testValidLogin();
		
		driver.get(buildUrl(""));
		
		WebElement toggleDropdown = driver.findElement(By.cssSelector(".navbar-nav .nav-item.dropdown .dropdown-toggle[role='button']#options-dropdown"));
		toggleDropdown.click();

		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		
		WebElement logoutButton = driver.findElement(By.cssSelector("button[type='reset']#logout"));
		logoutButton.click();
		
		WebStorage storage = (WebStorage) new Augmenter().augment(driver);;
		LocalStorage localStorage = storage.getLocalStorage();
		String token = localStorage.getItem("token");
		assertNull(token);
	}
}

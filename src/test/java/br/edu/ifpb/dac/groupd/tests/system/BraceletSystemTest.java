package br.edu.ifpb.dac.groupd.tests.system;

import static br.edu.ifpb.dac.groupd.tests.utils.TestUtils.buildFrontendUrl;
import static br.edu.ifpb.dac.groupd.tests.utils.TestUtils.os;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;

import br.edu.ifpb.dac.groupd.business.exception.UserEmailInUseException;
import br.edu.ifpb.dac.groupd.business.exception.UserNotFoundException;
import br.edu.ifpb.dac.groupd.business.service.BraceletService;
import br.edu.ifpb.dac.groupd.business.service.interfaces.UserService;
import br.edu.ifpb.dac.groupd.model.entities.Bracelet;
import br.edu.ifpb.dac.groupd.model.entities.User;
import br.edu.ifpb.dac.groupd.presentation.dto.BraceletRequest;
import br.edu.ifpb.dac.groupd.tests.system.pages.bracelet.BraceletRegisterPage;
import br.edu.ifpb.dac.groupd.tests.system.pages.bracelet.BraceletUpdatePage;
import br.edu.ifpb.dac.groupd.tests.system.pages.user.LoginPage;

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
	
	private User user = createUser();
	
	private Bracelet bracelet = validBracelet();
	
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
			userService.save(user);
		}
		user = createUser();
		bracelet = validBracelet();
//		login();
	}
	@AfterEach
	void teardown() throws Exception {
		driver.close();
	}
	@AfterAll
	static void tearDownClass() throws Exception {
		driver.quit();
	}
	void login() throws InterruptedException {
		LoginPage login = new LoginPage(driver);
		
		login.email(user.getEmail());
		login.password(user.getPassword());
		
		login.submit();
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		
		assertTrue(login.hasToastrSuccess());
		assertEquals(driver.getCurrentUrl(), buildFrontendUrl("profile"));
		
		assertNotNull(login.getToken());
	}
	
	private User createUser() {
		User user = new User();
		
		user.setEmail("admin@admin.com");
		user.setName("admin");
		user.setPassword("admin20221");
		
		return user;
	}
	
	@Test
	void testRegisterBraceletInvalid() throws InterruptedException{
		login();
		BraceletRegisterPage register = new BraceletRegisterPage(driver);
		
		register.submit();
		
		register.sleepWait();
		
		assertTrue(register.hasToastrErrors());
	}
	
	@Test
	void testRegisterBraceletValid() throws InterruptedException {
		login();
		BraceletRegisterPage register = new BraceletRegisterPage(driver);
		
		register.name(bracelet.getName());
		
		assertTrue(register.nameDoesNotHasErrors());
		
		register.submit();
		
		register.sleepWait();
		
		assertTrue(register.hasToastrSuccess());
		
		assertThat(driver.getCurrentUrl()).isEqualTo(buildFrontendUrl("bracelets"));
	}
	
	private void registerBracelet() throws UserNotFoundException {
		BraceletRequest bracelet = new BraceletRequest();
		bracelet.setName(this.bracelet.getName());
		this.bracelet = braceletService.createBracelet(getUser().getId(), bracelet);
	}
	
	@Test
	void testListBraceletRegistered() throws InterruptedException {
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
	
	@Test
	void testEditBraceletRegisteredInvalid() throws InterruptedException, UserNotFoundException {
		login();
		registerBracelet();
		
		BraceletUpdatePage update = new BraceletUpdatePage(driver, bracelet.getId());
		
		update.name(" ");
		
		assertTrue(update.nameHasErrors());
		
		update.submit();
		
		assertTrue(update.hasToastrErrors());
		
		update.name("");
		
		assertTrue(update.nameHasErrors());
		
		update.submit();
		
		assertTrue(update.hasToastrErrors());
		
	}
	@Test
	void testEditBraceletRegisteredValid() {}
	@Test
	void testDeleteBraceletRegistered() {}
	@Test
	void testBraceletRegisteredDoesNotHaveFences() {}
	@Test
	void testBraceletRegisteredHaveFences() {}
}

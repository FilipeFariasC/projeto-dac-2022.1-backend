package br.edu.ifpb.dac.groupd.tests.system;

import static br.edu.ifpb.dac.groupd.tests.utils.TestUtils.buildFrontendUrl;
import static br.edu.ifpb.dac.groupd.tests.utils.TestUtils.os;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.annotation.Testable;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
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
import br.edu.ifpb.dac.groupd.tests.system.pages.NavbarObject;
import br.edu.ifpb.dac.groupd.tests.system.pages.user.LoginPage;
import br.edu.ifpb.dac.groupd.tests.system.pages.user.UserProfilePage;
import br.edu.ifpb.dac.groupd.tests.system.pages.user.UserRegisterPage;
import br.edu.ifpb.dac.groupd.tests.system.pages.user.UserUpdatePage;

@Testable
@DisplayName("User System Tests")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@TestMethodOrder(OrderAnnotation.class)
@ActiveProfiles("test")
@Testcontainers(disabledWithoutDocker = true)
class UserSystemTest {

	private static WebDriver driver;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoderService passEncoder;
	
	private User user;
	
	
	@BeforeAll
	static void setUpClass() {
		String path = "";
		
		if(os("Linux")) {
			path = String.format("%s/%s", System.getProperty("user.dir"), "drivers-selenium/linux/chromedriver");
		} else if (os("Windows")) {
			path = String.format("%s/%s", System.getProperty("user.dir"), "drivers-selenium\\windows\\chromedriver.exe");
		}
		System.setProperty("webdriver.chrome.driver", path);
	}
	
	@BeforeEach
	void setUp() throws Exception {
		driver = new ChromeDriver();
	}
	
	@AfterEach
	void tearDown() {
		driver.quit();
	}
	@Test
	@Order(1)
	void testInvalidRegister() throws InterruptedException {
		UserRegisterPage registerUser = new UserRegisterPage(driver);
		
		registerUser.submit();

		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		
		assertTrue(registerUser.nameHasErrors());
		assertTrue(registerUser.emailHasErrors());
		assertTrue(registerUser.passwordHasErrors());

		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		
		assertTrue(registerUser.hasToastrErrors());
	}
	@Test
	@Order(2)
	void testValidRegister() throws InterruptedException {
		
		
		String nome = "ADMIN";
		String email = "admin@admin.com";
		String password = "admin20221";

		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		
		UserRegisterPage registerUser = new UserRegisterPage(driver);

		registerUser.name(nome);
		registerUser.email(email);
		registerUser.password(password);

		assertTrue(registerUser.nameDoesNotHasErrors());
		assertTrue(registerUser.emailDoesNotHasErrors());
		assertTrue(registerUser.passwordDoesNotHasErrors());

		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		
		registerUser.submit();

		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		
		assertTrue(registerUser.hasToastrSuccess());
		
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
		LoginPage login = new LoginPage(driver);
		
		login.submit();
		
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		
		assertTrue(login.emailHasErrors());
		assertTrue(login.passwordHasErrors());

		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		
		assertTrue(login.hasToastrErrors());
		
		assertNull(login.getToken());
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

		LoginPage login = new LoginPage(driver);
		
		login.email(email);
		login.password(password);
		
		login.submit();
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		
		assertTrue(login.hasToastrSuccess());
		assertEquals(driver.getCurrentUrl(), buildFrontendUrl("profile"));
		
		assertNotNull(login.getToken());
	}
	
	@Test
	@Order(5)
	void testLogout() throws InterruptedException {
		testValidLogin();
		
		NavbarObject navbar = new NavbarObject(driver);
		
		navbar.logout();
		
		assertNull(navbar.getToken());
	}
	@Test
	@Order(6)
	void testUserProfile() throws InterruptedException {
		testValidLogin();
		
		UserProfilePage profile = new UserProfilePage(driver);
		
		assertEquals(profile.userName(), user.getName());
		
		assertEquals(profile.userEmail(), user.getEmail());
	}
	
	@Test
	@Order(7)
	void testUserInvalidUpdate() throws InterruptedException {
		testValidLogin();
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		UserUpdatePage update = new UserUpdatePage(driver);
		
		update.clearName();
		
		assertTrue(update.nameHasErrors());
		
		update.submit();
		driver.manage().timeouts().implicitlyWait(1500, TimeUnit.MILLISECONDS);
		
		assertTrue(update.hasToastrErrors());
	}
	@Test
	@Order(7)
	void testUserValidUpdate() throws InterruptedException {
		testValidLogin();
		UserUpdatePage update = new UserUpdatePage(driver);
		String newName= user.getName()+"-UPDATE";
		update.name(newName);
		
		assertTrue(update.nameDoesNotHasErrors());
		
		update.submit();
		driver.manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		assertTrue(update.hasToastrSuccess());
		
		UserProfilePage profile = new UserProfilePage(driver);
		
		assertEquals(profile.userName(), newName);
		
		assertEquals(profile.userEmail(), user.getEmail());
	}
}

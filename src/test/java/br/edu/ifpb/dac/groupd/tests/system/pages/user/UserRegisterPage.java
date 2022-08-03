package br.edu.ifpb.dac.groupd.tests.system.pages.user;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import br.edu.ifpb.dac.groupd.tests.system.pages.AbstractPageToastr;
import br.edu.ifpb.dac.groupd.tests.utils.TestUtils;

public class UserRegisterPage extends AbstractPageToastr{
	
	@FindBy(how=How.CSS, css = "form input[type='text']#inputName")
	private WebElement inputName;
	
	@FindBy(how=How.CSS, css = "form input[type='email']#inputEmail")
	private WebElement inputEmail;
	
	@FindBy(how=How.CSS, css = "form input[type='password']#inputPassword")
	private WebElement inputPassword;
	
	@FindBy(how=How.CSS, css = "form button[type='submit']")
	private WebElement registerButton;
	
	public UserRegisterPage(WebDriver driver) {
		super(driver);
		
		driver.get(TestUtils.buildFrontendUrl("signIn"));
		
		PageFactory.initElements(driver, this);
	}
	public void name(String name) {
		this.inputName.sendKeys(name);
	}
	public boolean nameHasErrors() {
		return this.inputName.getAttribute("class").contains("is-invalid") || !nameDoesNotHasErrors();
	}
	public boolean nameDoesNotHasErrors() {
		return this.inputName.getAttribute("class").contains("is-valid");
	}
	
	public void email(String email) {
		this.inputEmail.sendKeys(email);
	}
	public boolean emailHasErrors() {
		return this.inputEmail.getAttribute("class").contains("is-invalid") || !emailDoesNotHasErrors();
	}
	public boolean emailDoesNotHasErrors() {
		return this.inputEmail.getAttribute("class").contains("is-valid");
	}
	
	public void password(String password) {
		this.inputPassword.sendKeys(password);
	}
	public boolean passwordHasErrors() {
		return this.inputPassword.getAttribute("class").contains("is-invalid") || !passwordDoesNotHasErrors();
	}
	public boolean passwordDoesNotHasErrors() {
		return this.inputPassword.getAttribute("class").contains("is-valid");
	}
	
	public void submit() {
		registerButton.click();
	}
}

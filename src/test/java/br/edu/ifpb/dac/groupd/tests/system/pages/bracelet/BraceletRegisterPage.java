package br.edu.ifpb.dac.groupd.tests.system.pages.bracelet;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import br.edu.ifpb.dac.groupd.tests.system.pages.AbstractPageToastr;
import br.edu.ifpb.dac.groupd.tests.utils.TestUtils;

public class BraceletRegisterPage extends AbstractPageToastr{
	
	@FindBy(how=How.CSS, css = "form input[type='text']#braceletFormName")
	private WebElement inputName;
	
	@FindBy(how=How.CSS, css = "form button[type='submit']")
	private WebElement registerButton;
	
	public BraceletRegisterPage(WebDriver driver) {
		super(driver);
		
		driver.get(TestUtils.buildFrontendUrl("bracelets/create"));
		
		PageFactory.initElements(driver, this);
	}
	public void name(String name) {
		this.clearName();
		this.inputName.sendKeys(name);
	}
	public boolean nameHasErrors() {
		return this.inputName.getAttribute("class").contains("is-invalid") || !nameDoesNotHasErrors();
	}
	public boolean nameDoesNotHasErrors() {
		return this.inputName.getAttribute("class").contains("is-valid");
	}
	
	public void submit() {
		registerButton.click();
	}
	public void clearName() {
		this.inputName.clear();
		this.inputName.sendKeys(Keys.chord(Keys.CONTROL, "a"));
		this.inputName.sendKeys(Keys.BACK_SPACE);
		this.getDriver().manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
	}
}
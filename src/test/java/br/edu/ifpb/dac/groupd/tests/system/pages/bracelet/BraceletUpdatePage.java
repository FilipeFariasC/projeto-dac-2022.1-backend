package br.edu.ifpb.dac.groupd.tests.system.pages.bracelet;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import br.edu.ifpb.dac.groupd.tests.system.pages.AbstractPageToastr;
import br.edu.ifpb.dac.groupd.tests.utils.TestUtils;

public class BraceletUpdatePage extends AbstractPageToastr{
	
	@FindBy(how=How.CSS, css = "form input[type='text']#braceletFormName")
	private WebElement inputName;
	
	@FindBy(how=How.CSS, css = "form button[type='submit']")
	private WebElement registerButton;
	
	public BraceletUpdatePage(WebDriver driver, Long id) {
		super(driver);
		
		driver.get(TestUtils.buildFrontendUrl(String.format("bracelets/update/%d", id)));
		
		PageFactory.initElements(driver, this);
	}
	public String name() {
		return this.inputName.getText();
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
		sleepWait();
	}
	public void clearName() {
		this.inputName.clear();
		this.inputName.sendKeys(Keys.chord(Keys.CONTROL, "a"));
		this.inputName.sendKeys(Keys.BACK_SPACE);
		this.sleepWait();
	}
}
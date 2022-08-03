package br.edu.ifpb.dac.groupd.tests.system.pages;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import br.edu.ifpb.dac.groupd.tests.utils.TestUtils;

public class NavbarObject extends AbstractPageToastr{
	
	@FindBy(how=How.CSS, css = ".navbar-nav .nav-item.dropdown .dropdown-toggle[role='button']#options-dropdown")
	private WebElement optionsDropdown;
	
	@FindBy(how=How.CSS, css = "button[type='reset']#logout")
	private WebElement logoutButton;
	
	public NavbarObject(WebDriver driver) {
		super(driver);
		
		driver.get(TestUtils.buildFrontendUrl(""));
		
		PageFactory.initElements(driver, this);
	}
	
	public void openOptionsDropdown() {
		this.optionsDropdown.click();
	}
	
	public void logout() {
		this.optionsDropdown.click();
		this.getDriver().manage().timeouts().implicitlyWait(500, TimeUnit.MILLISECONDS);
		this.logoutButton.click();
	}
}

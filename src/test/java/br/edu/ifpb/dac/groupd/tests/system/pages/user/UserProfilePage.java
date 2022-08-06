package br.edu.ifpb.dac.groupd.tests.system.pages.user;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import br.edu.ifpb.dac.groupd.tests.system.pages.AbstractPageToastr;

public class UserProfilePage extends AbstractPageToastr{
	
	@FindBy(how=How.CSS,css = "#user-name")
	private WebElement userName;
	
	@FindBy(how=How.CSS,css = "#user-email")
	private WebElement userEmail;
	
	public UserProfilePage(WebDriver driver) {
		super(driver);
		
		PageFactory.initElements(driver, this);
	}
	
	public String userName() {
		return userName.getText();
	}
	public String userEmail() {
		return userEmail.getText();
	}
}

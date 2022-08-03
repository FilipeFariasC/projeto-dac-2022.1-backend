package br.edu.ifpb.dac.groupd.tests.system.pages;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.html5.LocalStorage;
import org.openqa.selenium.html5.WebStorage;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

public class AbstractPageToastr {

	private final WebDriver driver;
	
	@FindBy(how=How.CSS,css = ".toast.toast-error")
	private List<WebElement> toastrErrors;
	
	@FindBy(how=How.CSS,css = ".toast.toast-success")
	private List<WebElement> toastrSuccess;
	
	public AbstractPageToastr(WebDriver driver) {
		this.driver = driver;
		
		PageFactory.initElements(driver, this);
	}
	
	protected WebDriver getDriver() {
		return driver;
	}
	
	public boolean hasToastrErrors() {
		return !toastrErrors.isEmpty();
	}
	public boolean hasToastrSuccess() {
		return !toastrSuccess.isEmpty();
	}
	
	public String getToken() {
		WebStorage storage = (WebStorage) new Augmenter().augment(driver);;
		LocalStorage localStorage = storage.getLocalStorage();
		
		return localStorage.getItem("token");
	}
}

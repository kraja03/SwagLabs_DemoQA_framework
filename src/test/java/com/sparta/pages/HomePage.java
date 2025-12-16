package com.sparta.pages;


import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.annotations.DefaultUrl;
import net.thucydides.core.pages.PageObject;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@DefaultUrl("https://www.saucedemo.com/")
public class HomePage extends PageObject {

    @FindBy(name = "user-name")
    private WebElementFacade userNameField;
    @FindBy(name = "password")

    private WebElementFacade passwordField;
    @FindBy(id = "login-button")

    private WebElementFacade loginButton;
    @FindBy(className = "error-message-container")

    private WebElementFacade errorMessage;

    public void enterUserName(String username) {
        userNameField.type(username);
    }

    public void enterPassword(String password) {
        passwordField.type(password);
    }

    public void clickLoginButton() {
        loginButton.click();
    }

    public String getErrorMessage() {
        return errorMessage.getText();
    }

    public boolean isErrorMessageDisplayed() {
        return errorMessage.isDisplayed();
    }

    public boolean isUserNameFieldDisplayed() {
        return userNameField.isDisplayed();
    }


}
package com.sparta.simpletests;

import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.thucydides.core.annotations.Managed;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.Select;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@ExtendWith(SerenityJUnit5Extension.class)
public class SwagLabsSerenityTests {

    @Managed
    WebDriver webDriver;
    private static final String BASE_URL = "https://www.saucedemo.com/";

    @Test
    @DisplayName("Check that the webdriver works")
    public void checkWebDriver() {
        webDriver.get(BASE_URL);
        Assertions.assertEquals("https://www.saucedemo.com/", webDriver.getCurrentUrl());
        Assertions.assertEquals("Swag Labs", webDriver.getTitle());
    }

    @Test
    @DisplayName("Given I enter a valid username and password, when I click login, then I should land on the inventory page")
    public void successfulLogin() throws InterruptedException {
        webDriver.get(BASE_URL);

        WebElement userNameField = webDriver.findElement(By.name("user-name"));
        WebElement passwordField = webDriver.findElement(By.name("password"));
        WebElement loginButton = webDriver.findElement(By.id("login-button"));

        userNameField.sendKeys("standard_user");
        passwordField.sendKeys("secret_sauce");
        loginButton.click();
        Thread.sleep(500);
        Assertions.assertEquals(webDriver.getCurrentUrl(), BASE_URL + "inventory.html");

    }

    @Test
    @DisplayName("Given I am logged in, when I view the inventory page, I should see the correct number of products")
    public void checkNumberOfProductsOnInventoryPage() {
        webDriver.get(BASE_URL);

        WebElement userNameField = webDriver.findElement(By.name("user-name"));
        WebElement passwordField = webDriver.findElement(By.name("password"));
        WebElement loginButton = webDriver.findElement(By.id("login-button"));
        loginButton.click();

        userNameField.sendKeys("standard_user");
        passwordField.sendKeys("secret_sauce", Keys.ENTER);

        List<WebElement> products = webDriver.findElements(By.className("inventory_item"));
        int productCount = products.size();
        Assertions.assertEquals(productCount, 6);

    }

    @Test
    @DisplayName("Given I enter a valid username and an invalid password, when I click login, then I should see an error message containing 'Epic sadface'")
    public void unsuccessfulLoginTest_InvalidPassword() {
        webDriver.get(BASE_URL);
        WebElement usernameField = webDriver.findElement(By.name("user-name"));
        WebElement passwordField = webDriver.findElement(By.name("password"));
        WebElement loginButton = webDriver.findElement(By.id("login-button"));
        usernameField.sendKeys("standard_user");
        passwordField.sendKeys("invalid");
        loginButton.click();
        WebElement alert = webDriver.findElement(By.className("error-message-container"));
        Assertions.assertTrue(alert.getText().contains("Epic sadface"));
    }

    @Test
    @DisplayName("Web scraping demo")
    public void retrieveProductInfo() throws IOException {
        // Navigate to the sauce labs demo site
        webDriver.get("https://www.saucedemo.com");

        WebElement usernameField = webDriver.findElement(By.id("user-name"));
        usernameField.sendKeys("standard_user");

        WebElement passwordField = webDriver.findElement(By.id("password"));
        passwordField.sendKeys("secret_sauce", Keys.ENTER);

        List<WebElement> products = webDriver.findElements(By.className("inventory_item"));

        try (PrintWriter writer = new PrintWriter(new FileWriter("inventoryItems.txt"))) {
            for (WebElement product : products) {
                WebElement nameElement = product.findElement(By.className("inventory_item_name"));
                WebElement priceElement = product.findElement(By.className("inventory_item_price"));
                String productInfo = nameElement.getText() + ": " + priceElement.getText();
                writer.println(productInfo);
                System.out.println(productInfo);
            }
        }
        Assertions.assertEquals(products.size(), 6);
    }

    @Test
    @DisplayName("Given I am logged in as a registered user, when I add product to cart, the cart updates")
    public void addProductToCart() {
        webDriver.get(BASE_URL);

        WebElement usernameField = webDriver.findElement(By.id("user-name"));
        WebElement passwordField = webDriver.findElement(By.id("password"));
        WebElement loginButton = webDriver.findElement(By.id("login-button"));

        usernameField.sendKeys("standard_user");
        passwordField.sendKeys("secret_sauce");
        loginButton.click();

        WebElement addToCartButton = webDriver.findElement(By.id("add-to-cart-sauce-labs-backpack"));
        addToCartButton.click();

        WebElement shoppingCart = webDriver.findElement(By.className("shopping_cart_badge"));
        Assertions.assertEquals("1", shoppingCart.getText());
    }

    @Test
    @DisplayName("Given I am logged in and added a product to cart, when I remove a product, the cart should be empty")
    public void removeProductFromCart() {
        webDriver.get(BASE_URL);
        WebElement usernameField = webDriver.findElement(By.id("user-name"));
        WebElement passwordField = webDriver.findElement(By.id("password"));
        WebElement loginButton = webDriver.findElement(By.id("login-button"));

        usernameField.sendKeys("standard_user");
        passwordField.sendKeys("secret_sauce");
        loginButton.click();

        WebElement addToCartButton = webDriver.findElement(By.id("add-to-cart-sauce-labs-backpack"));
        addToCartButton.click();
        WebElement removeFromCartButton = webDriver.findElement(By.id("remove-sauce-labs-backpack"));
        removeFromCartButton.click();

        List<WebElement> shoppingCartBadges = webDriver.findElements(By.className("shopping_cart_badge"));
        Assertions.assertTrue(shoppingCartBadges.isEmpty(), "Cart should be empty after removing the product");
    }

    @Test
    @DisplayName("Given I am logged in, when I sort products by price low to high, then the first product should be the cheapest")
    public void priceLowToHigh() {
        webDriver.get(BASE_URL);
        WebElement usernameField = webDriver.findElement(By.id("user-name"));
        WebElement passwordField = webDriver.findElement(By.id("password"));
        WebElement loginButton = webDriver.findElement(By.id("login-button"));

        usernameField.sendKeys("standard_user");
        passwordField.sendKeys("secret_sauce");
        loginButton.click();

        WebElement sortProductDropdown = webDriver.findElement(By.className("product_sort_container"));
        Select select = new Select(sortProductDropdown);
        select.selectByVisibleText("Price (low to high)");

        List<WebElement> productPrices = webDriver.findElements(By.className("inventory_item_price"));
        String firstPriceText = productPrices.get(0).getText();
        Assertions.assertEquals("$7.99", firstPriceText, "First product should have the lowest price");
    }

    @Test
    @DisplayName("Given I am logged in, when I click logout, then I should land on the login page")
    public void successfulLogout()  {
        webDriver.get(BASE_URL);

        WebElement userNameField = webDriver.findElement(By.name("user-name"));
        WebElement passwordField = webDriver.findElement(By.name("password"));
        WebElement loginButton = webDriver.findElement(By.id("login-button"));

        userNameField.sendKeys("standard_user");
        passwordField.sendKeys("secret_sauce");
        loginButton.click();

        WebElement menuButton = webDriver.findElement(By.id("react-burger-menu-btn"));
        menuButton.click();
        WebElement logoutLink = webDriver.findElement(By.id("logout_sidebar_link"));
        logoutLink.click();

        Assertions.assertEquals(BASE_URL, webDriver.getCurrentUrl());

    }
    @Test
    @DisplayName("Given I am logged in, when I click menu and all items, then I should land on the inventory page")
    public void allItems()  {
        webDriver.get(BASE_URL);

        WebElement userNameField = webDriver.findElement(By.name("user-name"));
        WebElement passwordField = webDriver.findElement(By.name("password"));
        WebElement loginButton = webDriver.findElement(By.id("login-button"));

        userNameField.sendKeys("standard_user");
        passwordField.sendKeys("secret_sauce");
        loginButton.click();

        WebElement menuButton = webDriver.findElement(By.id("react-burger-menu-btn"));
        menuButton.click();
        WebElement allItemsLink = webDriver.findElement(By.id("inventory_sidebar_link"));
        allItemsLink.click();

        Assertions.assertEquals(webDriver.getCurrentUrl(), BASE_URL + "inventory.html");

    }
    @Test
    @DisplayName("Given I am logged in, when I sort products by Name (Z to A), then the first product should be the red T Shirt")
    public void nameZToA() {
        webDriver.get(BASE_URL);
        WebElement usernameField = webDriver.findElement(By.id("user-name"));
        WebElement passwordField = webDriver.findElement(By.id("password"));
        WebElement loginButton = webDriver.findElement(By.id("login-button"));

        usernameField.sendKeys("standard_user");
        passwordField.sendKeys("secret_sauce");
        loginButton.click();

        WebElement sortDropdown = webDriver.findElement(By.className("product_sort_container"));
        Select select = new Select(sortDropdown);
        select.selectByVisibleText("Name (Z to A)");

        List<WebElement> productName = webDriver.findElements(By.className("inventory_item_name"));
        String firstNameZAText = productName.get(0).getText();
        Assertions.assertEquals("Test.allTheThings() T-Shirt (Red)", firstNameZAText, "First product should be Test.allTheThings() T-Shirt (Red)");
    }
    @Test
    @DisplayName("Given I am logged in as a registered user, when I add multiple products to cart, the cart updates")
    public void addMultipleProductsToCart() {
        webDriver.get(BASE_URL);

        WebElement usernameField = webDriver.findElement(By.id("user-name"));
        WebElement passwordField = webDriver.findElement(By.id("password"));
        WebElement loginButton = webDriver.findElement(By.id("login-button"));

        usernameField.sendKeys("standard_user");
        passwordField.sendKeys("secret_sauce");
        loginButton.click();

        WebElement addToCartButton = webDriver.findElement(By.id("add-to-cart-sauce-labs-backpack"));
        addToCartButton.click();
        WebElement addToCartButton1 = webDriver.findElement(By.id("add-to-cart-sauce-labs-onesie"));
        addToCartButton1.click();
        WebElement addToCartButton2 = webDriver.findElement(By.id("add-to-cart-sauce-labs-bolt-t-shirt"));
        addToCartButton2.click();
        WebElement addToCartButton3 = webDriver.findElement(By.id("add-to-cart-sauce-labs-fleece-jacket"));
        addToCartButton3.click();

        WebElement shoppingCart = webDriver.findElement(By.className("shopping_cart_badge"));
        Assertions.assertEquals("4", shoppingCart.getText());
    }

}

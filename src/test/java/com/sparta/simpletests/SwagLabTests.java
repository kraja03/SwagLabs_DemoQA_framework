package com.sparta.simpletests;


import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.util.List;

public class SwagLabTests {
    private static final String DRIVER_LOCATION = "src/test/resources/chromedriver";
    private static final String BASE_URL = "https://www.saucedemo.com/";
    private WebDriver webDriver;
    private static ChromeDriverService service;

    public static ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--headless");
        options.addArguments("--remote-allow-origins=*");

        return options;
    }

    @BeforeAll
    public static void beforeAll() throws IOException {
        service = new ChromeDriverService.Builder()
                .usingDriverExecutable(new File(DRIVER_LOCATION))
                .usingAnyFreePort()
                .build();
        service.start();
    }

    @BeforeEach
    public void setup() {
        webDriver = new RemoteWebDriver(service.getUrl(), getChromeOptions());
    }

    @AfterAll
    static void afterAll() {
        service.stop();
    }

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

        WebElement sortDropdown = webDriver.findElement(By.className("product_sort_container"));
        Select select = new Select(sortDropdown);
        select.selectByVisibleText("Price (low to high)");

        List<WebElement> productPrices = webDriver.findElements(By.className("inventory_item_price"));
        String firstPriceText = productPrices.get(0).getText();
        Assertions.assertEquals("$7.99", firstPriceText, "First product should have the lowest price");
    }

    }

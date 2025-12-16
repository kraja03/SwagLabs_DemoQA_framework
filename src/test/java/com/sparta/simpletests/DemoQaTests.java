package com.sparta.simpletests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;

public class DemoQaTests {
    private static final String DRIVER_LOCATION = "src/test/resources/chromedriver";
    private static final String BASE_URL = "https://demoqa.com/droppable/";
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
    @DisplayName("Given I am on the droppable page, when I drag the box to the drop area, then the text should change to 'Dropped!' " )
    public void dragAndDropTest(){
        webDriver.get(BASE_URL);

        WebElement dragBox = webDriver.findElement(By.id("draggable"));
        WebElement dropBox = webDriver.findElement(By.id("droppable"));
        Actions actions = new Actions(webDriver);
        actions.dragAndDrop(dragBox, dropBox).perform();
        String dropText = dropBox.getText();
        Assertions.assertEquals("Dropped!", dropText, "The drop box should display 'Dropped!'");
    }

    //Click Button to see alert
    @Test
    @DisplayName("Given I click the alert button, when the alert appears, then I should accept it")
    public void simpleAlertTest() {
        webDriver.get("https://demoqa.com/alerts");

        WebElement alertButton = webDriver.findElement(By.id("alertButton"));
        alertButton.click();

        Alert alert = webDriver.switchTo().alert();
        Assertions.assertEquals("You clicked a button", alert.getText());
        alert.accept();
    }
    // On button click, confirm box will appear - cancel
    @Test
    @DisplayName("Given I click the confirm button, when the confirm appears, then I can cancel it")
    public void confirmAlertCancelTest() {
        webDriver.get("https://demoqa.com/alerts");

        WebElement confirmButton = webDriver.findElement(By.id("confirmButton"));
        confirmButton.click();

        Alert confirm = webDriver.switchTo().alert();
        Assertions.assertEquals("Do you confirm action?", confirm.getText());
        confirm.dismiss();

        WebElement resultText = webDriver.findElement(By.id("confirmResult"));
        Assertions.assertTrue(resultText.getText().contains("Cancel"), "Confirm result should show 'Cancel'");
    }

    //On button click, confirm box will appear - ok
    @Test
    @DisplayName("Given I click the confirm button, when the confirm appears, then I can accept it")
    public void confirmAlertAcceptTest() {
        webDriver.get("https://demoqa.com/alerts");

        WebElement confirmButton = webDriver.findElement(By.id("confirmButton"));
        confirmButton.click();

        Alert confirm = webDriver.switchTo().alert();
        Assertions.assertEquals("Do you confirm action?", confirm.getText());
        confirm.accept();

        WebElement resultText = webDriver.findElement(By.id("confirmResult"));
        Assertions.assertTrue(resultText.getText().contains("Ok"), "Confirm result should show 'Ok'");
    }

//    On button click, prompt box will appear
    @Test
    @DisplayName("Given I click the prompt button, when the prompt appears, then I can enter a name")
    public void promptAlertTest() {
        webDriver.get("https://demoqa.com/alerts");

        WebElement promptButton = webDriver.findElement(By.id("promtButton"));
        promptButton.click();

        Alert prompt = webDriver.switchTo().alert();
        prompt.sendKeys("Khadijah");
        prompt.accept();

        WebElement resultText = webDriver.findElement(By.id("promptResult"));
        Assertions.assertTrue(resultText.getText().contains("Khadijah"), "Prompt result should contain entered name");
    }
    }

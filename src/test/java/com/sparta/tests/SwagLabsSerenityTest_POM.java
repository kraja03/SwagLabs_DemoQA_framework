package com.sparta.tests;

import com.sparta.pages.HomePage;
import com.sparta.pages.InventoryPage;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import net.thucydides.core.annotations.Managed;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;


@ExtendWith(SerenityJUnit5Extension.class)
public class SwagLabsSerenityTest_POM {

    @Managed
    HomePage homePage;
    InventoryPage inventoryPage;

    @Test
    @DisplayName("Given I enter a valid username and password, when I click login, then I should land on the inventory page")
    public void successfulLoginTest(){
        homePage.open();
        homePage.enterUserName("standard_user");
        homePage.enterPassword("secret_sauce");
        homePage.clickLoginButton();
        Assertions.assertEquals("https://www.saucedemo.com/inventory.html", homePage.getDriver().getCurrentUrl());
    }

    @Test
    @DisplayName("Given I enter a valid username and invalid password, when I click login, then I should see an error message")
    public void unsuccessfulLogin(){
        homePage.open();
        homePage.enterUserName("standard_user");
        homePage.enterPassword("invalid");
        homePage.clickLoginButton();
        Assertions.assertTrue(homePage.getErrorMessage().contains("Epic sadface"));
    }

    @Test
    @DisplayName("Given I am logged in, when I view the inventory page, I should see the correct number of products")
    public void checkNumberOfProductsOnInventoryPage(){
        homePage.open();
        homePage.enterUserName("standard_user");
        homePage.enterPassword("secret_sauce");
        homePage.clickLoginButton();

        int productsCount = inventoryPage.getProductsCount();
        Assertions.assertEquals(6, productsCount);

    }

    @Test
    @DisplayName("Given I am logged in as a registered user, when I add product to cart, the cart updates")
    public void addProductToCart(){
        homePage.open();
        homePage.enterUserName("standard_user");
        homePage.enterPassword("secret_sauce");
        homePage.clickLoginButton();

        inventoryPage.addFirstItemToCart();
        inventoryPage.getCartBadgeText();
        int count = Integer.parseInt(inventoryPage.getCartBadgeText());
        Assertions.assertEquals(1, count);
    }
    @Test
    @DisplayName("Given I am logged in and added a product to cart, when I remove a product, the cart should be empty")
    public void removeProductToCart() {
        homePage.open();
        homePage.enterUserName("standard_user");
        homePage.enterPassword("secret_sauce");
        homePage.clickLoginButton();

        inventoryPage.addFirstItemToCart();
        inventoryPage.removeBackpackFromCart();

        Assertions.assertFalse(inventoryPage.isCartBadgeDisplayed(), "Cart should be empty after removing the product");
    }
    @Test
    @DisplayName("Given I am logged in, when I sort products by price low to high, then the first product should be the cheapest")
    public void priceHighToLow() {
        homePage.open();
        homePage.enterUserName("standard_user");
        homePage.enterPassword("secret_sauce");
        homePage.clickLoginButton();

        inventoryPage.selectDropdown("Price (high to low)");
        Assertions.assertEquals("Price (high to low)", inventoryPage.getSelectedDropdownText());
    }

    @Test
    @DisplayName("Given I am logged in, when I click menu and logout, then I should land on the login page")
    public void successfulLogout() {
        homePage.open();
        homePage.enterUserName("standard_user");
        homePage.enterPassword("secret_sauce");
        homePage.clickLoginButton();

        inventoryPage.clickMenuButton();
        inventoryPage.clickLogout();
        Assertions.assertEquals("https://www.saucedemo.com/", homePage.getDriver().getCurrentUrl());
    }


    @Test
    @DisplayName("Given I am logged in, when I click menu and all items, then I should land on the inventory page")
    public void allItems() {
        homePage.open();
        homePage.enterUserName("standard_user");
        homePage.enterPassword("secret_sauce");
        homePage.clickLoginButton();

        inventoryPage.clickMenuButton();

        inventoryPage.clickInventoryLink();
        Assertions.assertEquals("https://www.saucedemo.com/inventory.html", homePage.getDriver().getCurrentUrl());

    }
    @Test
    @DisplayName("Given I am logged in, when I sort products by price low to high, then the first product should be the cheapest")
    public void nameZtoA() {
        homePage.open();
        homePage.enterUserName("standard_user");
        homePage.enterPassword("secret_sauce");
        homePage.clickLoginButton();

        inventoryPage.selectDropdown("Name (A to Z)");
        Assertions.assertEquals("Name (A to Z)", inventoryPage.getSelectedDropdownText());
    }
    @Test
    @DisplayName("Given I am logged in as a registered user, when I add multiple products to cart, the cart updates")
    public void addMultipleProductsToCart() {
        homePage.open();
        homePage.enterUserName("standard_user");
        homePage.enterPassword("secret_sauce");
        homePage.clickLoginButton();

        inventoryPage.addFirstItemToCart();
        inventoryPage.addProduct2ToCart();
        inventoryPage.addProduct3ToCart();
        inventoryPage.addProduct4ToCart();

        Assertions.assertTrue(inventoryPage.isCartBadgeDisplayed(), "Cart badge should be displayed");
        int count = Integer.parseInt(inventoryPage.getCartBadgeText());
        Assertions.assertEquals(4, count);

    }

}

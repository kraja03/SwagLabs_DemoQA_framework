package com.sparta.steps;

import com.sparta.pages.HomePage;
import com.sparta.pages.InventoryPage;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.thucydides.core.annotations.Managed;
import org.junit.jupiter.api.Assertions;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class InventoryStepdefs {
    @Managed
    InventoryPage inventoryPage;
    HomePage homePage;

    @Given("I am logged in as a registered user")
    public void iAmLoggedInAsARegisteredUser() {
        homePage.open();
        homePage.enterUserName("standard_user");
        homePage.enterPassword("secret_sauce");
        homePage.clickLoginButton();
        assertThat(homePage.getDriver().getCurrentUrl(), containsString("/inventory.html"));
    }

    @When("I view the inventory page")
    public void iViewTheInventoryPage() {
        assertThat(inventoryPage.getDriver().getCurrentUrl(), containsString("/inventory.html"));
    }

    @Then("I should see the correct number of products")
    public void iShouldSeeTheCorrectNumberOfProducts() {
        int productsCount = inventoryPage.getProductsCount();
        assertThat(productsCount, equalTo(6));
    }

    @And("I add the first product to my basket")
    public void iAddTheFirstProductToMyBasket() {
        inventoryPage.addFirstItemToCart();
    }

    @Then("the basket count should increase by 1")
    public void theBasketCountShouldIncreaseBy() {
        int count = Integer.parseInt(inventoryPage.getCartBadgeText());
        assertThat(count, equalTo(1));
    }

}

package com.sparta.pages;


import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.annotations.DefaultUrl;
import net.thucydides.core.pages.PageObject;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@DefaultUrl("https://www.saucedemo.com/inventory.html")

public class InventoryPage extends PageObject {

    @FindBy(className = "inventory_item")
    private List<WebElementFacade> products;

    @FindBy(id = "add-to-cart-sauce-labs-backpack")
    private WebElementFacade addBackpackButton;

    @FindBy(id = "remove-sauce-labs-backpack")
    private WebElementFacade removeBackpackButton;

    @FindBy(className = "shopping_cart_badge")
    private WebElementFacade shoppingCart;

    @FindBy(className = "product_sort_container")
    private WebElementFacade sortProductDropdown;

    @FindBy(id = "react-burger-menu-btn")
    private WebElementFacade menuButton;

    @FindBy(id = "logout_sidebar_link")
    private WebElementFacade logoutLink;

    @FindBy(id = "inventory_sidebar_link")
    private WebElementFacade inventoryLink;

    @FindBy(id = "add-to-cart-sauce-labs-onesie" )
    private WebElementFacade addProduct2;

    @FindBy(id = "add-to-cart-sauce-labs-bolt-t-shirt" )
    private WebElementFacade addProduct3;

    @FindBy(id = "add-to-cart-sauce-labs-fleece-jacket" )
    private WebElementFacade addProduct4;


    public int getProductsCount() {
        return products.size();
    }

    public void addFirstItemToCart() {
        addBackpackButton.click();
    }

    public void removeBackpackFromCart(){
        removeBackpackButton.click();
    }
    public String getCartBadgeText() {
        return shoppingCart.getText();
    }
    public boolean isCartBadgeDisplayed() {
        return shoppingCart.isPresent() && shoppingCart.isVisible();
    }
    public void selectDropdown(String visibleText){
        sortProductDropdown.selectByVisibleText(visibleText);
    }
    public String getSelectedDropdownText(){
        return sortProductDropdown.getSelectedVisibleTextValue();
    }
    public void clickMenuButton(){
        menuButton.click();
    }
    public void clickLogout(){
        logoutLink.click();
    }

    public void clickInventoryLink(){
        inventoryLink.click();
    }
    public void addProduct2ToCart() {
        addProduct2.click();
    }
    public void addProduct3ToCart() {
        addProduct3.click();
    }
    public void addProduct4ToCart() {
        addProduct4.click();
    }

}

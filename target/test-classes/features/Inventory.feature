Feature: Inventory
  In order to purchase items,
  As a registered user I want to be able to see products available to buy,
  So that I can choose items to add to my cart.

  Background:
    Given I am logged in as a registered user

    @Happy
    Scenario: When logged in, inventory page should show 6 products
      When I view the inventory page
      Then I should see the correct number of products

    @Happy
    Scenario: When adding an item to the basket, it increases count by 1
      When I view the inventory page
      And I add the first product to my basket
      Then the basket count should increase by 1

#    @Happy
#    Scenario: When adding multiple items to basket, it increases basket count
#      When I view the inventory page
#      And I add {int} products to my basket
#      Then the basket count should be {int}

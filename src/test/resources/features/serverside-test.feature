Feature: The Product Scraper will create a list of Products from a web page
  Users should be able to retrieve a JSON representation of products scraped from a web page

  Scenario: There are 17 products returned from the web page
    When I scrape the web page for products
    Then I get 17 items in the array

  Scenario Outline: I get the correct products from the web page
    When I scrape the web page for products
    Then I get a product with "<title>", "<description>" and "<price>"

    Examples:
      | title                                                | description                            | price |
      | Sainsbury's Strawberries 400g                        | by Sainsbury's strawberries            | 1.75 |
      | Sainsbury's Blueberries 200g                         | by Sainsbury's blueberries             | 1.75 |
      | Sainsbury's Raspberries 225g                         | by Sainsbury's raspberries             | 1.75 |
      | Sainsbury's Blackberries, Sweet 150g                 | by Sainsbury's blackberries            | 1.50 |
      | Sainsbury's Blueberries 400g                         | by Sainsbury's blueberries             | 3.25 |
      | Sainsbury's Blueberries, SO Organic 150g             | So Organic blueberries                 | 2.00 |
      | Sainsbury's Raspberries, Taste the Difference 150g   | Ttd raspberries                        | 2.50 |
      | Sainsbury's Cherries 400g                            | by Sainsbury's Family Cherry Punnet    | 2.50 |
      | Sainsbury's Blackberries, Tangy 150g                 | by Sainsbury's blackberries            | 1.50 |
      | Sainsbury's Strawberries, Taste the Difference 300g  | Ttd strawberries                       | 2.50 |
      | Sainsbury's Cherry Punnet 200g                       | Cherries                               | 1.50 |
      | Sainsbury's Mixed Berries 300g                       | by Sainsbury's mixed berries           | 3.50 |
      | Sainsbury's Mixed Berry Twin Pack 200g               | Mixed Berries                          | 2.75 |
      | Sainsbury's Redcurrants 150g                         | by Sainsbury's redcurrants             | 2.50 |
      | Sainsbury's Cherry Punnet, Taste the Difference 200g | Cherry Punnet                          | 2.50 |
      | Sainsbury's Blackcurrants 150g                       | Union Flag                             | 1.75 |
      | Sainsbury's British Cherry & Strawberry Pack 600g    | British Cherry & Strawberry Mixed Pack | 4.00 |

  Scenario Outline: I get the correct products from the web page
    When I scrape the web page for products
    Then I get a product with "<title>" and "<kcalPerHundredGrams>"

    Examples:
      | title                                                | kcalPerHundredGrams |
      | Sainsbury's Strawberries 400g                        | 33                  |
      | Sainsbury's Blueberries 200g                         | 45                  |
      | Sainsbury's Raspberries 225g                         | 32                  |
      | Sainsbury's Blackberries, Sweet 150g                 | 32                  |
      | Sainsbury's Blueberries 400g                         | 45                  |
      | Sainsbury's Blueberries, SO Organic 150g             | 45                  |
      | Sainsbury's Raspberries, Taste the Difference 150g   | 32                  |
      | Sainsbury's Cherries 400g                            | 52                  |
      | Sainsbury's Blackberries, Tangy 150g                 | 32                  |
      | Sainsbury's Strawberries, Taste the Difference 300g  | 33                  |
      | Sainsbury's Redcurrants 150g                         | 71                  |
      | Sainsbury's Cherry Punnet, Taste the Difference 200g | 48                  |


  Scenario: I calculate the correct total price
    When I scrape the web page for products
    Then I get "39.50" for the total price

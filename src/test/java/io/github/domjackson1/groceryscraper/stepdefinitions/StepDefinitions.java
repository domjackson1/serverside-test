package io.github.domjackson1.groceryscraper.stepdefinitions;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.github.domjackson1.groceryscraper.Application;
import io.github.domjackson1.groceryscraper.Product;
import io.github.domjackson1.groceryscraper.Products;
import io.github.domjackson1.groceryscraper.scrapers.ProductScraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

@ContextConfiguration(
        classes = Application.class,
        loader = SpringBootContextLoader.class)
public class StepDefinitions {

    @Autowired
    private ProductScraper productScraper;

    private Products products;

    @When("^I scrape the web page for products$")
    public void iScrapeTheWebPageForProducts() throws Throwable {
        products = productScraper.getProducts("https://jsainsburyplc.github.io/serverside-test/site/" +
                "www.sainsburys.co.uk/webapp/wcs/stores/servlet/gb/groceries/berries-cherries-currants6039.html");
    }

    @Then("^I get (\\d+) items in the array$")
    public void iGetItemsInTheArray(int expectedNumberOfProducts) throws Throwable {
        assertEquals(expectedNumberOfProducts, products.getResults().size());
    }

    @Then("^I get a product with \"([^\"]*)\", \"([^\"]*)\" and \"([^\"]*)\"$")
    public void iGetAProductWithCorrectInformation(String title, String description, String unitPrice) throws Throwable {
        Optional<Product> matchingObject = products.getResults().stream()
                .filter(item -> item.getTitle().equals(title))
                .findFirst();

        Product product = matchingObject.orElse(null);

        assertEquals(product.getDescription(), description);
        assertEquals(product.getUnitPrice(), new BigDecimal(unitPrice));
    }

    @Then("^I get a product with \"([^\"]*)\" and \"([^\"]*)\"$")
    public void iGetAProductWithCorrectKcal(String title, String kcalPerHundredGrams) throws Throwable {
        Optional<Product> matchingObject = products.getResults().stream()
                .filter(item -> item.getTitle().equals(title))
                .findFirst();

        Product product = matchingObject.orElse(null);

        assertEquals(product.getKcalPerHundredGrams(), Integer.parseInt(kcalPerHundredGrams));
    }

    @Then("^I get \"([^\"]*)\" for the total price$")
    public void iGetForTheTotalPrice(String totalPrice) throws Throwable {
        BigDecimal expectedTotalPrice = new BigDecimal(totalPrice);

        assertEquals(products.getTotal(), expectedTotalPrice);
    }
}

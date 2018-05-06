package io.github.domjackson1.groceryscraper.scrapers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ProductScraper.class)
public class TestProductScraper {

    @Autowired
    private ProductScraper productScraper;

    private Document productListings;
    private Elements expectedProductItems;

    @Before
    public void setup() throws IOException {
        File productsHtmlFile = new ClassPathResource("ProductListings.html").getFile();
        productListings = Jsoup.parse(productsHtmlFile, "UTF-8");

        File productItemsHtmlFile = new ClassPathResource("ProductItems.html").getFile();
        expectedProductItems = Jsoup.parse(productItemsHtmlFile, "UTF-8").select("div.product");

    }

    @Test
    public void shouldReturnListOfProductDivsFromPage() {
        Elements productItems = ProductScraper.getProductItemsHtmlElements(productListings);

        assertEquals(expectedProductItems.toString(), productItems.toString());
    }

    @Test
    public void shouldReturnProductTitleFromProductElement() {
        Elements productItems = ProductScraper.getProductItemsHtmlElements(productListings);
        Element productItem = productItems.first();

        String title = ProductScraper.getTitleFromProduct(productItem);
        String expectedTitle = "Test Product 1 500g";

        assertEquals(title, expectedTitle);
    }

    @Test
    public void shouldReturnProductPriceFromProductElement() {
        Elements productItems = ProductScraper.getProductItemsHtmlElements(productListings);
        Element productItem = productItems.first();

        BigDecimal price = ProductScraper.getPriceFromProduct(productItem);
        BigDecimal expectedPrice = new BigDecimal("1.75");

        assertEquals(price, expectedPrice);
    }

    @Test
    public void shouldHandleBadPriceFormatNoCurrencyCharacter() {
        Elements productItems = ProductScraper.getProductItemsHtmlElements(productListings);
        Element productItemTwo = productItems.get(1);

        BigDecimal priceOfProdctTwo = ProductScraper.getPriceFromProduct(productItemTwo);
        BigDecimal expectedPrice = new BigDecimal("2.00");

        assertEquals(priceOfProdctTwo, expectedPrice);

    }

    @Test(expected = NumberFormatException.class)
    public void shouldHandleBadPriceFormatsNotANumber() {
        Elements productItems = ProductScraper.getProductItemsHtmlElements(productListings);
        Element productItemThree = productItems.get(2);

        BigDecimal priceOfProdctThree = ProductScraper.getPriceFromProduct(productItemThree);

    }

}
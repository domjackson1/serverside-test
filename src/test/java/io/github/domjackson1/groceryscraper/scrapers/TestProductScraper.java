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

        assertEquals(expectedTitle, title);
    }

    @Test
    public void shouldReturnProductPriceFromProductElement() {
        Elements productItems = ProductScraper.getProductItemsHtmlElements(productListings);
        Element productItem = productItems.first();

        BigDecimal price = ProductScraper.getPriceFromProduct(productItem);
        BigDecimal expectedPrice = new BigDecimal("1.75");

        assertEquals(expectedPrice, price);
    }

    @Test
    public void shouldHandleBadPriceFormatNoCurrencyCharacter() {
        Elements productItems = ProductScraper.getProductItemsHtmlElements(productListings);
        Element productItemTwo = productItems.get(1);

        BigDecimal priceOfProdctTwo = ProductScraper.getPriceFromProduct(productItemTwo);
        BigDecimal expectedPrice = new BigDecimal("2.00");

        assertEquals(expectedPrice, priceOfProdctTwo);
    }

    @Test(expected = NumberFormatException.class)
    public void shouldHandleBadPriceFormatsNotANumber() {
        Elements productItems = ProductScraper.getProductItemsHtmlElements(productListings);
        Element productItemThree = productItems.get(2);

        BigDecimal priceOfProdctThree = ProductScraper.getPriceFromProduct(productItemThree);
    }

    @Test
    public void shouldReturnProductUrlFromProductElement() {
        Elements productItems = ProductScraper.getProductItemsHtmlElements(productListings);
        Element productItem = productItems.first();

        String relativeUrl = ProductScraper.getRelativeProductUrlFromProduct(productItem);
        String expectedRelativeUrl = "../../../../../../shop/gb/groceries/berries-cherries-currants/test-product-1-500g.html";

        assertEquals(expectedRelativeUrl, relativeUrl);
    }

    @Test
    public void shouldConvertRelativeUrlToAbsoluteUrl() {
        String relativeUrl = "../../shop/gb/test-product-3-200g.html";

        String absoluteUrl = productScraper.convertRelativeToAbsoluteUrl("http://www.online-shop.co.uk", relativeUrl);
        String expectedAbsoluteUrl = "http://www.online-shop.co.uk/shop/gb/test-product-3-200g.html";

        assertEquals(expectedAbsoluteUrl, absoluteUrl);
    }

    @Test
    public void shouldConvertRelativeUrlToAbsoluteUrlGivenSingleNumberRelativeUrl() {
        String relativeUrl = "2";

        String absoluteUrl = productScraper.convertRelativeToAbsoluteUrl("http://www.online-shop.co.uk", relativeUrl);
        String expectedAbsoluteUrl = "http://www.online-shop.co.uk/2";

        assertEquals(expectedAbsoluteUrl, absoluteUrl);

    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionIfCreatingMalformedUrlGivenBadBaseUrl() {
        String relativeUrl = "../../shop/gb/test-product-3-200g.html";

        String absoluteUrl = productScraper.convertRelativeToAbsoluteUrl("malformedUrl", relativeUrl);
    }
}
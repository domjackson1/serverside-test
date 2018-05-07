package io.github.domjackson1.groceryscraper.scrapers;

import io.github.domjackson1.groceryscraper.Product;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
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

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = ProductScraper.class)
public class TestProductScraper {

    @Autowired
    private ProductScraper productScraper;

    private Document productListings;
    private Elements expectedProductItems;
    private Document productOnePage;
    private Document productTwoPage;

    @Before
    public void setup() throws IOException {
        File productsHtmlFile = new ClassPathResource("ProductListings.html").getFile();
        productListings = Jsoup.parse(productsHtmlFile, "UTF-8");

        File productItemsHtmlFile = new ClassPathResource("ProductItems.html").getFile();
        expectedProductItems = Jsoup.parse(productItemsHtmlFile, "UTF-8").select("div.product");

        File productOnePageHtmlFile = new ClassPathResource("ProductOnePage.html").getFile();
        productOnePage = Jsoup.parse(productOnePageHtmlFile, "UTF-8");

        File productTwoPageHtmlFile = new ClassPathResource("ProductTwoPage.html").getFile();
        productTwoPage = Jsoup.parse(productTwoPageHtmlFile, "UTF-8");
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
    public void shouldReturnProductDescriptionFromProductPage() {
        String description = ProductScraper.getDescriptionFromProductPage(productOnePage);
        String expectedDescription = "A really good Test Product";

        assertEquals(expectedDescription, description);
    }

    @Test
    public void shouldNotReturnSecondLineFromProductDescriptionFromProductPage() {
        String description = ProductScraper.getDescriptionFromProductPage(productOnePage);
        String unexpectedDescription = "Ignored description line";

        assertThat(description, not(containsString(unexpectedDescription)));
    }

    @Test
    public void shouldReturnKcalValueFromProductPage() throws NoNutritionDataException {
        int kcalPerHundredGrams = ProductScraper.getKcalPerHundredGramsFromProductPage(productOnePage);
        int expectedDescription = 33;

        assertEquals(expectedDescription, kcalPerHundredGrams);
    }

    @Test(expected = NoNutritionDataException.class)
    public void shouldThrowExceptionIfNoKcalValueFoundOnProductPage() throws NoNutritionDataException {
        int kcalPerHundredGrams = ProductScraper.getKcalPerHundredGramsFromProductPage(productTwoPage);

    }

    @Test
    public void shouldReturnANewProductWithKcal() throws IOException {
        Elements productItems = ProductScraper.getProductItemsHtmlElements(productListings);
        Element productItem = productItems.first();

        ProductScraper productScraperMock = spy(productScraper);
        when(productScraperMock.getHtmlDocument(anyString())).thenReturn(productOnePage);

        Product expectedProduct = new Product("Test Product 1 500g", "A really good Test Product", new BigDecimal("1.75"), 33);

        Product product = productScraperMock.getProduct(productItem);

        Assert.assertThat(product, samePropertyValuesAs(expectedProduct));
    }

    @Test
    public void shouldReturnANewProductWithoutKcal() throws IOException {
        ProductScraper productScraperMock = spy(productScraper);
        when(productScraperMock.getHtmlDocument(anyString())).thenReturn(productTwoPage);

        Elements productItems = ProductScraper.getProductItemsHtmlElements(productListings);
        Element productItem = productItems.get(1);

        Product expectedProduct = new Product("Test Product 2 400g", "A really good Test Product 2", new BigDecimal("2.00"));

        Product product = productScraperMock.getProduct(productItem);

        Assert.assertThat(product, samePropertyValuesAs(expectedProduct));
    }
}
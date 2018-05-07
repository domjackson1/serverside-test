package io.github.domjackson1.groceryscraper.scrapers;

import io.github.domjackson1.groceryscraper.Product;
import io.github.domjackson1.groceryscraper.Products;
import org.apache.commons.validator.routines.BigDecimalValidator;
import org.apache.commons.validator.routines.CurrencyValidator;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Locale;

@Component
public class ProductScraper extends HtmlScraper {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    public static final String BASE_URL = "https://jsainsburyplc.github.io/serverside-test/site/www.sainsburys.co.uk";

    public Products getProducts(final String url) throws IOException {

        Document productListings = getHtmlDocument(url);
        Elements productItems = getProductItemsHtmlElements(productListings);

        Products products = new Products();

        for (Element productItem : productItems) {

            Product product = getProduct(productItem);
            products.addProduct(product);

        }

        return products;
    }

    public static Elements getProductItemsHtmlElements(final Document productListings) {
        return productListings.select("ul.productLister li.gridItem .product");
    }

    public Product getProduct(final Element productItem) throws IOException {
        String title = getTitleFromProduct(productItem);
        BigDecimal price = getPriceFromProduct(productItem);

        String relativeUrl = getRelativeProductUrlFromProduct(productItem);
        String absoluteUrl = convertRelativeToAbsoluteUrl(BASE_URL, relativeUrl);

        Document productPage = getHtmlDocument(absoluteUrl);

        String description = getDescriptionFromProductPage(productPage);

        try {
            int kcalPerHundredGrams = getKcalPerHundredGramsFromProductPage(productPage);
            return new Product(title, description, price, kcalPerHundredGrams);
        } catch (NoNutritionDataException e) {
            return new Product(title, description, price);
        }
    }

    public static String getTitleFromProduct(final Element productItem) {
        return productItem.select("h3 a").text();
    }

    public static BigDecimal getPriceFromProduct(final Element productItem) {
        Element pricePerUnit = productItem.select(".pricePerUnit").first();
        Elements unit = pricePerUnit.children();
        unit.remove();
        String priceString = pricePerUnit.text();
        BigDecimalValidator validator = CurrencyValidator.getInstance();

        if (!validator.isValid(priceString, Locale.UK)) throw new NumberFormatException();

        priceString = priceString.replace("£", "");
        return new BigDecimal(priceString);
    }

    public static String getRelativeProductUrlFromProduct(final Element productItem) {

        Element anchorElement = productItem.select("h3 a").first();

        return anchorElement.attr("href");
    }

    public static String getDescriptionFromProductPage(final Element productPage) {
        Element firstDivAfterDescriptionHeader = productPage.select("h3:contains(Description) + div").first();
        Element firstNonEmptyTextAfterDescriptionHeader = firstDivAfterDescriptionHeader.select("p:matches(^(?!\\s*$).+)").first();

        return firstNonEmptyTextAfterDescriptionHeader.text();
    }

    public static int getKcalPerHundredGramsFromProductPage(final Element productPage) throws NoNutritionDataException {
        Element kcalTableElement = productPage.select("td:matches(^.*(kcal).*$)").first();

        if (kcalTableElement == null) throw new NoNutritionDataException();

        String kcalPerHundredGramsString = kcalTableElement.text();
        kcalPerHundredGramsString = kcalPerHundredGramsString.replace("kcal", "");

        return convertKcalStringToInteger(kcalPerHundredGramsString);
    }

    public static int convertKcalStringToInteger(final String kcalPerHundredGramsString) {

        String kcalPerHundredGramsStringWithoutUnit = kcalPerHundredGramsString.replace("kcal", "");

        return Integer.parseInt(kcalPerHundredGramsStringWithoutUnit);
    }

}

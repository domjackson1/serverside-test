package io.github.domjackson1.groceryscraper.scrapers;

import io.github.domjackson1.groceryscraper.Product;
import io.github.domjackson1.groceryscraper.scrapers.HtmlScraper;
import org.apache.commons.validator.routines.BigDecimalValidator;
import org.apache.commons.validator.routines.CurrencyValidator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class ProductScraper extends HtmlScraper {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    public static final String BASE_URL = "https://jsainsburyplc.github.io/serverside-test/site/www.sainsburys.co.uk";

    public List<Product> getProducts(String url) throws IOException {

        Document productListings = getHtmlDocument(url);
        Elements productItems = getProductItemsHtmlElements(productListings);

        List<Product> products = new ArrayList<>();

        return products;
    }

    public static Elements getProductItemsHtmlElements(Document productListings) {
        return productListings.select("ul.productLister li.gridItem .product");
    }

    public Product getProduct(Element productItem) throws IOException {
        String title = getTitleFromProduct(productItem);
        BigDecimal price = getPriceFromProduct(productItem);

        String relativeUrl = getRelativeProductUrlFromProduct(productItem);
        String absoluteUrl = convertRelativeToAbsoluteUrl(BASE_URL, relativeUrl);

        Document productPage = getHtmlDocument(absoluteUrl);

        String description = getDescriptionFromProductPage(productPage);

        return new Product(title, description, price, 100);
    }

    public static String getTitleFromProduct(Element productItem) {
        return productItem.select("h3 a").text();
    }

    public static BigDecimal getPriceFromProduct(Element productItem) {
        Element pricePerUnit = productItem.select(".pricePerUnit").first();
        Elements unit = pricePerUnit.children();
        unit.remove();
        String priceString = pricePerUnit.text();
        BigDecimalValidator validator = CurrencyValidator.getInstance();

        if (!validator.isValid(priceString, Locale.UK)) throw new NumberFormatException();

        priceString = priceString.replace("£", "");
        return new BigDecimal(priceString);
    }

    public static String getRelativeProductUrlFromProduct(Element productItem) {

        Element anchorElement = productItem.select("h3 a").first();

        return anchorElement.attr("href");
    }

    public static String getDescriptionFromProductPage(Element productPage) {
        Element firstDivAfterDescriptionHeader = productPage.select("h3:contains(Description) + div").first();
        Element firstNonEmptyTextAfterDescriptionHeader = firstDivAfterDescriptionHeader.select("p:matches(^(?!\\s*$).+)").first();

        return firstNonEmptyTextAfterDescriptionHeader.text();
    }

}

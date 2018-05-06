package io.github.domjackson1.groceryscraper.scrapers;

import io.github.domjackson1.groceryscraper.Product;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ProductScraper extends HtmlScraper {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    public static final String BASE_URL = "https://jsainsburyplc.github.io/serverside-test/site/www.sainsburys.co.uk";

    public List<Product> getProducts(String url) throws IOException {

        Document productListings = getHtmlContent(url);
        Elements productItems = getProductItemsHtmlElements(productListings);

        List<Product> products = new ArrayList<>();

        return products;
    }

    private Document getHtmlContent(String url) throws IOException {
        if (!isValidUrl(url)) {
            LOGGER.error(BAD_URL_MESSAGE);
            throw new IllegalArgumentException();
        }

        return Jsoup.connect(url).get();
    }

    public static Elements getProductItemsHtmlElements(Document productListings) {
        return productListings.select("ul.productLister li.gridItem .product");
    }

    public static String getTitleFromProduct(Element productItem) {
        return productItem.select("h3 a").text();
    }


}

package io.github.domjackson1.groceryscraper;

import org.apache.commons.validator.routines.UrlValidator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class HtmlScraper {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public static final String BAD_URL_MESSAGE = "Invalid URL";

    public static final String BASE_URL = "https://jsainsburyplc.github.io/serverside-test/site/www.sainsburys.co.uk";

    private static final String[] schemes = {"http","https"};
    private UrlValidator urlValidator = new UrlValidator(schemes);

    public Document getHtmlDocument(String url) throws IOException {

        if (!isValidUrl(url)) {
            LOGGER.error(BAD_URL_MESSAGE);
            throw new IllegalArgumentException();
        }

        return Jsoup.connect(url).get();
    }

    public boolean isValidUrl(String url) {
        return this.urlValidator.isValid(url);
    }
}

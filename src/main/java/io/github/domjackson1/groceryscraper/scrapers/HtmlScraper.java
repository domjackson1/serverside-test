package io.github.domjackson1.groceryscraper.scrapers;

import org.apache.commons.validator.routines.UrlValidator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.MalformedURLException;

@Component
public class HtmlScraper {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public static final String BAD_URL_MESSAGE = "Invalid URL";

    private static final String[] schemes = {"http","https"};
    private UrlValidator urlValidator = new UrlValidator(schemes);

    public Document getHtmlDocument(final String url) throws IOException {

        try {
            return Jsoup.connect(url).get();
        } catch (IllegalArgumentException e) {
            LOGGER.error(String.format("%s: %s", BAD_URL_MESSAGE, url));
            return null;
        }

    }

    public boolean isValidUrl(final String url) {
        return this.urlValidator.isValid(url);
    }

    public String convertRelativeToAbsoluteUrl(final String baseUrl, final String relativeUrl) throws MalformedURLException {

        String urlPath = relativeUrl.replace("../", "");

        String url = String.format("%s/%s", baseUrl, urlPath);

        if (!isValidUrl(url)) {
            LOGGER.error(String.format("%s: %s", BAD_URL_MESSAGE, url));
            return null;
        }

        return url;
    }
}

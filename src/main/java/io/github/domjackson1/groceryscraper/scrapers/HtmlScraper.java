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

    public static final String BAD_URL_MESSAGE = "Invalid URL created";

    private static final String[] schemes = {"http","https"};
    private UrlValidator urlValidator = new UrlValidator(schemes);

    public Document getHtmlDocument(String url) throws IOException {
        return Jsoup.connect(url).get();
    }

    public boolean isValidUrl(String url) {
        return this.urlValidator.isValid(url);
    }

    public String convertRelativeToAbsoluteUrl(String baseUrl, String relativeUrl) throws MalformedURLException {

        relativeUrl = relativeUrl.replace("../", "");

        String url = String.format("%s/%s", baseUrl, relativeUrl);

        if (!isValidUrl(url)) {
            LOGGER.error(String.format("%s: %s", BAD_URL_MESSAGE, url));
            throw new MalformedURLException();
        }

        return url;
    }
}

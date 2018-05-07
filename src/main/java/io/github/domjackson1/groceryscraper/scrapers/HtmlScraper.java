package io.github.domjackson1.groceryscraper.scrapers;

import org.apache.commons.validator.routines.UrlValidator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;

@Component
public class HtmlScraper {

    private static final Logger LOGGER = LoggerFactory.getLogger(HtmlScraper.class);

    public static final String BAD_URL_MESSAGE = "Invalid URL";

    public static Document getHtmlDocument(final String url) throws Exception {

        try {
            return Jsoup.connect(url).get();
        } catch (Exception e) {
            LOGGER.error(String.format("%s: %s", e.getMessage(), url));
            throw e;
        }

    }

    public static boolean isValidUrl(final String url) {
        final String[] schemes = {"http","https"};
        final UrlValidator urlValidator = new UrlValidator(schemes);
        return urlValidator.isValid(url);
    }

    public static String convertRelativeToAbsoluteUrl(final String baseUrl, final String relativeUrl) throws MalformedURLException {

        String urlPath = relativeUrl.replace("../", "");

        String url = String.format("%s/%s", baseUrl, urlPath);

        if (!isValidUrl(url)) {
            LOGGER.error(String.format("%s: %s", BAD_URL_MESSAGE, url));
            throw new MalformedURLException();
        }

        return url;
    }
}

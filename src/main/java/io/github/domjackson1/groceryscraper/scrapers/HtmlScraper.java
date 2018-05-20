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

    private static final Logger LOGGER = LoggerFactory.getLogger(HtmlScraper.class);

    private static final String BAD_URL_MESSAGE = "Invalid URL";

    public static Document getHtmlDocument(final String url) throws IOException {

        try {
            return Jsoup.connect(url).get();
        } catch (IOException e) {
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
            String errorMessage = String.format("%s: %s", BAD_URL_MESSAGE, url);
            LOGGER.error(errorMessage);
            throw new MalformedURLException();
        }

        return url;
    }
}

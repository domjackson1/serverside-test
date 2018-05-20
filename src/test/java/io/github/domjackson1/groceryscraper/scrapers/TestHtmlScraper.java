package io.github.domjackson1.groceryscraper.scrapers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.MalformedURLException;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = HtmlScraper.class)
public class TestHtmlScraper {

    @Test
    public void shouldReturnNotValidUrl() {
        assertFalse(HtmlScraper.isValidUrl("not_valid_url"));
    }

    @Test
    public void shouldReturnIsValidUrl() {
        assertTrue(HtmlScraper.isValidUrl("http://url.com"));
    }

    @Test
    public void shouldConvertRelativeUrlToAbsoluteUrl() throws MalformedURLException {
        String relativeUrl = "../../shop/gb/test-product-3-200g.html";

        String absoluteUrl = HtmlScraper.convertRelativeToAbsoluteUrl("http://www.online-shop.co.uk", relativeUrl);
        String expectedAbsoluteUrl = "http://www.online-shop.co.uk/shop/gb/test-product-3-200g.html";

        assertEquals(expectedAbsoluteUrl, absoluteUrl);
    }

    @Test
    public void shouldConvertRelativeUrlToAbsoluteUrlGivenSingleNumberRelativeUrl() throws MalformedURLException {
        String relativeUrl = "2";

        String absoluteUrl = HtmlScraper.convertRelativeToAbsoluteUrl("http://www.online-shop.co.uk", relativeUrl);
        String expectedAbsoluteUrl = "http://www.online-shop.co.uk/2";

        assertEquals(expectedAbsoluteUrl, absoluteUrl);
    }

    @Test(expected = MalformedURLException.class)
    public void shouldThrowExceptionIfCreatingMalformedUrlGivenBadBaseUrl() throws MalformedURLException {
        String relativeUrl = "../../shop/gb/test-product-3-200g.html";

        String absoluteUrl = HtmlScraper.convertRelativeToAbsoluteUrl("malformedUrl", relativeUrl);
    }
}

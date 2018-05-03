package io.github.domjackson1.groceryscraper.scrapers;

import io.github.domjackson1.groceryscraper.scrapers.HtmlScraper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = HtmlScraper.class)
public class TestHtmlScraper {

    @Autowired
    private HtmlScraper htmlScraper;

    @Test
    public void shouldReturnNotValidUrl() {
        assertFalse(htmlScraper.isValidUrl("not_valid_url"));
    }

    @Test
    public void shouldReturnIsValidUrl() {
        assertTrue(htmlScraper.isValidUrl("http://url.com"));
    }
}

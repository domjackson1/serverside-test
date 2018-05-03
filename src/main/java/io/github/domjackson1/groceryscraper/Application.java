package io.github.domjackson1.groceryscraper;

import io.github.domjackson1.groceryscraper.scrapers.ProductScraper;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ProductScraper productScraper;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws IOException {

        List<Product> products = productScraper.getProducts("https://jsainsburyplc.github.io/serverside-test/site/" +
                "www.sainsburys.co.uk/webapp/wcs/stores/servlet/gb/groceries/berries-cherries-currants6039.html");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(System.out, products);

    }
}

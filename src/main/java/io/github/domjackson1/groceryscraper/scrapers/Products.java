package io.github.domjackson1.groceryscraper.scrapers;

import io.github.domjackson1.groceryscraper.Product;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Products {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public List<Product> results = new ArrayList<>();

    public void addProduct(final Product product) {
        this.results.add(product);
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();

        String jsonString = "";
        try {
            mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
            jsonString = mapper.writeValueAsString(this);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }

        return jsonString;
    }
}

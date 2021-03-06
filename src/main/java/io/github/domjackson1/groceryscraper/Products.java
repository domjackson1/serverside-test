package io.github.domjackson1.groceryscraper;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Products {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private List<Product> results;
    private BigDecimal total;

    public Products() {
        this.results = new ArrayList<>();
        total = new BigDecimal("0");
    }

    public void addProduct(final Product product) {
        this.results.add(product);
        updateTotal(product.getUnitPrice());
    }

    private void updateTotal(BigDecimal unitPrice) {
        this.total = total.add(unitPrice);
    }

    public BigDecimal getTotal() {
        return this.total;
    }

    public List<Product> getResults() {
        return this.results;
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

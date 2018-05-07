package io.github.domjackson1.groceryscraper;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;

@JsonSerialize(include= JsonSerialize.Inclusion.NON_DEFAULT)
public class Product {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private String title; // required
    private String description; // required
    private BigDecimal unitPrice; // required

    private int kcalPerHundredGrams; // optional

    private Product() {}

    public Product(String title, String description,
                   BigDecimal unitPrice) {
        this.title = title;
        this.description = description;
        this.unitPrice = unitPrice;
    }

    public Product(String title, String description,
                   BigDecimal unitPrice, int kcalPerHundredGrams) {
        this.title = title;
        this.description = description;
        this.unitPrice = unitPrice;
        this.kcalPerHundredGrams = kcalPerHundredGrams;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("unit_price")
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    @JsonProperty("kcal_per_100g")
    public int getKcalPerHundredGrams() {
        return kcalPerHundredGrams;
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

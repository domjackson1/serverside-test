package io.github.domjackson1.groceryscraper;

import org.codehaus.jackson.annotate.JsonProperty;

import java.math.BigDecimal;

public class Product {

    private String title;
    private String description;
    private BigDecimal unitPrice;
    private int kcalPer100Grams;

    public Product(@JsonProperty("title") String title, @JsonProperty("description") String description,
                   @JsonProperty("unit_price") BigDecimal unitPrice, @JsonProperty("kcal_per_100g") int kcalPer100Grams) {
        this.title = title;
        this.description = description;
        this.unitPrice = unitPrice;
        this.kcalPer100Grams = kcalPer100Grams;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getKcalPer100Grams() {
        return kcalPer100Grams;
    }

    public void setKcalPer100Grams(int kcalPer100Grams) {
        this.kcalPer100Grams = kcalPer100Grams;
    }
}

package io.github.domjackson1.groceryscraper.scrapers;

public class NoNutritionDataException extends Exception {

    public NoNutritionDataException() {}

    public NoNutritionDataException(final String message) {
        super(message);
    }
}

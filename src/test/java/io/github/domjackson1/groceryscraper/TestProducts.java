package io.github.domjackson1.groceryscraper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = Products.class)
public class TestProducts {

    @Autowired
    private Products products;

    @Test
    public void shouldHaveCorrectTotalPriceCalculated() {

        Product productOne = new Product("Product One", "", new BigDecimal("1.90"), 100);
        Product productTwo = new Product("Product Two", "", new BigDecimal("2.30"), 100);

        products.addProduct(productOne);
        products.addProduct(productTwo);

        BigDecimal totalPrice = products.getTotal();
        BigDecimal expectedPrice = new BigDecimal("4.20");

        assertEquals(expectedPrice, totalPrice);
    }
}

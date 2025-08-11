package com.sheepion.demo.mapper.data;

import java.math.BigDecimal;
import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

import com.sheepion.demo.model.Product;

public final class ProductTestData {

    private ProductTestData() {}

    /**
     * @return Valid products.
     */
    public static Stream<Arguments> validProducts() {
        return Stream.of(
            Arguments.of(Product.builder()
                .name("Potato")
                .description("Fresh Potato")
                .price(BigDecimal.valueOf(10))
                .stockQuantity(1000)
                .isAvailable(true)
                .build()),
            Arguments.of(Product.builder()
                .name("Apple")
                .description("Fresh Apple")
                .price(BigDecimal.valueOf(3.51))
                .stockQuantity(200)
                .isAvailable(false)
                .build())
        );
    }

    /**
     * @return Invalid products that missing required fields and no default value.
     */
    public static Stream<Arguments> invalidRequiredFieldProducts() {
        return Stream.of(
            // name
            Arguments.of(Product.builder()
                .name(null)
                .description("Fresh Potato")
                .price(BigDecimal.valueOf(10))
                .stockQuantity(1000)
                .isAvailable(true)
                .build()),
            // price
            Arguments.of(Product.builder()
                .name("Potato")
                .description("Fresh Potato")
                .price(null)
                .stockQuantity(1000)
                .isAvailable(true)
                .build())
        );
    }

    /**
     * @return Products that have null values for optional fields.
     */
    public static Stream<Arguments> defaultValueProducts() {
        return Stream.of(
            // stockQuantity 
            Arguments.of(Product.builder()
                .name("Tomato")
                .description("Red Tomato")
                .price(BigDecimal.valueOf(2.35))
                .stockQuantity(null)
                .isAvailable(true)
                .build()),
            // isAvailable 
            Arguments.of(Product.builder()
                .name("Cucumber")
                .description("Green Cucumber")
                .price(BigDecimal.valueOf(1.80))
                .stockQuantity(20)
                .isAvailable(null)
                .build()),
            // both are null
            Arguments.of(Product.builder()
                .name("Banana")
                .description("Yellow Banana")
                .price(BigDecimal.valueOf(4.20))
                .stockQuantity(null)
                .isAvailable(null)
                .build())
        );
    }
}



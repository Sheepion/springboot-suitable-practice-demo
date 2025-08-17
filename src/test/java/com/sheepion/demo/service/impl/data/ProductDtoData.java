package com.sheepion.demo.service.impl.data;

import java.util.stream.Stream;

import org.junit.jupiter.params.provider.Arguments;

import com.sheepion.demo.dto.ProductCreateDto;

public final class ProductDtoData {
    private ProductDtoData() {
    }

    public static Stream<Arguments> validProductCreateDtos() {
        return Stream.of(
                Arguments.of(ProductCreateDto.builder()
                        .name("Potato")
                        .description("Fresh Potato")
                        .price("10.00")
                        .stockQuantity(1000)
                        .isAvailable(true)
                        .build()),
                Arguments.of(ProductCreateDto.builder()
                        .name("Apple")
                        .description("Fresh Apple")
                        .price("3.51")
                        .stockQuantity(200)
                        .isAvailable(false)
                        .build()));
    }

    /**
     * invalid create dtos.
     * - name: not blank
     * - price: not null, min 0
     * - stockQuantity: not null, min 0
     * 
     * @return
     */
    public static Stream<Arguments> invalidFieldProductCreateDtos() {
        return Stream.of(
                // name
                Arguments.of(ProductCreateDto.builder()
                        .name(null)
                        .description("Fresh Potato")
                        .price("10.00")
                        .stockQuantity(1000)
                        .isAvailable(true)
                        .build()),
                // price
                Arguments.of(ProductCreateDto.builder()
                        .name("Potato")
                        .description("Fresh Potato")
                        .price(null)
                        .stockQuantity(1000)
                        .isAvailable(true)
                        .build()),

                Arguments.of(ProductCreateDto.builder()
                        .name("Potato")
                        .description("Fresh Potato")
                        .price("-1")
                        .stockQuantity(1000)
                        .isAvailable(true)
                        .build())

                // stock Quantity
                , Arguments.of(ProductCreateDto.builder()
                        .name("Potato")
                        .description("Fresh Potato")
                        .price("10.00")
                        .stockQuantity(null)
                        .isAvailable(true)
                        .build()),
                Arguments.of(ProductCreateDto.builder()
                        .name("Potato")
                        .description("Fresh Potato")
                        .price("10.00")
                        .stockQuantity(-1)
                        .isAvailable(true)
                        .build()));
    }
}

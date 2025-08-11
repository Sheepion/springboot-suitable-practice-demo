package com.sheepion.demo.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

import com.sheepion.demo.model.Product;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
// use @MybatisTest instead of @SpringBootTest, for faster test execution, since
// it only loads the mybatis related beans
@MybatisTest
// use the test container instead of the embedded database, for more realistic
// testing
@Import({ TestcontainersConfiguration.class })
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("ProductMapperTest")
class ProductMapperTest {
    @Resource
    private ProductMapper productMapper;

    void assertProductEquals(Product expected, Product actual) {
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getUuid(), actual.getUuid());
        Assertions.assertEquals(expected.getName(), actual.getName());
        Assertions.assertEquals(expected.getDescription(), actual.getDescription());
        // use BigDecimal.compareTo to compare the price
        Assertions.assertEquals(0, expected.getPrice().compareTo(actual.getPrice()));
        Assertions.assertEquals(expected.getStockQuantity(), actual.getStockQuantity());
        Assertions.assertEquals(expected.getIsAvailable(), actual.getIsAvailable());
        Assertions.assertEquals(expected.getCreatedAt(), actual.getCreatedAt());
        Assertions.assertEquals(expected.getUpdatedAt(), actual.getUpdatedAt());
        Assertions.assertEquals(expected.getDeleted(), actual.getDeleted());
    }

    @Nested
    @DisplayName("insert")
    class InsertTests {
        @ParameterizedTest
        @MethodSource("com.sheepion.demo.mapper.data.ProductTestData#validProducts")
        @DisplayName("should writeback id and uuid")
        void shoudReturnIdAndUuid(Product product) {
            productMapper.insert(product);
            log.info("product: {}", product);
            // check id and uuid
            Assertions.assertNotNull(product.getId());
            Assertions.assertNotNull(product.getUuid());
            // check createdAt and updatedAt
            Assertions.assertNotNull(product.getCreatedAt());
            Assertions.assertNotNull(product.getUpdatedAt());
            // not deleted by default
            Assertions.assertFalse(product.getDeleted());
        }

        @ParameterizedTest
        @MethodSource("com.sheepion.demo.mapper.data.ProductTestData#invalidRequiredFieldProducts")
        @DisplayName("should throw exception when required field is null")
        void shouldThrowException_whenRequiredFieldIsNull(Product product) {
            // insert a product
            Assertions.assertThrows(DataIntegrityViolationException.class, () -> productMapper.insert(product));
        }

        @ParameterizedTest
        @MethodSource("com.sheepion.demo.mapper.data.ProductTestData#defaultValueProducts")
        @DisplayName("should apply defaults when optional field is null")
        void shouldApplyDatabaseDefaults_whenOptionalFieldIsNull(Product product) {
            productMapper.insert(product);
            Product selected = productMapper.selectByUuid(product.getUuid());
            // stockQuantity, default to 0
            if (product.getStockQuantity() == null) {
                Assertions.assertEquals(0, selected.getStockQuantity());
            } else {
                Assertions.assertEquals(product.getStockQuantity(), selected.getStockQuantity());
            }
            // isAvailable, default to true
            if (product.getIsAvailable() == null) {
                Assertions.assertEquals(true, selected.getIsAvailable());
            } else {
                Assertions.assertEquals(product.getIsAvailable(), selected.getIsAvailable());
            }
        }
    }

    @Nested
    @DisplayName("selectByUuid")
    class SelectByUuidTests {

        @ParameterizedTest
        @MethodSource("com.sheepion.demo.mapper.data.ProductTestData#validProducts")
        @DisplayName("should return product")
        void shoudReturnProduct(Product product) {
            // insert a product
            productMapper.insert(product);
            // select the product
            Product selectedProduct = productMapper.selectByUuid(product.getUuid());
            // check the product
            assertProductEquals(product, selectedProduct);
        }

        @Test
        @DisplayName("should return null when product not found")
        void shouldReturnNull_whenProductNotFound() {
            // select the product
            Product selectedProduct = productMapper.selectByUuid("not-found");
            // check the product
            Assertions.assertNull(selectedProduct);
        }
    }
}

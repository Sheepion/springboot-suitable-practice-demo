package com.sheepion.demo.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import com.sheepion.demo.dto.ProductCreateDto;
import com.sheepion.demo.mapper.ProductMapper;
import com.sheepion.demo.model.Product;

import io.github.linpeilie.Converter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
// specify the beans we need,
// to boosting the test performance and avoid datasource connection
@SpringBootTest(classes = { ProductServiceImpl.class, Converter.class })
class ProductServiceImplTest {
    // spy bean, to use the real implementation of Converter (from mapstruct plus)
    @MockitoSpyBean
    private Converter converter;

    // mocked bean, to simulate the database operations
    @MockitoBean
    private ProductMapper productMapper;

    @Autowired
    private ProductServiceImpl productService;

    @Nested
    @DisplayName("createProductTest")
    class CreateTests {
        @ParameterizedTest
        @MethodSource("com.sheepion.demo.service.impl.data.ProductDtoData#validProductCreateDtos")
        void shouldReturnUuid_whenSuccess(ProductCreateDto params) {
            // Mock mapper behavior - simulate the insert operation
            // The insert method should set the uuid field on the product
            Mockito.doAnswer(invocation -> {
                Product product = invocation.getArgument(0);
                // Simulate the database auto-generation of uuid
                product.setUuid("test-uuid-12345");
                return 1; // Return number of affected rows
            }).when(productMapper).insert(Mockito.any(Product.class));

            // call create
            String uuid = productService.createProduct(params);

            // assertions
            Assertions.assertNotNull(uuid);
            Assertions.assertEquals("test-uuid-12345", uuid);
            log.info("Product created with UUID: {}", uuid);

            // Verify that the mapper insert method was called exactly once
            Mockito.verify(productMapper, Mockito.times(1)).insert(Mockito.any(Product.class));
        }

        @ParameterizedTest
        @MethodSource("com.sheepion.demo.service.impl.data.ProductDtoData#invalidFieldProductCreateDtos")
        void shouldThrowException_whenValidationFail(ProductCreateDto params) {
            // no need for mock

            // When the createProduct method is called with invalid params,
            // it should throw a ConstraintViolationException
            Assertions.assertThrows(
                    IllegalArgumentException.class,
                    () -> productService.createProduct(params),
                    "Expected createProduct to throw, but it didn't");

            // Verify that the mapper insert method was never called
            Mockito.verify(productMapper, Mockito.never()).insert(Mockito.any(Product.class));

        }
    }

}

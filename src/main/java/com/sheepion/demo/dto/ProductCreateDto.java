package com.sheepion.demo.dto;

import com.sheepion.demo.model.Product;

import io.github.linpeilie.annotations.AutoMapper;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Product create params")
@AutoMapper(target = Product.class)
public class ProductCreateDto {
    @Schema(description = "product name")
    @NotBlank(message = "Product name cannot be blank")
    private String name;

    private String description;

    @NotNull(message = "Product price cannot be null")
    @Min(value = 0, message = "Product price must be at least 0")
    private String price;

    @NotNull(message = "Product stock quantity cannot be null")
    @Min(value = 0, message = "Product stock quantity must be at least 0")
    private Integer stockQuantity;

    @Schema(description = "is product available", defaultValue = "true")
    private Boolean isAvailable;
}

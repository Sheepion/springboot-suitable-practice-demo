package com.sheepion.demo.vo;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Product")
public class ProductVo {
    //use uuid instead of id
    @Schema(description = "product uuid")
    private String uuid;
    @Schema(description = "product name")
    private String name;
    private String description;
    private String price; // Use String to avoid precision issues with BigDecimal
    private Integer stockQuantity;
    private Boolean isAvailable;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

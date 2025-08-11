package com.sheepion.demo.common;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Data;


/**
 * This is a base entity class for all entities.  
 * <p>It contains the uuid, createdAt, updatedAt fields.
 */
@Data
public class BaseEntity implements Serializable {
    // use String instead of UUID, for convenience
    private String uuid;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean deleted;

}

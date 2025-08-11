package com.sheepion.demo.mapper;


import org.apache.ibatis.annotations.Mapper;

import com.sheepion.demo.model.Product;

@Mapper
public interface ProductMapper {
    /**
     * Insert a product.
     * 
     * @param product the product to insert. Id and uuid will be auto-generated.
     * @return the number of rows affected
     */
    int insert(Product product);

    /**
     * Select a product by uuid.
     * @param uuid the uuid of the product to select.
     * @return the product
     */
    Product selectByUuid(String uuid);

    /**
     * Update a product by uuid.
     * @param product the product to update.
     * @return the number of rows affected
     */
    int updateByUuid(Product product);

    /**
     * Delete a product by uuid.
     * @param uuid the uuid of the product to delete.
     * @return the number of rows affected
     */
    int deleteByUuid(String uuid);
}

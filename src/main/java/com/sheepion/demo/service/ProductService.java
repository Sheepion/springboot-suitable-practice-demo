package com.sheepion.demo.service;

import com.sheepion.demo.dto.ProductCreateDto;
import com.sheepion.demo.model.Product;
import com.sheepion.demo.vo.ProductVo;

public interface ProductService {
    /**
     * Internal use only!
     * @param id
     * @return model
     */
    Product getProductById(String id);

    /**
     * Commenly used to return product information to the client.
     * @param uuid
     * @return vo
     */
    ProductVo getProductByUuid(String uuid);

    /**
     * create new product
     * @param product params
     * @return uuid of the created product
     */
    String createProduct(ProductCreateDto product);
}   

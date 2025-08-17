package com.sheepion.demo.service.impl;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.sheepion.demo.dto.ProductCreateDto;
import com.sheepion.demo.mapper.ProductMapper;
import com.sheepion.demo.model.Product;
import com.sheepion.demo.service.ProductService;
import com.sheepion.demo.vo.ProductVo;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import io.github.linpeilie.Converter;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductMapper productMapper;

    // converter from mapstruct-plus
    // used to convert between model, dto, vo
    @Autowired
    private Converter converter;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createProduct(ProductCreateDto params) {
        log.debug("createProduct: {}", params);
        // validate params solely, not using validation api from ProductCreateDto to
        // decouple
        if (StrUtil.isBlank(params.getName())) {
            throw new IllegalArgumentException("Product name cannot be blank");
        }
        if (params.getPrice() == null || NumberUtil.isLess(new BigDecimal(params.getPrice()), BigDecimal.ZERO)) {
            throw new IllegalArgumentException("Product price must be at least 0");
        }
        if (params.getStockQuantity() == null
                || NumberUtil.isLess(new BigDecimal(params.getStockQuantity()), BigDecimal.ZERO)) {
            throw new IllegalArgumentException("Product price must be at least 0");
        }

        Product product = converter.convert(params, Product.class);
        productMapper.insert(product);
        log.info("Product created with UUID: {}", product.getUuid());
        // return the uuid of the created product
        return product.getUuid();
    }

    @Override
    public Product getProductById(String id) {
        return null;
    }

    @Override
    public ProductVo getProductByUuid(String uuid) {
        log.debug("getProductByUuid: {}", uuid);
        Product product = productMapper.selectByUuid(uuid);
        if (product == null) {
            log.debug(uuid + " not found");
            return null;
        }

        ProductVo productVo = converter.convert(product, ProductVo.class);
        log.info("Product found: {}", productVo);
        return productVo;
    }

}

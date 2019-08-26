package com.zz.service;

import com.zz.entity.Product;
import com.zz.repository.ProductRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ProductService {
    @Resource
    ProductRepository pr;

    public Product save(Product product) {
        return pr.save(product);
    }

    public List<Product> findAll() {
        return pr.findAll();
    }
}

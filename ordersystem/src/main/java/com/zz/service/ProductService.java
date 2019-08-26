package com.zz.service;

import com.zz.entity.Product;
import com.zz.repository.ProductRepository;
import org.springframework.data.domain.Pageable;
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

    public Iterable<Product> findAll(Pageable pageable) {
        return pr.findAll(pageable);
    }

    public List<Product> findAll() {
        return pr.findAll();
    }

    public Product findAllById(String id) {
        return pr.findAllById(id);
    }
}

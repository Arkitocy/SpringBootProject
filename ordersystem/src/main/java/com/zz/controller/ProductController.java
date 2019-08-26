package com.zz.controller;

import com.zz.entity.Product;
import com.zz.service.ProductService;
import io.swagger.annotations.Api;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(value = "商品Controller")
@RestController
@RequestMapping("product")
public class ProductController {

    @Resource
    ProductService ps;

    @PostMapping("showall/{page}")
    public Iterable<Product> showall(@PathVariable("page") String page) {
        Pageable pageable = PageRequest.of(Integer.parseInt(page), 10);
        return ps.findAll(pageable);
    }

    @PostMapping("showall")
    public List<Product> showall() {
        return ps.findAll();
    }

    @PostMapping("showbyid/{id}")
    public Product showbyid(@PathVariable("id") String id) {
        return ps.findAllById(id);
    }
}

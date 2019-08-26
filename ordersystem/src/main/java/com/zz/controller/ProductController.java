package com.zz.controller;

import com.zz.entity.Product;
import com.zz.service.ProductService;
import io.swagger.annotations.Api;
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

    @PostMapping("showall")
    public List<Product> showall() {
        Map map = new HashMap();
        return ps.findAll();
    }
}

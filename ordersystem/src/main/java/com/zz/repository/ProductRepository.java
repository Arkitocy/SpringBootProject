package com.zz.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.zz.entity.Product;

public interface ProductRepository extends JpaRepository<Product,String>{
    List<Product> findAll();
}
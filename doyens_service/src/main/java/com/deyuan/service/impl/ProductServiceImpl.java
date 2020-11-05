package com.deyuan.service.impl;

import com.deyuan.dao.ProductDao;
import com.deyuan.pojo.Product;
import com.deyuan.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductServiceImpl implements IProductService {
    @Autowired
    private ProductDao dao;
    @Override
    public List<Product> findAll() {
        return dao.findAll();
    }

    @Override
    public void save(Product product) {
        dao.save(product);
    }
}

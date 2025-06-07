package com.project.DemoApp.service;

import com.project.DemoApp.models.Product;
import com.project.DemoApp.repository.AppRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppService {
    AppRepository repo;

    @Autowired
    AppService(AppRepository repository){
        repo = repository;
    }

    public List<Product> getProducts(){
        return repo.findAll();
    }

    public void addProduct(Product product){
        repo.save(product);
    }
}

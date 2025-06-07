package com.project.DemoApp.controllers;

import com.project.DemoApp.models.Product;
import com.project.DemoApp.service.AppService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class AppController {

    AppService appService;

    AppController(AppService _appService){
        appService = _appService;
    }

    @GetMapping("/")
    public String greet(){
        return "Hello.. Welcome!!";
    }

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts(){
        return new ResponseEntity<>(appService.getProducts(), HttpStatus.OK);
    }

    @PostMapping("/product")
    public void addProduct(@RequestBody Product product){
        appService.addProduct(product);
    }
}

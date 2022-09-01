package com.test.project.test_project.contollers;

import com.test.project.test_project.model.Product;
import com.test.project.test_project.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/shop")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/product")
    public ResponseEntity<Object> returnProductListByFilter(@RequestParam String nameFilter){
        return productService.returnProductListByFilter(nameFilter);
    }
}

package com.test.project.test_project.services;

import com.test.project.test_project.model.Product;
import com.test.project.test_project.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.AsyncResult;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ProductServiceTest {

    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

    @Test
    public void getResponseForRegexTest() throws Exception {
        List<Product> productList = new ArrayList<>();
        Product testProduct = new Product(1L,"test", "test");
        productList.add(testProduct);
        productList.add(new Product(2L,"eva","test"));
        List<Product> resultProductList = new ArrayList<>();
        resultProductList.add(testProduct);
        assertEquals(resultProductList , getResultForRegexFilterMethod().invoke(productService, "eva", productList));
    }

    private Method getResultForRegexFilterMethod() throws NoSuchMethodException {
        Method method = ProductService.class.getDeclaredMethod("getResponseForRegex", String.class, List.class);
        method.setAccessible(true);
        return method;
    }

    @Test
    public void getResultOfRegexAsyncTest() throws Exception {
        List<Product> productList = productRepository.findAll();
        Future<List<Product>> futureListProductActual = (Future<List<Product>>) getResultForRegexMethod().invoke(productService, "all");
        assertEquals(productList, futureListProductActual.get());
        Product evaProduct = new Product(3L, "eva", "eva Test");
        productList.remove(evaProduct);
        futureListProductActual = (Future<List<Product>>) getResultForRegexMethod().invoke(productService, "eva");
        assertEquals(productList, futureListProductActual.get());
    }

    private Method getResultForRegexMethod() throws NoSuchMethodException {
        Method method = ProductService.class.getDeclaredMethod("getResultOfRegexAsync", String.class);
        method.setAccessible(true);
        return method;
    }

    @Test
    public void returnProductListByFilterTest() throws Exception {
        List<Product> productList = productRepository.findAll();
        ResponseEntity<Object> responseListProductActual = (ResponseEntity<Object>) getResponseForProductList().invoke(productService, "all");
        assertEquals(ResponseEntity.ok().body(productList), responseListProductActual);
        Product evaProduct = new Product(3L, "eva", "eva Test");
        productList.remove(evaProduct);
        responseListProductActual = (ResponseEntity<Object>) getResponseForProductList().invoke(productService, "eva");
        assertEquals(ResponseEntity.ok().body(productList), responseListProductActual);
    }

    private Method getResponseForProductList() throws NoSuchMethodException {
        Method method = ProductService.class.getDeclaredMethod("returnProductListByFilter", String.class);
        method.setAccessible(true);
        return method;
    }

}

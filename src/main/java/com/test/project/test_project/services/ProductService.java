package com.test.project.test_project.services;

import com.test.project.test_project.model.Product;
import com.test.project.test_project.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

import static com.test.project.test_project.constants.ProductConstants.FILTER_TO_GET_ALL_PRODUCT_RECORDS;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

   /* @Async
    public ResponseEntity<Object> r1eturnProductByFilter(String nameFilter) {
        List<Product> productList = productRepository.findAll();
        System.out.println("the thread running is " + Thread.currentThread().getName());
        try{
            if(nameFilter.equalsIgnoreCase(FILTER_TO_GET_ALL_PRODUCT_RECORDS)){
                System.out.println("result is" + productList +"for thread"+ Thread.currentThread().getName() + "timestamp"+ LocalDateTime.now());
                return ResponseEntity.ok().body(productList);
            }else {
                System.out.println("result is" + getResponseForRegex(nameFilter, productList) +"for thread"+ Thread.currentThread().getName() + "timestamp"+ LocalDateTime.now());
                return ResponseEntity.ok().body(getResponseForRegex(nameFilter, productList));
            }
        }
        catch (PatternSyntaxException ex){
            return ResponseEntity.badRequest().body("Provided filter is not valid regex.");
        }
    }*/

    public ResponseEntity<Object> returnProductListByFilter(String nameFilter) {
        Future<List<Product>> future = getResultOfRegexAsync(nameFilter);
        System.out.println("the thread is" + Thread.currentThread().getName());
        try {
            return ResponseEntity.ok().body(future.get());
        }catch (ExecutionException | InterruptedException ex){
            return ResponseEntity.badRequest().body("There was an error while doing request");
        }
    }

    @Async("asyncExecutor")
    private Future<List<Product>> getResultOfRegexAsync(String nameFilter){
        List<Product> productList = productRepository.findAll();
        return switch (nameFilter) {
            case FILTER_TO_GET_ALL_PRODUCT_RECORDS -> new AsyncResult<>(productList);
            default -> new AsyncResult<>(getResponseForRegex(nameFilter, productList));
        };

    }

    private List<Product> getResponseForRegex(String nameFilter, List<Product> productList) {
        return productList.stream()
                .filter(product ->
                        !Pattern.compile(nameFilter)
                                .matcher(product.getName())
                                .find())
                .collect(Collectors.toList());
    }
}

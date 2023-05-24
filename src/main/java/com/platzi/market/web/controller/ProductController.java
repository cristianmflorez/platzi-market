package com.platzi.market.web.controller;

import com.platzi.market.domain.Product;
import com.platzi.market.domain.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController//dice a spring que es controlador de api rest
@RequestMapping("/products")//path
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/all")//path
    @Operation(description = "GET all products in DataBase")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<List<Product>> getall(){
        return new ResponseEntity<>(productService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")//path dinamico
    @Operation(description = "Get product by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "PRODUCT NOT FOUND", content = @Content(examples = {
                    @ExampleObject("PRODUCT NOT FOUND")}))
    })
    public ResponseEntity<Product> getProduct(@Parameter(required = true) @PathVariable("id") int prodcutId){
        return productService.getProduct(prodcutId)
                .map(product -> new ResponseEntity<>(product, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/category/{categoryId}")
    @Operation(description = "Get all products in one category by category id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "CATEGORY NOT FOUND", content = @Content(examples = {
                    @ExampleObject("CATEGORY NOT FOUND")}))
    })
    public ResponseEntity<List<Product>> getByCategory(@Parameter(required = true) @PathVariable("categoryId") int categoryId){
        if(productService.getByCategory(categoryId).get().isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else {
            return productService.getByCategory(categoryId)
                    .map(products -> new ResponseEntity<>(products, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
    }

    @PostMapping("/save")
    @Operation(description = "Save new product in data base")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<Product> save(@Parameter(required = true) @RequestBody Product product){
        return new ResponseEntity<>(productService.save(product), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(description = "Delete product by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "PRODUCT NOT FOUND", content = @Content(examples = {
                    @ExampleObject("PRODUCT NOT FOUND")}))
    })
    public ResponseEntity delete(@Parameter(required = true) @PathVariable("id") int productId){
        if(productService.delete(productId)){
            return new ResponseEntity(HttpStatus.OK);
        }else{
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }
}

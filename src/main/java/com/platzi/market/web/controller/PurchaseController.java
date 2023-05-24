package com.platzi.market.web.controller;

import com.platzi.market.domain.Product;
import com.platzi.market.domain.Purchase;
import com.platzi.market.domain.service.PurchaseService;
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

@RestController
@RequestMapping("/purchases")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @GetMapping("/all")
    @Operation(description = "GET all purchase in DataBase")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<List<Purchase>> getall(){
        return new ResponseEntity<>(purchaseService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{clientId}")//path dinamico
    @Operation(description = "Get all purchases of one client by client id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "404", description = "CLIENT NOT FOUND", content = @Content(examples = {
                    @ExampleObject("CLIENT NOT FOUND")}))
    })
    public ResponseEntity<List<Purchase>> getByclient(@Parameter(required = true) @PathVariable("clientId") String clientId){
        if(purchaseService.getByclient(clientId).get().isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else {
            return purchaseService.getByclient(clientId)
                    .map(purchases -> new ResponseEntity<>(purchases, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }
    }

    @PostMapping("/save")
    @Operation(description = "Save new product in data base")
    @ApiResponse(responseCode = "200", description = "OK")
    public ResponseEntity<Purchase> save(@Parameter(required = true) @RequestBody Purchase purchase){
        return new ResponseEntity<>(purchaseService.save(purchase), HttpStatus.CREATED);
    }
}

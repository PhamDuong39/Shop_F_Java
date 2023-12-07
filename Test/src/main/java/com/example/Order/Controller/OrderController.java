package com.example.Order.Controller;

import com.example.Common.Entity.Order;
import com.example.Common.Response.ResponseModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/Order")
public class OrderController {

    @Operation(summary = "get order", description = "API to get order.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "order get successfully"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    @GetMapping("/get-order")
    public ResponseEntity<Order> Test() {
        return ResponseEntity.ok(new Order());
    }
}

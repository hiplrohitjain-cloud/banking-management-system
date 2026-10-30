package com.rohit.banking.controller;

import com.rohit.banking.dto.ApiResponse;
import com.rohit.banking.dto.CustomerRequestDTO;
import com.rohit.banking.dto.CustomerResponseDTO;
import com.rohit.banking.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

    private final CustomerService customerService;
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CustomerResponseDTO>> createCustomer(@Valid @RequestBody CustomerRequestDTO requestDTO) {
            CustomerResponseDTO response =  customerService.createCustomer(requestDTO);

            return ResponseEntity.ok(new ApiResponse<>("Customer created successfully.", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<CustomerResponseDTO>>> getAllCustomers(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Page<CustomerResponseDTO> customers = customerService.getAllCustomers(page,size);

        return ResponseEntity.ok(new ApiResponse<>("Reterieve data successfully.",customers));
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<CustomerResponseDTO>> getCustomerById(@PathVariable Long id) {

        CustomerResponseDTO response = customerService.getCustomerById(id);
        return ResponseEntity.ok(new ApiResponse<>("Reterive data successfully.",response));
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<CustomerResponseDTO>> updateCustomer(@PathVariable Long id, @Valid @RequestBody CustomerRequestDTO requestDTO) {

        CustomerResponseDTO response = customerService.updateCustomer(id,requestDTO);
        return ResponseEntity.ok(new ApiResponse<>("Customer updated successfully.",response));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCustomer(@PathVariable Long id) {

        customerService.deleteCustomer(id);
        return ResponseEntity.ok(new ApiResponse<>("Customer deleted successfully.",null));
    }
}

package com.rohit.banking.service;

import com.rohit.banking.dto.CustomerRequestDTO;
import com.rohit.banking.dto.CustomerResponseDTO;
import com.rohit.banking.entity.Customer;
import com.rohit.banking.exception.CustomerNotFoundException;
import com.rohit.banking.exception.EmailAlreadyExistsException;
import com.rohit.banking.repository.CustomerRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    private Customer convertToEntity(CustomerRequestDTO requestDTO) {
        Customer customer = new Customer();

        customer.setFullName(requestDTO.getFullName());
        customer.setEmail(requestDTO.getEmail());
        customer.setPhone(requestDTO.getPhone());

        return customer;
    }

    public CustomerResponseDTO convertToResponseDTo(Customer customer) {
        return new CustomerResponseDTO(
                customer.getId(),
                customer.getFullName(),
                customer.getEmail(),
                customer.getPhone()
        );
    }

    public CustomerResponseDTO createCustomer(CustomerRequestDTO requestDTO) {

        Customer customer = convertToEntity(requestDTO);

        if(customerRepository.existsByEmail(customer.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists: "+ customer.getEmail());
        }
        Customer savedCustomer = customerRepository.save(customer);
        return convertToResponseDTo(savedCustomer);
    }

    public Page<CustomerResponseDTO> getAllCustomers(int page, int size) {

        Pageable pageable = PageRequest.of(page,size);

        return customerRepository.findAll(pageable).map(this::convertToResponseDTo);
    }

    public CustomerResponseDTO getCustomerById(Long id) {

        Customer getCustomer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException(
                "Customer not found: "+id
        ));
        return convertToResponseDTo(getCustomer);
    }

    public CustomerResponseDTO updateCustomer(Long id, CustomerRequestDTO requestDTO) {

        Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException("Customer not found with this id: "+id));

        if(!customer.getEmail().equalsIgnoreCase(requestDTO.getEmail()) && customerRepository.existsByEmail(requestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("Customer already exist with this email: "+ requestDTO.getEmail());
        }

        customer.setEmail(requestDTO.getEmail());
        customer.setPhone(requestDTO.getPhone());
        customer.setFullName(requestDTO.getFullName());

        Customer updateCustomer = customerRepository.save(customer);
        return convertToResponseDTo(updateCustomer);
    }

    public void deleteCustomer(Long id) {

        Customer customer = customerRepository.findById(id).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));

        customerRepository.deleteById(id);


    }
}

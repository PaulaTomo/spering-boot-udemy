package com.example.spring6restmvc.services;

import com.example.spring6restmvc.model.CustomerDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {

    List<CustomerDto> customerList();

    Optional<CustomerDto> getCustomerById(UUID id);

    CustomerDto saveNewCustomer(CustomerDto customer);

    Optional<CustomerDto> updateCustomerById(UUID customerId, CustomerDto customer);

    Boolean deleteById(UUID customerId);

    Optional<CustomerDto> patchCustomerById(UUID customerId, CustomerDto customer);
}

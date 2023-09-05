package com.example.spring6restmvc.controller;

import com.example.spring6restmvc.entities.Customer;
import com.example.spring6restmvc.mappers.CustomerMapper;
import com.example.spring6restmvc.model.BeerDto;
import com.example.spring6restmvc.model.CustomerDto;
import com.example.spring6restmvc.repositories.CustomerRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
public class CustomerControllerIT {

    @Autowired
    CustomerController customerController;

    @Autowired
    CustomerRepository customerRepository;
    
    @Autowired
    CustomerMapper customerMapper;

    @Test
    void deleteByIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            customerController.getCustomerById(UUID.randomUUID());
        });
    }

    @Rollback
    @Transactional
    @Test
    void deleteByIdFound() {
Customer customer = customerRepository.findAll().get(0);

        ResponseEntity responseEntity = customerController.deleteById(customer.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        assertThat(customerRepository.findById(customer.getId())).isEmpty();
    }

    @Test
    void testUpdateNotFound(){
        assertThrows(NotFoundException.class,() -> {
            customerController.updateCustomerById(UUID.randomUUID(), CustomerDto.builder().build());
        });
    }
    
    @Test
    void updateExistingCustomer(){
        Customer customer = customerRepository.findAll().get(0);
        CustomerDto customerDto = customerMapper.customerToCustomerDto(customer);
        customerDto.setId(null);
        customerDto.setVersion(null);
        final String customerName = "UPDATED";
        customerDto.setCustomerName(customerName);
        ResponseEntity responseEntity = customerController.updateCustomerById(customer.getId(), customerDto);
        
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
        
        Customer updateCustomer = customerRepository.findById(customer.getId()).get();
        
        assertThat(updateCustomer.getCustomerName()).isEqualTo(customerName);

    }

    @Test
    void saveNewCustomerTest(){
        CustomerDto customerDto = CustomerDto.builder()
                .customerName("New Customer")
                .build();

        ResponseEntity responseEntity = customerController.handlePost(customerDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();
        String[] locationUUID = responseEntity.getHeaders().getLocation().getPath().split("/");

        UUID saveUUID = UUID.fromString(locationUUID[4]);

        Customer customer = customerRepository.findById(saveUUID).get();
        assertThat(customer).isNotNull();

    }
    @Test
    void testCustomerIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            customerController.getCustomerById(UUID.randomUUID());
        });
    }

    @Test
    void testListCustomers() {
        List<CustomerDto> dtoList = customerController.customerList();

        assertThat(dtoList.size()).isEqualTo(3);
    }

    @Test
    void testGetById() {
        Customer customer = customerRepository.findAll().get(0);

        CustomerDto dto = customerController.getCustomerById(customer.getId());
        assertThat(dto).isNotNull();
    }

    @Rollback
    @Transactional
    @Test
    void testEmptyList() {
        customerRepository.deleteAll();

        List<CustomerDto> dtoList = customerController.customerList();
        assertThat(dtoList.size()).isEqualTo(0);
    }
}


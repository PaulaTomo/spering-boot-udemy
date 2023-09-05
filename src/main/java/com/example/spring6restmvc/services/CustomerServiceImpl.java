package com.example.spring6restmvc.services;

import com.example.spring6restmvc.model.CustomerDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {
    private Map<UUID, CustomerDto> customerMap;

    public  CustomerServiceImpl(){
        this.customerMap = new HashMap<>();

        CustomerDto customer1 = CustomerDto.builder()
                .id(UUID.randomUUID())
                .customerName("John Doe")
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        CustomerDto customer2 = CustomerDto.builder()
                .id(UUID.randomUUID())
                .customerName("Alice")
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        CustomerDto customer3 = CustomerDto.builder()
                .id(UUID.randomUUID())
                .customerName("Mario Rossi")
                .version(1)
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        customerMap = new HashMap<>();
        customerMap.put(customer1.getId(),customer1);
        customerMap.put(customer2.getId(),customer2);
        customerMap.put(customer3.getId(),customer3);

    }
    @Override
    public List<CustomerDto> customerList() {
        return new ArrayList<>(customerMap.values());
    }

    @Override
    public Optional<CustomerDto> getCustomerById(UUID id) {
        log.debug("Get customer by Id - in service. Id: " + id.toString());
        return Optional.of(customerMap.get(id));
    }

    @Override
    public CustomerDto saveNewCustomer(CustomerDto customer) {

        CustomerDto saveCustomer = CustomerDto.builder()
                .id(UUID.randomUUID())
                .customerName(customer.getCustomerName())
                .version(customer.getVersion())
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .build();

        customerMap.put(saveCustomer.getId(),saveCustomer);
        return saveCustomer;
    }

    @Override
    public Optional<CustomerDto> updateCustomerById(UUID customerId, CustomerDto customer) {
        CustomerDto existing = customerMap.get(customerId);
        existing.setCustomerName(customer.getCustomerName());
        customerMap.put(customerId,existing);
        return Optional.of(existing);
    }

    @Override
    public Boolean deleteById(UUID customerId) {
        customerMap.remove(customerId);
        return true;
    }

    @Override
    public Optional<CustomerDto> patchCustomerById(UUID customerId, CustomerDto customer) {

        CustomerDto existing = customerMap.get(customerId);

        if(StringUtils.hasText(customer.getCustomerName())){
            existing.setCustomerName(customer.getCustomerName());
        }
        return Optional.of(existing);

    }

}

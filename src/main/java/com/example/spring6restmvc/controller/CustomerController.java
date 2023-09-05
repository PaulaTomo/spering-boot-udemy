package com.example.spring6restmvc.controller;

import com.example.spring6restmvc.model.CustomerDto;
import com.example.spring6restmvc.services.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
public class CustomerController {

    private final CustomerService customerService;
    public static final String CUSTOMER_PATH = "/api/v1/customer";
    public static final String CUSTOMER_PATH_ID = CUSTOMER_PATH + "/{customerId}";

    @PatchMapping(CUSTOMER_PATH_ID)
    public ResponseEntity updateCustomerPatchById(@PathVariable("customerId") UUID customerId, @RequestBody CustomerDto customer ){

        customerService.patchCustomerById(customerId, customer);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @DeleteMapping(CUSTOMER_PATH_ID)
        public ResponseEntity deleteById(@PathVariable("customerId") UUID customerId){

            customerService.deleteById(customerId);
return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    @PutMapping(CUSTOMER_PATH_ID)
    public ResponseEntity updateCustomerById(@PathVariable("customerId") UUID customerId,@RequestBody CustomerDto customer) {

       if(customerService.updateCustomerById(customerId,customer).isEmpty()){
           throw new NotFoundException();
       }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @PostMapping(CUSTOMER_PATH)
    public ResponseEntity handlePost(@RequestBody CustomerDto customer) {

        CustomerDto savedCustomer = customerService.saveNewCustomer(customer);


        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", CUSTOMER_PATH + "/" + savedCustomer.getId().toString());

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

 @GetMapping(CUSTOMER_PATH)
    public List<CustomerDto> customerList() {
        return customerService.customerList();
    }

    @GetMapping(CUSTOMER_PATH_ID)
    public CustomerDto getCustomerById(@PathVariable("customerId") UUID customerId) {

        log.debug("Get Customer by Id - in controller");

        return customerService.getCustomerById(customerId).orElseThrow(NotFoundException::new);
    }

}

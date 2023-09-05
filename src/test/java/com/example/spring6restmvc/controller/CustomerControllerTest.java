package com.example.spring6restmvc.controller;

import com.example.spring6restmvc.model.CustomerDto;
import com.example.spring6restmvc.services.CustomerService;
import com.example.spring6restmvc.services.CustomerServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CustomerService customerService;

    CustomerServiceImpl customerServiceImpl = new CustomerServiceImpl();

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<CustomerDto> customerArgumentCaptor;

    @BeforeEach
    void seUp(){
        customerServiceImpl = new CustomerServiceImpl();

    }
    @Test
    void testPatchCustomer() throws Exception {
        CustomerDto customer = customerServiceImpl.customerList().get(0);

        Map<String,Object> customerMap = new HashMap<>();
        customerMap.put("customerName", "New Name");

        mockMvc.perform(patch( CustomerController.CUSTOMER_PATH + "/" + customer.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(customerMap)))
                        .andExpect(status().isNoContent());

        verify(customerService).patchCustomerById(uuidArgumentCaptor.capture(), customerArgumentCaptor.capture());
        assertThat(customer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
        assertThat(customerMap.get("customerName")).isEqualTo(customerArgumentCaptor.getValue().getCustomerName());
    }

@Test
void testDeleteCustomer() throws Exception{
        CustomerDto customer = customerServiceImpl.customerList().get(0);

    mockMvc.perform(delete(CustomerController.CUSTOMER_PATH + "/" + customer.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

    verify(customerService).deleteById(uuidArgumentCaptor.capture());

    assertThat(customer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
}
    @Test
    void testUpdateCustomer() throws Exception{
        CustomerDto customer = customerServiceImpl.customerList().get(0);


        given(customerService.updateCustomerById(any(), any())).willReturn(Optional.of(CustomerDto.builder()
                .build()));

        mockMvc.perform(put(CustomerController.CUSTOMER_PATH_ID, customer.getId())
                        .content(objectMapper.writeValueAsString(customer))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(customerService).updateCustomerById(uuidArgumentCaptor.capture(), any(CustomerDto.class));

        assertThat(customer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

        @Test
        void testCreateNewCustomer() throws Exception{
            CustomerDto customer = customerServiceImpl.customerList().get(0);

            customer.setVersion(null);
            customer.setId(null);

            given(customerService.saveNewCustomer(any(CustomerDto.class))).willReturn(customerServiceImpl.customerList().get(1));

            mockMvc.perform(post(CustomerController.CUSTOMER_PATH)
                            .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(customer)))
                    .andExpect(status().isCreated())
                    .andExpect(header().exists("Location"));
    }

    @Test
    void testListCustomer() throws Exception{
        given(customerService.customerList()).willReturn(customerServiceImpl.customerList());

        mockMvc.perform(get(CustomerController.CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()",is(3)));

    }

    @Test
    void getCustomerById() throws Exception {
        CustomerDto testCustomer = customerServiceImpl.customerList().get(0);

        given(customerService.getCustomerById(testCustomer.getId())).willReturn(Optional.of(testCustomer));

        mockMvc.perform(get(CustomerController.CUSTOMER_PATH + "/" + testCustomer.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id",is(testCustomer.getId().toString())))
                .andExpect(jsonPath("$.customerName",is(testCustomer.getCustomerName())));

    }
}
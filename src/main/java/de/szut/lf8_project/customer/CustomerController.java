package de.szut.lf8_project.customer;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("customer")
public class CustomerController {
    private CustomerService service;
    private CustomerMapping customerMapping;

    public CustomerController(CustomerService service, CustomerMapping customerMapping){
        this.customerMapping = customerMapping;
        this.service = service;
    }
}

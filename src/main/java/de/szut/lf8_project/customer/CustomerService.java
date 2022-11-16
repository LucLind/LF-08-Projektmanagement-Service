package de.szut.lf8_project.customer;

import org.springframework.stereotype.Service;

/**
 * Customer service class.
 */
@Service
public class CustomerService {
    private CustomerRepository repository;

    /**
     * constructor
     * @param repository the customer repository.
     */
    public CustomerService(CustomerRepository repository){
        this.repository = repository;
    }
}

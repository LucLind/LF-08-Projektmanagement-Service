package de.szut.lf8_project.customer;

import de.szut.lf8_project.customer.dto.GetCustomerDTO;
import de.szut.lf8_project.customer.dto.PostCustomerDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

/**
 * Customer Controller class
 */
@RestController
@RequestMapping("customer")
public class CustomerController {
    private CustomerService service;
    private CustomerMapping customerMapping;

    /**
     * constructor for CustomerController
     * @param service the service
     * @param customerMapping the customer mapping
     */
    public CustomerController(CustomerService service, CustomerMapping customerMapping){
        this.customerMapping = customerMapping;
        this.service = service;
    }

    /**
     * Dummy-methog for posting customers
     * @param dto the post customer DTO
     * @return Response entity OK for test purposes
     */
    @PostMapping
    public ResponseEntity<GetCustomerDTO> postCustomer(@RequestBody @Valid PostCustomerDTO dto){
        // Kunden werden noch nicht implementiert daher hier nur als dummy ein OK zurückgeben.
        var response = new GetCustomerDTO(0L, "Warning: CUSTOMERS ARE STILL NOT IMPLEMENTED", "YOU WILL ONLY RECEIVE TEST DATA.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Method for getting all Customers (Testing purposes / dummy data)
     * @return Response entity OK for test purposes
     */
    @GetMapping
    public ResponseEntity<Set<GetCustomerDTO>> getAllCustomers(){
        // Kunden werden noch nicht implementiert daher hier nur als dummy ein OK zurückgeben.
        var response = new HashSet<GetCustomerDTO>();
        response.add(new GetCustomerDTO(0L,
                "WARNING: CUSTOMERS ARE STILL NOT IMPLEMENTED",
                "YOU WILL ONLY RECIEVE TEST DATA."));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Method for getting a specific customer by its id (Testing purposes / dummy data)
     * @param id the customer id
     * @return Response entity OK for test purposes
     */
    @GetMapping("/{id}")
    public ResponseEntity<GetCustomerDTO> getCustomerById(@PathVariable Long id){
        // Kunden werden noch nicht implementiert daher hier nur als dummy ein OK zurückgeben.
        var response = new GetCustomerDTO(0L, "WARNING: CUSTOMERS ARE STILL NOT IMPLEMENTED", "YOU WILL ONLY RECIEVE TEST DATA.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

package de.szut.lf8_project.customer;

import de.szut.lf8_project.customer.dto.GetCustomerDTO;
import de.szut.lf8_project.customer.dto.PostCustomerDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("customer")
public class CustomerController {
    private CustomerService service;
    private CustomerMapping customerMapping;

    public CustomerController(CustomerService service, CustomerMapping customerMapping){
        this.customerMapping = customerMapping;
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<GetCustomerDTO> postCustomer(@RequestBody @Valid PostCustomerDTO dto){
        // Kunden werden noch nicht implementiert daher hier nur als dummy ein OK zurückgeben.
        var response = new GetCustomerDTO(0L, "WaRNING: CUSTOMERS ARE STILL NOT IMPLEMENTED", "YOU WILL ONLY RECIEVE TEST DATA.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<Set<GetCustomerDTO>> getAllCustomers(){
        // Kunden werden noch nicht implementiert daher hier nur als dummy ein OK zurückgeben.
        var response = new HashSet<GetCustomerDTO>();
        response.add(new GetCustomerDTO(0L,
                "WARNING: CUSTOMERS ARE STILL NOT IMPLEMENTED",
                "YOU WILL ONLY RECIEVE TEST DATA."));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetCustomerDTO> getCustomerById(@PathVariable Long id){
        // Kunden werden noch nicht implementiert daher hier nur als dummy ein OK zurückgeben.
        var response = new GetCustomerDTO(0L, "WARNING: CUSTOMERS ARE STILL NOT IMPLEMENTED", "YOU WILL ONLY RECIEVE TEST DATA.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

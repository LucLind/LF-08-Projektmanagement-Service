package de.szut.lf8_project.customer;

import de.szut.lf8_project.customer.dto.GetCustomerDTO;
import de.szut.lf8_project.customer.dto.PostCustomerDTO;
import de.szut.lf8_project.project.dto.GetProjectDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "erstellt einen neuen Kunden-Dummy")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Kunde erfolgreich erstellt- Dummy",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetCustomerDTO.class))}),
            @ApiResponse(responseCode = "400", description = "ungültiges JSON gepostet- Dummy",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "keine Berechtigung- Dummy",
                    content = @Content)})
    @PostMapping
    public ResponseEntity<GetCustomerDTO> postCustomer(@RequestBody @Valid PostCustomerDTO dto){
        // Kunden werden noch nicht implementiert daher hier nur als dummy ein OK zurückgeben.
        var response = new GetCustomerDTO(0L, "WARNING: CUSTOMERS ARE STILL NOT IMPLEMENTED", "YOU WILL ONLY RECEIVE TEST DATA.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Method for getting all Customers (Testing purposes / dummy data)
     * @return Response entity OK for test purposes
     */
    @Operation(summary = "Alle Kunden auslesen-Dummy")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alle Kunden erfolgreich ausgelesen- Dummy",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetCustomerDTO.class))}),
            @ApiResponse(responseCode = "400", description = "ungültiges JSON gepostet-Dummy",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "keine Berechtigung-Dummy",
                    content = @Content)})
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
    @Operation(summary = "Alle Projekte eines Kunden auslesen-Dummy")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alle Projekte eines Kunden erfolgreich ausgelesen- Dummy",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GetCustomerDTO.class))}),
            @ApiResponse(responseCode = "400", description = "ungültiges JSON gepostet-Dummy",
                    content = @Content),
            @ApiResponse(responseCode = "401", description = "keine Berechtigung-Dummy",
                    content = @Content)})
    @GetMapping("/{id}")
    public ResponseEntity<GetCustomerDTO> getCustomerById(@PathVariable Long id){
        // Kunden werden noch nicht implementiert daher hier nur als dummy ein OK zurückgeben.
        var response = new GetCustomerDTO(0L, "WARNING: CUSTOMERS ARE STILL NOT IMPLEMENTED", "YOU WILL ONLY RECEIVE TEST DATA.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

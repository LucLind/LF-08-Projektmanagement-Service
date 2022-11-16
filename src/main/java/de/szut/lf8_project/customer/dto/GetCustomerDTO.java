package de.szut.lf8_project.customer.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The GetCustomerDTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetCustomerDTO {
    private Long id;
    private String firstName;
    private String lastName;
}

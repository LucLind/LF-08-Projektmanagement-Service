package de.szut.lf8_project.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * Customer repository interface.
 */
@Service
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {
}

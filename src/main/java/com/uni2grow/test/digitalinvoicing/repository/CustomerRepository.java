package com.uni2grow.test.digitalinvoicing.repository;

import com.uni2grow.test.digitalinvoicing.entity.Address;
import com.uni2grow.test.digitalinvoicing.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    @Query("SELECT e FROM Customer e " +
           "WHERE e.name LIKE %?1% " +
           "OR e.email LIKE %?1% " +
           "OR e.phone LIKE %?1% " )
    Page<Customer> findByKey(String key, Pageable paging);
}

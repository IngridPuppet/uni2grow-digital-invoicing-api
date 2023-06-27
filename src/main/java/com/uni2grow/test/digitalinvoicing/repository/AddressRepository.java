package com.uni2grow.test.digitalinvoicing.repository;

import com.uni2grow.test.digitalinvoicing.entity.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AddressRepository extends JpaRepository<Address, Integer> {
    @Query("SELECT e FROM Address e " +
           "WHERE e.street LIKE %?1% " +
           "OR e.city LIKE %?1% " +
           "OR e.state LIKE %?1% " +
           "OR e.country LIKE %?1% " +
           "OR e.zipCode LIKE %?1% ")
    Page<Address> findByKey(String key, Pageable paging);
}

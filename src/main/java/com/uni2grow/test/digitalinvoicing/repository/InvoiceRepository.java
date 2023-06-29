package com.uni2grow.test.digitalinvoicing.repository;

import com.uni2grow.test.digitalinvoicing.entity.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
    @Query("SELECT e FROM Invoice e LEFT JOIN e.customer c " +
           "WHERE CAST(e.number AS String) LIKE %?1% " +
           "OR c.name LIKE %?1% ")
    Page<Invoice> findByKey(String key, Pageable paging);
}

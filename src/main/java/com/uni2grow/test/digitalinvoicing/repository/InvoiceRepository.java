package com.uni2grow.test.digitalinvoicing.repository;

import com.uni2grow.test.digitalinvoicing.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer> {
}

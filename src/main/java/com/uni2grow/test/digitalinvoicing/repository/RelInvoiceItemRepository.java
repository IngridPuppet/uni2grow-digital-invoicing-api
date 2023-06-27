package com.uni2grow.test.digitalinvoicing.repository;

import com.uni2grow.test.digitalinvoicing.entity.RelInvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RelInvoiceItemRepository extends JpaRepository<RelInvoiceItem, Integer> {
}

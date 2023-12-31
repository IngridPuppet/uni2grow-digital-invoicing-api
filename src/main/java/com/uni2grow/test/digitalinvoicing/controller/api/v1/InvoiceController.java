package com.uni2grow.test.digitalinvoicing.controller.api.v1;

import com.uni2grow.test.digitalinvoicing.entity.Invoice;
import com.uni2grow.test.digitalinvoicing.repository.InvoiceRepository;
import com.uni2grow.test.digitalinvoicing.repository.RelInvoiceItemRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@RestController
@Transactional
@RequestMapping("/api/v1/invoices")
public class InvoiceController {
    @Autowired
    EntityManager entityManager;

    @Autowired
    InvoiceRepository invoiceRepository;

    @Autowired
    RelInvoiceItemRepository relInvoiceItemRepository;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Invoice getOne(@PathVariable Integer id)
    {
        return invoiceRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<Invoice> getAll()
    {
        return invoiceRepository.findAll();
    }

    @GetMapping("/paginated")
    @ResponseStatus(HttpStatus.OK)
    public Page<Invoice> getPaginated(@PageableDefault(sort = {"issueDate"}, direction = Sort.Direction.DESC) Pageable paging,
                                      @RequestParam(defaultValue = "") String key)
    {
        // Search by number or customer's name
        return invoiceRepository.findByKey(key, paging);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Invoice store(@RequestBody Invoice invoice)
    {
        if (invoice.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID must be null.");
        }

        // Stash inventory

        var relInvoiceItems = invoice.getRelInvoiceItems();
        for (var relInvoiceItem: relInvoiceItems) {
            if (relInvoiceItem.getId() != null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The inventory must be brand new.");
            }
        }

        // Initial persist

        invoice.setIssueDate(Instant.now());
        entityManager.persist(invoice);
        entityManager.refresh(invoice);

        // Refresh inventory

        relInvoiceItems.forEach(relInvoiceItem -> relInvoiceItem.setInvoice(invoice));
        invoice.setRelInvoiceItems(relInvoiceItems);
        invoice.totalize();

        return entityManager.merge(invoice);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Invoice update(@PathVariable Integer id, @RequestBody Invoice invoice)
    {
        if (invoice.getId() == null || !invoice.getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID is invalid.");
        }

        // Throw 404 if resource does not exist

        if (!invoiceRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        // Wipe current inventory

        relInvoiceItemRepository.deleteAll(getOne(id).getRelInvoiceItems());

        // Refresh inventory

        for (var relInvoiceItem: invoice.getRelInvoiceItems()) {
            relInvoiceItem.setId(null);
            relInvoiceItem.setInvoice(invoice);
        }

        // Re-compute total

        invoice.totalize();

        return entityManager.merge(invoice);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Integer id)
    {
        Invoice invoice = invoiceRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT));

        invoiceRepository.delete(invoice);
    }
}

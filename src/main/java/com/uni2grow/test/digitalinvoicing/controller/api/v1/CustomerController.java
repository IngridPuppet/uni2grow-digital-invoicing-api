package com.uni2grow.test.digitalinvoicing.controller.api.v1;

import com.uni2grow.test.digitalinvoicing.entity.Customer;
import com.uni2grow.test.digitalinvoicing.repository.CustomerRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@Transactional
@RequestMapping("/api/v1/customers")
public class CustomerController {
    @Autowired
    EntityManager entityManager;

    @Autowired
    CustomerRepository customerRepository;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Customer getOne(@PathVariable Integer id)
    {
        return customerRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<Customer> getAll()
    {
        return customerRepository.findAll();
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Customer store(@RequestBody Customer customer)
    {
        if (customer.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID must be null.");
        }

        var address = Optional.ofNullable(customer.getAddress());
        if (address.isPresent() && address.get().getId() == null) {
            entityManager.persist(customer.getAddress());
        }

        entityManager.persist(customer);
        entityManager.refresh(customer);

        return customer;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Customer update(@PathVariable Integer id, @RequestBody Customer customer)
    {
        if (customer.getId() == null || !customer.getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID is invalid.");
        }

        // Throws 404 if resource does not exist
        getOne(id);

        return entityManager.merge(customer);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Integer id)
    {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT));

        customerRepository.delete(customer);
    }
}

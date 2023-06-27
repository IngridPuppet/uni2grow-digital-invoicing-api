package com.uni2grow.test.digitalinvoicing.controller.api.v1;

import com.uni2grow.test.digitalinvoicing.entity.Address;
import com.uni2grow.test.digitalinvoicing.repository.AddressRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@Transactional
@RequestMapping("/api/v1/addresses")
public class AddressController {
    @Autowired
    EntityManager entityManager;

    @Autowired
    AddressRepository addressRepository;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Address getOne(@PathVariable Integer id)
    {
        return addressRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<Address> getAll()
    {
        return addressRepository.findAll();
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Address store(@RequestBody Address address)
    {
        if (address.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID must be null.");
        }

        entityManager.persist(address);
        entityManager.refresh(address);

        return address;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Address update(@PathVariable Integer id, @RequestBody Address address)
    {
        if (address.getId() == null || !address.getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID is invalid.");
        }

        // Throws 404 if resource does not exist
        getOne(id);

        return entityManager.merge(address);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Integer id)
    {
        Address address = addressRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT));

        addressRepository.delete(address);
    }
}

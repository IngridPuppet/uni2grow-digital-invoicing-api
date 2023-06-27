package com.uni2grow.test.digitalinvoicing.controller.api.v1;

import com.uni2grow.test.digitalinvoicing.entity.Address;
import com.uni2grow.test.digitalinvoicing.entity.Customer;
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
}

package com.uni2grow.test.digitalinvoicing.controller.api.v1;

import com.uni2grow.test.digitalinvoicing.entity.Item;
import com.uni2grow.test.digitalinvoicing.repository.ItemRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@Transactional
@RequestMapping("/api/v1/items")
public class ItemController {
    @Autowired
    EntityManager entityManager;

    @Autowired
    ItemRepository itemRepository;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Item getOne(@PathVariable Integer id)
    {
        return itemRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public List<Item> getAll()
    {
        return itemRepository.findAll();
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Item store(@RequestBody Item item)
    {
        if (item.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID must be null.");
        }

        entityManager.persist(item);
        entityManager.refresh(item);

        return item;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Item update(@PathVariable Integer id, @RequestBody Item item)
    {
        if (item.getId() == null || !item.getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID is invalid.");
        }

        // Throws 404 if resource does not exist
        getOne(id);

        return entityManager.merge(item);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Integer id)
    {
        Item item = itemRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NO_CONTENT));

        itemRepository.delete(item);
    }
}

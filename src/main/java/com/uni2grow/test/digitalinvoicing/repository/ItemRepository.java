package com.uni2grow.test.digitalinvoicing.repository;

import com.uni2grow.test.digitalinvoicing.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ItemRepository extends JpaRepository<Item, Integer> {
    @Query("SELECT e FROM Item e " +
           "WHERE e.name LIKE %?1% ")
    Page<Item> findByKey(String key, Pageable paging);
}

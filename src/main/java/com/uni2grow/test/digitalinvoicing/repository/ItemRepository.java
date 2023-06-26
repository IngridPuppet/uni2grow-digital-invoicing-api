package com.uni2grow.test.digitalinvoicing.repository;

import com.uni2grow.test.digitalinvoicing.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Integer> {
}

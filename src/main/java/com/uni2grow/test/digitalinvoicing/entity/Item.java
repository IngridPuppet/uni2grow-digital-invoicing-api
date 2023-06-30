package com.uni2grow.test.digitalinvoicing.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    @NotEmpty
    private String name;

    @Column
    @PositiveOrZero
    private Double price;

    @OneToMany(mappedBy = "item")
    @JsonIgnore
    private List<RelInvoiceItem> relInvoiceItems;
}

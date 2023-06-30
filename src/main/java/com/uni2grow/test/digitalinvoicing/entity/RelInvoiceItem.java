package com.uni2grow.test.digitalinvoicing.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Entity
@Table(name = "rel_invoices_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RelInvoiceItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JsonIgnore
    private Invoice invoice;

    @ManyToOne(optional = false)
    private Item item;

    @Column(nullable = false)
    private Integer quantity;

    // This property records the item's price
    // at the time of issuing the invoice
    @Column(nullable = false)
    @PositiveOrZero
    private Double priceOfRecord;
}

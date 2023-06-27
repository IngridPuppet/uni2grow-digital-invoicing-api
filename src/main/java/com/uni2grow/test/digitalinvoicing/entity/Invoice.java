package com.uni2grow.test.digitalinvoicing.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "invoices")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false, updatable = false)
    @Builder.Default
    private UUID number = UUID.randomUUID();

    @Column
    @Positive
    private Double total;

    @Column(nullable = false)
    private Instant issueDate;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Address billingAddress;

    @OneToMany(mappedBy = "invoice", cascade=CascadeType.MERGE, orphanRemoval = true)
    private List<RelInvoiceItem> relInvoiceItems;

    /**
     * Re-computes the invoice's total.
     */
    public void totalize() {
        double sum = 0.0;

        for (var relInvoiceItem: relInvoiceItems) {
            sum += relInvoiceItem.getQuantity() * relInvoiceItem.getPriceOfRecord();
        }

        total = sum;
    }
}

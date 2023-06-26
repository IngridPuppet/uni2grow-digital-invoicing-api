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

    @Column(unique = true)
    @GeneratedValue
    private UUID number;

    @Column
    @Positive
    private Double total;

    @Column
    private Instant issueDate;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private Address billingAddress;

    @OneToMany(mappedBy = "invoice")
    private List<RelInvoiceItem> relInvoiceItems;
}

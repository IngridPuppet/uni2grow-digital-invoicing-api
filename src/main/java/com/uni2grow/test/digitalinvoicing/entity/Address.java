package com.uni2grow.test.digitalinvoicing.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "addresses")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String street;

    @Column(nullable = false)
    private String city;

    @Column
    private String state;

    @Column
    private String country;

    @Column
    private String zipCode;

    @OneToMany(mappedBy = "address")
    @JsonIgnore
    private List<Customer> customers;

    @OneToMany(mappedBy = "billingAddress")
    @JsonIgnore
    private List<Invoice> invoices;

    @PreRemove
    private void hookPreRemove() {
        customers.forEach(customer -> customer.setAddress(null));
        invoices.forEach(invoice -> invoice.setBillingAddress(null));
    }
}

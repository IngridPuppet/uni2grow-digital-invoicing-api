package com.uni2grow.test.digitalinvoicing.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import java.util.List;

@Entity
@Table(name = "customers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    @Email
    private String email;

    @Column
    private String phone;

    @ManyToOne(optional = false)
    private Address address;

    @OneToMany(mappedBy = "customer")
    @JsonIgnore
    private List<Invoice> invoices;

    @PreRemove
    private void preRemove() {
        invoices.forEach(invoice -> invoice.setCustomer(null));
    }
}

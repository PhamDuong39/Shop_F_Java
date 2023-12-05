package com.example.Common.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "Orders")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "AccountId")
    @ManyToOne
    private Account account;

    private double OrginalPrice;
    private String Fullname;
    private String SDT;
    private String Address;
    private LocalDate CreatedAt;
    private LocalDate UpdatedAt;
}

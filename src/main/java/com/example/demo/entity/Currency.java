package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Data
@Setter
@Entity
@Table(name="\"Currency\"")
public class Currency {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name="currency", length = 3)
    private String currency;
    @Column(name = "currency_name")
    private String currencyName;
    public Currency(){}
    public Currency(UUID id, String currency, String currencyName) {
        this.id = id;
        this.currency = currency;
        this.currencyName = currencyName;
    }

    public UUID getId() {
        return id;
    }

    public String getCurrency() {
        return currency;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public String ToString()
    {
        return currency + " " + currencyName;
    }
}

package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Data
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "\"DollarExchangeRate\"")
public class DollarExchangeRate {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @ManyToOne(cascade = CascadeType.ALL)
    private Currency currency;
    @Column(name = "dollar_rate")
    private Double dollarRate;
    private ZonedDateTime date;
}

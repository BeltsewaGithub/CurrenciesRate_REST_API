package com.example.demo;

import com.example.demo.entity.DollarExchangeRate;

import java.util.Comparator;

public class ExchangeRateDateComparator implements Comparator<DollarExchangeRate>
{
    @Override
    public int compare(DollarExchangeRate o1, DollarExchangeRate o2) {
        return o1.getDate().compareTo(o2.getDate());
    }
}

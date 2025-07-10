package com.example.demo.controller;

import com.example.demo.ExchangeRateDateComparator;
import com.example.demo.HibernateSessionFactoryUtil;
import com.example.demo.entity.Currency;
import com.example.demo.entity.DollarExchangeRate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

//@Slf4j
@RestController
@RequiredArgsConstructor
public class MainController
{
    @Autowired
    private CurrencyRepository currencyRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @GetMapping("/api/main")
    public String mainListener()
    {
        return "Hello World!";
    }
    //@RequestParam String currencyCode
    @SneakyThrows
    @GetMapping("/api/getAllCurrencies")
    public String getAll()
    {
        String jsonData = null;

        List<Currency> currencies = currencyRepository.findAll();
        try {
            jsonData = objectMapper.writeValueAsString(currencies);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return jsonData;
    }
    @PostMapping("/api/postCurrency")
    public String addCurrency(@RequestBody Currency currency)
    {
        try
        {
            currencyRepository.save(currency);
            return "success";
        }
        catch (Exception ex)
        {
          return ex.getMessage();
        }
    }
    @GetMapping("/api/getExchangeRate")
    public String getExchangeRate(@RequestParam String currencyFromCode, @RequestParam String currencyToCode)
    {
        return this.getExchangeRate(currencyFromCode, currencyToCode, 1.0);
    }
    @GetMapping("/api/getExRate")
    public String getExchangeRate(@RequestParam(name = "currencyFromCode") String currencyFromCode,
                                  @RequestParam(name = "currencyToCode") String currencyToCode,
                                  @RequestParam(name = "ratio") Double ratio)
    {
String res = "";
        //ratio*currencyFrom = ratio*n*currencyTo; return n;
        var session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        String hql = "from Currency where currency = :currencyCode";

        Query<Currency> getCurrency = session.createQuery(hql, Currency.class);

        getCurrency.setParameter("currencyCode", currencyFromCode);
        Currency currencyFrom = getCurrency.list().get(0);

        getCurrency.setParameter("currencyCode", currencyToCode);
        Currency currencyTo = getCurrency.list().get(0);

        String hqlCurFrom = "from DollarExchangeRate where currency = :currencyId";

        Query<DollarExchangeRate> getCurrencyFromExchangeRateQuery = session.createQuery(hqlCurFrom);
//        getCurrencyFromExchangeRateQuery.setParameter("currencyId", currencyFrom.getId());
        getCurrencyFromExchangeRateQuery.setParameter("currencyId", currencyFrom);
        List<DollarExchangeRate> currencyFromRate = getCurrencyFromExchangeRateQuery.list();

        Query<DollarExchangeRate> getCurrencyToExchangeRateQuery = session.createQuery(hqlCurFrom);
        getCurrencyToExchangeRateQuery.setParameter("currencyId", currencyTo);
        List<DollarExchangeRate> currencyToRate = getCurrencyToExchangeRateQuery.list();

        Comparator comparator = new ExchangeRateDateComparator();
        Collections.sort(currencyFromRate, Collections.reverseOrder(comparator));
        Collections.sort(currencyToRate, Collections.reverseOrder(comparator));

        DollarExchangeRate derFrom = currencyFromRate.get(0);
        Double dollarRateCurrencyFrom = derFrom.getDollarRate();

        DollarExchangeRate derTo = currencyToRate.get(0);
        Double dollarRateCurrencyTo = derTo.getDollarRate();

        res =
                ratio + " " + currencyFrom.getCurrency() + " = " + ratio*(dollarRateCurrencyTo/dollarRateCurrencyFrom);
        return res;
    }

}

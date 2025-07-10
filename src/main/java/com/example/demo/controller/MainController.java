package com.example.demo.controller;

import com.example.demo.entity.Currency;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

//@Slf4j
@RestController
@RequiredArgsConstructor
public class MainController
{
    /*
    @Autowired
    private CatRepository catRepo;
    @GetMapping("/api/getCat")
    public String getCat()
    {
        Cat cat = new Cat("Barsik", 5);
        String jsonData = null;
        try {
            jsonData = objectMapper.writeValueAsString(cat);
        } catch (JsonProcessingException e) {
            System.out.println("Error with cat");
        }
        return jsonData;
    }
    @PostMapping("/api/postCat")
    public void addCat(@RequestBody Cat cat)
    {
        catRepo.save(cat);
    }
    */
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
    public void addCurrency(@RequestBody Currency currency)
    {
        currencyRepository.save(currency);
    }


}

package com.example.demo;

import com.example.demo.entity.Currency;
import org.hibernate.query.Query;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import org.hibernate.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class CurrenciesRateApplication {

	public static void main(String[] args)
	{
		SpringApplication.run(CurrenciesRateApplication.class, args);

		try {
			Parser.fill();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

//		for(Currency i : Parser.findAll()){
//			System.out.println(i.getCurrencyName() + " " + i.getCurrency());
//		}

	}

}

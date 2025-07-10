package com.example.demo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class CurrenciesRateApplication {

	public static void main(String[] args)
	{
		SpringApplication.run(CurrenciesRateApplication.class, args);
		try {
			fill();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	public static void fill() throws IOException
	{
		List<List<String>> currenciesList = getCurrencies();

		String url = "jdbc:postgresql://localhost:5432/ExchangeRate";
		String user = "postgres";
		String password = "73590332417PoL";

		try
		{
			Connection con = DriverManager.getConnection(url, user, password);
			Statement st = con.createStatement();

			for (List<String> a : currenciesList)
			{
				String query = "CREATE EXTENSION IF NOT EXISTS \"uuid-ossp\"; INSERT INTO public" +
						".\"Currency\" VALUES (uuid_generate_v4(), '" + a.get(1) + "', '" + a.get(2) + "'" + ")";
				//st.executeQuery(query);
				st.execute(query);
			}
//			ResultSet rs = st.executeQuery("SELECT * FROM public.\"Currency\"");
//			while (rs.next()) {
//				//System.out.println(rs.getString(1) + rs.getString(2) + rs.getString(3));
//			}

		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public static List<List<String>> getCurrencies()
	{
		List<List<String>> currenciesList = new ArrayList<List<String>>();
		try {

			//Поиск всех данных таблицы на сайте
			Document doc =
					Jsoup.connect("https://www.softtour.by/asian-currencies").get();
			Elements currency = doc.select("tbody > tr");
			ArrayList currenciesTableData = new ArrayList<>();
			for (var cur : currency) {
				cur.getElementsByTag("td").stream().forEach(t ->
				{
					if (t.text() != "") currenciesTableData.add(t.text());
				});
			}

			//Заполнение currenciesList
			String[] currency1 = new String[4];
			int j = 0;
			for(var a : currenciesTableData)
			{
				if (j < 4) {
					currency1[j] = a.toString();
					j++;
				}
				else
				{
					currenciesList.add(Arrays.stream(currency1).toList());
					j = 0;
					currency1[j] = a.toString();
					j++;
				}
			}
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		return currenciesList;
	}
}

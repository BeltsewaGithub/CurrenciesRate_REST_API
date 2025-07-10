package com.example.demo;

import com.example.demo.entity.Currency;
import org.hibernate.Transaction;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Parser {
    public static List<Currency> findAll() {
        List<Currency> currencies =
                (List<Currency>) HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("From Currency").list();
        return currencies;
    }
    public static void fill() throws IOException
    {

        List<List<String>> currenciesList = getCurrencies();
        for (List<String> a : currenciesList)
        {
            var session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
            session.beginTransaction();
            String currencyCode = a.get(1);
            String currencyName = a.get(2);
            String query = "from Currency" + " where currency = '" + currencyCode + "'";
            if(session.createQuery(query).list().isEmpty())
            {
                UUID id = null;
                Currency currency = new Currency(id, currencyCode, currencyName);
                session.save(currency);
            }
            Transaction tx = session.getTransaction();
            tx.commit();
            session.close();
			/*String query = "CREATE EXTENSION IF NOT EXISTS \"uuid-ossp\"; INSERT INTO public" + ".\"Currency\"
			VALUES (uuid_generate_v4(), '" + a.get(1) + "', '" + a.get(2) + "'" + ")";
			HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery(query);
			*/
        }
    }
    public static List<List<String>> getCurrencies()
    {
        String sourseUrl = "https://www.softtour.by/asian-currencies";
        List<List<String>> currenciesList = new ArrayList<List<String>>();
        try {

            //Поиск всех данных таблицы на сайте
            Document doc =
                    Jsoup.connect(sourseUrl).get();
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
/*
<!-- обычное подключение без hibernate -->
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
        st.executeQuery(query);
        st.execute(query);
    }

    ResultSet rs = st.executeQuery("SELECT * FROM public.\"Currency\"");
    while (rs.next()) {
        //System.out.println(rs.getString(1) + rs.getString(2) + rs.getString(3));
    }

} catch (Exception ex) {
    System.out.println(ex.getMessage());
}
st.executeQuery(query);
st.execute(query);
*/
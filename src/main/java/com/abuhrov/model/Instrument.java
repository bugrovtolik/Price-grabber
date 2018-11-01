package com.abuhrov.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class Instrument {
    private String url;
    private String name;
    //EUR by default
    private Currency currency = Currency.getInstance(Locale.GERMANY);
    private double price;
    private double oldPrice;

    public void save() {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:price-grabber.db");
             Statement stmt = conn.createStatement()) {
            // create a new table if not exists
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS instrument (\n"
                    + "	id integer PRIMARY KEY,\n"
                    + "	url text,\n"
                    + "	name text NOT NULL,\n"
                    + "	currency text NOT NULL,\n"
                    + "	price real NOT NULL,\n"
                    + "	old_price real\n"
                + ");"
            );
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:D:price-grabber.db");
            PreparedStatement pstmt = conn.prepareStatement(
                "INSERT INTO instrument(url,name,currency,price,old_price) " +
                    "VALUES(?,?,?,?,?)"
            )) {
                pstmt.setString(1, url);
                pstmt.setString(2, name);
                pstmt.setString(3, currency.getCurrencyCode());
                pstmt.setDouble(4, price);
                pstmt.setDouble(5, oldPrice);
                pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static List<Instrument> selectAll(){
        String sql = "SELECT * FROM instrument";
        List<Instrument> instruments = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:price-grabber.db");
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            while (rs.next()) {
                Instrument instrument = new Instrument();
                instrument.setUrl(rs.getString("url"));
                instrument.setName(rs.getString("name"));
                instrument.setCurrency(Currency.getInstance(rs.getString("currency")));
                instrument.setPrice(rs.getDouble("price"));
                instrument.setOldPrice(rs.getDouble("old_price"));
                instruments.add(instrument);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return instruments;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(double oldPrice) {
        this.oldPrice = oldPrice;
    }
}

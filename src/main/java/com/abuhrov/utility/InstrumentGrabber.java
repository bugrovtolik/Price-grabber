package com.abuhrov.utility;

import com.abuhrov.model.Instrument;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Currency;
import java.util.Locale;

public class InstrumentGrabber {
    public Instrument parseUrl(String url) throws IOException, ParseException {
        Locale locale = Locale.GERMANY;
        NumberFormat nf = NumberFormat.getInstance(locale);
        String price = "";
        Element element;
        Document document = Jsoup.connect(url)
                .userAgent("Mozilla/4.0")
                .header("Accept-Language", "en")
                .get();

        Instrument instrument = new Instrument();

        //setting url
        instrument.setUrl(url);

        //looking for name
        element = document.select("[itemprop=name]").last();
        if (element == null) {
            throw new ParseException("Couldn't find name!", 0);
        }
        //setting name
        instrument.setName(element.text());

        //looking for price
        element = document.select("[itemprop=price]").first();
        if (element == null) {
            element = document.select("[data-selenium=price]").first();
            if (element == null) {
                element = document.select("[itemprop=lowPrice]").first();
                if (element == null) {
                    throw new ParseException("Couldn't find price!", 0);
                }
            }
        }
        //checking price format (tag value or attribute value)
        if (!element.attr("content").isEmpty()) {
            instrument.setPrice(Double.parseDouble(element.attr("content")));
        } else {
            //if is tag value, then we need to know currency before parse it
            price = element.text();
        }

        //looking for currency
        element = document.select("[itemprop=priceCurrency]").first();
        if (element != null) {
            instrument.setCurrency(Currency.getInstance(element.attr("content")));
        } else {
            for (Currency currency : Currency.getAvailableCurrencies()) {
                if (price.startsWith(currency.getSymbol(locale)) || price.endsWith(currency.getSymbol(locale))) {
                    instrument.setCurrency(currency);
                    break;
                }
            }
        }

        //if we haven't set price yet
        if (!price.isEmpty()) {
            instrument.setPrice(
                nf.parse(
                    price.replace(
                        instrument.getCurrency().getSymbol(locale),
                        ""
                    )
                ).doubleValue()
            );
        }

        element = document.select(".prod-pricebox-price-uvp-retail").first();

        if (element != null) {
            instrument.setOldPrice(
                nf.parse(
                    element.text().replace(
                        instrument.getCurrency().getSymbol(locale),
                        ""
                    )
                ).doubleValue()
            );
        }

        return instrument;
    }
}

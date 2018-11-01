package com.abuhrov;

import com.abuhrov.model.Instrument;
import com.abuhrov.utility.InstrumentGrabber;

import java.io.IOException;
import java.text.ParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        InstrumentGrabber grabber = new InstrumentGrabber();
        String leftAlignFormat = "| %-60s | %9.2f %-3s | %9.2f %-3s |%n";
        Scanner scanner = new Scanner(System.in);
        String url = "";
        System.out.println("Enter \"db\" to get all data from db, empty line to exit..");
        do {
            System.out.print("URL: ");

            try {
                url = scanner.nextLine();
            } catch (InputMismatchException ex){
                System.out.println("Couldn't get that!");
                continue;
            }

            if (url.equals("db")) {
                List<Instrument> instruments = Instrument.selectAll();

                System.out.format("+--------------------------------------------------------------+---------------+---------------+%n");
                System.out.format("|                             Name                             |     Price     |   Old price   |%n");
                System.out.format("+--------------------------------------------------------------+---------------+---------------+%n");

                for (Instrument instrument : instruments) {
                    System.out.format(
                            leftAlignFormat,
                            instrument.getName(),
                            instrument.getPrice(),
                            instrument.getCurrency().getSymbol(Locale.GERMANY),
                            instrument.getOldPrice(),
                            instrument.getCurrency().getSymbol(Locale.GERMANY)
                    );
                }

                System.out.format("+--------------------------------------------------------------+---------------+---------------+%n");
            } else if (!url.isEmpty()) {
                Instrument instrument = null;
                try {
                    instrument = grabber.parseUrl(url);
                } catch (IOException | ParseException e) {
                    System.out.println(e.getMessage());
                    return;
                }

                instrument.save();
            }
        } while (!url.isEmpty());
        System.out.println("Closing..");
    }
}
package com.abuhrov;

import com.abuhrov.model.Instrument;
import org.junit.Test;

public class InstrumentTest {

    @Test
    public void create() {
        Instrument instrument = new Instrument();
        instrument.setName("");

        System.out.println(instrument.getCurrency().getNumericCode());
        System.out.println(instrument.getName());
        System.out.println(instrument.getUrl());
        System.out.println(instrument.getPrice());
        System.out.println(instrument.getOldPrice());
    }
}
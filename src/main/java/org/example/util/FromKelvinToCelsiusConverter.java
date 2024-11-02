package org.example.util;

public class FromKelvinToCelsiusConverter implements TempConverter {

    @Override
    public double convert(double temp) {
        return Math.ceil((temp -273.15)*100)/100;
    }
}

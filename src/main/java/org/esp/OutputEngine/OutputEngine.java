package org.esp.OutputEngine;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class OutputEngine {
    public void outputMessage(String outputMessage, double rollingAverage){
        System.out.println("[Output Engine]: Average rolling " +
                (outputMessage != null ? outputMessage: "") +
                BigDecimal.valueOf(rollingAverage).setScale(2, RoundingMode.HALF_UP).doubleValue());
    }
}

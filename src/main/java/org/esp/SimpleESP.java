package org.esp;

import org.esp.IngestionEngine.IngestionEngine;
import org.esp.OutputEngine.OutputEngine;
import org.esp.ProcessingEngine.ProcessingEngine;

public class SimpleESP {
    public static void main(String[] args) {
        int ingestionPort = 8080;
        double windowSize = 10;
        new IngestionEngine(
                new ProcessingEngine(
                        new OutputEngine(),
                        windowSize
                ),
                ingestionPort
        );
    }
}

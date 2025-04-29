package org.esp.ProcessingEngine;


import org.esp.OutputEngine.OutputEngine;
import org.esp.model.Event;

import java.util.LinkedList;
import java.util.Queue;

public class ProcessingEngine {
    private final Double windowSize;
    private final OutputEngine outputEngine;
    private final Queue<Double> numberWindow;
    private double currentSum;

    public ProcessingEngine(OutputEngine outputEngine, Double windowSize) {
        this.outputEngine = outputEngine;
        this.windowSize = windowSize;
        this.currentSum = 0;
        this.numberWindow = new LinkedList<>();
    }

    public void processEvent(Event event) {
        try {
            System.out.println("[Processing Engine]: Processed Event " + event);
            double eventNumber = Double.parseDouble(event.getRawData());
            numberWindow.add(eventNumber);
            currentSum += eventNumber;

            System.out.println("[Processing Engine]: Parsed " + eventNumber);
            if (numberWindow.size() > windowSize) {
                double oldestNumber = numberWindow.remove();
                currentSum -= oldestNumber;
            }

            double rollingAverage = currentSum / numberWindow.size();
            if (rollingAverage > (rollingAverage/2))
                outputEngine.outputMessage("High value detected: ", rollingAverage);
            if (!numberWindow.isEmpty())
                outputEngine.outputMessage(null, rollingAverage);

            else System.out.println("[Processing Engine Info]: Window is empty, no average yet.");

        } catch (NumberFormatException e) {
            System.out.println("[Processing Engine Error]: Error Parsing eventData " + e.getMessage());
        }
    }
}

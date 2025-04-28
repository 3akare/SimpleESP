package ProcessingEngine;

import java.util.LinkedList;
import java.util.Queue;

public class ProcessingEngine {
    private final int windowSize;
    private final Queue<Integer> numberWindow;
    private int currentSum;

    public ProcessingEngine(int windowSize){
        this.windowSize = windowSize;
        this.currentSum = 0;
        this.numberWindow = new LinkedList<Integer>();
    }

    public void processEvent(String eventDataLine){
        System.out.println("[Processing Engine]: Processed Event " + eventDataLine);
        int eventNumber = Integer.parseInt(eventDataLine);
        numberWindow.add(eventNumber);
        currentSum += eventNumber;

        try{
            System.out.println("[Processed]: Parsed Event " + eventNumber);
            if (numberWindow.size() > windowSize) {
                int oldestNumber = numberWindow.remove();
                currentSum -= oldestNumber;
            }

            if (!numberWindow.isEmpty()) {
                System.out.println("[Processed]: rollingAverage " + currentSum / numberWindow.size());
            } else {
                System.out.println("[Processing Engine Info]: Window is empty, no average yet.");
            }

        } catch(NumberFormatException e){
            System.out.println("[Processed Engine Error]: Error Parsing eventData " + e.getMessage());
        }
    }
}

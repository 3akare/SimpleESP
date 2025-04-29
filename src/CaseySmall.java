import IngestionEngine.IngestionEngine;
import OutputEngine.OutputEngine;
import ProcessingEngine.ProcessingEngine;

public class CaseySmall {
    public static void main(String[] args) {
        int ingestionPort = 8080;
        double windowSize = 10;

        IngestionEngine ingestionEngine = new IngestionEngine(
                new ProcessingEngine(
                        new OutputEngine(), windowSize), ingestionPort
        );
    }
}

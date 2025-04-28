import IngestionEngine.IngestionEngine;
import ProcessingEngine.ProcessingEngine;

public class CaseySmall{
  public static void main(String[] args){
    int ingestionPort = 8080;
    int windowSize = 10;

    ProcessingEngine processingEngine = new ProcessingEngine(windowSize);
    IngestionEngine ingestionEngine = new IngestionEngine(processingEngine, ingestionPort);
  }
}

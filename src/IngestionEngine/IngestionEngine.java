package IngestionEngine;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import ProcessingEngine.ProcessingEngine;
import model.Event;

public class IngestionEngine {
    private final int ingestionPort;
    private final ProcessingEngine processingEngine;

    public IngestionEngine(ProcessingEngine processingEngine, int ingestionPort) {
        this.processingEngine = processingEngine;
        this.ingestionPort = ingestionPort;
        startEngine();
    }

    private void startEngine(){
        System.out.println("[Ingestion Engine]: Starting...");
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(this.ingestionPort);
            System.out.println("[Ingestion Engine]: Started and listening on port " + this.ingestionPort);
            while (true) {
                Socket socket;
                System.out.println("[Ingestion Engine]: Waiting for a client connection...");
                socket = serverSocket.accept();
                System.out.println("[Ingestion Engine]: Client connected: " + socket.getInetAddress());
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String eventline;


                while ((eventline = reader.readLine()) != null) {
                    Event event = new Event(eventline);
                    if (event.getRawData().equalsIgnoreCase("stop")) {
                        System.out.println("[Ingestion Engine]: Received 'stop' from client. Closing connection.");
                        break;
                    }
                    System.out.println("[Ingestion Engine]: Ingested " + event);
                    processingEngine.processEvent(event);
                }
                reader.close();
                if (!socket.isClosed()) socket.close();
                System.out.println("[Ingestion Engine Info]: Client disconnected (readLine returned null).");
            }
        } catch (IOException serverSocketCreationError) {
            System.out.println("[Ingestion Engine Error]: Could not start server socket: " + serverSocketCreationError.getMessage());
        } finally {
            try {
                if (serverSocket != null && !serverSocket.isClosed()) serverSocket.close();
            } catch (IOException closeServerError) {
                System.out.println("[Ingestion Engine Error]: Error closing server socket: " + closeServerError.getMessage());
            }
        }
        System.out.println("[Ingestion Engine]: Server stopped.");
    }
}

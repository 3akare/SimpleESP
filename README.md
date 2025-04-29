# Simple Event Stream Processor (SimpleESP)
**Author:** David Bakare

## Project Goal

This project is a hands-on implementation of a simplified Event Stream Processor (ESP). The primary goal is to learn and demonstrate the core concepts and challenges of building systems that process continuous streams of data in real-time.

It showcases the understanding of:
* The Event Stream Processing paradigm.
* Handling continuous, unbounded data streams ("data in motion").
* Breaking down a real-time processing system into modular components.
* Basic state management in a streaming context.
* Fundamental time awareness in event processing.

While inspired by potential use cases in **IoT/embedded systems** (handling streams of sensor data) and concepts applicable to **fintech** (real-time event analysis), this version is built as a **single-application, non-production-grade** system to focus on core principles rather than distributed complexity or robust fault tolerance.

## Core Concepts Demonstrated

This project illustrates the following key ESP concepts:

* **Event Streams:** Processing data as a continuous flow of discrete events.
* **Real-time Processing:** Analyzing and acting on events as they arrive with low latency.
* **Pipeline Architecture:** Data flowing sequentially through modular components (Ingestion -> Processing -> Output).
* **Ingestion:** Receiving external events into the system.
* **Processing:** Applying business logic (transformations, aggregations, filtering) to events.
* **State Management (Simplified):** Maintaining minimal in-memory state necessary for processing (e.g., for aggregations).
* **Output:** Delivering processing results to a destination.
* **Basic Time Awareness:** Associating a timestamp with events as they enter the system.

## Architecture (Simplified, Single-Application)

The SimpleESP is designed with a clear separation of concerns, implemented as distinct classes/modules within a single Java application process. Data flows unidirectionally through the defined pipeline:

```
+-----------------+     +-------------------+     +--------------+     +--------------+
| Event Producer  | --> | Ingestion Engine  | --> | Processing   | --> | Output       |
| (e.g., nc,      |     | (Network Socket)  |     | Engine       |     | Engine       |
| script, sim.)   |     |                   |     | (Logic +     |     | (e.g., Console)|
|                 |     | Reads raw events, |     | Simple State)|     | Prints results|
|                 |     | adds timestamp,   |     | Applies rules,|     |              |
|                 |     | creates Event obj.|     | updates state,|     |              |
|                 |     |                   |     | generates     |     |              |
|                 |     |                   |     | output data   |     |              |
+-----------------+     +-------------------+     +--------------+     +--------------+
```

In this simplified version, the "engines" are not separate processes or services; they are interconnected components running within the same Java Virtual Machine (JVM).

## Components (Engines)
The project is structured into the following main components:

### 1. Ingestion Engine (`IngestionEngine.java`)

* **Role:** Responsible for receiving raw event data from external sources and converting it into a standardized internal `Event` object.
* **Implementation:** Implemented using a `ServerSocket` to listen for incoming TCP connections on a specified port. It continuously accepts connections (sequentially in this version) and reads line-based string data from connected clients using `BufferedReader`.
* **Time Awareness:** Crucially, upon receiving a raw data string, it creates an `Event` object and adds a **timestamp** representing the time the event was ingested into the system (`System.currentTimeMillis()`).
* **Handoff:** Passes the created `Event` object to the `ProcessingEngine`.

### 2. Processing Engine (`ProcessingEngine.java`)
* **Role:** The core of the ESP, responsible for applying defined processing logic to the incoming `Event` stream.
* **Implementation:** Receives `Event` objects from the `IngestionEngine`. It extracts the raw data and timestamp and applies one or more processing rules.
* **Processing Logic Examples (Implemented):**
    * **Rolling Average:** Calculates the average of the last `N` numbers received in the stream. This requires maintaining state.
    * **Simple Filtering/Thresholding:** Checks if the incoming number exceeds a predefined threshold and triggers a separate output if it does.
* **Simplified State Management:** Uses in-memory Java collections (`Queue` and simple variables) to store the minimal state needed for operations like the rolling average (the last `N` numbers and their sum).
* **Handoff:** Passes the results of the processing (e.g., the calculated average, filter alerts) to the `OutputEngine`. Includes basic error handling for tasks like parsing incoming data.

### 3. Simplified State Management (within Processing Engine)
* **Role:** To store and manage the necessary information from past events required for processing current events (e.g., the list of numbers for the rolling average).
* **Implementation:** In this simplified version, state is managed using standard Java collections (`java.util.Queue`) held as instance variables within the `ProcessingEngine` class.
* **Limitations:** This state is **in-memory only**, **not persistent** (lost on application restart), and **not distributed** (only exists within this single application instance). This is a deliberate simplification for learning, contrasting with the complex distributed state stores in production ESPs.

### 4. Output Engine (`OutputEngine.java`)
* **Role:** Responsible for taking the processed results from the `ProcessingEngine` and delivering them to a final destination.
* **Implementation:** In this base version, it simply formats the received data (e.g., the rolling average) and prints it to the standard console (`System.out.println`).
* **Expandability:** This component could be extended to send data to a file, a database, another network service, etc.

### 5. Event Model (`Event.java`)
* **Role:** A simple data class to represent a processed event within the pipeline.
* **Implementation:** A POJO (Plain Old Java Object) containing the raw event data (e.g., the input string) and the timestamp assigned by the Ingestion Engine.

## Implementation Details
* **Language:** Java SE
* **Code Structure:** Organized into logical packages (e.g., `ingestion`, `processing`, `output`, `model`).

## How to Run and Test
1.  **Clone the repository:**
    ```bash
    git clone https://github.com/3akare/SimpleESP.git
    ```
2.  **Build the project:**
    ```bash
    # Using Maven:
    mvn clean install package
    ```
3.  **Run the application:**
    ```bash
    # Using Maven:
    java -jar target/SimpleESP-1.0-SNAPSHOT.jar 
    ```
    The application should start and the Ingestion Engine will begin listening on the configured port (defaulting to 8080).
4.  **Send test data using `nc` (netcat):**
    * Open a terminal.
    * Connect to the server:
        ```bash
        nc localhost 8080
        ```
    * Type numbers (one per line) and press Enter. You should see the raw input in the Ingestion Engine console and the calculated rolling average/filter output in the Output Engine console.
    * Type `stop` and press Enter to close the connection from that client. The server will then listen for the next connection.
5.  **Use the test script (Optional):**
    * If you created the `test_ingestion.sh` script, make it executable (`chmod +x test_ingestion.sh`).
    * Run it to simulate multiple clients connecting sequentially:
        ```bash
        ./test_ingestion.sh [port] [num_clients]
        ```

## Limitations of this Simplified Version
It is important to understand that this SimpleESP is a learning and portfolio project and has significant limitations compared to production-ready Event Stream Processing platforms like Apache Kafka Streams, Apache Flink, etc. These limitations were deliberate to focus on core concepts without overwhelming complexity:
* **No Distribution:** The entire system runs as a single application instance on one machine. It cannot scale horizontally to handle high data volumes beyond the capacity of that machine.
* **No Robust Fault Tolerance:** If the application crashes or the machine fails, processing stops, and any in-memory state is lost. There are no mechanisms for automatic recovery or ensuring data is not lost or processed exactly once in case of failures.
* **No Persistent or Distributed State:** State is only in the memory of a single process. Real ESPs use distributed, persistent state stores (like RocksDB, distributed databases) to ensure state survives failures and is accessible from any processing instance.
* **Basic Time Handling:** Events are primarily ordered by ingestion time. Handling out-of-order events based on their *original* event time requires more sophisticated techniques (like watermarks) not implemented here.
* **Limited Processing Features:** Only a few specific processing rules are implemented. Production ESPs offer a wide range of operators (joins, complex aggregations, window types, etc.).
* **Sequential Client Handling (in Ingestion):** The current `ServerSocket` implementation handles client connections one after another. A production server would handle many concurrent connections using threads or asynchronous I/O.

## Potential Future Enhancements

This project could be extended in many ways to explore more advanced ESP concepts:
* Implement concurrent client handling in the Ingestion Engine using threads or NIO.
* Add persistence to the state management (e.g., save state to a local file or database periodically).
* Explore distributing the processing across multiple machines (a significant undertaking).
* Implement more advanced time management (event time, watermarks, different window types).
* Add more complex processing rules or a more flexible rule definition mechanism.
* Implement different output types (e.g., writing to a file, sending to a message queue).
* Integrate with a real message queue like Apache Kafka for ingestion and output.
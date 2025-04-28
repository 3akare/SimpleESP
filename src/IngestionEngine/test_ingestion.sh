#!/bin/bash

# --- Configuration ---
HOST="localhost"
PORT=${1:-8080}
NUM_CLIENTS=${2:-5}
MESSAGE="HelloFromClient"
STOP_COMMAND="stop"

# --- Usage Check ---
if [ "$#" -gt 2 ]; then
    echo "Usage: $0 [port] [num_clients]"
    echo "Example: $0 8080 10"
    echo "Example: $0"
    exit 1
fi

echo "--- Preparing $NUM_CLIENTS clients ---"

# Create a named pipe for synchronization
PIPE_NAME="/tmp/trigger_pipe"
rm -f $PIPE_NAME
mkfifo $PIPE_NAME

# Start clients, waiting for the trigger
for i in $(seq 1 $NUM_CLIENTS); do
    {
        # Wait for the trigger
        read < $PIPE_NAME
        # After trigger, connect and send
        {
            echo "$MESSAGE #$i"
            sleep 0.5
            echo "$STOP_COMMAND"
        } | nc "$HOST" "$PORT"
    } &
done

echo "--- All clients ready. Triggering simultaneous send. ---"
# Broadcast trigger to all clients
for i in $(seq 1 $NUM_CLIENTS); do
    echo "GO" > $PIPE_NAME
done

# Cleanup
rm -f $PIPE_NAME

echo "--- All clients triggered ---"
exit 0

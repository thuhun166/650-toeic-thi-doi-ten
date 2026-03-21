# Distributed Token Ring System

This project implements a distributed system with 6 servers in a virtual ring using the Token Ring algorithm (jeton) for mutual exclusion and fault tolerance.

## Features
- 6 servers in virtual ring topology with token passing
- Lamport clock for event ordering across nodes
- Each node has its own MySQL database
- 1 universal client for testing and sending data
- Token cycles through the ring: Node1 → Node2 → ... → Node6 → Node1

## Project Structure
- `src/client/`: 1 universal client application to test all nodes
- `src/server/`: Server components (Main, TokenRing, NodeHandler, Database, etc.)

## How to Run Locally

1. Build: `javac -cp build -d build src/server/*.java src/client/*.java`
2. Setup DB: Run `setup.sql` to create databases and tables.
3. Start servers (each in separate terminal):
   ```
   NODE_ID=1 PORT=2001 MYSQL_URL=jdbc:mysql://localhost:3306/db1 java -cp build server.Main
   NODE_ID=2 PORT=2002 MYSQL_URL=jdbc:mysql://localhost:3306/db2 java -cp build server.Main
   ...
   NODE_ID=6 PORT=2006 MYSQL_URL=jdbc:mysql://localhost:3306/db6 java -cp build server.Main
   ```
4. Run client: `java -cp build client.Client`

## Deploy to Railway

Each team member:
1. Fork the repo to your GitHub.
2. Create Railway account and new project from forked repo.
3. Add MySQL plugin for each project.
4. Set Environment Variables:
   - `NODE_ID` (1-6, unique per person)
   - `PORT` (8080, auto-assigned by Railway)
   - `MYSQL_URL` (from MySQL plugin)
   - `PEERS` (Railway URLs of all 6 nodes)
5. Deploy and get your URL.

## Token Ring Algorithm

- Node 1 starts with the token.
- When a node has the token, it can process requests and store data.
- Token passes to the next node in the ring automatically.
- Lamport clock updates with each message to maintain causal ordering.

## Project Structure
- `src/client/`: Client applications (GUI and Web)
- `src/server/`: Server components (Main, handlers, database, etc.)

## How to Run
1. Install JDK and MySQL.
2. Create databases: db1 to db6 with tables server1 to server6.
3. Run InitialServer for each server.
4. Run Server1 to Server6 in separate terminals.
5. Run Client to interact via GUI or web interface.
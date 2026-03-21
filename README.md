# Distributed System Project

This project implements a distributed system with 6 servers in a virtual ring using the Token Ring algorithm (jeton) for mutual exclusion and fault tolerance.

## Features
- 6 servers in virtual ring topology
- Token passing for synchronization
- Lamport clock for event ordering
- MySQL databases for each server
- Fault detection and recovery
- Creative improvement: Web-based client interface using simple HTTP server

## How to Run
1. Install JDK and MySQL.
2. Create databases: db1 to db6 with tables server1 to server6.
3. Run InitialServer for each server.
4. Run Server1 to Server6 in separate terminals.
5. Run Client to interact via GUI or web interface.
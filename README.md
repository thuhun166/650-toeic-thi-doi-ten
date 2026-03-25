# Distributed Print Ring

This project models a distributed print service with 6 servers arranged in a virtual ring and 1 client that submits print jobs.

The focus is the Token Ring algorithm. Real printing is not required; each server writes a reasonable print log to MySQL and console output.

## Why Lamport clock is still reasonable here

Lamport clock is not required to make Token Ring work, because the token itself already provides mutual exclusion.

It is still useful in this project because it helps:

- explain the logical order of distributed events,
- show when a job was queued versus when it was actually printed,
- make logs and database records easier to defend in a cloud/distributed-systems demo.

So in this implementation:

- Token Ring is the main synchronization mechanism.
- Lamport clock is supporting metadata for event ordering and observability.

## System model

- 6 print servers in one virtual ring
- 1 client sends print or cancel requests to any server
- Each server keeps a local queue of pending jobs
- Only the server holding the token may enter the critical section and update its print log
- Print results are stored in MySQL tables `print_jobs` and `ring_metadata`
- Each server connects to its own database, for example `print_ring_node1`, `print_ring_node2`, ..., `print_ring_node6`

## Message protocol

- `PRINT|jobId|documentContent`
- `CANCEL|jobId`
- `QUERY`
- `STATUS`
- Token transfer between servers: `TOKEN|fromNode|lamport|epoch|sequence`

Legacy aliases are also accepted:

- `INSERT|jobId|documentContent` behaves like `PRINT`
- `DELETE|jobId` behaves like `CANCEL`

## Token Ring behavior

1. Node 1 creates the initial token.
2. A client can submit a print job to any node.
3. That node queues the job immediately.
4. When the token arrives, the node processes queued jobs in order.
5. The node logs the print or cancel result in MySQL and console output.
6. After `TOKEN_PASS_DELAY_MS`, the token is sent to the next node in the ring.

## Token loss recovery and duplicate-token protection

This project now includes both items that were missing before:

- Lost token detection:
  Node 1 runs a watchdog. If no token activity is observed for `TOKEN_LOSS_TIMEOUT_MS`, it regenerates a new token.
- Duplicate token protection:
  Every token carries `epoch` and `sequence`.
  Each node stores the highest token it has seen in `ring_metadata`.
  Any stale or duplicate token is ignored.

This is intentionally simple and centralized through Node 1, which keeps the design easy to explain while still solving the common demo failure cases.

## Files that matter

- [src/server/Main.java](C:/Users/Administrator/Desktop/DistributedSystemProject/src/server/Main.java)
- [src/server/TokenRing.java](C:/Users/Administrator/Desktop/DistributedSystemProject/src/server/TokenRing.java)
- [src/server/Database.java](C:/Users/Administrator/Desktop/DistributedSystemProject/src/server/Database.java)
- [src/server/NodeHandler.java](C:/Users/Administrator/Desktop/DistributedSystemProject/src/server/NodeHandler.java)
- [src/server/ProcessData.java](C:/Users/Administrator/Desktop/DistributedSystemProject/src/server/ProcessData.java)
- [src/server/RoutingTable.java](C:/Users/Administrator/Desktop/DistributedSystemProject/src/server/RoutingTable.java)
- [src/client/Client.java](C:/Users/Administrator/Desktop/DistributedSystemProject/src/client/Client.java)

The folder `BaiDoXe/` is only a teacher-style sample reference and is not part of the active runtime.

## Local build

Windows PowerShell:

```powershell
javac -cp "lib/*" -d build src\server\*.java src\client\*.java
```

Linux/macOS:

```bash
javac -cp "lib/*" -d build src/server/*.java src/client/*.java
```

## Local run example

Run 6 nodes in 6 terminals:

```powershell
$env:NODE_ID="1"; $env:PORT="2001"; $env:MYSQL_URL="jdbc:mysql://localhost:3306/print_ring_node1"; $env:PEERS="localhost:2001,localhost:2002,localhost:2003,localhost:2004,localhost:2005,localhost:2006"; java -cp "build;lib/*" server.Main
$env:NODE_ID="2"; $env:PORT="2002"; $env:MYSQL_URL="jdbc:mysql://localhost:3306/print_ring_node2"; $env:PEERS="localhost:2001,localhost:2002,localhost:2003,localhost:2004,localhost:2005,localhost:2006"; java -cp "build;lib/*" server.Main
$env:NODE_ID="3"; $env:PORT="2003"; $env:MYSQL_URL="jdbc:mysql://localhost:3306/print_ring_node3"; $env:PEERS="localhost:2001,localhost:2002,localhost:2003,localhost:2004,localhost:2005,localhost:2006"; java -cp "build;lib/*" server.Main
$env:NODE_ID="4"; $env:PORT="2004"; $env:MYSQL_URL="jdbc:mysql://localhost:3306/print_ring_node4"; $env:PEERS="localhost:2001,localhost:2002,localhost:2003,localhost:2004,localhost:2005,localhost:2006"; java -cp "build;lib/*" server.Main
$env:NODE_ID="5"; $env:PORT="2005"; $env:MYSQL_URL="jdbc:mysql://localhost:3306/print_ring_node5"; $env:PEERS="localhost:2001,localhost:2002,localhost:2003,localhost:2004,localhost:2005,localhost:2006"; java -cp "build;lib/*" server.Main
$env:NODE_ID="6"; $env:PORT="2006"; $env:MYSQL_URL="jdbc:mysql://localhost:3306/print_ring_node6"; $env:PEERS="localhost:2001,localhost:2002,localhost:2003,localhost:2004,localhost:2005,localhost:2006"; java -cp "build;lib/*" server.Main
```

Run the client:

```powershell
java -cp "build;lib/*" client.Client
```

## Railway deployment

Deploy 6 Railway services from the same repository.

Each service needs:

- `NODE_ID`: `1` to `6`
- `PORT`: `8080`
- `BIND_HOST`: `0.0.0.0`
- `MYSQL_URL`: database URL of that specific server
- `PEERS`: both service addresses in ring order
- `TOKEN_PASS_DELAY_MS`: optional, default `1500`
- `TOKEN_MONITOR_INTERVAL_MS`: optional, default `2000`
- `TOKEN_LOSS_TIMEOUT_MS`: optional, default `12000`
- `SOCKET_CONNECT_TIMEOUT_MS`: optional, default `3000`

Example `PEERS`:

```text
node1.railway.internal:8080,node2.railway.internal:8080,node3.railway.internal:8080,node4.railway.internal:8080,node5.railway.internal:8080,node6.railway.internal:8080
```

The repo already includes:

- [Dockerfile](C:/Users/Administrator/Desktop/DistributedSystemProject/Dockerfile)
- [start.sh](C:/Users/Administrator/Desktop/DistributedSystemProject/start.sh)
- [railway.toml](C:/Users/Administrator/Desktop/DistributedSystemProject/railway.toml)

Railway docs used for the deployment assumptions:

- [Builds](https://docs.railway.com/deploy/builds)
- [Variables](https://docs.railway.com/variables)

## Optional SQL bootstrap

If you want to pre-create the local databases manually, use [setup.sql](C:/Users/Administrator/Desktop/DistributedSystemProject/setup.sql).

In normal use, the application will auto-create `print_jobs` and `ring_metadata` inside the database referenced by `MYSQL_URL`. This means you do not need to open phpMyAdmin to create tables manually, as long as the target database already exists.

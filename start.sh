#!/bin/bash
echo "------------------------------------------"
echo "Starting Parking Node ID: $NODE_ID"
echo "Port: $PORT"
echo "------------------------------------------"

# Chạy ứng dụng với Classpath đầy đủ
java -cp "build:lib/mysql-connector.jar" server.Main
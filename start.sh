#!/bin/bash
echo "=========================================="
echo "Token Ring Server ID: $NODE_ID is starting..."
echo "=========================================="

# Lưu ý quan trọng: Railway cấp MYSQL_URL dạng mysql://user:pass@host:port/db
# Nhưng JDBC cần dạng jdbc:mysql://host:port/db
# Chúng ta sẽ xử lý biến này trong code Java hoặc dùng biến môi trường riêng

java -cp "build:lib/mysql-connector.jar" server.Main
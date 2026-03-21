## Hướng Dẫn Chạy Project Token Ring Hệ Phân Tán Với 6 Máy Chủ

### Yêu Cầu
- JDK 8+
- MySQL Server
- Chạy setup.sql để tạo databases và tables

### Bước Chạy Local
1. Biên dịch: `javac -cp build -d build src/server/*.java src/client/*.java`
2. Khởi tạo: Chạy `setup.sql` để tạo db1..db6 và tables server1..server6
3. Chạy servers (mỗi server trong terminal riêng):
   - `NODE_ID=1 PORT=2001 MYSQL_URL=jdbc:mysql://localhost:3306/db1 java -cp build server.Main`
   - `NODE_ID=2 PORT=2002 MYSQL_URL=jdbc:mysql://localhost:3306/db2 java -cp build server.Main`
   - ...
   - `NODE_ID=6 PORT=2006 MYSQL_URL=jdbc:mysql://localhost:3306/db6 java -cp build server.Main`
4. Chạy 1 client duy nhất: `java -cp build client.Client`

### Cơ Chế Token Ring
- Node 1 khởi động với token
- Token di chuyển vòng tròn: N1 → N2 → ... → N6 → N1
- Mỗi node khi có token có thể xử lý request từ client
- Lamport clock cập nhật với mỗi message
- Client gửi INSERT/DELETE/QUERY request đến bất kỳ node nào

### Deploy Railway
1. Mỗi người fork repo, tạo Railway account
2. Add MySQL plugin cho mỗi project
3. Set env: NODE_ID (1-6), MYSQL_URL, PEERS (Railway URLs)
4. Deploy và test qua 1 client
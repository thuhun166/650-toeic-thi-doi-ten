## Hướng Dẫn Chạy Project Hệ Phân Tán Với 6 Máy Chủ

### Yêu Cầu
- JDK 8+
- MySQL Server
- Chạy setup.sql để tạo databases và tables

### Bước Chạy
1. Biên dịch: `find src -name "*.java" -exec javac -d build {} +` (hoặc dùng IDE)
2. Khởi tạo: `java -cp build distributed.server1.InitialServer`
3. Chạy servers (mỗi server trong terminal riêng):
   - `java -cp build distributed.server1.Server1`
   - Tương tự cho Server2-6 (sửa package và port)
4. Chạy client: `java -cp build distributed.client.Client`
5. Web client: `java -cp build distributed.client.WebClient` (truy cập http://localhost:8080)

### Cải Tiến
- Thêm web interface đơn giản
- Mở rộng thành 6 servers
- Fault tolerance cơ bản với token passing
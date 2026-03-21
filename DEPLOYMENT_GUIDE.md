# Hướng Dẫn Triển Khai Chi Tiết - Hệ Thống Token Ring 6 Máy Chủ
## (Complete Deployment Guide - 6-Node Token Ring System)

---

## PHẦN 1: THIẾT LẬP KHO GITHUB CHÍNH (MAIN REPO SETUP)

### Bước 1.1: Người Quản Lý Dự Án Tạo Main Repo

**Người thực hiện:** 1 người (quản lý dự án / trưởng nhóm)

1. Tạo repository mới trên GitHub:
   - Vào https://github.com/new
   - Repository name: `DistributedTokenRing`
   - Description: `Distributed Token Ring System with 6 Servers`
   - Chọn Public (để các thành viên khác có thể fork)
   - Chọn: Add a README file
   - Chọn: Add .gitignore (chọn Java template)

2. Clone repo vừa tạo về máy:
   ```bash
   git clone https://github.com/YOUR_USERNAME/DistributedTokenRing.git
   cd DistributedTokenRing
   ```

3. Copy toàn bộ source code từ project hiện tại vào repo:
   - Copy thư mục `src/` (toàn bộ)
   - Copy thư mục `build/` (toàn bộ)
   - Copy các file: `README.md`, `HUONGDAN.md`, `setup.sql`, `railway.toml`, `manifest.mf`, `config.properties`

4. Tạo các file cấu hình chung:

   **File: `.gitignore`** (nếu chưa có)
   ```
   .DS_Store
   *.class
   *.jar
   *.exe
   build/
   bin/
   dist/
   *.log
   *.sql
   .env
   __pycache__/
   node_modules/
   ```

   **File: `ARCHITECTURE.md`** - Mô tả kiến trúc
   ```markdown
   # Kiến Trúc Hệ Thống
   
   ## Cơ Chế Token Ring
   - 6 servers chạy trên 6 Railway accounts khác nhau
   - Jeton (token) di chuyển vòng tròn: Node1 → Node2 → ... → Node6 → Node1
   - Mỗi node khi có token có thể xử lý request từ client
   - Lamport clock đảm bảo ordering của events
   
   ## Mô Tả Từng Node
   | Node | Person | PORT (Local) | PORT (Railway) | MySQL | PEERS |
   |------|--------|--------------|----------------|-------|-------|
   | 1    | (Tên)  | 2001         | 8080           | db1   | URL1  |
   | 2    | (Tên)  | 2002         | 8080           | db2   | URL2  |
   | ... | ...    | ...          | ...            | ...   | ...   |
   | 6    | (Tên)  | 2006         | 8080           | db6   | URL6  |
   
   ## Commit Convention
   - Message format: `[NODE_X] Description` or `[SETUP] Description`
   - Example: `[NODE_1] Implement token passing logic`
   ```

5. Commit và push code lên GitHub:
   ```bash
   git add .
   git commit -m "[SETUP] Initial commit - 6 nodes token ring system"
   git push origin main
   ```

6. Tạo file cấu hình tập trung `DEPLOYMENT_CONFIG.json`:
   ```json
   {
     "project_name": "DistributedTokenRing",
     "total_nodes": 6,
     "nodes": {
       "node1": {
         "node_id": 1,
         "local_port": 2001,
         "next_node": 2,
         "prev_node": 6,
         "database": "db1",
         "table": "server1"
       },
       "node2": {
         "node_id": 2,
         "local_port": 2002,
         "next_node": 3,
         "prev_node": 1,
         "database": "db2",
         "table": "server2"
       },
       "node3": {
         "node_id": 3,
         "local_port": 2003,
         "next_node": 4,
         "prev_node": 2,
         "database": "db3",
         "table": "server3"
       },
       "node4": {
         "node_id": 4,
         "local_port": 2004,
         "next_node": 5,
         "prev_node": 3,
         "database": "db4",
         "table": "server4"
       },
       "node5": {
         "node_id": 5,
         "local_port": 2005,
         "next_node": 6,
         "prev_node": 4,
         "database": "db5",
         "table": "server5"
       },
       "node6": {
         "node_id": 6,
         "local_port": 2006,
         "next_node": 1,
         "prev_node": 5,
         "database": "db6",
         "table": "server6"
       }
     },
     "deployment_timeline": {
       "phase1_setup": "Mỗi người fork repo và setup local",
       "phase2_testing": "Test locally với Node1 chạy trên máy này",
       "phase3_railway": "Mỗi người deploy node của mình lên Railway",
       "phase4_integration": "Sau khi toàn bộ deploy, update PEERS variable"
     }
   }
   ```

7. Gửi link repo cho 5 thành viên còn lại

---

## PHẦN 2: SETUP CỦA MỖI THÀNH VIÊN TRÊN MÁY CỦA HỰ (LOCAL SETUP)

**Người thực hiện:** Mỗi người trong nhóm (5 người không phải PM)

### Bước 2.1: Fork Repo

1. Vào trang main repo: `https://github.com/MANAGE_USERNAME/DistributedTokenRing`
2. Bấm nút **Fork** (góc phải trên)
3. Đợi hoàn tất - bạn sẽ có repo ở: `https://github.com/YOUR_USERNAME/DistributedTokenRing`

### Bước 2.2: Clone Fork Về Máy

```bash
# Clone forked repo
git clone https://github.com/YOUR_USERNAME/DistributedTokenRing.git
cd DistributedTokenRing

# Tặng upstream reference đến main repo
git remote add upstream https://github.com/MANAGER_USERNAME/DistributedTokenRing.git

# Kiểm tra
git remote -v
# Output:
# origin    https://github.com/YOUR_USERNAME/DistributedTokenRing.git (fetch)
# origin    https://github.com/YOUR_USERNAME/DistributedTokenRing.git (push)
# upstream  https://github.com/MANAGER_USERNAME/DistributedTokenRing.git (fetch)
# upstream  https://github.com/MANAGER_USERNAME/DistributedTokenRing.git (noexec)
```

### Bước 2.3: Tạo Config Cục Bộ Của Bạn

Mỗi người tạo file riêng theo Node được assigned:

**File: `config_NODE_X.properties`** (thay X = 1,2,3,4,5,6)

Ví dụ nếu bạn là Node 1:

**File: `config_NODE_1.properties`**
```properties
# NODE 1 Configuration (Local)
NODE_ID=1
PORT_LOCAL=2001
PORT_RAILWAY=8080

# MySQL Local
MYSQL_HOST=localhost
MYSQL_PORT=3306
MYSQL_USER=root
MYSQL_PASSWORD=password
MYSQL_DATABASE=db1
MYSQL_URL=jdbc:mysql://localhost:3306/db1

# Ring Topology (Local Testing)
NEXT_NODE=2
PREV_NODE=6
NEXT_NODE_HOST=localhost
NEXT_NODE_PORT=2002

# Initial Token
HAS_TOKEN=true
LAMPORT_CLOCK=0

# Server Name
SERVER_NAME=Server1
TABLE_NAME=server1
```

**File: `config_NODE_2.properties`**
```properties
# NODE 2 Configuration (Local)
NODE_ID=2
PORT_LOCAL=2002
PORT_RAILWAY=8080

MYSQL_HOST=localhost
MYSQL_PORT=3306
MYSQL_USER=root
MYSQL_PASSWORD=password
MYSQL_DATABASE=db2
MYSQL_URL=jdbc:mysql://localhost:3306/db2

NEXT_NODE=3
PREV_NODE=1
NEXT_NODE_HOST=localhost
NEXT_NODE_PORT=2003

HAS_TOKEN=false
LAMPORT_CLOCK=0

SERVER_NAME=Server2
TABLE_NAME=server2
```

(Tương tự cho Node 3,4,5,6)

### Bước 2.4: Setup MySQL Local

1. Mở MySQL Command Line hoặc MySQL Workbench
2. Chạy lệnh sau:

```sql
-- Tạo 6 databases
CREATE DATABASE db1;
CREATE DATABASE db2;
CREATE DATABASE db3;
CREATE DATABASE db4;
CREATE DATABASE db5;
CREATE DATABASE db6;

-- Tạo table cho Node 1
USE db1;
CREATE TABLE server1 (
    id INT PRIMARY KEY,
    content VARCHAR(255) NOT NULL,
    timestamp BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL
);

-- Tương tự cho Node 2
USE db2;
CREATE TABLE server2 (
    id INT PRIMARY KEY,
    content VARCHAR(255) NOT NULL,
    timestamp BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL
);

-- ... (tương tự cho server3, server4, server5, server6)
USE db3;
CREATE TABLE server3 (
    id INT PRIMARY KEY,
    content VARCHAR(255) NOT NULL,
    timestamp BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL
);

USE db4;
CREATE TABLE server4 (
    id INT PRIMARY KEY,
    content VARCHAR(255) NOT NULL,
    timestamp BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL
);

USE db5;
CREATE TABLE server5 (
    id INT PRIMARY KEY,
    content VARCHAR(255) NOT NULL,
    timestamp BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL
);

USE db6;
CREATE TABLE server6 (
    id INT PRIMARY KEY,
    content VARCHAR(255) NOT NULL,
    timestamp BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL
);
```

### Bước 2.5: Compile Code Local

```bash
# Vào thư mục project
cd DistributedTokenRing

# Build project
javac -cp build -d build src/server/*.java src/client/*.java

# Kiểm tra build success
ls build/server/
ls build/client/
```

### Bước 2.6: Test Chạy Local (Chỉ Node 1)

**Người Node 1 chạy thử:**

Terminal 1 - Chạy Server Node 1:
```bash
set NODE_ID=1
set PORT=2001
set MYSQL_URL=jdbc:mysql://localhost:3306/db1
cd build && java -cp . server.Main
```

Terminal 2 - Chạy Client:
```bash
cd build && java -cp . client.Client
```

**Kiểm tra:**
- Client window hiện ra
- Có thể nhìn thấy Node 1 option trong dropdown
- Kết nối server thành công (không lỗi connection refused)

---

## PHẦN 3: SETUP RAILWAY (DEPLOYMENT)

**Người thực hiện:** Mỗi người lần lượt (bắt đầu từ Node 1 người PM)

### Bước 3.1: Tạo Railway Account

1. Vào https://railway.app
2. Bấm **Sign Up**
3. Chọn **GitHub** để đăng ký bằng GitHub (khuyến nghị)
4. Hoàn tất authentication

### Bước 3.2: Tạo Project Mới Trên Railway

1. Bấm **Create a new project**
2. Chọn **Deploy from GitHub repo**
3. Chọn forked repo của bạn: `YourUsername/DistributedTokenRing`
4. Chọn **Deploy Now**
5. Đợi Railway initialize...

### Bước 3.3: Cấu Hình Environment Variables

Sau khi project tạo thành công:

1. Vào tab **Variables**
2. Bấm **Add Variable** và nhập các giá trị dưới đây:

**Cho Node 1:**
```
NODE_ID = 1
PORT = 8080
MYSQL_HOST = (sẽ lấy từ plugin)
MYSQL_USER = (sẽ lấy từ plugin)
MYSQL_PASSWORD = (sẽ lấy từ plugin)
MYSQL_DATABASE = db1
MYSQL_URL = (sẽ được tạo từ các biến trên)
PEERS = (để trống lúc này, cập nhật sau khi toàn bộ deploy)
```

**Cho Node 2:**
```
NODE_ID = 2
PORT = 8080
MYSQL_HOST = (sẽ lấy từ plugin)
MYSQL_USER = (sẽ lấy từ plugin)
MYSQL_PASSWORD = (sẽ lấy từ plugin)
MYSQL_DATABASE = db2
MYSQL_URL = (sẽ được tạo từ các biến trên)
PEERS = (để trống lúc này)
```

(Tương tự cho Node 3,4,5,6 - chỉ thay NODE_ID và MYSQL_DATABASE)

### Bước 3.4: Thêm MySQL Plugin

1. Vào tab **Plugins**
2. Bấm **Add Plugin**
3. Chọn **MySQL**
4. Đợi setup MySQL hoàn tất - Railway sẽ tự động tạo:
   - MYSQL_HOST
   - MYSQL_USER
   - MYSQL_PASSWORD
   - MYSQL_PORT
   - MYSQL_URL (hoặc DATABASE_URL)

5. Sao chép các giá trị này vào Environment Variables

### Bước 3.5: Deploy lên Railway

1. Vào tab **Deployments**
2. Bấm **Deploy**
3. Chọn branch: **main**
4. Chờ build hoàn tất (3-5 phút)
5. Kiểm tra status: **Success ✓**

### Bước 3.6: Lấy Railway URL

1. Vào tab **Settings**
2. Tìm phần **Public Networking** hoặc **URL**
3. Sao chép Railway URL: Ví dụ `distributed-token-ring-production.up.railway.app`
4. Ghi lại: `https://distributed-token-ring-production.up.railway.app:8080`

**Ghi chú quan trọng:** Lưu URL này vì bạn sẽ cần gửi cho PM để update PEERS variable cho tất cả!

---

## PHẦN 4: PHỐI HỢP VÀ HOÀN THIỆN (FINALIZATION)

**Người thực hiện:** Người PM, sau khi toàn bộ 6 node deploy thành công

### Bước 4.1: Thu Thập URL Từ Toàn Bộ 6 Nodes

Sau khi tất cả 6 người deploy xong, PM yêu cầu mỗi người gửi:
- Node ID
- Railway URL

**Ví dụ danh sách:**
```
Node 1 (Person A): https://node1-production.up.railway.app:8080
Node 2 (Person B): https://node2-production.up.railway.app:8080
Node 3 (Person C): https://node3-production.up.railway.app:8080
Node 4 (Person D): https://node4-production.up.railway.app:8080
Node 5 (Person E): https://node5-production.up.railway.app:8080
Node 6 (Person F): https://node6-production.up.railway.app:8080
```

### Bước 4.2: Cập Nhật PEERS Variable

PM tạo PEERS variable format:
```
PEERS=node1-production.up.railway.app:8080,node2-production.up.railway.app:8080,node3-production.up.railway.app:8080,node4-production.up.railway.app:8080,node5-production.up.railway.app:8080,node6-production.up.railway.app:8080
```

Sau đó PM cập nhật PEERS này vào **tất cả 6 Railway projects**:

1. Vào mỗi Railway project
2. Tab **Variables**
3. Thêm/cập nhật variable: `PEERS = (giá trị ở trên)`
4. Bấm **Save**
5. Railway sẽ tự động redeploy

### Bước 4.3: Kiểm Tra Kết Nối Giữa Các Nodes

1. SSH vào 1 node (hoặc check logs):
   ```bash
   railway run java server.Main
   ```

2. Kiểm tra logs xem có kết nối được tới node tiếp theo không

3. Nếu error, kiểm tra:
   - PEERS variable có đúng không
   - Port có đúng không (8080)
   - Network connectivity

### Bước 4.4: Test Qua Client

1. PM chạy Client từ máy:
   ```bash
   java -cp build client.Client
   ```

2. Chọn Node 1, gửi INSERT request
3. Kiểm tra request xử lý trên Railway logs

---

## PHẦN 5: GIT WORKFLOW (TEAM COLLABORATION)

### Mỗi Lần Cần Update Code

1. **Pull latest từ upstream:**
   ```bash
   git fetch upstream
   git rebase upstream/main
   ```

2. **Tạo feature branch cho công việc của bạn:**
   ```bash
   git checkout -b feature/NODE_1_token_passing
   ```

3. **Commit changes:**
   ```bash
   git add .
   git commit -m "[NODE_1] Implement token passing to Node2"
   ```

4. **Push lên fork của bạn:**
   ```bash
   git push origin feature/NODE_1_token_passing
   ```

5. **Tạo Pull Request trên GitHub:**
   - Vào fork của bạn
   - Bấm **Contribute** > **Open pull request**
   - So sánh: `MANAGER_USERNAME/main` ← `your-username/feature/NODE_1_...`
   - Thêm description, bấm **Create pull request**

6. **PM merge pull request (sau khi review)**

---

## PHẦN 6: TROUBLESHOOTING

### Lỗi: Connection Refused khi chạy Local

**Nguyên nhân:** Server Node tiếp theo chưa chạy hoặc port sai

**Giải pháp:**
1. Chắc chắn toàn bộ 6 Node đang chạy (6 terminal)
2. Kiểm tra PORT config có đúng không
3. Kiểm tra PEERS URL đúng không

### Lỗi: MYSQL Connection Error

**Nguyên nhân:** MySQL URL sai hoặc database chưa tạo

**Giải pháp:**
1. Chạy setup.sql để tạo databases
2. Kiểm tra MYSQL_URL format: `jdbc:mysql://localhost:3306/db1`
3. Kiểm tra MySQL service running

### Lỗi: Token không di chuyển

**Nguyên nhân:** Token passing logic lỗi hoặc latency cao

**Giải pháp:**
1. Kiểm tra logs của Node tiếp theo
2. Đảm bảo RoutingTable đúng
3. Kiểm tra network connectivity

### Railway Deployment Lỗi

**Nguyên nhân:** Build lỗi hoặc env variables sai

**Giải pháp:**
1. Kiểm tra Railway build logs
2. Xác nhận MYSQL_URL format đúng
3. Re-deploy: Vào **Deployments** > bấm **Deploy Now**

---

## CHECKLIST - QUÁ TRÌNH HOÀN THIỆN

### Giai Đoạn 1: SETUP (Người PM - 1h)
- [ ] Tạo main repo trên GitHub
- [ ] Push source code lên main repo
- [ ] Tạo DEPLOYMENT_CONFIG.json
- [ ] Gửi link repo cho 5 người còn lại

### Giai Đoạn 2: LOCAL SETUP (Mỗi người - 1h30)
- [ ] Fork repo
- [ ] Clone fork về máy
- [ ] Tạo config_NODE_X.properties
- [ ] Setup MySQL local (6 databases, 6 tables)
- [ ] Compile code: `javac -cp build -d build src/server/*.java src/client/*.java`
- [ ] Test chạy Node 1 + Client local

### Giai Đoạn 3: RAILWAY DEPLOY (Mỗi người - 2h)
- [ ] Tạo Railway account
- [ ] Tạo Railway project từ forked repo
- [ ] Add MySQL plugin
- [ ] Set NODE_ID, MYSQL_DATABASE, PEERS env vars
- [ ] Deploy lên Railway
- [ ] Lấy Railway URL và gửi cho PM

### Giai Đoạn 4: FINALIZATION (Người PM - 30p)
- [ ] Thu thập 6 URLs từ 6 người
- [ ] Tạo PEERS variable string
- [ ] Cập nhật PEERS vào tất cả 6 Railway projects
- [ ] Chờ toàn bộ redeploy
- [ ] Test kết nối end-to-end

### Giai Đoạn 5: TESTING & VALIDATION (Toàn bộ - 1h)
- [ ] Chạy Client, gửi INSERT request
- [ ] Kiểm tra token di chuyển qua 6 nodes
- [ ] Kiểm tra data lưu vào từng database
- [ ] Test DELETE, QUERY requests
- [ ] Ghi log thành công

---

## Tài Liệu Tham Khảo

- [Railway Documentation](https://docs.railway.app)
- [GitHub Forking Guide](https://docs.github.com/en/get-started/quickstart/fork-a-repo)
- [Git Basics](https://git-scm.com/book/en/v2)
- [MySQL Documentation](https://dev.mysql.com/doc/)
- [Token Ring Algorithm](https://en.wikipedia.org/wiki/Token_ring)

---

**Phiên bản:** 1.0  
**Cập nhật lần cuối:** 2026-03-21  
**Tác giả:** Distributed Systems Team

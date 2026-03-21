# QUICK REFERENCE CARD - Token Ring Deployment
## (Dán lên tường hoặc in ra!)

---

### 📋 QUICK FACTS

**Project:** Distributed Token Ring System - 6 Servers  
**Duration:** 5-7 days  
**Team Size:** 6 people + 1 PM  
**Technology:** Java 8+, MySQL, Railway  

---

### 👤 YOUR ROLE

**Choose one:** Node 1, Node 2, Node 3, Node 4, Node 5, or Node 6

| Node | Port (Local) | Port (Railway) | MySQL | Initial Token? |
|------|--------------|----------------|-------|----------------|
| 1    | 2001         | 8080           | db1   | **YES**        |
| 2    | 2002         | 8080           | db2   | NO             |
| 3    | 2003         | 8080           | db3   | NO             |
| 4    | 2004         | 8080           | db4   | NO             |
| 5    | 2005         | 8080           | db5   | NO             |
| 6    | 2006         | 8080           | db6   | NO             |

---

### ⚡ QUICK START (Local Testing)

**1. MySQL Setup** (Run once)
```sql
CREATE DATABASE db1;  -- replace with db2, db3... for your node
USE db1;
CREATE TABLE server1 (  -- replace with server2, server3... for your node
    id INT PRIMARY KEY,
    content VARCHAR(255) NOT NULL,
    timestamp BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL
);
```

**2. Clone Repo**
```bash
git clone https://github.com/YOUR_USERNAME/DistributedTokenRing.git
cd DistributedTokenRing
javac -cp build -d build src/server/*.java src/client/*.java
```

**3. Run Node** (Terminal 1)
```bash
set NODE_ID=1
set PORT=2001
set MYSQL_URL=jdbc:mysql://localhost:3306/db1
cd build && java -cp . server.Main
```

**4. Run Client** (Terminal 2)
```bash
cd build && java -cp . client.Client
```

---

### 🚀 RAILWAY DEPLOYMENT (After Local Testing Works)

1. **Create Railway Account** → https://railway.app (use GitHub login)
2. **New Project** → Deploy from your forked GitHub repo
3. **Add MySQL Plugin** → Railway auto-sets MYSQL_HOST, MYSQL_USER, etc.
4. **Set Environment Variables:**
   ```
   NODE_ID = 1 (your assigned number)
   PORT = 8080
   MYSQL_DATABASE = db1 (matching your node)
   MYSQL_URL = jdbc:mysql://[MYSQL_HOST]:[PORT]/db1
   PEERS = (PM will send this after all deploy)
   ```
5. **Deploy Now** → Wait 3-5 min for green ✓
6. **Send URL to PM** → Example: `https://node1-prod.railway.app:8080`

---

### 📊 TOKEN RING FLOW

```
Node1 (has token)
    ↓ processes request
    ↓ passes token
Node2 (receives token)
    ↓ processes request  
    ↓ passes token
Node3 (receives token)
    ↓ ... continues ...
Node6 (receives token)
    ↓ passes token back to...
Node1 (cycle repeats)
```

**Important:** Only node with token can process requests!

---

### 📝 WHAT TO DO WHEN

**Day 1-2:** Local setup  
- [ ] Fork repo  
- [ ] Create MySQL databases  
- [ ] Compile code  
- [ ] Test with Node 1  

**Day 3-4:** Railway deployment  
- [ ] Create Railway account  
- [ ] Deploy your node  
- [ ] Send URL to PM  

**Day 5-6:** Integration  
- [ ] PM updates PEERS  
- [ ] Wait for all 6 to redeploy  

**Day 7:** Testing  
- [ ] Run client  
- [ ] Send INSERT/DELETE/QUERY  
- [ ] Verify token cycles through all 6  

---

### ⚠️ COMMON ISSUES & FIXES

| Problem | Solution |
|---------|----------|
| "Connection refused" | Check MySQL running, check port number, check PEERS |
| Token stuck at Node 1 | Verify Node 2 deployed, check PEERS format |
| Railway build fails | Check env variables, re-deploy |
| Can't see data in MySQL | Check MYSQL_DATABASE matches your node, check if token reached you |
| Lamport clock not incrementing | Check if requests are being processed, node needs to receive token first |

---

### 📞 FILES TO READ

**First:** `DEPLOYMENT_GUIDE.md` - Everything explained step-by-step  
**Your Role:** `CHECKLIST_NODE_X.md` - Personalized for your node  
**PM:** `PM_GUIDE.md` - For project coordination  
**Config:** `DEPLOYMENT_CONFIG.json` - System configuration details  

---

### 🔧 ENVIRONMENT VARIABLES

**Local Machine:**
```bash
set NODE_ID=1
set PORT=2001
set MYSQL_URL=jdbc:mysql://localhost:3306/db1
set PEERS=localhost:2001,localhost:2002,localhost:2003,localhost:2004,localhost:2005,localhost:2006
```

**Railway (set in Variables tab):**
```
NODE_ID = 1
PORT = 8080
MYSQL_URL = jdbc:mysql://[HOST]:3306/db1
PEERS = node1.railway.app:8080,node2.railway.app:8080,...
```

---

### ✅ SUCCESS CHECKLIST

- [ ] Code compiles: `javac -cp build -d build src/server/*.java src/client/*.java`
- [ ] Node runs locally without errors
- [ ] Client can connect and send data
- [ ] Railway deployment shows green ✓
- [ ] Logs show "Connected to next node"
- [ ] Token cycles: Node1 → Node2 → ... → Node6 → Node1
- [ ] Data persists in MY database (db1, db2, db3, etc)
- [ ] No "Connection refused" errors in Railway logs

---

### 🆘 NEED HELP?

1. Check: `DEPLOYMENT_GUIDE.md` (Phần 6: Troubleshooting)
2. Read: Your checklist - Has detailed steps
3. Contact: PM or Team Lead in chat
4. Share: Error message/logs in team chat

---

### 📌 IMPORTANT NOTES

- ⚠️ **Only Node 1 starts with token!** (HAS_TOKEN=true in config)
- ⚠️ **PEERS variable is FINAL step** - Done after all 6 nodes deploy
- ⚠️ **Don't change port numbers** - They must match config
- ⚠️ **Each person needs own Railway account** - Not shared!
- ⚠️ **DATABASE must match node** - db1 for Node1, db2 for Node2, etc

---

### 📅 TIMELINE

```
Day 1-2:    Local Setup (fork, MySQL, compile)     
Day 2:      Local Testing (run Node 1 + client)     
Day 3-4:    Railway Deploy (each person)
Day 5-6:    Integration (PM collects URLs)
Day 7:      End-to-End Test (verify everything works)
```

---

**Version:** 1.0  | **Updated:** 2026-03-21  | **For:** Each Team Member

Print this! ➡️ Put on desk! ➡️ Reference while working! 📌

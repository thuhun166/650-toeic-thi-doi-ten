# Checklist - NODE 1 (Team Member Assignment)

**Person:** (Name: _______________________)  
**Node ID:** 1  
**Assigned Date:** 2026-03-21  
**Expected Completion:** (5-7 days from start)

---

## PRE-DEPLOYMENT READING
- [ ] Read: `DEPLOYMENT_GUIDE.md` - Full Details (Part 1-2)
- [ ] Understand: Token Ring algorithm concept
- [ ] Understand: Lamport Clock for event ordering
- [ ] Ask PM: Any questions on requirements

---

## PHASE 1: LOCAL SETUP (Day 1-2)

### Step 1.1: Fork and Clone
- [ ] Fork main repo from PM's GitHub
- [ ] Clone fork: `git clone https://github.com/YOUR_USERNAME/DistributedTokenRing.git`
- [ ] Add upstream: `git remote add upstream https://github.com/PM_USERNAME/DistributedTokenRing.git`
- [ ] Verify remotes: `git remote -v` shows both origin and upstream

### Step 1.2: Create Local Configuration
- [ ] Create: `config_NODE_1.properties` in project root
- [ ] Fill in:
  - NODE_ID=1
  - PORT_LOCAL=2001
  - MYSQL_DATABASE=db1
  - TABLE_NAME=server1
  - HAS_TOKEN=true (only Node 1!)
  - MYSQL_URL=jdbc:mysql://localhost:3306/db1

### Step 1.3: Setup MySQL Local
- [ ] Create database: `CREATE DATABASE db1;`
- [ ] Create table:
  ```sql
  USE db1;
  CREATE TABLE server1 (
      id INT PRIMARY KEY,
      content VARCHAR(255) NOT NULL,
      timestamp BIGINT NOT NULL,
      status VARCHAR(50) NOT NULL
  );
  ```
- [ ] Verify: `SHOW TABLES;` shows "server1"

### Step 1.4: Compile Code
- [ ] Open terminal in project root
- [ ] Run: `javac -cp build -d build src/server/*.java src/client/*.java`
- [ ] Check for errors: Should show 0 errors
- [ ] Verify build/ folder has .class files

---

## PHASE 2: LOCAL TESTING (Day 2)

### Step 2.1: Start Node 1 Server
```bash
set NODE_ID=1
set PORT=2001
set MYSQL_URL=jdbc:mysql://localhost:3306/db1
cd build
java -cp . server.Main
```
- [ ] Server starts without errors
- [ ] See log: "Node 1 initialized with token"
- [ ] Server listening on port 2001

### Step 2.2: Start Client
(In new terminal)
```bash
cd build
java -cp . client.Client
```
- [ ] Client GUI appears
- [ ] Node 1 visible in dropdown
- [ ] Can select Node 1

### Step 2.3: Test Operations
- [ ] Insert data: ID=1, Content="Test Data"
- [ ] See success message in client
- [ ] Check MySQL: `SELECT * FROM server1;` shows data
- [ ] Query: See your inserted data
- [ ] Delete: Remove the data
- [ ] Verify deletion in MySQL

### Step 2.4: Test Token
- [ ] Observe logs: Token marked as held
- [ ] Verify Lamport clock increments with each operation

---

## PHASE 3: RAILWAY SETUP (Day 3-4)

### Step 3.1: Create Railway Account
- [ ] Go to https://railway.app
- [ ] Sign up with GitHub
- [ ] Complete email verification
- [ ] Verify: Can login to Railway dashboard

### Step 3.2: Create Railway Project
- [ ] In Railway: Click "Create a new project"
- [ ] Select: "Deploy from GitHub repo"
- [ ] Choose: `YourUsername/DistributedTokenRing`
- [ ] Click: "Deploy Now"
- [ ] Wait: 3-5 minutes for initial build
- [ ] Verify: Build completes (should see green checkmark)

### Step 3.3: Add MySQL Plugin
- [ ] In Railway project, tab "Plugins"
- [ ] Click: "Add Plugin"
- [ ] Select: "MySQL"
- [ ] Wait: MySQL initializes (1-2 minutes)
- [ ] Copy: MYSQL_HOST, MYSQL_USER, MYSQL_PASSWORD (will be auto-generated)

### Step 3.4: Set Environment Variables
- [ ] In Railway project, tab "Variables"
- [ ] Add/update these variables:
  ```
  NODE_ID = 1
  PORT = 8080
  MYSQL_DATABASE = db1
  MYSQL_URL = jdbc:mysql://[MYSQL_HOST]:[MYSQL_PORT]/db1
  PEERS = (leave empty for now, PM will update)
  ```
- [ ] Verify: All variables show in the list

### Step 3.5: Deploy to Railway
- [ ] In Railway, tab "Deployments"
- [ ] Click: "Deploy Now"
- [ ] Watch: Build logs (2-5 minutes)
- [ ] Wait for: Green "✓ Success" indicator
- [ ] If error: Check build logs and fix

### Step 3.6: Get Railway URL
- [ ] In Railway, tab "Settings"
- [ ] Find: "Public Networking" or "View Railway URL"
- [ ] Copy: Your unique Railway URL (e.g., `node1-prod.up.railway.app`)
- [ ] **IMPORTANT:** Send this URL to PM immediately!
  - [ ] Format: `https://[YOUR_RAILWAY_URL]:8080`
  - [ ] Example: `https://node1-prod.up.railway.app:8080`

---

## PHASE 4: WAITING FOR OTHER NODES (Day 5-6)

- [ ] Wait: PM collects all 6 Railway URLs
- [ ] Wait: PM sends updated PEERS variable
- [ ] When PM sends: Update PEERS in your Railway project
  - [ ] In Railway Variables, find PEERS
  - [ ] Paste the complete list from PM
  - [ ] Railway auto-redeploys
- [ ] Wait: All 6 nodes redeploy with new PEERS

---

## PHASE 5: END-TO-END TESTING (Day 7)

### Step 5.1: Verify Inter-node Connection
- [ ] Check Railway logs: See connections to Node 2
- [ ] No "Connection refused" errors
- [ ] Can see Node 2 in RoutingTable

### Step 5.2: Test with Universal Client
**PM starts client or team chooses one person:**
- [ ] Client connects to Node 1
- [ ] Insert operation succeeds
- [ ] Check token passes to Node 2 (see in logs)
- [ ] Lamport clock increments

### Step 5.3: Verify Token Ring
- [ ] Node 1 passes token to Node 2 (check logs)
- [ ] Node 2 passes to Node 3 (check logs)
- [ ] ... continues to Node 6
- [ ] Node 6 passes back to Node 1 (continuous cycle)
- [ ] No token loss or duplication

### Step 5.4: Test Data Persistence
- [ ] Send INSERT request
- [ ] Verify: Data appears in my database (db1 table server1)
- [ ] Verify: Other nodes can query my data
- [ ] Send DELETE request
- [ ] Verify: Data removed from my database

### Step 5.5: Document Results
- [ ] [ ] Screenshot: Railway successful deployment
- [ ] [ ] Screenshot: Client connected to Node 1
- [ ] [ ] Screenshot: Data in MySQL (server1 table)
- [ ] [ ] Note: Lamport clock final value
- [ ] [ ] Report to PM: "Node 1 testing complete - all pass"

---

## TROUBLESHOOTING

### Problem: Can't connect to MySQL locally
**Solution:**
- Verify MySQL service running: `net start MySQL80` (Windows) or check System Preferences (Mac)
- Check MYSQL_URL format: Should be `jdbc:mysql://localhost:3306/db1`
- Verify database and table created: `SHOW DATABASES;` and `USE db1; SHOW TABLES;`

### Problem: "Connection refused" when running server
**Solution:**
- Port 2001 might be in use: `netstat -anb | findstr 2001` (Windows)
- Try different port in config_NODE_1.properties
- Stop other Java processes

### Problem: Railway build fails
**Solution:**
- Check Railway build logs (red error messages)
- Verify environment variables are set correctly
- Re-deploy: Click "Deploy Now" again

### Problem: Can't reach Railway from client
**Solution:**
- Verify Railway URL in PEERS variable is correct
- Check if Railway app is still running (check Deployments tab)
- Verify firewall isn't blocking outbound connections

---

## SIGN-OFF

After completing ALL steps above:

- [ ] **Local testing:** PASS ✓
- [ ] **Railway deployment:** PASS ✓
- [ ] **Node connectivity:** PASS ✓
- [ ] **End-to-end testing:** PASS ✓
- [ ] **Railway URL sent to PM:** YES ✓

**Completion Date:** _______________

**Any Issues Encountered:**
```
(Write any problems and solutions here)




```

**PM Sign-off:** _______________  
**Date:** _______________

---

**Need Help?**
- Review: `DEPLOYMENT_GUIDE.md`
- Contact: PM or Team Lead
- GitChat: Share error logs in team chat

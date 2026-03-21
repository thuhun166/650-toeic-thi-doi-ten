# Checklist - NODE [X] (Team Member Assignment)

**Person:** (Name: _______________________)  
**Node ID:** [1-6]  
**Assigned Date:** 2026-03-21  
**Expected Completion:** (5-7 days from start)

---

## IMPORTANT NOTES FOR NODE 2-6

**Before starting this checklist:**
1. Node 1 person must complete their checklist first
2. Node 1 person shares their Railway URL with the team
3. You wait for all 6 nodes to be deployed before final integration
4. **ONLY NODE 1 has initial token (HAS_TOKEN=true)**
5. Your node has HAS_TOKEN=false in config

---

## PRE-DEPLOYMENT READING
- [ ] Read: `DEPLOYMENT_GUIDE.md` - Full Details (Part 1-2)
- [ ] Read: `CHECKLIST_NODE_1.md` - See how Node 1 does it
- [ ] Understand: Token Ring algorithm concept
- [ ] Understand: Your node receives token from Node[X-1], passes to Node[X+1]
- [ ] Ask PM: Which Node number am I? (1, 2, 3, 4, 5, or 6?)

---

## PHASE 1: LOCAL SETUP (Day 1-2)

### Step 1.1: Fork and Clone
- [ ] Fork main repo from PM's GitHub
- [ ] Clone fork: `git clone https://github.com/YOUR_USERNAME/DistributedTokenRing.git`
- [ ] Add upstream: `git remote add upstream https://github.com/PM_USERNAME/DistributedTokenRing.git`
- [ ] Verify remotes: `git remote -v` shows both origin and upstream

### Step 1.2: Create Local Configuration

**IDENTIFY YOUR NODE:**
```
Find your Node number from PM:
- Node 1: local_port=2001, mysql_db=db1, table=server1
- Node 2: local_port=2002, mysql_db=db2, table=server2
- Node 3: local_port=2003, mysql_db=db3, table=server3
- Node 4: local_port=2004, mysql_db=db4, table=server4
- Node 5: local_port=2005, mysql_db=db5, table=server5
- Node 6: local_port=2006, mysql_db=db6, table=server6
```

- [ ] Create: `config_NODE_[X].properties` in project root (replace [X] with your number)
- [ ] Fill in (use YOUR node number from above):
  ```properties
  NODE_ID=[X]
  PORT_LOCAL=[200X]
  MYSQL_DATABASE=db[X]
  TABLE_NAME=server[X]
  HAS_TOKEN=false
  MYSQL_URL=jdbc:mysql://localhost:3306/db[X]
  NEXT_NODE=[X+1 or 1 if X=6]
  PREV_NODE=[X-1 or 6 if X=1]
  ```

### Step 1.3: Setup MySQL Local
- [ ] Create database: `CREATE DATABASE db[X];` (replace [X])
- [ ] Create table:
  ```sql
  USE db[X];
  CREATE TABLE server[X] (
      id INT PRIMARY KEY,
      content VARCHAR(255) NOT NULL,
      timestamp BIGINT NOT NULL,
      status VARCHAR(50) NOT NULL
  );
  ```
- [ ] Verify: `SHOW TABLES;` shows "server[X]"

### Step 1.4: Compile Code
- [ ] Open terminal in project root
- [ ] Run: `javac -cp build -d build src/server/*.java src/client/*.java`
- [ ] Check for errors: Should show 0 errors
- [ ] Verify build/ folder has .class files

---

## PHASE 2: LOCAL TESTING (Day 2)

**IMPORTANT:** You will test ONLY with Node 1. This requires Node 1 to be running!

### Step 2.1: Coordinate with Node 1 Person
- [ ] Ask Node 1 person: Start Node 1 server
- [ ] Verify: Node 1 is running on port 2001
- [ ] Continue only after Node 1 ready

### Step 2.2: Start Your Node Server
```bash
set NODE_ID=[X]
set PORT=[200X]
set MYSQL_URL=jdbc:mysql://localhost:3306/db[X]
set PEERS=localhost:2001,localhost:2002,localhost:2003,localhost:2004,localhost:2005,localhost:2006
cd build
java -cp . server.Main
```
- [ ] Server starts without errors
- [ ] See log: "Node [X] initialized"
- [ ] Server listening on port [200X]
- [ ] See connection to Node 1 (previous node in ring)

### Step 2.3: Test with Client
(Ask Node 1 person to keep their client open, or use another terminal)
```bash
cd build
java -cp . client.Client
```
- [ ] Client GUI appears
- [ ] Can select different nodes from dropdown
- [ ] Select Node 1, send data

### Step 2.4: Monitor Token Passing
- [ ] Watch your server logs
- [ ] You should NOT have token initially (Node 1 has it)
- [ ] Eventually token reaches you (see log message)
- [ ] You become token holder briefly
- [ ] Token passes to next node
- [ ] Token returns to you in a cycle

### Step 2.5: Test Your Database
- [ ] When you have token, you can process requests
- [ ] Insert data targeting your node
- [ ] Verify data in your database: `SELECT * FROM server[X];`
- [ ] Delete data
- [ ] Query data

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
  NODE_ID = [X]
  PORT = 8080
  MYSQL_DATABASE = db[X]
  MYSQL_URL = jdbc:mysql://[MYSQL_HOST]:[MYSQL_PORT]/db[X]
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
- [ ] Copy: Your unique Railway URL (e.g., `nodeX-prod.up.railway.app`)
- [ ] **IMPORTANT:** Send this URL to PM immediately!
  - [ ] Format: `https://[YOUR_RAILWAY_URL]:8080`
  - [ ] Example: `https://nodeX-prod.up.railway.app:8080`

---

## PHASE 4: WAITING FOR OTHER NODES (Day 5-6)

- [ ] Wait: All other nodes to deploy
- [ ] PM collects all 6 Railway URLs
- [ ] PM calculates PEERS variable with all 6 URLs
- [ ] When PM sends: Update PEERS in your Railway project
  - [ ] In Railway Variables, find PEERS (or create if not exists)
  - [ ] Paste the complete list from PM
  - [ ] Railway auto-redeploys
- [ ] Wait: All 6 nodes redeploy with new PEERS
- [ ] Verify: Deployment shows "✓ Success"

---

## PHASE 5: END-TO-END TESTING (Day 7)

### Step 5.1: Verify Inter-node Connection
- [ ] Check Railway logs: See connections from Node[X-1] and to Node[X+1]
- [ ] No "Connection refused" errors
- [ ] Can see other nodes in RoutingTable

### Step 5.2: Monitor Token Passing
- [ ] Start PM's universal client
- [ ] Send INSERT request to Node 1
- [ ] Monitor your Railway logs
- [ ] See token being passed:
  - Node 1 processes (0-5 seconds)
  - Token passes to Node 2
  - Node 2 processes (0-5 seconds)
  - Token passes to Node 3
  - continues until...
  - Token reaches YOUR node
  - Your node processes
  - Token passes forward
  - continues back to Node 1

### Step 5.3: Test Data Persistence
- [ ] Lamport clock increments: See values like 1, 2, 3, 4, 5...
- [ ] When YOUR node has token, data is your responsibility
- [ ] Query operations show data persistently stored
- [ ] Delete operations remove data

### Step 5.4: Verify Ring Completion
- [ ] Monitor: 20-30 seconds of execution
- [ ] Should see token pass through all 6 nodes
- [ ] Should see token return to Node 1 multiple times
- [ ] No token loss or duplication

### Step 5.5: Document Results
- [ ] [ ] Screenshot: Railway deployment successful
- [ ] [ ] Screenshot: Server logs showing token received and passed
- [ ] [ ] Screenshot: Data in your MySQL database
- [ ] [ ] Note: Your node's Lamport clock final value
- [ ] [ ] Report to PM: "Node [X] testing complete - all pass"

---

## TROUBLESHOOTING

### Problem: Can't see token at my node
**Solution:**
- Verify PEERS variable format is correct (comma-separated)
- Check if all 6 nodes deployed successfully
- Wait: Token takes 6-30 seconds to reach each node
- Check: MYSQL_DATABASE matches your node number (db[X])

### Problem: "Connection refused" in logs
**Solution:**
- Verify all 6 nodes deployed to Railway
- Verify PEERS variable has latest URLs from PM
- Wait: 2-3 minutes after PM updates PEERS for all to redeploy
- Redeploy your project: Click "Deploy Now" in Railway

### Problem: My database shows no data
**Solution:**
- Verify TOKEN reached your node (check logs)
- Verify correct database selected: db[X]
- Verify INSERT request was sent to your node
- Lamport clock shows activity proves token arrived

### Problem: Railway build fails
**Solution:**
- Check Railroad build logs for error messages
- Verify NODE_ID is unique (1-6)
- Verify MYSQL_DATABASE matches db[X] (not db[other])
- Re-deploy: Click "Deploy Now" again

---

## SIGN-OFF

After completing ALL steps above:

- [ ] **Local testing with Node 1:** PASS ✓
- [ ] **Railway deployment:** PASS ✓
- [ ] **Token received at my node:** YES ✓
- [ ] **Data persisted:** YES ✓
- [ ] **Railway URL sent to PM:** YES ✓

**Completion Date:** _______________

**Any Issues Encountered:**
```
(Write any problems and solutions here)




```

**PM Sign-off:** _______________  
**Date:** _______________

---

## NODE ASSIGNMENT QUICK REFERENCE

| Node | Next | Prev | LocalPort | RailwayPort | MySQL DB | Table |
|------|------|------|-----------|-------------|----------|-------|
| 1    | 2    | 6    | 2001      | 8080        | db1      | server1 |
| 2    | 3    | 1    | 2002      | 8080        | db2      | server2 |
| 3    | 4    | 2    | 2003      | 8080        | db3      | server3 |
| 4    | 5    | 3    | 2004      | 8080        | db4      | server4 |
| 5    | 6    | 4    | 2005      | 8080        | db5      | server5 |
| 6    | 1    | 5    | 2006      | 8080        | db6      | server6 |

---

**Need Help?**
- Review: `DEPLOYMENT_GUIDE.md` Part 2-3
- Review: `CHECKLIST_NODE_1.md` - See similar process
- Contact: PM or Team Lead
- Share: Error logs in team chat

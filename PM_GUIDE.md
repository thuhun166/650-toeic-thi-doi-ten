# Project Manager Guide - Hệ Thống Token Ring 6 Servers
## (PM/Project Control Document)

**Role:** Project Manager / Team Lead  
**Responsibility:** Coordinate all 6 team members through deployment phases  
**Timeline:** 5-7 days total

---

## PM PHASE 1: SETUP (Day 1 - 1-2 hours)

### 1.1: GitHub Main Repo Setup ✓
- [ ] Create GitHub repository: `DistributedTokenRing`
- [ ] Clone to local machine
- [ ] Copy all source files from BaiDoXe/DistributedSystemProject:
  - [ ] src/ folder (all packages)
  - [ ] build/ folder (with compiled classes)
  - [ ] README.md, HUONGDAN.md
  - [ ] setup.sql, config.properties, railway.toml
- [ ] Create DEPLOYMENT_GUIDE.md (comprehensive guide - done ✓)
- [ ] Create DEPLOYMENT_CONFIG.json (system config - done ✓)
- [ ] Create CHECKLIST_NODE_1.md (for Node 1 person - done ✓)
- [ ] Create CHECKLIST_NODE_2-6_TEMPLATE.md (for other nodes - done ✓)
- [ ] Create .gitignore (copy Java template)
- [ ] Create ARCHITECTURE.md (system overview)

### 1.2: Initial Commit and Push
```bash
git add .
git commit -m "[SETUP] Initial commit - Token Ring distributed system"
git push origin main
```
- [ ] Verify: All files pushed to GitHub
- [ ] Verify: Main repo is PUBLIC (so others can fork)

### 1.3: Create Team Assignment Document
Create file: `TEAM_ASSIGNMENT.md`
```markdown
# Team Assignment - Token Ring Project

| Node | Person Name | GitHub | Node Status | Railway URL |
|------|------------|--------|-------------|-------------|
| 1    | __________ | user1  | SETUP       |             |
| 2    | __________ | user2  | SETUP       |             |
| 3    | __________ | user3  | SETUP       |             |
| 4    | __________ | user4  | SETUP       |             |
| 5    | __________ | user5  | SETUP       |             |
| 6    | __________ | user6  | SETUP       |             |
```
- [ ] Fill in team member names and GitHub usernames
- [ ] Share with team
- [ ] Update URL column as they deploy

### 1.4: Send Initial Messages to Team

**Message to all 6 people:**
```
Subject: [IMPORTANT] Distributed Systems Project - Token Ring Deployment

Hi Team,

The main repository is ready: https://github.com/YOUR_USERNAME/DistributedTokenRing

READ FIRST:
1. DEPLOYMENT_GUIDE.md - Complete step-by-step instructions
2. Your assigned checklist:
   - Node 1: CHECKLIST_NODE_1.md
   - Nodes 2-6: CHECKLIST_NODE_2-6_TEMPLATE.md (copy and rename)

TIMELINE:
- Day 1-2: Local setup (fork, MySQL, compile)
- Day 2: Local testing with Node 1
- Day 3-4: Railway deployment
- Day 5-6: Wait for all 6 nodes deployed
- Day 7: End-to-end testing

YOUR ASSIGNMENT:
- Node [X]: (Person Name)

Next step: Fork repo and follow your checklist. Ask if any questions!

PM
```

---

## PM TRACKING: PHASES 2-3 (Day 2-4)

### Phase 2 Checklist - Local Setup & Testing

Each day, check progress with each team member:

**DAY 1-2 CHECKPOINT:**
```
Team Member      | Fork  | Clone | Config | MySQL | Compile | Status
Node 1 (Name)    | [ ]   | [ ]   | [ ]    | [ ]   | [ ]     | 
Node 2 (Name)    | [ ]   | [ ]   | [ ]    | [ ]   | [ ]     |
Node 3 (Name)    | [ ]   | [ ]   | [ ]    | [ ]   | [ ]     |
Node 4 (Name)    | [ ]   | [ ]   | [ ]    | [ ]   | [ ]     |
Node 5 (Name)    | [ ]   | [ ]   | [ ]    | [ ]   | [ ]     |
Node 6 (Name)    | [ ]   | [ ]   | [ ]    | [ ]   | [ ]     |
```

### Phase 3 Checklist - Railway Deployment

**DAY 3-4 CHECKPOINT:**
```
Node | Person  | Railway Account | Project Created | MySQL Added | Vars Set | Deployed | URL
1    | (Name)  | [ ]             | [ ]             | [ ]         | [ ]      | [ ]      |
2    | (Name)  | [ ]             | [ ]             | [ ]         | [ ]      | [ ]      |
3    | (Name)  | [ ]             | [ ]             | [ ]         | [ ]      | [ ]      |
4    | (Name)  | [ ]             | [ ]             | [ ]         | [ ]      | [ ]      |
5    | (Name)  | [ ]             | [ ]             | [ ]         | [ ]      | [ ]      |
6    | (Name)  | [ ]             | [ ]             | [ ]         | [ ]      | [ ]      |
```

---

## PM PHASE 4: INTEGRATION & FINALIZATION (Day 5-6 - 1 hour)

### 4.1: Collect Railway URLs

After all 6 people report:
- [ ] Node 1 URL: ___________________________________
- [ ] Node 2 URL: ___________________________________
- [ ] Node 3 URL: ___________________________________
- [ ] Node 4 URL: ___________________________________
- [ ] Node 5 URL: ___________________________________
- [ ] Node 6 URL: ___________________________________

### 4.2: Create PEERS Variable String

Format: `url:port,url:port,url:port,url:port,url:port,url:port`

**Example:**
```
node1-prod.railway.app:8080,node2-prod.railway.app:8080,node3-prod.railway.app:8080,node4-prod.railway.app:8080,node5-prod.railway.app:8080,node6-prod.railway.app:8080
```

PEERS Value to use:
```
________________________________________________________________________
________________________________________________________________________
________________________________________________________________________
```

### 4.3: Update PEERS in All 6 Railway Projects

**For each of 6 nodes:**

1. [ ] **Node 1:** Log into Railway project
   - Go to Variables tab
   - Find or create: `PEERS`
   - Paste complete PEERS string
   - Save (auto-redeploy)
   - Wait: Deployment completes (2-5 min)
   - Verify: Green ✓

2. [ ] **Node 2:** (repeat above)
   
3. [ ] **Node 3:** (repeat above)

4. [ ] **Node 4:** (repeat above)

5. [ ] **Node 5:** (repeat above)

6. [ ] **Node 6:** (repeat above)

### 4.4: Final Verification After PEERS Update

After ALL 6 projects redeploy:
- [ ] Check each node's Railway logs
- [ ] Each should show: "Connected to next node"
- [ ] No error messages about unknown hosts
- [ ] All 6 builds show "✓ Success"

---

## PM PHASE 5: END-TO-END TESTING (Day 7 - 1-2 hours)

### 5.1: Prepare Testing Environment

**Option A: Test on your machine**
```bash
# If you want to run client from your computer
cd DistributedSystemProject
javac -cp build -d build src/client/*.java
cd build
java -cp . client.Client
```

**Option B: Test from one node's Railway**
- Deploy client GUI to Railway as well (optional)

### 5.2: Execute Test Sequence

**Test 1: INSERT Data**
- [ ] Client: Select Node 1
- [ ] Client: ID = 1, Content = "TestMessage001"
- [ ] Client: Click INSERT
- [ ] Result: "Success" message
- [ ] Logs: Watch each node receive token in order

**Monitor in Railway Logs:**
- [ ] See Node 1 logs: "Received client request, token held"
- [ ] See Node 1 logs: "Inserting data into db1"
- [ ] See Node 1 logs: "Passing token to Node 2"
- [ ] After ~2-5 sec: See Node 2 logs: "Received token from Node 1"
- [ ] After ~2-5 sec: See Node 3 logs: "Received token from Node 2"
- [ ] ... continues through Node 6
- [ ] Eventually: Node 1 receives token again (cycle completes)

### 5.3: Verify Data Persistence

After first INSERT, verify in each database:

```sql
-- Connect to each Railway MySQL (via Railway CLI or MySQL Workbench)
SELECT * FROM server1;  -- Should show the inserted data
SELECT * FROM server2;  -- Should show same data (replicated or queried)
...
SELECT * FROM server6;
```

- [ ] Data appears in db1 (Node 1's database)
- [ ] Data queryable from all nodes

### 5.4: Test Query Operation

**Test 2: QUERY Data**
- [ ] Client: Select Node 1
- [ ] Client: Click QUERY
- [ ] Result: Should show all data in database
- [ ] Lamport clock shows increment

### 5.5: Test DELETE Operation

**Test 3: DELETE Data**
- [ ] Client: Select Node 1, ID = 1
- [ ] Client: Click DELETE
- [ ] Result: "Success" message
- [ ] Verify: Data gone from MySQL
- [ ] Verify: DELETE record in logs

### 5.6: Test Multiple Nodes

**Test 4: Insert to different nodes**
```
Insert via Node 1: ID=1, Content="Data_Node1"
Insert via Node 2: ID=2, Content="Data_Node2"
Insert via Node 3: ID=3, Content="Data_Node3"
... etc
```

- [ ] Send 6 separate INSERT requests to different nodes
- [ ] Verify: Each data goes to correct database (db1, db2, db3, etc)
- [ ] Verify: Token passes through all nodes for each request

### 5.7: Monitor Lamport Clock

- [ ] Track lamport clock values in logs
- [ ] Should increment: 0 → 1 → 2 → 3 → ... (never decreases)
- [ ] Clocks should be synchronized across all nodes (within reason)

### 5.8: Long-Running Test (Optional - 5 min)

**Test 5: Continuous Operation**
- [ ] Send 10-20 INSERT requests over 5 minutes
- [ ] Monitor: Token continuously cycling through all 6 nodes
- [ ] Verify: No errors, no timeouts
- [ ] Verify: All data properly stored

---

## PM DOCUMENTATION & REPORTING

### 5.9: Create Test Report

**File: `TEST_RESULTS.md`**
```markdown
# End-to-End Test Results - Token Ring System

**Date:** 2026-03-XX  
**Tester:** PM  
**Status:** ✓ PASS / ✗ FAIL

## Test 1: Single INSERT
- [ ] Request sent to Node 1
- [ ] Token propagated through all 6 nodes
- [ ] Data stored in db1
- [ ] Result: PASS / FAIL

## Test 2: Token Ring Completeness
- [ ] Token starts at Node 1
- [ ] Node 1 → 2 → 3 → 4 → 5 → 6 → 1
- [ ] No token loss observed
- [ ] All nodes received token
- [ ] Result: PASS / FAIL

## Test 3: Data Persistence
- [ ] Data visible in Node 1 database
- [ ] Data queryable from other nodes
- [ ] No data corruption
- [ ] Result: PASS / FAIL

## Test 4: Lamport Clock
- [ ] Clock increments with each message
- [ ] No decrements observed
- [ ] Causal ordering maintained
- [ ] Result: PASS / FAIL

## Test 5: QUERY & DELETE Operations
- [ ] QUERY returns correct data
- [ ] DELETE removes data from database
- [ ] Operations acknowledged
- [ ] Result: PASS / FAIL

## Overall Result: ✓ PASS

System is functioning correctly. All 6 nodes are communicating, token is circulating properly, and data is persistent.

Tested by: _______________
Date: _______________
```

- [ ] Complete test report
- [ ] Share with team (optionally)

---

## TROUBLESHOOTING CHECKLIST (If Issues Arise)

### Issue: Token stuck at Node 1

**Diagnostic:**
```sql
-- Check if data is actually getting written
SELECT COUNT(*) FROM server1;
-- Check Lamport clock in logs
-- Check for "Connection refused" errors
```

**Solution:**
- [ ] Restart Node 1: Re-deploy in Railway
- [ ] Verify PEERS string has exactly 6 comma-separated URLs
- [ ] Check next node (Node 2) is actually deployed

### Issue: Some nodes not receiving token

**Diagnostic:**
- [ ] Check Railway logs for "Connection refused" at missing node
- [ ] Verify that node's PEERS variable is updated

**Solution:**
- [ ] Redeploy the missing node
- [ ] Wait 2-3 min for all redeploys
- [ ] Reset PEERS variable in all (reorder entries if needed)

### Issue: Railway deployment keeps failing

**Diagnostic:**
- Check Railway build logs for specific error

**Common causes:**
- JAVA version mismatch (should be Java 8+)
- MYSQL_URL format wrong
- Missing dependencies

**Solution:**
- Contact the person and have them fix their node's config
- Re-deploy after fix

### Issue: Client can't connect

**Diagnostic:**
- Verify Railway URLs are accessible from your machine
- Try: `curl https://node1-prod.railway.app` (should not timeout)

**Solution:**
- Check firewall settings
- Verify Railway app is still running (not crashed)
- Verify correct PORT (should be 8080)

---

## SUCCESS CRITERIA - PROJECT COMPLETION

✓ **Project is complete when ALL of the following met:**

- [ ] All 6 Railway deployments showing "✓ Success"
- [ ] All 6 nodes have unique NODE_ID (1-6)
- [ ] PEERS variable updated in all 6 projects
- [ ] Token successfully circulates through all 6 nodes
- [ ] Data successfully stored in each node's database
- [ ] Lamport clock increments correctly
- [ ] Client can send INSERT/DELETE/QUERY to any node
- [ ] No errors in logs during 5-minute test run
- [ ] All 6 team members confirm their node working
- [ ] Test results documented

**PM Sign-off:** _______________  
**Date:** _______________

---

## QUICK COMMAND REFERENCE (PM Commands)

### Check all 6 nodes status:
```bash
# Each node's Railway URL
curl -I https://node1.railway.app:8080
curl -I https://node2.railway.app:8080
curl -I https://node3.railway.app:8080
curl -I https://node4.railway.app:8080
curl -I https://node5.railway.app:8080
curl -I https://node6.railway.app:8080
```

### Run local client:
```bash
cd DistributedSystemProject/build
java -cp . client.Client
```

### Check local MySQL:
```sql
SHOW DATABASES;
USE db1;
SELECT * FROM server1;
SELECT * FROM server2;
...
SELECT * FROM server6;
```

---

## TIMELINE SUMMARY FOR PM

| Day | Phase | Action | Owner | Duration |
|-----|-------|--------|-------|----------|
| 1   | Setup | GitHub + docs | PM | 1-2h |
| 1-2 | Local | Fork, MySQL, compile | Each | 1.5h |
| 2   | Test  | Run Node 1 + client | Node1 | 1h |
| 3-4 | Rail  | Railway deployment | Each | 2h |
| 5-6 | Wait  | Collect URLs, update PEERS | PM | 1h |
| 7   | Test  | End-to-end testing | PM + team | 1-2h |

**Total Project Duration: 5-7 days**

---

**Questions?** Manage coordination through GitHub issues and team chat.  
**Good luck!** - PM

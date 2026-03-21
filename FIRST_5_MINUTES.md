# ⚡ FIRST 5 MINUTES - Quick Start Card
## Token Ring Railway Deployment

---

## 👨‍💼 PROJECT MANAGER - Bắt Đầu Ngay

### Minute 1-2: Read This
```
You need to coordinate 6 people deploying 
a distributed system to Railway in 3-4 days.
No local testing - straight to production.
```

### Minute 2-3: Do This
```
□ Create GitHub repo: DistributedTokenRing
□ Copy source code to repo (already in src/, build/)
□ Push to GitHub
```

### Minute 3-4: Send This
```
Hi team,

Repo: https://github.com/YOUR_USERNAME/DistributedTokenRing

READ:
1. FULL_DEPLOYMENT_INDEX.md (choose your role)
2. RAILWAY_DEPLOYMENT_DIRECT.md (main guide)
3. Your specific checklist

ASSIGNMENTS:
Node 1: (Person A)
Node 2: (Person B)
Node 3: (Person C)
Node 4: (Person D)
Node 5: (Person E)
Node 6: (Person F)

Timeline: 3-4 days total
No local testing - deploy straight to Railway!
```

### Minute 4-5: Read Full Guide
```
□ Open: PM_RAILWAY_DIRECT.md
□ Follow 10-step workflow
□ Track progress daily
```

**Next Step:** Open [PM_RAILWAY_DIRECT.md](PM_RAILWAY_DIRECT.md)

---

## 👨‍💻 TEAM MEMBER - Bắt Đầu Ngay

### Minute 1: Get Your Assignment
```
PM will tell you:
- Your Node number (1, 2, 3, 4, 5, or 6)
- Main repo URL
```

### Minute 2: Know Your Path
```
DAY 1:  Fork repo → Create Railway account
DAY 1-2: Deploy Railway project
DAY 2:  Get Railway URL → Send to PM
DAY 3:  Wait for PEERS from PM
DAY 3:  Update PEERS → Redeploy
DAY 4:  Verify logs → Done! ✓
```

### Minute 3: Read Checklist
```
□ Open: INDIVIDUAL_DEPLOYMENT_CHECKLIST.md
□ Read entire thing (15 minutes)
□ Bookmark for reference
```

### Minute 4-5: Start Deploying
```
□ Fork the repo
  → https://github.com/YOUR_USERNAME/DistributedTokenRing

□ Go to Railway
  → Create new project
  → Deploy from YOUR fork

□ Come back tomorrow!
```

**Next Step:** Open [INDIVIDUAL_DEPLOYMENT_CHECKLIST.md](INDIVIDUAL_DEPLOYMENT_CHECKLIST.md)

---

## 🎯 BOTH - The Quick Facts

```
WHAT:      6-server Token Ring distributed system
WHERE:     Railway (cloud platform)
TIME:      3-4 days
PEOPLE:    PM + 6 developers
LANGUAGE:  Java
DATABASE:  MySQL

NODE ASSIGNMENT:
├─ Node 1: [Person A] - has initial token
├─ Node 2: [Person B]
├─ Node 3: [Person C]
├─ Node 4: [Person D]
├─ Node 5: [Person E]
└─ Node 6: [Person F]

FLOW:
Fork → Railway Account → Create Project → Add MySQL 
→ Deploy → Get URL → PM Updates PEERS → Redeploy → ✓ LIVE
```

---

## 📍 DOCUMENTS AT A GLANCE

```
START HERE:
└─ FULL_DEPLOYMENT_INDEX.md
   (This tells you which doc to read next)

FOR PROJECT MANAGER:
├─ PM_RAILWAY_DIRECT.md (main workflow)
└─ RAILWAY_DEPLOYMENT_DIRECT.md (reference)

FOR EACH TEAM MEMBER:
├─ INDIVIDUAL_DEPLOYMENT_CHECKLIST.md (follow this!)
└─ RAILWAY_DEPLOYMENT_DIRECT.md (details if needed)

TECHNICAL REFERENCE:
└─ SOURCE_CODE_DEPLOYMENT.md (code + architecture)

CONFIG REFERENCE:
└─ DEPLOYMENT_CONFIG.json (all settings)

CHEAT SHEET:
└─ QUICK_REFERENCE.md (one-page)
```

---

## ⏱️ TIME ESTIMATE PER PERSON

```
Person 1 (Node 1):
├─ Fork repo + Railway setup:        1 hour
├─ Deploy + get URL:                 1 hour  
├─ Wait for PM:                      varies
├─ Update PEERS + redeploy:          30 min
└─ Verify logs:                      15 min
TOTAL: 3-4 hours spread over 3-4 days

Person 2-6 (Node 2-6):
├─ Same as above:                    3-4 hours each
└─ All done in parallel!

PM:
├─ Setup + coordination:             1-2 hours
├─ Collect URLs:                     30 min
├─ Create PEERS:                     15 min
├─ Update PEERS everywhere:          1 hour
├─ Run tests:                        1-2 hours
└─ TOTAL: 4-6 hours over 3-4 days
```

---

## ✅ SUCCESS CHECKLIST (End of Day 4)

```
EVERYONE:
□ Node deployed to Railway (green ✓)
□ MySQL database created
□ Environment variables set
□ Logs show no errors
□ Node connected to ring

SYSTEM:
□ All 6 nodes operational
□ PEERS updated everywhere
□ Token cycling: 1→2→3→4→5→6→1
□ Data persists in databases
□ Tests pass ✓

STATUS: 🎉 LIVE AND OPERATIONAL!
```

---

## 🆘 STUCK? CHECK THIS

```
Error: "Build fails"
→ Check NODE_ID is unique (1-6)
→ Verify MYSQL_DATABASE = db[number]
→ Check Railway logs for error details

Error: "Connection refused"
→ Wait for PM to send PEERS
→ Then update Variable + redeploy
→ Check logs again

Error: "Token not passing"
→ Verify all 6 nodes deployed
→ Check PEERS format (comma-separated)
→ Ensure no typos in PEERS string

Still stuck?
→ Check: RAILWAY_DEPLOYMENT_DIRECT.md (PHẦN 10)
→ Or ask PM
```

---

## 🚀 READY?

### PM: Start here
1. Create repo
2. Assign nodes
3. Open: **PM_RAILWAY_DIRECT.md**

### Team Member: Start here
1. Get node number from PM
2. Fork repo
3. Open: **INDIVIDUAL_DEPLOYMENT_CHECKLIST.md**

### Everyone: Reference
- **FULL_DEPLOYMENT_INDEX.md** (master guide)
- **RAILWAY_DEPLOYMENT_DIRECT.md** (details)
- **SOURCE_CODE_DEPLOYMENT.md** (technical)

---

**Estimated reading time for this card: 5 minutes ✓**

**Next action: Open your role-specific document**

🎯 Let's go! 

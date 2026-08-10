# RCMS Manual QA Test Cases & Database Verification Suite

## 1. System Access & Environment Configurations

- **Application Portal URL**: [http://localhost:8080](http://localhost:8080)
- **H2 Database Console**: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:rcms_db`, Username: `sa`, Password: *blank*)
- **File Upload Path**: `uploads/papers/`

---

## 2. Pre-Seeded Academic User Cheat Sheet (25 Seeded Users)

### System Administrators (`ADMIN` Role)
| Name | Email Address | Password | Primary Domain |
|:---|:---|:---|:---|
| Lead Administrator | `admin@rcms.com` | `admin123` | Platform Governance |
| Dr. Sarah Jenkins | `admin2@rcms.com` | `admin123` | Event Operations |
| Prof. Michael Faraday | `admin3@rcms.com` | `admin123` | Publications Chair |

### Research Authors (`AUTHOR` Role)
| Name | Email Address | Password | Affiliation / University |
|:---|:---|:---|:---|
| Dr. Alan Turing | `author@rcms.com` | `password123` | IAS Princeton |
| Dr. Marie Curie | `author2@rcms.com` | `password123` | Sorbonne University |
| Prof. Richard Feynman | `author3@rcms.com` | `password123` | Caltech |
| Dr. Barbara Liskov | `author4@rcms.com` | `password123` | MIT CSAIL |
| Dr. Donald Knuth | `author5@rcms.com` | `password123` | Stanford University |
| Prof. Andrew Ng | `author6@rcms.com` | `password123` | Stanford AI Lab |
| Dr. Fei-Fei Li | `author7@rcms.com` | `password123` | Stanford HAI |
| Prof. Yann LeCun | `author8@rcms.com` | `password123` | NYU Courant |
| Dr. Geoffrey Hinton | `author9@rcms.com` | `password123` | University of Toronto |
| Prof. Yoshua Bengio | `author10@rcms.com` | `password123` | MILA |
| Dr. Tim Berners-Lee | `author11@rcms.com` | `password123` | Oxford University |
| Prof. Margaret Hamilton | `author12@rcms.com` | `password123` | MIT Software Engineering |

### Peer Reviewers (`REVIEWER` Role)
| Name | Email Address | Password | Specialty Area |
|:---|:---|:---|:---|
| Prof. Ada Lovelace | `reviewer@rcms.com` | `password123` | Algorithmic Computing |
| Prof. Claude Shannon | `reviewer2@rcms.com` | `password123` | Information Theory |
| Dr. Grace Hopper | `reviewer3@rcms.com` | `password123` | Compiler Verification |
| Prof. Edsger Dijkstra | `reviewer4@rcms.com` | `password123` | Graph Theory & Systems |
| Dr. Leslie Lamport | `reviewer5@rcms.com` | `password123` | Distributed Consensus |
| Prof. Shafi Goldwasser | `reviewer6@rcms.com` | `password123` | Zero-Knowledge Cryptography |
| Dr. Judea Pearl | `reviewer7@rcms.com` | `password123` | Causal Inference |
| Prof. Silvio Micali | `reviewer8@rcms.com` | `password123` | Algorithmic Economics |
| Dr. Daphne Koller | `reviewer9@rcms.com` | `password123` | Computational Biology |
| Prof. Robert Tarjan | `reviewer10@rcms.com` | `password123` | Data Structure Optimization |

---

## 3. Detailed Manual Test Cases

### Role 1: Public & Guest Visitor

#### Test Case TC-PUB-01: Public Homepage & Metric Counter Verification
- **Steps**:
  1. Open browser and navigate to `http://localhost:8080/`.
  2. Inspect top Navigation Bar brand logo and navigation items.
  3. Inspect Metric Counter cards.
- **Expected Result**:
  - Logo displays clearly without visual box artifacts.
  - Counters display active seeded statistics (Conferences: `2`, Papers: `2`, Authors: `12`, Reviewers: `10`, Certificates: `1`).

#### Test Case TC-PUB-02: Certificate Verification Search
- **Steps**:
  1. Navigate to `/certificates/verify` or use the home page search widget.
  2. Enter verification code `RCMS8888`.
  3. Click **Verify**.
- **Expected Result**:
  - Displays authentic certificate details (Recipient: *Dr. Barbara Liskov*, Type: *PRESENTATION*, Status: *VALID*).

---

### Role 2: Research Author

#### Test Case TC-ATH-01: Author Sign In & Shortcut Login
- **Steps**:
  1. Click **Sign In** on header navbar.
  2. Click **Author Quick Login Fill** (`author@rcms.com` / `password123`).
  3. Click **Sign In**.
- **Expected Result**:
  - Redirects to `/author/dashboard`.
  - Header user dropdown shows **Dr. Alan Turing**.

#### Test Case TC-ATH-02: Research Paper Manuscript Submission
- **Steps**:
  1. Click **Submit Paper** or navigate to `/author/submit-paper`.
  2. Select Conference: *ICCSAI 2026*, Track: *AI & ML*.
  3. Enter Title: *Quantum Circuit Compression via Tensor Decomposition*.
  4. Enter Abstract: *Novel tensor network reduction for quantum gate count optimization.*
  5. Upload sample PDF manuscript.
  6. Click **Submit Manuscript**.
- **Expected Result**:
  - Displays success message *"Paper submitted successfully!"*.
  - Paper appears in Author Dashboard with status `SUBMITTED`.

---

### Role 3: Peer Reviewer

#### Test Case TC-REV-01: Peer Evaluation Submission
- **Steps**:
  1. Log in as Reviewer (`reviewer@rcms.com` / `password123`).
  2. Navigate to Reviewer Dashboard `/reviewer/dashboard`.
  3. Click **Evaluate / Submit Review** on assigned paper `#1`.
  4. Enter Score: `9`, Select Recommendation: `ACCEPT`.
  5. Enter Review Comments: *"Solid methodology and thorough experimental evaluation."*.
  6. Click **Submit Evaluation**.
- **Expected Result**:
  - Displays success message *"Review submitted successfully!"*.
  - Review status updates to `COMPLETED`.

---

### Role 4: System Administrator

#### Test Case TC-ADM-01: User Management & Role Promotion
- **Steps**:
  1. Log in as System Admin (`admin@rcms.com` / `admin123`).
  2. Navigate to `/admin/users`.
  3. Locate user **Prof. Margaret Hamilton** (`author12@rcms.com`).
  4. Change role select dropdown from `AUTHOR` to `REVIEWER`.
  5. Click **Update**.
- **Expected Result**:
  - Success message: *"User role updated successfully!"*.
  - User role badge updates to yellow `REVIEWER` badge.

#### Test Case TC-ADM-02: Final Decision & Certificate Generation
- **Steps**:
  1. Logged in as Admin, navigate to `/admin/papers`.
  2. Open paper detail view for paper `#1`.
  3. Select Status: `ACCEPTED` and click **Update Status**.
  4. Click **Issue Certificate**.
- **Expected Result**:
  - Paper status updates to green `ACCEPTED` badge.
  - Unique certificate generated and immediately verifiable via verification code.

---

## 4. SQL Database Verification Queries

Run the following queries in H2 Console or MySQL Workbench to verify database state integrity:

```sql
-- 1. Verify User Count & Role Distribution
SELECT role, COUNT(*) AS TotalCount FROM users GROUP BY role;

-- 2. List All Active Conferences
SELECT id, title, status, start_date FROM conferences;

-- 3. Verify Submitted Papers & Statuses
SELECT id, title, status, author_id FROM papers;

-- 4. Verify Issued Digital Certificates
SELECT certificate_code, type, user_id, paper_id FROM certificates;
```

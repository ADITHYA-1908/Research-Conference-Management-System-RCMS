# RCMS User Guide, Demonstration Walkthrough & Test Data Manual

Welcome to the **Research Conference Management System (RCMS)** comprehensive User Guide and Testing Manual. This document provides step-by-step instructions for operating, demonstrating, and performing manual quality assurance across all user roles and viewport sizes.

---

## 1. Executive Demonstration Workflow (10–15 Minutes)

Follow this sequential workflow to showcase the full end-to-end capabilities of RCMS to stakeholders, faculty evaluators, or client teams:

```text
    1. Open RCMS Homepage (http://localhost:8080)
                      ↓
    2. Public Certificate Verification (Code: RCMS8888)
                      ↓
    3. Login as System Admin (admin@rcms.com / admin123)
                      ↓
    4. Inspect Admin Dashboard Metrics & User Roster (25 Seeded Users)
                      ↓
    5. Review Conference & Research Track Settings
                      ↓
    6. Logout → Login as Author (author@rcms.com / password123)
                      ↓
    7. Submit PDF Research Manuscript to Selected Track
                      ↓
    8. Logout → Login as Admin (admin@rcms.com / admin123)
                      ↓
    9. Assign Expert Peer Reviewer (reviewer@rcms.com) to Submission
                      ↓
    10. Logout → Login as Reviewer (reviewer@rcms.com / password123)
                      ↓
    11. Submit Peer Evaluation (Score 1-10, Recommendation, Comments)
                      ↓
    12. Logout → Login as Admin (admin@rcms.com / admin123)
                      ↓
    13. Process Final Chair Decision (ACCEPT / REJECT) & Issue Certificate
                      ↓
    14. Logout → Login as Author → View Acceptance & Download Certificate
                      ↓
    15. Publicly Verify Certificate using the newly generated verification code
```

---

## 2. Role-Based Feature & Usage Manual

### 🌐 Public & Guest Visitor Role
- **Homepage (`/`)**: Displays real-time platform statistics (active conferences, paper submissions, registered authors, reviewers, issued certificates) and the quick certificate verification widget.
- **Conference Directory (`/conferences`)**: Browse active and upcoming academic conferences.
- **Conference Details (`/conferences/{id}`)**: Review Calls for Papers (CFP), key dates, track listings, and registration options.
- **Certificate Verification (`/certificates/verify`)**: Public lookup for digital presentation and participation certificates by verification code (e.g. `RCMS8888`).
- **Access Control**: Unauthenticated access to role dashboards automatically redirects users to `/login`.

---

### 🛡️ System Administrator Role (`admin@rcms.com` / `admin123`)
- **Admin Dashboard (`/admin/dashboard`)**: Displays system metrics cards and recent manuscript submission overviews.
- **User Directory (`/admin/users`)**: Inspect all 25 pre-seeded user accounts, modify role permissions, and view registered authors (`/admin/authors`).
- **Conference Management (`/admin/conferences`, `/admin/conference/new`, `/admin/conference/edit/{id}`)**: Create, update, or delete academic conferences, set start/end dates, submission deadlines, and review deadlines.
- **Track Management (`/admin/tracks`, `/admin/track/save`)**: Define research tracks (e.g., *Artificial Intelligence*, *Distributed Systems*) for conferences.
- **Paper Evaluation & Reviewer Assignment (`/admin/papers`, `/admin/paper/{id}`)**: View submitted manuscripts, inspect author details, assign expert reviewers (`/admin/paper/{id}/assign`), and issue final chair decisions (`ACCEPTED` / `REJECTED`).
- **Certificate Issuance (`/admin/certificates`, `/admin/certificate/issue`)**: Issue official digital certificates to authors or attendees for conference participation.

---

### ✍️ Research Author Role (`author@rcms.com` / `password123`)
- **Author Dashboard (`/author/dashboard`)**: View author statistics (Total Submitted, Accepted, Under Review) and a list of personal submissions.
- **Paper Submission (`/author/submit-paper`)**: Submit new manuscripts by selecting an open conference and track, providing title, keywords, abstract, and uploading a PDF file.
- **Status Tracking (`/author/paper/{id}`)**: Track real-time review progress, view decision statuses, or withdraw a submission (`/author/paper/{id}/withdraw`).
- **Profile Settings (`/author/profile`)**: Update personal details (name, phone, institution, bio) and change account credentials.

---

### 🔍 Peer Reviewer Role (`reviewer@rcms.com` / `password123`)
- **Reviewer Dashboard (`/reviewer/dashboard`)**: View reviewer statistics (Total Assigned, Pending Reviews, Completed Reviews) and assigned manuscript cards.
- **Assigned Manuscripts (`/reviewer/papers`)**: Review manuscripts assigned by system administrators.
- **Evaluation Form (`/reviewer/review-form/{id}`)**: Submit evaluation scores (1–10 scale), technical comments, and recommendations (`ACCEPT` / `REJECT`).
- **Review History (`/reviewer/review-detail/{id}`)**: Inspect completed evaluation history.

---

> [!NOTE]
> **DEVELOPMENT / DEMO FIXTURES NOTICE**:
> The user accounts and certificate code listed below are **development/demo test fixtures** pre-seeded into the database (`DataInitializer.java`) to enable immediate manual QA testing and feature demonstration.

## 3. Development / Demo Test Credentials

The system comes pre-seeded with **25 academic accounts** for instant testing:

| Role | Name | Email | Password | Primary Focus |
|:---|:---|:---|:---|:---|
| 🛡️ **ADMIN** | Lead Administrator | `admin@rcms.com` | `admin123` | Platform Governance |
| 🛡️ **ADMIN** | Dr. Sarah Jenkins | `admin2@rcms.com` | `admin123` | Event & Track Oversight |
| ✍️ **AUTHOR** | Dr. Alan Turing | `author@rcms.com` | `password123` | Computer Science & AI Submissions |
| ✍️ **AUTHOR** | Dr. Marie Curie | `author2@rcms.com` | `password123` | Physics & Applied Systems |
| ✍️ **AUTHOR** | Dr. Barbara Liskov | `author4@rcms.com` | `password123` | Distributed Systems Submissions |
| 🔍 **REVIEWER** | Prof. Ada Lovelace | `reviewer@rcms.com` | `password123` | Algorithmic Evaluation |
| 🔍 **REVIEWER** | Dr. Grace Hopper | `reviewer3@rcms.com` | `password123` | Systems & Compiler Verification |

> 🏷️ **Demo Certificate Code**: **`RCMS8888`** (Issued to Dr. Barbara Liskov for paper presentation as a demo fixture).

---

## 4. Realistic Sample Test Data

### Sample Conference Configuration
```text
Conference Title: International Conference on AI & Intelligent Systems 2027
Short Code:       ICAIIS2027
Description:      Global forum for artificial intelligence, machine learning, and computer vision research.
Venue:            Grand Academic Center, San Francisco, CA / Hybrid
Dates:            2027-04-15 to 2027-04-18
Submission DL:    2027-02-15
Review DL:        2027-03-15
Tracks:           1. Artificial Intelligence & Machine Learning
                  2. Computer Vision & Robotics
                  3. Natural Language Processing
```

### Sample Manuscripts for State Transition Verification
1. **Paper 1 (ACCEPTED)**:
   - **Title**: `Lightweight Vision Transformers for Edge-Based Object Detection`
   - **Track**: Computer Vision & Robotics
   - **Keywords**: `Vision Transformers, Edge AI, Object Detection, Deep Learning`
   - **Expected State**: Accepted with generated presentation certificate.

2. **Paper 2 (UNDER_REVIEW)**:
   - **Title**: `Fault-Tolerant Byzantine Consensus in Asynchronous High-Throughput Blockchains`
   - **Track**: Distributed Systems
   - **Keywords**: `Blockchain, Byzantine Fault Tolerance, Distributed Consensus`
   - **Expected State**: Assigned to reviewer, pending review submission.

3. **Paper 3 (REJECTED)**:
   - **Title**: `A Heuristic Approach to Unconstrained NP-Hard Graph Coloring`
   - **Track**: Theoretical Computer Science
   - **Keywords**: `Graph Theory, Heuristics, Algorithms`
   - **Expected State**: Evaluated with low scores and rejected decision.

---

## 5. Negative & Validation Test Matrix

| Category | Test Case Scenario | Input Value / File | Expected System Behavior |
|:---|:---|:---|:---|
| **Empty Input** | Missing Paper Title | `""` (Empty string) | Validation error: *"Title is required."* |
| **Empty Input** | Missing Abstract | `""` (Empty string) | Validation error: *"Abstract is required."* |
| **Invalid Format**| Non-PDF Document | `manuscript.docx` | File upload rejected: *"Only PDF files are allowed."* |
| **Invalid Format**| Image File | `figure.jpg` | File upload rejected: *"Only PDF files are allowed."* |
| **Oversized File**| PDF Exceeding 10MB | `large_file_12MB.pdf` | Upload rejected with size error. |
| **Special Chars** | Special Characters | `AI-Based "Smart" Systems & O'Connor's Study` | Accepted and rendered without escaping defects. |
| **Boundary Value**| Score Out of Range | Score: `15` (Valid range: 1–10) | Form rejected: *"Score must be between 1 and 10."* |
| **Invalid Lookup**| Certificate Lookup | `INVALID999` | Page displays: *"No authentic certificate found with code: INVALID999"*. |

---

## 6. Manual QA Test Case Suite

| ID | Role | Feature Area | Input Test Data | Action Taken | Expected Result | Status |
|:---|:---|:---|:---|:---|:---|:---|
| **TC-01** | Public | Certificate Lookup | Code: `RCMS8888` | Submit code on homepage widget | Displays Dr. Barbara Liskov's Certificate | **PASS** |
| **TC-02** | Public | Certificate Lookup | Code: `INVALID999` | Submit invalid code | Displays invalid code error alert | **PASS** |
| **TC-03** | Public | Conference List | Nav Link `/conferences` | Click "Browse Conferences" | Lists active conference directories | **PASS** |
| **TC-04** | Admin | Authentication | `admin@rcms.com` / `admin123` | Submit login form | Redirects to `/admin/dashboard` | **PASS** |
| **TC-05** | Admin | User Directory | Nav Link `/admin/users` | View User Directory | Lists all 25 seeded user accounts | **PASS** |
| **TC-06** | Admin | Create Conference | Name: `ICAIIS 2027` | Submit new conference form | Conference saved and listed | **PASS** |
| **TC-07** | Admin | Assign Reviewer | Reviewer: `reviewer@rcms.com` | Click "Assign Reviewer" | Reviewer assigned; status `UNDER_REVIEW` | **PASS** |
| **TC-08** | Author | Authentication | `author@rcms.com` / `password123` | Submit login form | Redirects to `/author/dashboard` | **PASS** |
| **TC-09** | Author | Submit Paper | PDF file + Metadata | Submit paper form | Paper submitted; status `SUBMITTED` | **PASS** |
| **TC-10** | Author | Invalid Upload | Upload `document.docx` | Submit paper form | Rejected: *"Only PDF files allowed"* | **PASS** |
| **TC-11** | Reviewer| Authentication | `reviewer@rcms.com` / `password123`| Submit login form | Redirects to `/reviewer/dashboard` | **PASS** |
| **TC-12** | Reviewer| Submit Review | Score: `9`, Rec: `ACCEPT` | Submit evaluation form | Review saved; status `COMPLETED` | **PASS** |
| **TC-13** | Security| Route Access | Navigate to `/admin/dashboard` | Access as Author or Guest | Access denied; redirected to `/login` | **PASS** |

---

## 7. Mobile & Responsive Viewport Checklist

Verify UI layout and touch interaction across standard screen sizes:

| Viewport Width | Device Category | Testing Checklist | Pass / Fail |
|:---|:---|:---|:---|
| **1920×1080** | Desktop Ultra-Wide | Header navigation wide layout, grid card alignment. | **PASS** |
| **1440×900** | Desktop Standard | Centered containers, balanced form padding. | **PASS** |
| **1366×768** | Laptop Compact | Navbar links fit without text wrapping. | **PASS** |
| **1024×768** | Tablet Landscape | Table container scrolls horizontally if needed. | **PASS** |
| **768×1024** | Tablet Portrait | Stat cards stack into 2-column layout. | **PASS** |
| **430×932** | Mobile Large | Mobile hamburger drawer opens smoothly. Mobile back button visible. | **PASS** |
| **390×844** | Mobile Standard | Primary action buttons stack vertically. Mobile back button visible. | **PASS** |
| **375×667** | Mobile Small | Certificate search input fits screen. Mobile back button visible. | **PASS** |

---

## 8. Related System Documentation

- 📖 [Developer Setup Guide](SETUP.md)
- 🐳 [Docker Infrastructure Guide](DOCKER_SETUP.md)
- 🛢️ [Database Architecture & Profiles](DATABASE_SETUP.md)
- 🛠️ [Development & Architecture Guide](DEVELOPMENT.md)
- ❓ [Troubleshooting & FAQ](TROUBLESHOOTING.md)

package com.rcms.config;

import com.rcms.entity.*;
import com.rcms.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConferenceRepository conferenceRepository;

    @Autowired
    private PaperRepository paperRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private TrackRepository trackRepository;

    @Autowired
    private CertificateRepository certificateRepository;

    @Override
    public void run(String... args) throws Exception {
        // Seed 25 Users across ADMIN, AUTHOR, and REVIEWER roles
        List<User> seededUsers = seed25Users();

        // Seed Conferences, Tracks, Papers, Reviews, Certificates
        seedConferencesAndRelatedData(seededUsers);
    }

    private List<User> seed25Users() {
        List<User> users = new ArrayList<>();

        // --- 3 ADMIN USERS ---
        createOrGet(users, "System Administrator", "admin@rcms.com", "admin123", "ADMIN", "Central University", "+1-555-0100", "Lead System Administrator for Research Conference Management System.");
        createOrGet(users, "Dr. Sarah Jenkins", "admin2@rcms.com", "admin123", "ADMIN", "MIT - Office of Research", "+1-555-0101", "Director of Academic Events & Conference Operations.");
        createOrGet(users, "Prof. Michael Faraday", "admin3@rcms.com", "admin123", "ADMIN", "Cambridge Academic Board", "+1-555-0102", "Dean of Academic Publications & Peer Review Governance.");

        // --- 12 AUTHOR USERS ---
        createOrGet(users, "Dr. Alan Turing", "author@rcms.com", "password123", "AUTHOR", "Institute for Advanced Study", "+1-555-0200", "Pioneer in Theoretical Computer Science, Computability & Artificial Intelligence.");
        createOrGet(users, "Dr. Marie Curie", "author2@rcms.com", "password123", "AUTHOR", "Sorbonne University", "+1-555-0201", "Senior Researcher in Experimental Physics & Nuclear Technologies.");
        createOrGet(users, "Prof. Richard Feynman", "author3@rcms.com", "password123", "AUTHOR", "Caltech Quantum Institute", "+1-555-0202", "Quantum Electrodynamics & Nano-Scale Computing Specialist.");
        createOrGet(users, "Dr. Barbara Liskov", "author4@rcms.com", "password123", "AUTHOR", "MIT CSAIL", "+1-555-0203", "Turing Award Laureate in Distributed Systems & Object-Oriented Design.");
        createOrGet(users, "Dr. Donald Knuth", "author5@rcms.com", "password123", "AUTHOR", "Stanford University", "+1-555-0204", "Author of The Art of Computer Programming & Algorithm Analysis.");
        createOrGet(users, "Prof. Andrew Ng", "author6@rcms.com", "password123", "AUTHOR", "Stanford AI Lab", "+1-555-0205", "Machine Learning & Deep Learning Curriculum Director.");
        createOrGet(users, "Dr. Fei-Fei Li", "author7@rcms.com", "password123", "AUTHOR", "Stanford Human-Centered AI", "+1-555-0206", "Computer Vision, ImageNet Creator & Spatial Intelligence Pioneer.");
        createOrGet(users, "Prof. Yann LeCun", "author8@rcms.com", "password123", "AUTHOR", "NYU Center for Data Science", "+1-555-0207", "Convolutional Neural Networks & Self-Supervised Learning Researcher.");
        createOrGet(users, "Dr. Geoffrey Hinton", "author9@rcms.com", "password123", "AUTHOR", "University of Toronto", "+1-555-0208", "Deep Learning & Neural Network Backpropagation Co-Inventor.");
        createOrGet(users, "Prof. Yoshua Bengio", "author10@rcms.com", "password123", "AUTHOR", "MILA - Montreal AI", "+1-555-0209", "Neural Language Modeling & Generative AI Pioneer.");
        createOrGet(users, "Dr. Tim Berners-Lee", "author11@rcms.com", "password123", "AUTHOR", "Oxford University & W3C", "+1-555-0210", "Inventor of the World Wide Web & Decentralized Web Architect.");
        createOrGet(users, "Prof. Margaret Hamilton", "author12@rcms.com", "password123", "AUTHOR", "MIT Software Engineering Division", "+1-555-0211", "Apollo Guidance Computer Lead Software Engineer.");

        // --- 10 REVIEWER USERS ---
        createOrGet(users, "Prof. Ada Lovelace", "reviewer@rcms.com", "password123", "REVIEWER", "Department of Mathematics", "+1-555-0300", "Professor & Peer Review Specialist in Algorithmic Computing.");
        createOrGet(users, "Prof. Claude Shannon", "reviewer2@rcms.com", "password123", "REVIEWER", "Bell Labs Research", "+1-555-0301", "Father of Information Theory & Digital Circuit Design.");
        createOrGet(users, "Dr. Grace Hopper", "reviewer3@rcms.com", "password123", "REVIEWER", "Harvard Computation Lab", "+1-555-0302", "COBOL Co-Creator & Compiler Verification Specialist.");
        createOrGet(users, "Prof. Edsger Dijkstra", "reviewer4@rcms.com", "password123", "REVIEWER", "UT Austin CS Dept", "+1-555-0303", "Graph Theory & Concurrent Programming Formal Verification Reviewer.");
        createOrGet(users, "Dr. Leslie Lamport", "reviewer5@rcms.com", "password123", "REVIEWER", "Microsoft Research", "+1-555-0304", "Distributed Consensus (Paxos) & LaTeX Creator.");
        createOrGet(users, "Prof. Shafi Goldwasser", "reviewer6@rcms.com", "password123", "REVIEWER", "Weizmann Institute of Science", "+1-555-0305", "Zero-Knowledge Proofs & Cryptography Senior Reviewer.");
        createOrGet(users, "Dr. Judea Pearl", "reviewer7@rcms.com", "password123", "REVIEWER", "UCLA AI Laboratory", "+1-555-0306", "Bayesian Networks & Causal Inference Specialist.");
        createOrGet(users, "Prof. Silvio Micali", "reviewer8@rcms.com", "password123", "REVIEWER", "MIT Theory of Computation", "+1-555-0307", "Algorand Creator & Provable Security Reviewer.");
        createOrGet(users, "Dr. Daphne Koller", "reviewer9@rcms.com", "password123", "REVIEWER", "Insitro & Stanford", "+1-555-0308", "Probabilistic Graphical Models & Computational Biology Expert.");
        createOrGet(users, "Prof. Robert Tarjan", "reviewer10@rcms.com", "password123", "REVIEWER", "Princeton University", "+1-555-0309", "Data Structures & Graph Algorithms Chief Review Editor.");

        return users;
    }

    private void createOrGet(List<User> list, String name, String email, String password, String role, String institution, String phone, String bio) {
        User u = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setName(name);
            newUser.setEmail(email);
            newUser.setPassword(password);
            newUser.setRole(role);
            newUser.setInstitution(institution);
            newUser.setPhone(phone);
            newUser.setBio(bio);
            return userRepository.save(newUser);
        });
        list.add(u);
    }

    private void seedConferencesAndRelatedData(List<User> users) {
        Conference savedConf1;
        if (conferenceRepository.count() == 0) {
            // Conference 1: ICCSAI 2026
            Conference conf1 = new Conference();
            conf1.setTitle("International Conference on Computer Science & AI (ICCSAI 2026)");
            conf1.setTheme("Next-Generation Intelligent & Distributed Computing Systems");
            conf1.setDescription("ICCSAI 2026 brings together leading academic scientists, researchers, and scholars to exchange experience on AI, Cloud, and Edge Computing.");
            conf1.setLocation("Main Campus Auditorium, University Center");
            conf1.setStartDate(LocalDate.of(2026, 10, 15));
            conf1.setEndDate(LocalDate.of(2026, 10, 17));
            conf1.setSubmissionDeadline(LocalDate.of(2026, 9, 30));
            conf1.setStatus("OPEN");
            savedConf1 = conferenceRepository.save(conf1);

            // Conference 2: IEEE Security & Privacy 2026
            Conference conf2 = new Conference();
            conf2.setTitle("IEEE Symposium on Cyber Security & Quantum Cryptography (ISQC 2026)");
            conf2.setTheme("Post-Quantum Cryptography & Zero-Trust Architecture");
            conf2.setDescription("Premier academic symposium dedicated to zero-knowledge proofs, post-quantum encryption, and hardware enclave security.");
            conf2.setLocation("ETH Zurich Conference Hall, Switzerland");
            conf2.setStartDate(LocalDate.of(2026, 11, 20));
            conf2.setEndDate(LocalDate.of(2026, 11, 22));
            conf2.setSubmissionDeadline(LocalDate.of(2026, 10, 15));
            conf2.setStatus("OPEN");
            conferenceRepository.save(conf2);
        } else {
            savedConf1 = conferenceRepository.findAll().get(0);
        }

        Track savedTrack1;
        Track savedTrack2;
        if (trackRepository.count() == 0) {
            Track track1 = new Track();
            track1.setName("Artificial Intelligence & Machine Learning");
            track1.setDescription("Neural networks, deep learning, NLP, and computer vision.");
            track1.setConference(savedConf1);
            savedTrack1 = trackRepository.save(track1);

            Track track2 = new Track();
            track2.setName("Distributed Systems & Cloud Computing");
            track2.setDescription("Consensus protocols, serverless, microservices, and edge computing.");
            track2.setConference(savedConf1);
            savedTrack2 = trackRepository.save(track2);
        } else {
            List<Track> tracks = trackRepository.findAll();
            savedTrack1 = tracks.get(0);
            savedTrack2 = tracks.size() > 1 ? tracks.get(1) : tracks.get(0);
        }

        User authorTuring = users.stream().filter(u -> u.getEmail().equals("author@rcms.com")).findFirst().orElse(users.get(3));
        User authorLiskov = users.stream().filter(u -> u.getEmail().equals("author4@rcms.com")).findFirst().orElse(users.get(6));
        User reviewerAda = users.stream().filter(u -> u.getEmail().equals("reviewer@rcms.com")).findFirst().orElse(users.get(15));
        User reviewerGrace = users.stream().filter(u -> u.getEmail().equals("reviewer3@rcms.com")).findFirst().orElse(users.get(17));

        Paper savedPaper1;
        Paper savedPaper2;
        if (paperRepository.count() == 0) {
            Paper paper1 = new Paper();
            paper1.setTitle("Optimizing Transformer Architectures for Resource-Constrained Embedded Devices");
            paper1.setAbstractText("This paper introduces a compact quantization algorithm and layer pruning technique for modern transformer neural networks, enabling real-time execution on low-power microcontrollers.");
            paper1.setKeywords("Artificial Intelligence, Neural Networks, Embedded Systems, Edge Computing");
            paper1.setFilePath("sample_manuscript1.pdf");
            paper1.setStatus("UNDER_REVIEW");
            paper1.setAuthor(authorTuring);
            paper1.setConference(savedConf1);
            paper1.setTrack(savedTrack1);
            savedPaper1 = paperRepository.save(paper1);

            Paper paper2 = new Paper();
            paper2.setTitle("Fault-Tolerant Byzantine Consensus in Asynchronous High-Throughput Blockchains");
            paper2.setAbstractText("We propose a novel asynchronous consensus protocol achieving O(n^2) message complexity while guaranteeing safety under partial synchrony assumptions.");
            paper2.setKeywords("Distributed Systems, Consensus Protocols, Byzantine Fault Tolerance");
            paper2.setFilePath("sample_manuscript2.pdf");
            paper2.setStatus("ACCEPTED");
            paper2.setAuthor(authorLiskov);
            paper2.setConference(savedConf1);
            paper2.setTrack(savedTrack2);
            savedPaper2 = paperRepository.save(paper2);
        } else {
            List<Paper> papers = paperRepository.findAll();
            savedPaper1 = papers.get(0);
            savedPaper2 = papers.size() > 1 ? papers.get(1) : papers.get(0);
        }

        if (reviewRepository.count() == 0) {
            Review review1 = new Review();
            review1.setPaper(savedPaper1);
            review1.setReviewer(reviewerAda);
            review1.setStatus("PENDING");
            reviewRepository.save(review1);

            Review review2 = new Review();
            review2.setPaper(savedPaper2);
            review2.setReviewer(reviewerGrace);
            review2.setScore(96);
            review2.setComments("Exceptional theoretical analysis and rigorous proof of safety.");
            review2.setRecommendation("ACCEPT");
            review2.setStatus("COMPLETED");
            reviewRepository.save(review2);
        }

        if (certificateRepository.count() == 0) {
            Certificate cert = new Certificate();
            cert.setUser(authorLiskov);
            cert.setConference(savedConf1);
            cert.setPaper(savedPaper2);
            cert.setType("PRESENTATION");
            cert.setCertificateCode("RCMS8888");
            certificateRepository.save(cert);
        }
    }
}

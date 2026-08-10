# Generate Sample Research Paper PDFs for RCMS Testing
# Uses iText-style raw PDF generation (no external dependencies)

function New-SamplePDF {
    param(
        [string]$FilePath,
        [string]$Title,
        [string]$Authors,
        [string]$Abstract,
        [string]$Keywords,
        [string[]]$Sections
    )

    # Build PDF content manually using raw PDF specification
    $objects = @()
    $objectOffsets = @()

    # Collect all text lines
    $textLines = @()
    $textLines += "RESEARCH PAPER"
    $textLines += ""
    $textLines += $Title
    $textLines += ""
    $textLines += "Authors: $Authors"
    $textLines += ""
    $textLines += "ABSTRACT"
    $textLines += ""
    # Word-wrap abstract at ~90 chars
    $words = $Abstract -split '\s+'
    $line = ""
    foreach ($w in $words) {
        if (($line + " " + $w).Length -gt 90) {
            $textLines += $line.Trim()
            $line = $w
        } else {
            $line = "$line $w"
        }
    }
    if ($line.Trim()) { $textLines += $line.Trim() }

    $textLines += ""
    $textLines += "Keywords: $Keywords"
    $textLines += ""

    foreach ($sec in $Sections) {
        $secLines = $sec -split "`n"
        foreach ($sl in $secLines) {
            $words2 = $sl -split '\s+'
            $line2 = ""
            foreach ($w2 in $words2) {
                if (($line2 + " " + $w2).Length -gt 90) {
                    $textLines += $line2.Trim()
                    $line2 = $w2
                } else {
                    $line2 = "$line2 $w2"
                }
            }
            if ($line2.Trim()) { $textLines += $line2.Trim() }
        }
        $textLines += ""
    }

    # Build PDF stream content
    $streamLines = @()
    $streamLines += "BT"
    $streamLines += "/F1 11 Tf"
    $y = 750
    foreach ($tl in $textLines) {
        if ($y -lt 60) { break }
        $escaped = $tl -replace '[\\()]', '\$0'
        $streamLines += "1 0 0 1 50 $y Tm"
        # Bold-like effect for headers
        if ($tl -match '^(RESEARCH PAPER|ABSTRACT|Keywords:|Authors:|[0-9]+\.)' -or $tl -eq $Title) {
            $streamLines += "/F1 13 Tf"
            $streamLines += "($escaped) Tj"
            $streamLines += "/F1 11 Tf"
        } else {
            $streamLines += "($escaped) Tj"
        }
        $y -= 16
    }
    $streamLines += "ET"
    $streamContent = ($streamLines -join "`n")
    $streamBytes = [System.Text.Encoding]::ASCII.GetBytes($streamContent)
    $streamLen = $streamBytes.Length

    # Build the PDF document
    $pdf = @()
    $pdf += "%PDF-1.4"

    # Object 1: Catalog
    $objectOffsets += ($pdf -join "`n").Length + 1
    $pdf += "1 0 obj"
    $pdf += "<< /Type /Catalog /Pages 2 0 R >>"
    $pdf += "endobj"

    # Object 2: Pages
    $objectOffsets += ($pdf -join "`n").Length + 1
    $pdf += "2 0 obj"
    $pdf += "<< /Type /Pages /Kids [3 0 R] /Count 1 >>"
    $pdf += "endobj"

    # Object 3: Page
    $objectOffsets += ($pdf -join "`n").Length + 1
    $pdf += "3 0 obj"
    $pdf += "<< /Type /Page /Parent 2 0 R /MediaBox [0 0 612 792] /Contents 4 0 R /Resources << /Font << /F1 5 0 R >> >> >>"
    $pdf += "endobj"

    # Object 4: Content Stream
    $objectOffsets += ($pdf -join "`n").Length + 1
    $pdf += "4 0 obj"
    $pdf += "<< /Length $streamLen >>"
    $pdf += "stream"
    $pdf += $streamContent
    $pdf += "endstream"
    $pdf += "endobj"

    # Object 5: Font
    $objectOffsets += ($pdf -join "`n").Length + 1
    $pdf += "5 0 obj"
    $pdf += "<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica >>"
    $pdf += "endobj"

    # Cross-reference table
    $xrefOffset = ($pdf -join "`n").Length + 1
    $pdf += "xref"
    $pdf += "0 6"
    $pdf += "0000000000 65535 f "
    for ($i = 0; $i -lt 5; $i++) {
        $pdf += ("{0:D10} 00000 n " -f $objectOffsets[$i])
    }

    $pdf += "trailer"
    $pdf += "<< /Size 6 /Root 1 0 R >>"
    $pdf += "startxref"
    $pdf += "$xrefOffset"
    $pdf += "%%EOF"

    $pdfContent = $pdf -join "`n"
    [System.IO.File]::WriteAllText($FilePath, $pdfContent, [System.Text.Encoding]::ASCII)
    Write-Host "Created: $FilePath"
}

# ============================================================
# Paper 1: AI/ML Research Paper
# ============================================================
New-SamplePDF `
    -FilePath "$PSScriptRoot\Sample_Paper_1_Deep_Learning_Medical_Imaging.pdf" `
    -Title "Deep Learning Approaches for Medical Image Classification: A Comparative Study" `
    -Authors "Dr. Alan Turing, Dr. Barbara Liskov, Prof. Ada Lovelace" `
    -Abstract "This paper presents a comprehensive comparative analysis of deep learning architectures for medical image classification tasks. We evaluate Convolutional Neural Networks (CNNs), Vision Transformers (ViTs), and hybrid architectures across three benchmark medical imaging datasets: ChestX-ray14, ISIC 2019 (dermoscopy), and PathMNIST (histopathology). Our experiments demonstrate that hybrid CNN-Transformer models achieve state-of-the-art accuracy of 94.7 percent on chest X-ray classification while maintaining computational efficiency suitable for clinical deployment. We further propose an ensemble strategy combining ResNet-50, EfficientNet-B4, and Swin Transformer that yields a 2.3 percent improvement over individual model baselines. Statistical significance tests confirm the robustness of our findings across all evaluation metrics including AUC-ROC, F1-score, and sensitivity." `
    -Keywords "Deep Learning, Medical Imaging, CNN, Vision Transformer, Transfer Learning, Classification" `
    -Sections @(
        "1. INTRODUCTION
Medical image analysis has witnessed transformative advances through the application of deep learning techniques. Traditional computer-aided diagnosis systems relied heavily on handcrafted features and domain-specific preprocessing pipelines. The emergence of convolutional neural networks revolutionized this paradigm by enabling end-to-end feature learning directly from raw pixel data. Recent developments in attention mechanisms and transformer architectures have further expanded the methodological toolkit available to researchers. This study systematically compares these architectural families across standardized evaluation protocols.",
        "2. RELATED WORK
Prior work in medical image classification includes the seminal CheXNet architecture by Rajpurkar et al. which demonstrated radiologist-level performance on pneumonia detection. Subsequent studies explored transfer learning from ImageNet-pretrained models, with DenseNet-121 and InceptionV3 emerging as popular backbone choices. The introduction of Vision Transformers by Dosovitskiy et al. opened new avenues for capturing global contextual information in medical images. Recent hybrid approaches such as TransMed and MedViT attempt to combine the inductive biases of CNNs with the long-range dependency modeling of transformers.",
        "3. METHODOLOGY
We adopt a standardized experimental framework comprising three phases: data preprocessing and augmentation, model training with hyperparameter optimization, and comprehensive evaluation. All images are resized to 224x224 pixels and normalized using ImageNet statistics. Data augmentation includes random horizontal flips, rotations up to 15 degrees, and color jitter. We employ AdamW optimizer with cosine annealing learning rate scheduling. Training is conducted on NVIDIA A100 GPUs with mixed-precision computation to accelerate convergence.",
        "4. RESULTS AND DISCUSSION
Our experimental results reveal several key findings. First, EfficientNet-B4 consistently outperforms other single-model architectures with an average AUC-ROC of 0.937 across all datasets. Second, Swin Transformer achieves comparable performance with 23 percent fewer parameters. Third, our proposed ensemble strategy achieves state-of-the-art results with AUC-ROC of 0.961 on ChestX-ray14, 0.943 on ISIC 2019, and 0.952 on PathMNIST.",
        "5. CONCLUSION
This study provides empirical evidence supporting the effectiveness of hybrid CNN-Transformer architectures for medical image classification. Our ensemble approach demonstrates consistent improvements across diverse imaging modalities. Future work will explore federated learning paradigms for privacy-preserving model training across multiple clinical institutions.",
        "REFERENCES
[1] Rajpurkar, P. et al. CheXNet: Radiologist-Level Pneumonia Detection. arXiv:1711.05225, 2017.
[2] Dosovitskiy, A. et al. An Image is Worth 16x16 Words: Transformers for Image Recognition. ICLR, 2021.
[3] Tan, M., Le, Q. EfficientNet: Rethinking Model Scaling for CNNs. ICML, 2019.
[4] Liu, Z. et al. Swin Transformer: Hierarchical Vision Transformer. ICCV, 2021.
[5] He, K. et al. Deep Residual Learning for Image Recognition. CVPR, 2016."
    )

# ============================================================
# Paper 2: Cybersecurity Research Paper
# ============================================================
New-SamplePDF `
    -FilePath "$PSScriptRoot\Sample_Paper_2_IoT_Network_Security.pdf" `
    -Title "Anomaly Detection in IoT Networks Using Federated Machine Learning" `
    -Authors "Dr. Grace Hopper, Prof. Donald Knuth, Dr. Tim Berners-Lee" `
    -Abstract "The proliferation of Internet of Things devices has introduced unprecedented security challenges in modern network infrastructures. This paper proposes a federated learning-based anomaly detection framework for identifying malicious traffic patterns in heterogeneous IoT environments. Our approach enables distributed model training across edge nodes without centralizing sensitive network data, thereby preserving privacy while maintaining detection accuracy. Experimental evaluation on the CIC-IoT-2023 and N-BaIoT benchmark datasets demonstrates that our federated approach achieves 96.2 percent detection accuracy with a false positive rate below 1.8 percent, comparable to centralized baselines while reducing data transmission overhead by 78 percent." `
    -Keywords "IoT Security, Federated Learning, Anomaly Detection, Network Intrusion Detection, Edge Computing, Privacy" `
    -Sections @(
        "1. INTRODUCTION
The rapid expansion of Internet of Things ecosystems has created vast attack surfaces that traditional perimeter-based security models cannot adequately protect. By 2025, an estimated 75 billion IoT devices are projected to be deployed globally, generating massive volumes of heterogeneous network traffic. Centralized monitoring approaches face scalability limitations and raise significant privacy concerns when processing data from distributed sensor networks. Federated learning offers a promising paradigm for collaborative model training that keeps raw data at the network edge.",
        "2. SYSTEM ARCHITECTURE
Our proposed framework consists of three principal components: (1) lightweight traffic feature extractors deployed on IoT gateway nodes, (2) local anomaly detection models trained using gradient-based federated optimization, and (3) a central aggregation server that coordinates model updates using secure aggregation protocols. Each edge node maintains a local Random Forest and LSTM hybrid model that processes network flow statistics including packet rates, byte distributions, and protocol entropy measurements.",
        "3. EXPERIMENTAL EVALUATION
We evaluate our framework using two established IoT security benchmarks. The CIC-IoT-2023 dataset contains 33 attack categories across smart home, industrial, and healthcare IoT scenarios. The N-BaIoT dataset comprises traffic from nine commercial IoT devices under both benign and botnet-infected conditions. Our federated approach achieves an F1-score of 0.954 on CIC-IoT-2023 and 0.968 on N-BaIoT, with convergence achieved within 15 communication rounds.",
        "4. CONCLUSION
This research demonstrates the viability of federated learning for privacy-preserving anomaly detection in IoT networks. Our framework maintains competitive detection performance while eliminating the need for centralized data collection. Ongoing work focuses on Byzantine-resilient aggregation mechanisms and deployment on resource-constrained microcontroller platforms.",
        "REFERENCES
[1] McMahan, B. et al. Communication-Efficient Learning of Deep Networks. AISTATS, 2017.
[2] Meidan, Y. et al. N-BaIoT: Network-Based Detection of IoT Botnet Attacks. IEEE Pervasive Computing, 2018.
[3] Mothukuri, V. et al. A Survey on Security and Privacy of Federated Learning. Future Generation Computer Systems, 2021."
    )

# ============================================================
# Paper 3: Cloud Computing Research Paper
# ============================================================
New-SamplePDF `
    -FilePath "$PSScriptRoot\Sample_Paper_3_Serverless_Edge_Computing.pdf" `
    -Title "Optimizing Serverless Function Placement in Edge-Cloud Continuum Architectures" `
    -Authors "Dr. Vint Cerf, Prof. Leslie Lamport, Dr. Shafi Goldwasser" `
    -Abstract "Serverless computing paradigms are increasingly being extended from centralized cloud platforms to geographically distributed edge infrastructure. This paper addresses the function placement optimization problem in edge-cloud continuum environments, where latency-sensitive workloads must be dynamically allocated across heterogeneous computing nodes. We formulate the placement decision as a constrained optimization problem and propose a reinforcement learning-based scheduler that reduces average end-to-end latency by 41 percent compared to round-robin baselines while maintaining resource utilization above 82 percent. Evaluation on a 12-node testbed with realistic microservice workloads validates the practical applicability of our approach." `
    -Keywords "Serverless Computing, Edge Computing, Function Placement, Reinforcement Learning, Latency Optimization, Microservices" `
    -Sections @(
        "1. INTRODUCTION
The evolution of cloud computing toward edge-cloud continuum architectures demands new approaches to workload orchestration. Traditional serverless platforms such as AWS Lambda and Azure Functions operate within centralized data centers, introducing latency penalties for geographically distributed applications. Edge computing promises sub-10ms response times by processing data closer to its source, but heterogeneous resource availability and dynamic demand patterns complicate scheduling decisions.",
        "2. PROBLEM FORMULATION
We model the edge-cloud continuum as a weighted graph G = (V, E) where vertices represent computing nodes with heterogeneous CPU, memory, and GPU capacities, and edges represent network links with measured latency and bandwidth characteristics. The function placement problem seeks to assign incoming serverless invocations to computing nodes that minimize aggregate response latency subject to resource capacity constraints and data locality preferences.",
        "3. PROPOSED APPROACH
Our reinforcement learning scheduler employs a Proximal Policy Optimization agent that observes system state vectors comprising current node utilizations, pending request queues, and historical latency measurements. The action space corresponds to node selection decisions, and the reward signal combines negative latency with resource balance penalties. We train the agent using simulated workload traces derived from Azure Function execution logs.",
        "4. RESULTS
Evaluation on our 12-node edge-cloud testbed demonstrates that the RL scheduler achieves average latency of 8.3ms compared to 14.1ms for round-robin and 10.7ms for least-loaded heuristics. Resource utilization remains balanced across nodes with standard deviation below 6.2 percent. The scheduling overhead per decision is under 0.4ms, confirming real-time applicability.",
        "5. CONCLUSION
This work demonstrates that reinforcement learning provides an effective approach to serverless function placement in distributed edge-cloud environments. Our scheduler adapts to dynamic workload patterns and heterogeneous infrastructure without requiring manual tuning of placement heuristics.",
        "REFERENCES
[1] Jonas, E. et al. Cloud Programming Simplified: A Berkeley View on Serverless Computing. Technical Report, 2019.
[2] Schulman, J. et al. Proximal Policy Optimization Algorithms. arXiv:1707.06347, 2017.
[3] Shahrad, M. et al. Serverless in the Wild: Characterizing and Optimizing Azure Functions. USENIX ATC, 2020."
    )

Write-Host "`nAll 3 sample research paper PDFs generated successfully!"
Write-Host "Location: $PSScriptRoot"

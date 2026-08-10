package com.rcms.repository;

import com.rcms.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByReviewerId(Long reviewerId);
    List<Review> findByPaperId(Long paperId);
    Optional<Review> findByPaperIdAndReviewerId(Long paperId, Long reviewerId);
    boolean existsByPaperIdAndReviewerId(Long paperId, Long reviewerId);
}

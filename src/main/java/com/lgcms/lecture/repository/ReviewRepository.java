package com.lgcms.lecture.repository;

import com.lgcms.lecture.domain.Review;
import com.lgcms.lecture.dto.internal.LectureReviewsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {


    Page<Review> findAllByLectureId(String lectureId, Pageable pageable);


    Page<Review> findAllByMemberId(Long memberI, Pageable pageable);

    @Query("""
                SELECT new com.lgcms.lecture.dto.internal.LectureReviewsResponse(
                    r.id,
                    r.memberId,
                    r.lectureId,
                    r.suggestion,
                    r.star,
                    r.nickname,
                    (SELECT rcMax.answer FROM ReviewContent rcMax WHERE rcMax.review.id = r.id AND rcMax.id = 
                        (SELECT MAX(rcSub.id) FROM ReviewContent rcSub WHERE rcSub.review.id = r.id)
                    ) as difficulty,
                    (SELECT rcMin.answer FROM ReviewContent rcMin WHERE rcMin.review.id = r.id AND rcMin.id = 
                        (SELECT MIN(rcSub.id) FROM ReviewContent rcSub WHERE rcSub.review.id = r.id)
                    ) as usefulness,
                    r.createdAt
                )
                FROM Review r
            """)
    List<LectureReviewsResponse> findAllReview();
}

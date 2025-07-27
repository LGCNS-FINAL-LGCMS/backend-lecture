package com.lgcms.lecture.repository;

import com.lgcms.lecture.controller.ReviewController;
import com.lgcms.lecture.domain.Review;
import com.lgcms.lecture.dto.response.ReviewResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findAllByLectureId(String lectureId);

    List<Review> findAllByMemberId(Long memberId);
}

package com.lgcms.lecture.repository;

import com.lgcms.lecture.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {


    Page<Review> findAllByLectureId(String lectureId, Pageable pageable);


    Page<Review> findAllByMemberId(Long memberI, Pageable pageable);
}

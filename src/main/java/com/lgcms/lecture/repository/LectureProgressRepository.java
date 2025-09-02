package com.lgcms.lecture.repository;

import com.lgcms.lecture.domain.LectureProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LectureProgressRepository extends JpaRepository<LectureProgress, Long> {
    Optional<LectureProgress> findByMemberIdAndLectureId(Long memberId, String lectureId);
}

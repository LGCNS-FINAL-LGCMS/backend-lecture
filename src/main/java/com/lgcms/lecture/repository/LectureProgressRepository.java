package com.lgcms.lecture.repository;

import com.lgcms.lecture.domain.LectureProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureProgressRepository extends JpaRepository<LectureProgress, Long> {
}

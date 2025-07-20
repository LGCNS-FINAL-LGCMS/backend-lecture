package com.lgcms.lecture.repository;

import com.lgcms.lecture.domain.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, String> {
    Lecture findByMemberId(Long memberId);

    boolean existsByIdAndMemberId(String lectureId, Long memberId);
}

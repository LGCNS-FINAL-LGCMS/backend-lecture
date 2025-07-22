package com.lgcms.lecture.repository;

import com.lgcms.lecture.domain.LectureAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureAnswerRepository extends JpaRepository<LectureAnswer, Long> {
}

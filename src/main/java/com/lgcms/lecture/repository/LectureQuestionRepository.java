package com.lgcms.lecture.repository;

import com.lgcms.lecture.domain.LectureQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureQuestionRepository extends JpaRepository<LectureQuestion, Long> {
    boolean existsByIdAndMemberId(Long qnaId, Long memberId);
}

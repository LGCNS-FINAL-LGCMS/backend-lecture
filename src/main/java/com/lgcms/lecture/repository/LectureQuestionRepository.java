package com.lgcms.lecture.repository;

import com.lgcms.lecture.domain.LectureQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectureQuestionRepository extends JpaRepository<LectureQuestion, Long> {
    boolean existsByIdAndMemberId(Long qnaId, Long memberId);

    

    List<LectureQuestion> findAllByMemberId(Long memberId);

    List<LectureQuestion> findAllByLectureId(String lectureId);
}

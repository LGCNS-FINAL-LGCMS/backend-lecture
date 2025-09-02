package com.lgcms.lecture.repository;

import com.lgcms.lecture.domain.LectureQuestion;
import com.lgcms.lecture.dto.internal.LectureQuestionsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectureQuestionRepository extends JpaRepository<LectureQuestion, Long> {
    boolean existsByIdAndMemberId(Long qnaId, Long memberId);


    List<LectureQuestion> findAllByMemberId(Long memberId);

    @Query("""
                SELECT q
                FROM LectureQuestion q
                LEFT JOIN FETCH q.lectureAnswers
                WHERE q.lectureId = :lectureId
            """)
    Page<LectureQuestion> findAllByLectureId(@Param("lectureId") String lectureId, Pageable pageable);

}

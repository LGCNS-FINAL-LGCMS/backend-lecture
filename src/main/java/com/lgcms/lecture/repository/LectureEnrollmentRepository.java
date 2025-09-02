package com.lgcms.lecture.repository;

import com.lgcms.lecture.domain.LectureEnrollment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectureEnrollmentRepository extends JpaRepository<LectureEnrollment, Long> {

    boolean existsByLectureIdAndStudent_MemberId(String lectureId, Long memberId);

    @Query("""
    select le 
    from LectureEnrollment le
    join fetch le.lecture
    where le.memberId = :memberId
""")
    Page<LectureEnrollment> findByMemberIdWithLecture(@Param("memberId") Long memberId, Pageable pageable);

    @Modifying
    @Query("DELETE FROM LectureEnrollment le WHERE le.memberId = :memberId AND le.lecture.id = :lectureId")
    void deleteByMemberIdAndLectureId(@Param("memberId") Long memberId, @Param("lectureId") String lectureId);
}

package com.lgcms.lecture.repository;

import com.lgcms.lecture.domain.LectureEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureEnrollmentRepository extends JpaRepository<LectureEnrollment, Long> {

    boolean existsByLectureIdAndStudent_MemberId(String lectureId, Long memberId);
}

package com.lgcms.lecture.repository;

import com.lgcms.lecture.domain.Lecture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, String> {
    Lecture findByMemberId(Long memberId);

    boolean existsByIdAndMemberId(String lectureId, Long memberId);

    List<Lecture> findByCategory(String category);

    List<Lecture> findByTitleContaining(String keyword);

    @Query("""
                SELECT l
                FROM Lecture l
                WHERE l.title LIKE CONCAT('%', :keyword, '%')
                  AND l.category LIKE CONCAT('%', :category, '%')
            """)
    Page<Lecture> findAllByKeywordAndCategory(@Param("keyword") String keyword, @Param("category") String category, Pageable pageable);

    @Query(value = """
            SELECT l
            FROM Lecture l
            WHERE l.title LIKE CONCAT('%', :keyword, '%')
            """)
    Page<Lecture> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query(value = """
            SELECT l
            FROM Lecture l
            WHERE l.category LIKE CONCAT('%', :category, '%')
            """)
    Page<Lecture> findByCategoryAsPage(@Param("category") String category, Pageable pageable);
}

package com.lgcms.lecture.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LectureProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    private String lectureId;

    private Integer progress;

    private String lastWatchedLessonId;

    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }

    public void updateProgress(int progress){
        this.progress = (progress > 98) ? 100 : progress;
    }

    public void updateLastWatched(String id){
        this.lastWatchedLessonId = id;
    }
}

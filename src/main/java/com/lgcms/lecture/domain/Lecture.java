package com.lgcms.lecture.domain;

import com.lgcms.lecture.domain.type.LectureStatus;
import com.lgcms.lecture.domain.type.VideoStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder @Getter
public class Lecture {
    @Id
    private String id;

    private Long userId;

    private String title;

    private String information;

    private String category;

    private String level;

    private String thumbnail;

    private String sampleItemUrl;

    private Long price;

    @Enumerated(EnumType.STRING)
    private VideoStatus videoStatus;

    @Enumerated(EnumType.STRING)
    private LectureStatus lectureStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "lecture", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LectureEnrollment> enrollments = new ArrayList<>();

    public void modifyLectureStatus(LectureStatus lectureStatus){
        this.lectureStatus  = lectureStatus;
    }

    public void modifyVideoStatus(VideoStatus videoStatus){
        this.videoStatus = videoStatus;
    }

}

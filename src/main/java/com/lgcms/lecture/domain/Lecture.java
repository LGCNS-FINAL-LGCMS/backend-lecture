package com.lgcms.lecture.domain;

import com.lgcms.lecture.domain.type.LectureStatus;
import com.lgcms.lecture.domain.type.VideoStatus;
import com.lgcms.lecture.dto.request.lecture.LectureModifyDto;
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

    private Long memberId;

    private String title;

    private String information;

    private String category;

    private String level;

    private String thumbnail;

    private String sampleItemUrl;

    private Long price;

    private Long totalAmount;

    private Long reviewCount;

    private Long avgRating;

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

    public void modifyTotalAmount(Long rate){
        this.totalAmount += rate;
    }

    public void modifyLecture(LectureModifyDto dto){
        if(dto.getInformation() != null) this.information = dto.getInformation();

        if(dto.getPrice() != null) this.price = dto.getPrice();

        if(dto.getTitle() != null) this.title = dto.getTitle();
    }

}

package com.lgcms.lecture.domain;

import com.lgcms.lecture.domain.type.ImageStatus;
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

    private String nickname;

    private String description;

    private String information;

    private String category;

    private String level;

    private String thumbnail;

    private Long price;

    private Long totalAmount;

    private Long reviewCount;

    private Double averageStar;

    @Enumerated(EnumType.STRING)
    private VideoStatus videoStatus;

    @Enumerated(EnumType.STRING)
    private LectureStatus lectureStatus;

    @Enumerated(EnumType.STRING)
    private ImageStatus imageStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
    @PrePersist
    public void initLecture(){
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.reviewCount = 0L;
        this.averageStar = 0.0;
    }

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
        this.updatedAt = LocalDateTime.now();
    }

    public void modifyDate(){
        this.updatedAt = LocalDateTime.now();
    }

    public void updateReview(Integer star){
        this.averageStar = ((this.averageStar * this.reviewCount) + star) / (this.reviewCount + 1);
        this.reviewCount++;
    }
}

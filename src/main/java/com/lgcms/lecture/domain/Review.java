package com.lgcms.lecture.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    private Integer star;

    private String lectureId;

    private String nickname;

    private String comment;

    @Column(columnDefinition = "text")
    private String suggestion;

    private LocalDateTime createdAt;

    @PrePersist
    public void setDate(){
        this.createdAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "review", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<ReviewContent> reviewContents;

    public void addReviewContent(ReviewContent content){
        this.reviewContents.add(content);
        content.addReview(this);
    }
}

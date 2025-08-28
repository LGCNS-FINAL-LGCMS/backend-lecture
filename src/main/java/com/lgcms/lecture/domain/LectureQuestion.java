package com.lgcms.lecture.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "lecture_question")
@Getter@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LectureQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    private String lectureId;

    private String title;

    @Column(length = 2000)
    private String content;

    private LocalDateTime createdAt;

    @PrePersist
    public void initQuestion(){
        this.createdAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "lectureQuestion", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LectureAnswer> lectureAnswers;

    public void addAnswer(LectureAnswer lectureAnswer){
        this.lectureAnswers.add(lectureAnswer);
        lectureAnswer.addLectureQuestion(this);
    }

    public void updateQuestion(String title, String content){
        if(title != null) this.title = title;
        if(content != null) this.content = content;
    }
}

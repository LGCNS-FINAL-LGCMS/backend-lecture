package com.lgcms.lecture.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "lecture_answer")
@Getter@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LectureAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2000)
    private String content;

    private LocalDateTime createdAt;

    @PrePersist
    public void initAnswer(){
        this.createdAt = LocalDateTime.now();
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_question_id")
    private LectureQuestion lectureQuestion;

    protected void addLectureQuestion(LectureQuestion lectureQuestion){
        this.lectureQuestion = lectureQuestion;
    }

    public void updateAnswer(String content){
        this.content = content;
    }
}

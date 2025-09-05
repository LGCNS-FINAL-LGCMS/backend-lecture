package com.lgcms.lecture.dto.response.qna;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QnaResponse {
    private Long id;
    private String title;
    private String content;
    private String lectureId;
    private LocalDateTime createdAt;
    private List<AnswerResponse> answer;
}

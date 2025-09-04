package com.lgcms.lecture.dto.response.qna;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QnaResponse {
    private String title;
    private String question;
    private String questionCreatedAt;
    private List<AnswerResponse> answer;
}

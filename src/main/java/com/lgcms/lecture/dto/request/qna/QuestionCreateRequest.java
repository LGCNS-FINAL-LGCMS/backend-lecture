package com.lgcms.lecture.dto.request.qna;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuestionCreateRequest {

    private String title;

    private String content;

    private String lectureId;
}

package com.lgcms.lecture.dto.request.qna;

import lombok.NonNull;

public record QuestionUpdateRequest(
        String title,
        String content
) {}

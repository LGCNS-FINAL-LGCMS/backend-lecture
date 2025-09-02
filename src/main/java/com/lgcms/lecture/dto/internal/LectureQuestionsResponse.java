package com.lgcms.lecture.dto.internal;

import java.time.LocalDateTime;

public record LectureQuestionsResponse(
            Long id,
            Long memberId,
            String lectureId,
            String title,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {}
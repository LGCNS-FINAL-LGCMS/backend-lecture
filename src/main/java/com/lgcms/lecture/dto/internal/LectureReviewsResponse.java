package com.lgcms.lecture.dto.internal;

import java.time.LocalDateTime;

public record LectureReviewsResponse(
            Long id,
            Long memberId,
            String lectureId,
            String suggestion,
            Long star,
            String nickname,
            Integer difficulty,
            Integer usefulness,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {}
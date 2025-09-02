package com.lgcms.lecture.dto.internal;

import java.time.LocalDateTime;

public record LectureReviewsResponse(
            Long id,
            Long memberId,
            String lectureId,
            String suggestion, //개선점
            Integer star, //별점 //Integer 변경
            String nickname,
            Integer difficulty, //난이도
            Integer usefulness, //유익함
            LocalDateTime createdAt
            // updatedAt 삭제
    ) {}
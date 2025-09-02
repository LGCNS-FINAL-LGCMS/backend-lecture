package com.lgcms.lecture.dto.internal;

import java.time.LocalDateTime;

public record LectureQuestionsResponse(
            Long id, //index
            Long memberId,
            String lectureId,
            String title,
            String contents,
            LocalDateTime createdAt

            //updatedAt 삭제
    ) {}
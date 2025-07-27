package com.lgcms.lecture.dto.request.review;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewCreateRequest {

    private String content;

    private Integer start;

    private String nickname;

    private String detail;

    private String etc;
}

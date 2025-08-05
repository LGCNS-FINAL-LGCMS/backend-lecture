package com.lgcms.lecture.dto.request.review;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReviewCreateRequest {

    private String content;

    private Integer star;

    private String nickname;

    private String suggestion;

    private List<ReviewContentRequest> reviewContentRequests;
}

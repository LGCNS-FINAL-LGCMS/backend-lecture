package com.lgcms.lecture.controller;

import com.lgcms.lecture.common.dto.BaseResponse;
import com.lgcms.lecture.dto.request.review.ReviewCreateRequest;
import com.lgcms.lecture.dto.response.review.ReviewResponse;
import com.lgcms.lecture.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/student/review/{id}")
    public ResponseEntity<BaseResponse> registerReview(@PathVariable("id") String lectureId,
                                                       @RequestBody ReviewCreateRequest reviewCreateRequest,
                                                       @RequestHeader("X-USER-ID") Long memberId) {
        reviewService.createReview(lectureId, memberId, reviewCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(BaseResponse.ok(null));
    }

    @GetMapping("/review/list/{id}")
    public ResponseEntity<BaseResponse<Page<ReviewResponse>>> getAllReview(@PathVariable("id") String lectureId,
                                                                           @RequestHeader("X-USER-ID") Long memberId,
                                                                           @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ReviewResponse> reviewResponseList = reviewService.getAllReviews(lectureId, pageable);

        return ResponseEntity.ok(BaseResponse.ok(reviewResponseList));
    }

    @GetMapping("/review")
    public ResponseEntity<BaseResponse<Page<ReviewResponse>>> getReview(@RequestHeader("X-USER-ID") Long memberId,
                                                                        @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ReviewResponse> reviewResponseList = reviewService.getReview(memberId, pageable);

        return ResponseEntity.ok(BaseResponse.ok(reviewResponseList));
    }
}

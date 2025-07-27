package com.lgcms.lecture.service;

import com.lgcms.lecture.controller.ReviewController;
import com.lgcms.lecture.domain.Review;
import com.lgcms.lecture.dto.request.review.ReviewCreateRequest;
import com.lgcms.lecture.dto.response.ReviewResponse;
import com.lgcms.lecture.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    @Transactional
    public void createReview(String lectureId, Long memberId, ReviewCreateRequest reviewCreateRequest) {
        Review review = Review.builder()
                .lectureId(lectureId)
                .memberId(memberId)
                .star(reviewCreateRequest.getStart())
                .content(reviewCreateRequest.getContent())
                .details(reviewCreateRequest.getDetail())
                .etc(reviewCreateRequest.getEtc())
                .build();

        reviewRepository.save(review);
    }

    @Transactional
    public List<ReviewResponse> getAllReviews(String lectureId) {
        List<Review> reviewList = reviewRepository.findAllByLectureId(lectureId);

        List<ReviewResponse> reviewResponseList = reviewList.stream()
                .map(review -> ReviewResponse.builder()
                        .start(review.getStar())
                        .content(review.getContent())
                        .nickname(review.getNickname())
                        .build()
                ).toList();
        return reviewResponseList;
    }

    @Transactional
    public List<ReviewResponse> getReview(Long memberId) {
        List<Review> reviewList = reviewRepository.findAllByMemberId(memberId);

        List<ReviewResponse> reviewResponseList = reviewList.stream()
                .map(review -> ReviewResponse.builder()
                        .start(review.getStar())
                        .content(review.getContent())
                        .nickname(review.getNickname())
                        .build()
                ).toList();

        return reviewResponseList;
    }
}

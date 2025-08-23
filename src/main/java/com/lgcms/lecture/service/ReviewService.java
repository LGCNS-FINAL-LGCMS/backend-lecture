package com.lgcms.lecture.service;

import com.lgcms.lecture.common.dto.exception.BaseException;
import com.lgcms.lecture.common.dto.exception.LectureError;
import com.lgcms.lecture.domain.Lecture;
import com.lgcms.lecture.domain.Review;
import com.lgcms.lecture.domain.ReviewContent;
import com.lgcms.lecture.dto.request.review.ReviewContentRequest;
import com.lgcms.lecture.dto.request.review.ReviewCreateRequest;
import com.lgcms.lecture.dto.response.ReviewResponse;
import com.lgcms.lecture.repository.LectureRepository;
import com.lgcms.lecture.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final LectureRepository lectureRepository;

    @Transactional
    public void createReview(String lectureId, Long memberId, ReviewCreateRequest reviewCreateRequest) {
        Review review = Review.builder()
                .lectureId(lectureId)
                .memberId(memberId)
                .star(reviewCreateRequest.getStar())
                .comment(reviewCreateRequest.getComment())
                .nickname(reviewCreateRequest.getNickname())
                .suggestion(reviewCreateRequest.getSuggestion())
                .reviewContents(new ArrayList<>())
                .build();

        for(ReviewContentRequest reviewContentRequest : reviewCreateRequest.getReviewContentRequests()){
            ReviewContent content = ReviewContent.builder()
                    .question(reviewContentRequest.question())
                    .answer(reviewContentRequest.answer())
                    .build();
            review.addReviewContent(content);
        }

        Lecture lecture = lectureRepository.findById(lectureId).orElseThrow(()-> new BaseException(LectureError.LECTURE_NOT_FOUND));

        lecture.updateReview(review.getStar());

        reviewRepository.save(review);
    }

    @Transactional
    public List<ReviewResponse> getAllReviews(String lectureId) {
        List<Review> reviewList = reviewRepository.findAllByLectureId(lectureId);

        List<ReviewResponse> reviewResponseList = reviewList.stream()
                .map(review -> ReviewResponse.builder()
                        .star(review.getStar())
                        .comment(review.getComment())
                        .nickname(review.getNickname())
                        .createdAt(review.getCreateAt())
                        .build()
                ).toList();
        return reviewResponseList;
    }

    @Transactional
    public List<ReviewResponse> getReview(Long memberId) {
        List<Review> reviewList = reviewRepository.findAllByMemberId(memberId);

        List<ReviewResponse> reviewResponseList = reviewList.stream()
                .map(review -> ReviewResponse.builder()
                        .star(review.getStar())
                        .comment(review.getComment())
                        .nickname(review.getNickname())
                        .build()
                ).toList();

        return reviewResponseList;
    }
}

package com.lgcms.lecture.service;

import com.lgcms.lecture.common.dto.exception.BaseException;
import com.lgcms.lecture.common.dto.exception.LectureError;
import com.lgcms.lecture.common.dto.exception.QnaError;
import com.lgcms.lecture.domain.LectureQuestion;
import com.lgcms.lecture.dto.request.qna.QuestionCreateRequest;
import com.lgcms.lecture.dto.request.qna.QuestionUpdateRequest;
import com.lgcms.lecture.repository.LectureAnswerRepository;
import com.lgcms.lecture.repository.LectureQuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@RequiredArgsConstructor
@Slf4j
public class QnaService {
    private final LectureQuestionRepository lectureQuestionRepository;
    private final LectureAnswerRepository lectureAnswerRepository;
    private final LectureService lectureService;

    @Transactional
    public void registerQuestion(Long memberId, QuestionCreateRequest questionCreateRequest) {
        if(!lectureService.isExist(memberId, questionCreateRequest.getLectureId())){
            throw new BaseException(LectureError.LECTURE_FORBIDDEN);
        }
        LectureQuestion lectureQuestion = LectureQuestion.builder()
                .lectureId(questionCreateRequest.getLectureId())
                .title(questionCreateRequest.getTitle())
                .content(questionCreateRequest.getContent())
                .lectureAnswers(new ArrayList<>())
                .build();
        lectureQuestionRepository.save(lectureQuestion);
    }

}

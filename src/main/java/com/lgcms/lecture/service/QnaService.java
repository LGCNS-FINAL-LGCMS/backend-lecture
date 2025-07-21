package com.lgcms.lecture.service;

import com.lgcms.lecture.common.dto.exception.BaseException;
import com.lgcms.lecture.common.dto.exception.LectureError;
import com.lgcms.lecture.common.dto.exception.QnaError;
import com.lgcms.lecture.domain.LectureQuestion;
import com.lgcms.lecture.dto.request.qna.QuestionCreateRequest;
import com.lgcms.lecture.dto.request.qna.QuestionUpdateRequest;
import com.lgcms.lecture.dto.response.qna.AnswerResponse;
import com.lgcms.lecture.dto.response.qna.QnaListResponse;
import com.lgcms.lecture.repository.LectureAnswerRepository;
import com.lgcms.lecture.repository.LectureQuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

    @Transactional
    public void updateQuestion(Long memberId, Long qeustionId, QuestionUpdateRequest questionUpdateRequest) {
        verifyQuestion(memberId, qeustionId);

        LectureQuestion question = lectureQuestionRepository.findById(qeustionId)
                .orElseThrow(()-> new BaseException(QnaError.QNA_NOT_FOUND));

        question.updateQuestion(questionUpdateRequest.title(), questionUpdateRequest.content());
    }

    @Transactional
    public void deleteQuestion(Long memberId, Long questionId) {
        verifyQuestion(memberId, questionId);

        lectureQuestionRepository.deleteById(questionId);
    }

    @Transactional
    public void verifyQuestion(Long memberId, Long qnaId){
        if(!lectureQuestionRepository.existsByIdAndMemberId(qnaId,memberId)){
            throw new BaseException(QnaError.QNA_FORBIDDEN);
        }
    }

    @Transactional
    public List<QnaListResponse> getQnaList(Long lectureId) {
        List<LectureQuestion> lectureQuestions = lectureQuestionRepository.findAllByLectureId(lectureId);

        return lectureQuestions.stream()
                .map(question -> new QnaListResponse(
                        question.getTitle(),
                        question.getContent(),
                        question.getLectureAnswers().stream()
                                .map(answer -> new AnswerResponse(
                                        answer.getContent()
                                )).toList()
                )).toList();
    }

    @Transactional
    public List<QnaListResponse> getMemberQnaList(Long memberId) {
        List<LectureQuestion> lectureQuestions = lectureQuestionRepository.findAllByMemberId(memberId);

        return lectureQuestions.stream()
                .map(question -> new QnaListResponse(
                        question.getTitle(),
                        question.getContent(),
                        question.getLectureAnswers().stream()
                                .map(answer -> new AnswerResponse(
                                        answer.getContent()
                                )).toList()
                )).toList();
    }
}

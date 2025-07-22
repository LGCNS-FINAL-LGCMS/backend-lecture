package com.lgcms.lecture.service;

import com.lgcms.lecture.common.dto.exception.BaseException;
import com.lgcms.lecture.common.dto.exception.LectureError;
import com.lgcms.lecture.common.dto.exception.QnaError;
import com.lgcms.lecture.domain.LectureAnswer;
import com.lgcms.lecture.domain.LectureQuestion;
import com.lgcms.lecture.dto.request.qna.AnswerRequest;
import com.lgcms.lecture.dto.request.qna.AnswerUpdateRequest;
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
    public void verifyAnswer(Long memberId, String lectureId){
        if(!lectureService.isLecturer(memberId,lectureId)) throw new BaseException(QnaError.QNA_NOT_FOUND);
    }

    @Transactional
    public List<QnaListResponse> getQnaList(String lectureId) {
        List<LectureQuestion> lectureQuestions = lectureQuestionRepository.findAllByLectureId(lectureId);

        return lectureQuestions.stream()
                .map(question -> new QnaListResponse(
                        question.getTitle(),
                        question.getContent(),
                        question.getId(),
                        question.getLectureAnswers().stream()
                                .map(answer -> new AnswerResponse(
                                        answer.getContent(),
                                        answer.getId()
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
                        question.getId(),
                        question.getLectureAnswers().stream()
                                .map(answer -> new AnswerResponse(
                                        answer.getContent(),
                                        answer.getId()
                                )).toList()
                )).toList();
    }

    @Transactional
    public void registerAnswer(Long questionId, Long memberId, AnswerRequest answerCreateRequest) {
        if(!lectureService.isLecturer(memberId, answerCreateRequest.lectureId()))
            throw new BaseException(QnaError.QNA_FORBIDDEN);
        LectureQuestion lectureQuestion = lectureQuestionRepository.findById(questionId)
                .orElseThrow(() -> new BaseException(QnaError.QNA_NOT_FOUND));
        LectureAnswer lectureAnswer = LectureAnswer.builder()
                .content(answerCreateRequest.content())
                .build();
        lectureQuestion.addAnswer(lectureAnswer);
    }

    @Transactional
    public void updateAnswer(Long memberId, Long answerId, AnswerRequest answerUpdateRequest) {
        if(!lectureService.isLecturer(memberId, answerUpdateRequest.lectureId()))
            throw new BaseException(QnaError.QNA_FORBIDDEN);

        LectureAnswer lectureAnswer = lectureAnswerRepository.findById(answerId)
                .orElseThrow(() -> new BaseException(QnaError.QNA_NOT_FOUND));

        lectureAnswer.updateAnswer(answerUpdateRequest.content());
    }
}

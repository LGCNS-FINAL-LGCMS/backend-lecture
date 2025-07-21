package com.lgcms.lecture.dto.response.qna;

import java.util.List;

public record QnaListResponse(String title,
                              String content,
                              List<AnswerResponse> answers
){}

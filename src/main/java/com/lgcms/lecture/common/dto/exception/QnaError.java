package com.lgcms.lecture.common.dto.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum QnaError implements ErrorCodeInterface {
    QNA_NOT_FOUND("QNAE-01", "해당 Q&A를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    QNA_FORBIDDEN("QNAE-02","해당 Q&A에 수정 권한이 없습니다.", HttpStatus.FORBIDDEN) ;

    private final String status;
    private final String message;
    private final HttpStatus httpStatus;

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.builder()
                .status(status)
                .message(message)
                .httpStatus(httpStatus)
                .build();
    }
}

package com.lgcms.lecture.common.dto.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum LectureError implements ErrorCodeInterface {
    LECTURE_NOT_FOUND("LECE-01","강의를 찾을 수 없습니다.", HttpStatus.NOT_FOUND ),
    LECTURE_SAVE_FAIL("LECE-02", "강의 신청이 실패했습니다. 잠시 후 다시 시도해주세요.", HttpStatus.INTERNAL_SERVER_ERROR),
    LECTURE_NOT_STUDENT("LECE-03","수강생만 질문을 등록할 수 있습니다.", HttpStatus.FORBIDDEN),
    LECTURE_FORBIDDEN("LECE-04","강의에 접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
    LECTURE_ENROLL_FORBIDDEN("LECE-05", "등록된 강좌가 없습니다.",HttpStatus.FORBIDDEN)
    ;

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

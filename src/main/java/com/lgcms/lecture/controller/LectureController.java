package com.lgcms.lecture.controller;


import com.lgcms.lecture.common.dto.BaseResponse;
import com.lgcms.lecture.dto.request.lecture.LectureModifyDto;
import com.lgcms.lecture.dto.request.lecture.LectureRequestDto;
import com.lgcms.lecture.dto.request.lecture.LectureStatusDto;
import com.lgcms.lecture.dto.response.LectureResponseDto;
import com.lgcms.lecture.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/lecture")
@RequiredArgsConstructor
public class LectureController {

    private final LectureService lectureService;

    //강의 개설 신청
    @PostMapping("")
    public ResponseEntity<BaseResponse> saveLecture(@RequestBody LectureRequestDto lectureRequestDto ) {
        Long memberId = Long.parseLong("1");
        String lectureId = lectureService.saveLecture(lectureRequestDto, memberId);
        lectureService.joinLecture(memberId,lectureId);
        return ResponseEntity.ok(BaseResponse.ok(null));
    }

    //to-do 페이지네이션 , 전체조회
    @GetMapping("/list")
    public ResponseEntity<BaseResponse> getLectureList(@PageableDefault(size=10, sort="id", direction= Sort.Direction.DESC) Pageable pageable) {
        Long memberId = Long.parseLong("1");
        List<LectureResponseDto> lectureList = lectureService.getLectureList();
        return ResponseEntity.ok(BaseResponse.ok(lectureList));
    }
    //단일 조회
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> getLecture(@PathVariable("id") String lectureId){
        Long memberId = Long.parseLong("1");
        LectureResponseDto lecture = lectureService.getLecture(lectureId, memberId);
        return ResponseEntity.ok(BaseResponse.ok(lecture));
    }
    //강의 상태 수정
    @PutMapping("")
    public ResponseEntity<BaseResponse> modifyLectureStatus(@RequestBody LectureStatusDto lectureStatusDto) {
        return ResponseEntity.ok(BaseResponse.ok(lectureService.modifyLectureStatus(lectureStatusDto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> modifyLecture(@RequestPart("thumbnail") MultipartFile thumbnail,
                                                      @RequestPart("data")LectureModifyDto lectureModifyDto,
                                                      @PathVariable("id") String lectureId){
        Long memberId = Long.parseLong("1");
        String id = lectureService.modifyLecture(lectureModifyDto, memberId, lectureId);
        return ResponseEntity.ok(BaseResponse.ok(null));
    }

    @PostMapping("/join/{id}")
    public ResponseEntity<BaseResponse> joinLecture(@PathVariable("id") String lectureId){
        Long memberId = Long.parseLong("1");
        lectureService.joinLecture(memberId,lectureId);
        return ResponseEntity.ok(BaseResponse.ok(null));
    }

    @GetMapping("/verify")
    public boolean verifyLecture(@RequestParam Long memberId, @RequestParam String lectureId){

        return lectureService.isExist(memberId, lectureId);
    }
}
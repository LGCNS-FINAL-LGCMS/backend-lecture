package com.lgcms.lecture.controller;


import com.lgcms.lecture.dto.request.LectureRequestDto;
import com.lgcms.lecture.dto.request.LectureStatusDto;
import com.lgcms.lecture.service.FileService;
import com.lgcms.lecture.service.LectureService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/lecture")
@RequiredArgsConstructor
public class lectureController {

    private final LectureService lectureService;
    private final FileService fileService;

    @PostMapping()
    public ResponseEntity<?> registerLecture(@RequestPart("meta") LectureRequestDto lectureRequestDto,
                                             @RequestPart("thumbnail") MultipartFile thumbnail,
                                             @RequestPart("video") MultipartFile file) {
        String lectureId = lectureService.saveLecture(lectureRequestDto);
        fileService.saveImage(thumbnail, lectureId);
        fileService.saveVideo(file, lectureId);
        return ResponseEntity.ok("success");
    }

    @GetMapping()
    public ResponseEntity<?> getLectureList(@PageableDefault(size=10, sort="id", direction= Sort.Direction.DESC) Pageable pageable) {
        lectureService.getLectureList();
        return ResponseEntity.ok("success!");
    }

    @PutMapping()
    public ResponseEntity<?> modifyLectureStatus(@RequestBody LectureStatusDto lectureStatusDto) {

        return ResponseEntity.ok(lectureService.modifyLectureStatus(lectureStatusDto));
    }
}
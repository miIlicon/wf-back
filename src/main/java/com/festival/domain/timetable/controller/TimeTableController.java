package com.festival.domain.timetable.controller;

import com.festival.domain.timetable.dto.TimeTableCreateReq;
import com.festival.domain.timetable.dto.TimeTableDateReq;
import com.festival.domain.timetable.dto.TimeTableRes;
import com.festival.domain.timetable.service.TimeTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/timetable")
public class TimeTableController {

    private final TimeTableService timeTableService;

    @PostMapping
    public ResponseEntity<Long> createTimeTable(@RequestBody TimeTableCreateReq timeTableCreateReq) {
        return ResponseEntity.ok().body(timeTableService.create(timeTableCreateReq));
    }

    @PutMapping("/{timeTableId}")
    public ResponseEntity<Long> updateTimeTable(@PathVariable Long timeTableId,
                                                @RequestBody TimeTableCreateReq timeTableCreateReq) {
        return ResponseEntity.ok().body(timeTableService.update(timeTableId, timeTableCreateReq));
    }

    @DeleteMapping("/{timeTableId}")
    public ResponseEntity<Void> deleteTimeTable(@PathVariable Long timeTableId) {
        timeTableService.delete(timeTableId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<TimeTableRes>> getTimeTables(@RequestBody TimeTableDateReq timeTableDateReq) {
        return ResponseEntity.ok().body(timeTableService.getList(timeTableDateReq));
    }
}

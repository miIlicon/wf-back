package com.festival.domain.timetable.controller;

import com.festival.common.util.ValidationUtils;
import com.festival.domain.timetable.dto.TimeTableDateReq;
import com.festival.domain.timetable.dto.TimeTableReq;
import com.festival.domain.timetable.dto.TimeTableRes;
import com.festival.domain.timetable.service.TimeTableService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/timetable")
public class TimeTableController {

    private final TimeTableService timeTableService;
    private final ValidationUtils validationUtils;

    @PreAuthorize("hasRole({'ADMIN'})")
    @PostMapping
    public ResponseEntity<Long> createTimeTable(@Valid TimeTableReq timeTableReq) {
        validationUtils.isTimeTableValid(timeTableReq);
        return ResponseEntity.ok().body(timeTableService.createTimeTable(timeTableReq));
    }

    @PreAuthorize("hasRole({'ADMIN'})")
    @PutMapping(value = "/{timeTableId}")
    public ResponseEntity<Long> updateTimeTable(@PathVariable Long timeTableId,
                                                @Valid TimeTableReq timeTableReq) {
        validationUtils.isTimeTableValid(timeTableReq);
        return ResponseEntity.ok().body(timeTableService.updateTimeTable(timeTableId, timeTableReq));
    }

    @PreAuthorize("hasRole({'ADMIN'})")
    @DeleteMapping("/{timeTableId}")
    public ResponseEntity<Void> deleteTimeTable(@PathVariable Long timeTableId) {
        timeTableService.deleteTimeTable(timeTableId);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/list")
    public ResponseEntity<List<TimeTableRes>> getTimeTableList(@Valid TimeTableDateReq timeTableDateReq) {
        return ResponseEntity.ok().body(timeTableService.getTimeTableList(timeTableDateReq));
    }
}

package com.festival.domain.timetable.controller;

import com.festival.common.util.ValidationUtils;
import com.festival.domain.timetable.dto.TimeTableCreateReq;
import com.festival.domain.timetable.dto.TimeTableDateReq;
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

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public ResponseEntity<Long> createTimeTable(@Valid TimeTableCreateReq timeTableCreateReq) throws Exception {
        if (!validationUtils.isTimeTableValid(timeTableCreateReq)) {
            throw new Exception();
        }
        return ResponseEntity.ok().body(timeTableService.create(timeTableCreateReq));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{timeTableId}")
    public ResponseEntity<Long> updateTimeTable(@PathVariable Long timeTableId,
                                                @Valid TimeTableCreateReq timeTableCreateReq) throws Exception {
        if (!validationUtils.isTimeTableValid(timeTableCreateReq)) {
            throw new Exception();
        }
        return ResponseEntity.ok().body(timeTableService.update(timeTableId, timeTableCreateReq));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{timeTableId}")
    public ResponseEntity<Void> deleteTimeTable(@PathVariable Long timeTableId) {
        timeTableService.delete(timeTableId);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("permitAll()")
    @GetMapping
    public ResponseEntity<List<TimeTableRes>> getTimeTables(@Valid TimeTableDateReq timeTableDateReq) {
        return ResponseEntity.ok().body(timeTableService.getList(timeTableDateReq));
    }
}

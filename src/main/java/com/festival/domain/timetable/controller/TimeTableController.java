package com.festival.domain.timetable.controller;

import com.festival.common.util.ValidationUtils;
import com.festival.domain.timetable.dto.TimeTableDateReq;
import com.festival.domain.timetable.dto.TimeTableReq;
import com.festival.domain.timetable.dto.TimeTableRes;
import com.festival.domain.timetable.service.TimeTableService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/timetable")
public class TimeTableController {

    private final TimeTableService timeTableService;
    private final ValidationUtils validationUtils;

    //@PreAuthorize("hasRole({'ADMIN'})")
    @PostMapping(produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> createTimeTable(@Valid @ParameterObject TimeTableReq timeTableReq) {
        validationUtils.isTimeTableValid(timeTableReq);
        return ResponseEntity.ok().body(timeTableService.createTimeTable(timeTableReq));
    }

    //@PreAuthorize("hasRole({'ADMIN'})")
    @PutMapping(value = "/{timeTableId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> updateTimeTable(@PathVariable("timeTableId") Long timeTableId,
                                                @Valid @ParameterObject TimeTableReq timeTableReq) {
        validationUtils.isTimeTableValid(timeTableReq);
        return ResponseEntity.ok().body(timeTableService.updateTimeTable(timeTableId, timeTableReq));
    }

    //@PreAuthorize("hasRole({'ADMIN'})")
    @DeleteMapping(value = "/{timeTableId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteTimeTable(@PathVariable("timeTableId") Long timeTableId) {
        timeTableService.deleteTimeTable(timeTableId);
        return ResponseEntity.ok().build();
    }

    //@PreAuthorize("permitAll()")
    @GetMapping(value = "/list", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TimeTableRes>> getTimeTableList(@Valid TimeTableDateReq timeTableDateReq) {
        return ResponseEntity.ok().body(timeTableService.getTimeTableList(timeTableDateReq));
    }

}

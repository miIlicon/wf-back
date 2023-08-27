package com.festival.domain.timetable.repository;

import com.festival.domain.timetable.dto.TimeTableRes;
import com.festival.domain.timetable.dto.TimeTableSearchCond;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TimeTableRepositoryCustom {
    List<TimeTableRes> getList(TimeTableSearchCond timeTableSearchCond, Pageable pageable);
}

package com.festival.domain.timetable.repository;

import com.festival.domain.timetable.model.TimeTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimeTableRepository extends JpaRepository<TimeTable, Long>, TimeTableRepositoryCustom {
}

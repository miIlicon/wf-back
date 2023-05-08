package com.festival.domain.info.festivalEvent.repository;

import com.festival.domain.info.festivalEvent.data.entity.FestivalEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FestivalEventRepository extends JpaRepository<FestivalEvent, Long> {
}

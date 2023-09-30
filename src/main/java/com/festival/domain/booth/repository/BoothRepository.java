package com.festival.domain.booth.repository;

import com.festival.domain.booth.model.Booth;
import com.festival.domain.guide.repository.GuideRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BoothRepository extends JpaRepository<Booth, Long>, BoothRepositoryCustom {
    Booth getBoothByTitle(String title);

    List<Booth> findByStartDateEqualsAndOperateStatusEquals(LocalDate now, String value);
    List<Booth> findByEndDateEquals(LocalDate now);
}

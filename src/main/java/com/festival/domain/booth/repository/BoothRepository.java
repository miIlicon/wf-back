package com.festival.domain.booth.repository;

import com.festival.common.base.OperateStatus;
import com.festival.domain.booth.model.Booth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface BoothRepository extends JpaRepository<Booth, Long>, BoothRepositoryCustom {
    List<Booth> findByStartDateEqualsAndOperateStatusEquals(LocalDate inputDate, OperateStatus operateStatus);
    List<Booth> findByEndDateEquals(LocalDate inputDate);
}

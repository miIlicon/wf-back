package com.festival.domain.booth.repository;

import com.festival.domain.booth.model.Booth;
import com.festival.domain.guide.repository.GuideRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoothRepository extends JpaRepository<Booth, Long>, BoothRepositoryCustom {
    Booth getBoothByTitle(String title);

}

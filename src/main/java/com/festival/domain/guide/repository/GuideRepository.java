package com.festival.domain.guide.repository;

import com.festival.domain.guide.model.Guide;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuideRepository extends JpaRepository<Guide, Long>, GuideRepositoryCustom {
}

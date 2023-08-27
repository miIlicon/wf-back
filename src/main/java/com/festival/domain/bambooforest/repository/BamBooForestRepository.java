package com.festival.domain.bambooforest.repository;

import com.festival.domain.bambooforest.model.BamBooForest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BamBooForestRepository extends JpaRepository<BamBooForest, Long>, BamBooForestRepositoryCustom {
}

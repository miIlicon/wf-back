package com.festival.domain.booth.repository;

import com.festival.domain.booth.model.Booth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoothRepository extends JpaRepository<Booth, Long> {
}

package com.festival.domain.viewcount.repository;

import com.festival.domain.viewcount.ViewCount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViewCountRepository extends JpaRepository<ViewCount, Long> {
}

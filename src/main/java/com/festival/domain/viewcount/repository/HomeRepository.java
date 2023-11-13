package com.festival.domain.viewcount.repository;

import com.festival.domain.viewcount.model.Home;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HomeRepository extends JpaRepository<Home, Long> {
}

package com.festival.old.domain.film.repository;

import com.festival.old.domain.film.data.entity.Film;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FilmRepository extends JpaRepository<Film, Long> {
    Page<Film> findByAdminId(Long adminId, Pageable pageable);
}

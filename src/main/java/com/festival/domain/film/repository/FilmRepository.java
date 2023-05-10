package com.festival.domain.film.repository;

import com.festival.domain.film.data.entity.Film;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilmRepository extends JpaRepository<Film, Long> {
}

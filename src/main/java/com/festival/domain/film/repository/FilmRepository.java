package com.festival.domain.film.repository;

import com.festival.domain.film.data.entity.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;



public interface FilmRepository extends JpaRepository<Film, Long> {
}

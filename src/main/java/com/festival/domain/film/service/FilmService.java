package com.festival.domain.film.service;

import com.festival.domain.admin.data.entity.Admin;
import com.festival.domain.admin.repository.AdminRepository;
import com.festival.domain.film.data.dto.FilmReq;
import com.festival.domain.film.data.dto.FilmRes;
import com.festival.domain.film.data.entity.Film;
import com.festival.domain.film.repository.FilmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FilmService {
    private final AdminRepository adminRepository;
    private final FilmRepository filmRepository;
    public Film create(FilmReq filmReq, Long adminId) {
        Admin admin = adminRepository.findById(adminId).orElse(null);

        Film film = Film.of(filmReq, admin);
        filmRepository.save(film);

        return film;
    }
}

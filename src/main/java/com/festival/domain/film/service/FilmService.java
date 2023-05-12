package com.festival.domain.film.service;

import com.festival.domain.admin.data.entity.Admin;
import com.festival.domain.admin.exception.AdminNotFoundException;
import com.festival.domain.admin.repository.AdminRepository;
import com.festival.domain.film.data.dto.FilmReq;
import com.festival.domain.film.data.dto.FilmRes;
import com.festival.domain.film.data.entity.Film;
import com.festival.domain.film.exception.FilmNotFoundException;
import com.festival.domain.film.repository.FilmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FilmService {
    private final AdminRepository adminRepository;
    private final FilmRepository filmRepository;
    public FilmRes create(FilmReq filmReq) {
        Admin admin = adminRepository.findById(1L).orElseThrow(() -> new AdminNotFoundException("관리자를 찾을 수 없습니다."));

        Film film = Film.of(filmReq, admin);
        filmRepository.save(film);

        return FilmRes.of(film);
    }

    public FilmRes find(Long filmId) {
        Film film = filmRepository.findById(filmId).orElseThrow(() -> new FilmNotFoundException("존재하지 않는 영상입니다."));


        return FilmRes.of(film);
    }

    public Page<FilmRes> list(Long adminId, int offset) {

        Pageable pageable = PageRequest.of(offset, 6);
        Page<Film> films = filmRepository.findByAdminId(adminId, pageable);

        return films.map(FilmRes::of);
    }

    public FilmRes modify(FilmReq filmReq, Long filmId) {
        Admin admin = adminRepository.findById(1L).orElseThrow(() -> new AdminNotFoundException("관리자를 찾을 수 없습니다."));
        Film film = filmRepository.findById(filmId).orElseThrow(() -> new FilmNotFoundException("존재하지 않는 영상입니다."));

        film.modify(filmReq);

        return FilmRes.of(film);
    }

    public FilmRes delete(Long filmId) {
        Film film = filmRepository.findById(filmId).orElseThrow(() -> new FilmNotFoundException("존재하지 않는 영상입니다."));
        filmRepository.delete(film);

        return FilmRes.of(film);
    }
}

package com.festival.domain.film.controller;

import com.festival.domain.film.data.dto.FilmReq;
import com.festival.domain.film.data.dto.FilmRes;
import com.festival.domain.film.data.entity.Film;
import com.festival.domain.film.service.FilmService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;
    @PostMapping("/film")
    public Film createFilm(@RequestBody FilmReq filmReq){
        return filmService.create(filmReq, 1L);
    }


    @GetMapping("/film")
    public ResponseEntity<FilmRes> findFilm(@RequestParam("id") Long filmId){
        FilmRes filmRes = filmService.find(filmId);

        return ResponseEntity.ok().body(filmRes);
    }

    @GetMapping("/film/list")
    public ResponseEntity<Page<FilmRes>> findFilmList(@RequestParam("page") int offset){
        Page<FilmRes> result = filmService.list(1L, offset);

        return ResponseEntity.ok().body(result);
    }
}

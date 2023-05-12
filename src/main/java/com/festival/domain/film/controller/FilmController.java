package com.festival.domain.film.controller;

import com.festival.domain.film.data.dto.FilmReq;
import com.festival.domain.film.data.dto.FilmRes;
import com.festival.domain.film.data.entity.Film;
import com.festival.domain.film.service.FilmService;
import jakarta.validation.Valid;
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
    public ResponseEntity<FilmRes> createFilm(@RequestBody @Valid FilmReq filmReq){
        return ResponseEntity.ok().body(filmService.create(filmReq));
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

    @PutMapping("/film")
    public ResponseEntity<FilmRes> modifyFilm(@RequestPart("dto") @Valid FilmReq filmReq, @RequestParam("id") Long filmId){
        return ResponseEntity.ok().body(filmService.modify(filmReq, filmId));
    }
    @DeleteMapping("/film")
    public ResponseEntity<FilmRes> deleteFilm(@RequestParam("id") Long filmId){
        FilmRes filmRes = filmService.delete(filmId);

        return ResponseEntity.ok().body(filmRes);
    }
}

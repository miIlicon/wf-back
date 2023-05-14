package com.festival.domain.film.controller;

import com.festival.common.base.CommonIdResponse;
import com.festival.domain.film.data.dto.FilmReq;
import com.festival.domain.film.data.dto.FilmRes;
import com.festival.domain.film.data.entity.Film;
import com.festival.domain.film.service.FilmService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/film")
    public ResponseEntity<CommonIdResponse> createFilm(@RequestBody @Valid FilmReq filmReq){
        return ResponseEntity.ok().body(filmService.create(filmReq));
    }


    @GetMapping("/film/{id}")
    public ResponseEntity<FilmRes> findFilm(@PathVariable("id") Long filmId){
        FilmRes filmRes = filmService.find(filmId);
        return ResponseEntity.ok().body(filmRes);
    }

    @GetMapping("/film/list")
    public ResponseEntity<Page<FilmRes>> findFilmList(@RequestParam("page") int offset){
        Page<FilmRes> result = filmService.list(offset);

        return ResponseEntity.ok().body(result);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/film/{id}")
    public ResponseEntity<CommonIdResponse> modifyFilm(@RequestPart("dto") @Valid FilmReq filmReq, @PathVariable("id") Long filmId){
        return ResponseEntity.ok().body(filmService.modify(filmReq, filmId));
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/film/{id}")
    public ResponseEntity<CommonIdResponse> deleteFilm(@PathVariable("id") Long filmId){
        return ResponseEntity.ok().body(filmService.delete(filmId));
    }
}

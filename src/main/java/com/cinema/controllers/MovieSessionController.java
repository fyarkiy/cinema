package com.cinema.controllers;

import com.cinema.model.dto.MovieSessionRequestDto;
import com.cinema.model.dto.MovieSessionResponseDto;
import com.cinema.service.MovieSessionService;
import com.cinema.service.impl.mapper.MovieSessionMapper;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movie-sessions")
public class MovieSessionController {
    private static final String DATE_FORMATTER = "d-MM-yyyy";
    private MovieSessionService movieSessionService;
    private MovieSessionMapper movieSessionMapper;

    public MovieSessionController(MovieSessionService movieSessionService,
                                  MovieSessionMapper movieSessionMapper) {
        this.movieSessionService = movieSessionService;
        this.movieSessionMapper = movieSessionMapper;
    }

    @PostMapping()
    public void addMovieSession(@RequestBody MovieSessionRequestDto movieSessionRequestDto) {
        movieSessionService.add(movieSessionMapper
                .mapToMovieSessionFromDto(movieSessionRequestDto));
    }

    @GetMapping("/available")
    public List<MovieSessionResponseDto> getMovieSessions(@RequestParam Long id,
                                                          @RequestParam String date) {
        return movieSessionService.findAvailableSessions(id,
                LocalDate.parse(date, DateTimeFormatter.ofPattern(DATE_FORMATTER)))
                .stream()
                .map(ms -> movieSessionMapper.mapMovieSessionToDto(ms))
                .collect(Collectors.toList());
    }
}

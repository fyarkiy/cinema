package com.cinema.service.impl.mapper;

import com.cinema.model.Movie;
import com.cinema.model.dto.MovieRequestDto;
import com.cinema.model.dto.MovieResponseDto;
import org.springframework.stereotype.Component;

@Component
public class MovieMapper {
    public Movie mapDtoToMovie(MovieRequestDto movieRequestDto) {
        return new Movie(movieRequestDto.getTitle(), movieRequestDto.getDescription());
    }

    public MovieResponseDto mapMovieToDto(Movie movie) {
        return new MovieResponseDto(movie.getId(), movie.getTitle());
    }

}

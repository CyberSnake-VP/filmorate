package example.service;

import example.dto.request.film.CreateFilmRequest;
import example.dto.request.film.UpdateFilmRequest;
import example.dto.response.FilmResponse;

import java.util.List;

public interface FilmService {
    FilmResponse save(CreateFilmRequest request);
    FilmResponse update(UpdateFilmRequest request, Long id);
    void delete(Long id);
    FilmResponse findById(Long id);
    List<FilmResponse> findAll();

    void addLike(Long filmId, Long userId);
    void removeLike(Long filmId, Long userId);
    List<FilmResponse> getPopular(Long count);
}

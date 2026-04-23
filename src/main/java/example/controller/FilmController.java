package example.controller;

import example.dto.request.film.CreateFilmRequest;
import example.dto.request.film.UpdateFilmRequest;
import example.dto.response.FilmResponse;
import example.service.FilmService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FilmResponse save(@Valid @RequestBody CreateFilmRequest request) {
        log.info("HTTP POST /films started");
        FilmResponse response = filmService.save(request);
        log.info("HTTP POST /films finished");
        return response;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<FilmResponse> findAll() {
        log.info("HTTP GET /films started");
        List<FilmResponse> responses = filmService.findAll();
        log.info("HTTP GET /films finished");
        return responses;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FilmResponse findById(@PathVariable Long id) {
        log.info("HTTP GET /films/{id} started: id={}", id);
        FilmResponse response = filmService.findById(id);
        log.info("HTTP GET /films/{id} finished: id={}", id);
        return response;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        log.info("HTTP DELETE /films/{id} started: id={}", id);
        filmService.delete(id);
        log.info("HTTP DELETE /films/{id} finished: id={}", id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public FilmResponse update(@PathVariable Long id,
                               @Valid @RequestBody UpdateFilmRequest request) {
        log.info("HTTP PUT /films/{id} started: id={}", id);
        FilmResponse response = filmService.update(request, id);
        log.info("HTTP PUT /films/{id} finished: id={}", id);
        return response;
    }

    @PutMapping("/{filmId}/like/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addLike(@PathVariable Long filmId, @PathVariable Long userId) {
        log.info("HTTP PUT /films/{filmId}/like/{userId} started: filmId={}, userId={}", filmId, userId);
        filmService.addLike(filmId, userId);
        log.info("HTTP PUT /films/{filmId}/like/{userId} finished: filmId={}, userId={}", filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeLike(@PathVariable Long filmId, @PathVariable Long userId) {
        log.info("HTTP DELETE /films/{filmId}/like/{userId} started: filmId={}, userId={}", filmId, userId);
        filmService.removeLike(filmId, userId);
        log.info("HTTP DELETE /films/{filmId}/like/{userId} finished: filmId={}, userId={}", filmId, userId);
    }

}

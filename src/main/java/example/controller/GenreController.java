package example.controller;

import example.dto.response.GenreResponse;
import example.model.Genre;
import example.service.GenreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/genres")
public class GenreController {
    private final GenreService genreService;

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GenreResponse getGenreById(@PathVariable Long id) {
        log.info("HTTP GET /genres/{id} started: id={}", id);
        GenreResponse response = genreService.findById(id);
        log.info("HTTP GET /genres/{id} finished: id={}", id);
        return response;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<GenreResponse> getAll() {
        log.info("HTTP GET /genres started");
        List<GenreResponse> responses = genreService.findAll();
        log.info("HTTP GET /genres finished count={}", responses.size());
        return responses;
    }

}

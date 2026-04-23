package example.service;

import example.dal.genre.GenreRepository;
import example.dto.response.GenreResponse;
import example.exception.NotFoundException;
import example.mapper.GenreMapper;
import example.model.Genre;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenreServiceImpl implements GenreService {
    private final GenreRepository genreRepository;
    private static final String NOT_FOUND_MESSAGE = "Genre not found";


    @Override
    public List<GenreResponse> findAll() {
        log.info("Find all genres started");
        List<Genre> genres = genreRepository.findAll();
        log.info("Find all genres finished");
        return genres.stream().
                map(GenreMapper::toGenreResponse)
                .toList();
    }

    @Override
    public GenreResponse findById(Long id) {
        log.info("Find genre by id started id={}", id);
        Genre genre = genreRepository.findById(id).orElseThrow(()-> {
            log.warn("Find genre by id finished id={}", id);
            return new NotFoundException(NOT_FOUND_MESSAGE);
        });
        log.info("Find genre by id finished id={}", id);
        return GenreMapper.toGenreResponse(genre);
    }
}

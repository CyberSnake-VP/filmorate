package example.service;

import example.dal.filmgenre.FilmGenreRepository;
import example.dal.film.FilmRepository;
import example.dal.filmlikes.FIlmLikesRepository;
import example.dal.genre.GenreRepository;
import example.dal.mpa.MpaRepository;
import example.dal.user.UserRepository;
import example.dto.request.film.CreateFilmRequest;
import example.dto.request.film.UpdateFilmRequest;
import example.dto.response.FilmResponse;
import example.exception.NotFoundException;
import example.mapper.FilmMapper;
import example.model.Film;
import example.model.Genre;
import example.model.MpaRating;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmServiceImpl implements FilmService {
    private final FilmRepository filmRepository;
    private final MpaRepository mpaRepository;
    private final GenreRepository genreRepository;
    private final FilmGenreRepository filmGenreRepository;
    private final FIlmLikesRepository filmLikesRepository;
    private final UserRepository userRepository;

    private static final String NOT_FOUND_MESSAGE = "Film not found";
    private static final String NOT_FOUND_MPA_MESSAGE = "MPA rating not found";
    private static final String NOT_FOUND_GENRE_MESSAGE = "Some genres not found";
    private static final String NOT_FOUND_USER_MESSAGE = "User not found";

    @Override
    public FilmResponse save(CreateFilmRequest request) {
        log.info("Save film started:");

        Film film = FilmMapper.toFilm(request);

        if (request.mpaId() != null) {
            log.debug("Save film: MPA rating checking id: {}", request.mpaId());

            MpaRating mpaRating = mpaRepository.getMpaById(request.mpaId())
                    .orElseThrow(() -> {
                        log.warn("Save film failed: MPA rating not found. mpaId={}", request.mpaId());
                        return new NotFoundException(NOT_FOUND_MPA_MESSAGE);
                    });

            film.setMpa(mpaRating);
        }

        film = filmRepository.save(film);

        log.debug("Save film: genres checking genres={}", request.genreIds());
        if (request.genreIds() != null && !request.genreIds().isEmpty()) {
            List<Long> genreIds = request.genreIds();
            genreIds = new ArrayList<>(new HashSet<>(genreIds));
            List<Genre> genres = genreRepository.findAllByIds(genreIds);

            if (genres.size() != request.genreIds().size()) {
                log.warn("Save film failed: Some genres not found. genreIds={}", request.genreIds());
                throw new NotFoundException(NOT_FOUND_GENRE_MESSAGE);
            }

            log.debug("Save film: add genres to film genres={}", genres);

            filmGenreRepository.addGenresToFilm(film.getId(), genreIds);
            film.setGenres(genres);
        }
        log.info("Save film finished. filmId={}", film.getId());
        return FilmMapper.toFilmResponse(film);
    }

    @Override
    public FilmResponse update(UpdateFilmRequest request, Long id) {
        log.info("Update film started: id={}", id);

        log.debug("Update film: find film by id and update in mapper id={}", id);
        Film film = filmRepository.findById(id).map(f -> FilmMapper.toFilmUpdate(request, f)).orElseThrow(() -> {
            log.warn("Update film failed: Film not found. id={}", id);
            return new NotFoundException(NOT_FOUND_MESSAGE);
        });

        if (request.hasMpaId()) {
            log.debug("Update film:  MPA rating checking");
            MpaRating mpa = mpaRepository.getMpaById(request.mpaId())
                    .orElseThrow(() -> {
                        log.warn("Update film failed: Mpa id not found. mpaId={}", request.mpaId());
                        return new NotFoundException(NOT_FOUND_MPA_MESSAGE);
                    });
            film.setMpa(mpa);
            film.setMpaId(request.mpaId());
        }

        if (request.hasGenreIds()) {
            log.debug("Update film: genres checking genres={}", request.genreIds());
            List<Long> genreIdsInRequest = request.genreIds();
            genreIdsInRequest = new ArrayList<>(new HashSet<>(genreIdsInRequest));
            List<Genre> genresInRequest = genreRepository.findAllByIds(genreIdsInRequest);

            if (genresInRequest.size() != genreIdsInRequest.size()) {
                log.warn("Update film failed: Some genres not found. genreIds={}", genreIdsInRequest);
                throw new NotFoundException(NOT_FOUND_GENRE_MESSAGE);
            }

            filmGenreRepository.deleteGenresFromFilm(id);
            filmGenreRepository.addGenresToFilm(id, genreIdsInRequest);

            film.setGenres(genresInRequest);
        }

        return FilmMapper.toFilmResponse(filmRepository.update(film));
    }

    @Override
    public void delete(Long id) {
        log.info("Delete film started: id={}", id);
        filmRepository.delete(id);
        log.info("Delete film finished: id={}", id);
    }

    @Override
    public FilmResponse findById(Long id) {
        log.info("Find film by Id started: id={}", id);

        Film film = filmRepository.findById(id).orElseThrow(() -> {
            log.warn("Find film by Id failed: Film not found. id={}", id);
            return new NotFoundException(NOT_FOUND_MESSAGE);
        });

        log.debug("Find film by Id: get ratings");
        if (film.getMpaId() != null) {
            MpaRating mpaRating = mpaRepository.getMpaById(film.getMpaId())
                    .orElseThrow(() -> {
                        log.warn("Find film by Id failed: MPA rating not found. mpaId={}", film.getMpaId());
                        return new NotFoundException(NOT_FOUND_MPA_MESSAGE);
                    });
            film.setMpa(mpaRating);
        }

        log.debug("Find film by Id: get genres");
        List<Genre> genres = genreRepository.findByFilmId(id);
        if (genres != null) {
            film.setGenres(genres);
        }

        log.info("Get film by id completed: id={}", id);
        return FilmMapper.toFilmResponse(film);
    }

    @Override
    public List<FilmResponse> findAll() {
        log.info("Find all films started");

        log.debug("Find all films: get films from repository");
        List<Film> films = filmRepository.findAll();

        if (films.isEmpty()) {
            log.debug("Find all films: no films found");
            return Collections.emptyList();
        }

        log.debug("Find all films: get genres and ratings");
        List<Genre> genres = genreRepository.findAll();
        List<MpaRating> mpaRatings = mpaRepository.findAll();

        List<Long> filmIds = films.stream().map(Film::getId).toList();

        // Получаем список жанров для списка фильмов, где id фильма и список его id жанров
        log.debug("Find all films: get film genres");
        Map<Long, List<Long>> filmGenres = filmGenreRepository.getFilmGenres(filmIds);

        // Собираем мапу из id жанра и объекта жанра Function.identity() вместо genre -> genre (значение мапы)
        Map<Long, Genre> genreMap = genres.stream()
                .collect(Collectors.toMap(
                        Genre::getId,
                        Function.identity()
                ));
        // Собираем мапу из id MPA рейтинга и объекта MPA рейтинга
        Map<Long, MpaRating> ratingMap = mpaRatings.stream()
                .collect(Collectors.toMap(
                        MpaRating::getId,
                        Function.identity()
                ));
        // мапы нужны для быстрого доступа к объектам жанров и MPA рейтингов по ключам.
        // Заполняем поля фильмов если данные есть.
        for (Film film : films) {
            List<Long> genreIds = filmGenres.get(film.getId());
            if (genreIds != null) {
                List<Genre> result = genreIds.stream()
                        .map(genreMap::get)
                        .toList();
                film.setGenres(result);
            }

            if (film.getMpaId() != null) {
                MpaRating mpa = ratingMap.get(film.getMpaId());
                film.setMpa(mpa);
            }
        }
        log.info("Find all films finished. films={}", films);
        return films.stream().map(FilmMapper::toFilmResponse).toList();
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        log.info("Add like to film started: filmId={}, userId={}", filmId, userId);


        if (isNotExistFilm(filmId)) {
            log.warn("Add like to film failed: Film not found. filmId={}", filmId);
            throw new NotFoundException(NOT_FOUND_MESSAGE);
        }
        if (isNotExistUser(userId)) {
            log.warn("Add like to film failed: User not found. userId={}", userId);
            throw new NotFoundException(NOT_FOUND_USER_MESSAGE);
        }

        log.debug("Add like to film: checked already like. filmId={}, userId={}", filmId, userId);
        if (filmLikesRepository.isLiked(filmId, userId)) {
            log.debug("Add like to film: already liked");
            return;
        }

        log.debug("Add like to film: adding lake. filmId={}, userId={}", filmId, userId);
        filmLikesRepository.addLike(filmId, userId);
        log.info("Add like to film: finished");
    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        log.info("Remove like started: filmId={}, userId={}", filmId, userId);

        if (isNotExistFilm(filmId)) {
            log.warn("Remove like to film failed: Film not found. filmId={}", filmId);
            throw new NotFoundException(NOT_FOUND_MESSAGE);
        }
        if (isNotExistUser(userId)) {
            log.warn("Remove like to film failed: User not found. userId={}", userId);
            throw new NotFoundException(NOT_FOUND_USER_MESSAGE);
        }

        log.debug("Remove like: checking exist the like. filmId={}, userId={}", filmId, userId);
        boolean isLiked = filmLikesRepository.isLiked(filmId, userId);

        if (!isLiked) {
            log.debug("Remove like: like not found");
            return;
        }

        log.debug("Remove like: removing like to film. filmId={}, userId={}", filmId, userId);
        filmLikesRepository.removeLike(filmId, userId);
        log.info("Remove like: finished");
    }

    @Override
    public List<FilmResponse> getPopular(Long count) {
        return List.of();
    }

    private boolean isNotExistFilm(Long filmId) {
        return !filmRepository.existsById(filmId);
    }

    private boolean isNotExistUser(Long userId) {
        return !userRepository.existById(userId);
    }
}

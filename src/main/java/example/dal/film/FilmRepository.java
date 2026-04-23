package example.dal.film;


import example.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmRepository {
    Film save(Film film);
    Film update(Film film);
    void delete(Long id);
    Optional<Film> findById(Long id);
    List<Film> findAll();
    boolean existsById(Long id);
}

package example.dal.genre;

import example.model.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository {
    Optional<Genre> findById(Long id);
    List<Genre> findAll();
    List<Genre> findByFilmId(Long id);
    List<Genre> findAllByIds(List<Long> ids);
}

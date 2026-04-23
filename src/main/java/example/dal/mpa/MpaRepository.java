package example.dal.mpa;

import example.model.MpaRating;

import java.util.List;
import java.util.Optional;

public interface MpaRepository {
    Optional<MpaRating> getMpaById(Long id);
    List<MpaRating> findAll();
}

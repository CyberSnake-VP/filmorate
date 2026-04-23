package example.dal.mpa;

import example.dal.BaseRepository;
import example.model.MpaRating;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MpaRepositoryImpl extends BaseRepository<MpaRating> implements MpaRepository {
    public MpaRepositoryImpl(JdbcTemplate jdbc, RowMapper<MpaRating> rowMapper) {
        super(jdbc, rowMapper);
    }

    private static final String FIND_BY_ID_QUERY = "SELECT * FROM mpa_ratings WHERE mpa_id = ?";
    private static final String FIND_ALL_QUERY = "SELECT * FROM mpa_ratings";


    @Override
    public Optional<MpaRating> getMpaById(Long id) {
        return findOne(FIND_BY_ID_QUERY, id);
    }

    @Override
    public List<MpaRating> findAll() {
        return findAll(FIND_ALL_QUERY);
    }


}

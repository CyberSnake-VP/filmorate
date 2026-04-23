package example.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(of = "id")
public class Film {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;

    // лайки (ID пользователей)
    private Set<Long> likes;

    // жанры (полные объекты)
    private List<Genre> genres;

    // рейтинг (полный объект)
    private MpaRating mpa;

    // Техническое поле для маппинга из БД.
    // Можно не маппить его в RowMapper, а получить в FilmRepository через метод Long getMpaId(Long filmId);
    // чтобы из репозитория получить по id MPA рейтинга получить объект MpaRating
    private Long mpaId;
}

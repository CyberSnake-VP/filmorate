package example.dal.filmlikes;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FIlmLikesRepository {
    void addLike(Long filmId, Long userId);
    void removeLike(Long filmId, Long userId);
    Map<Long, Set<Long>> getLikesForFilms(List<Long>filmId);
    boolean isLiked(Long filmId, Long userId);
}

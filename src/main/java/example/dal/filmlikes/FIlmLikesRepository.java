package example.dal.filmlikes;

import java.util.List;

public interface FIlmLikesRepository {
    void addLike(Long filmId, Long userId);
    void removeLike(Long filmId, Long userId);
    List<Long> getLikes(Long filmId);
    boolean isLiked(Long filmId, Long userId);
}

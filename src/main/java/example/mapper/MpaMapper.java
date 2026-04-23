package example.mapper;

import example.dto.response.MpaResponse;
import example.model.MpaRating;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MpaMapper {

    public static MpaResponse toMpaResponse(MpaRating mpaRating) {
        return new MpaResponse(
                mpaRating.getId(),
                mpaRating.getName()
        );
    }
}

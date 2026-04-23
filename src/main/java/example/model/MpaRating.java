package example.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "id")
public class MpaRating {
    private Long id;
    private String name;
}

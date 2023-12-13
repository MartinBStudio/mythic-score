package bs.windrunner.dashboard.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class CharacterModel {
    private String name;
    private List<DungeonModel> dungeons;
    @Builder.Default private int score =23;
}

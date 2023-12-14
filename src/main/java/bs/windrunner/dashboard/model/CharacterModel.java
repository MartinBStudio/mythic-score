package bs.windrunner.dashboard.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class CharacterModel {
    private String name;
    private List<DungeonModel> dungeons;
    private String realm;
    private String characterClass;
    @Builder.Default private int totalScore =0;
    @Builder.Default private int tierOneScore =0;
    @Builder.Default private int tierFourScore =0;
    @Builder.Default private int tierEightScore =0;
}

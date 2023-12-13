package bs.windrunner.dashboard.model;


import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DungeonModel {
    private String shortName;
    private String name;
    private int finishedKeyLevel;
}

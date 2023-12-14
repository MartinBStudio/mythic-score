package bs.windrunner.dashboard.service.dataprovider.components;

import bs.windrunner.dashboard.model.CharacterModel;
import bs.windrunner.dashboard.model.DungeonModel;
import bs.windrunner.dashboard.service.utils.Utils;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DummyDataComponent {
    private final Utils utils;
    public String getData(String charName) {
        var finishedDungeons = new ArrayList<DungeonModel>();
        for (int i = 0; i < 8; i++) {
            String dungeonName = "FakeDungeon";
            String shortName = "FD";
            int level = i == 0 || i == 1 || i == 2 || i == 3 || i == 4 ? 20 : 2;
            finishedDungeons.add(DungeonModel.builder().shortName(shortName).name(dungeonName).finishedKeyLevel(level).build());
        }
        List<DungeonModel> sortedDungeons = finishedDungeons.stream()
                .sorted(Comparator.comparingInt(DungeonModel::getFinishedKeyLevel).reversed())
                .toList();
        // Get the top 8 values
        List<DungeonModel> top8Dungeons = sortedDungeons.stream()
                .limit(8)
                .toList();
        return new Gson().toJson(List.of(CharacterModel.builder().name(charName).dungeons(top8Dungeons).score(utils.countDungeonScore(top8Dungeons)).build()));
    }
}

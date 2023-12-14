package bs.windrunner.dashboard.service.dataprovider.components;

import bs.windrunner.dashboard.model.CharacterModel;
import bs.windrunner.dashboard.model.DungeonModel;
import bs.windrunner.dashboard.service.utils.Utils;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class DummyDataComponent {
    private final Utils utils;
    public String getData() {
        List<String> nameList = utils.formatNamesParam(generateRandomStringWithNames());
        var characters = new ArrayList<CharacterModel>();
        for (String s : nameList) {
          characters.add(utils.buildCharacter(generateRandomDungeons(),s,"Realm","Class"));
        }
        return new Gson().toJson(characters);
    }
    private  String generateRandomStringWithNames() {
        String[] names = {"Illidan", "Nefarian", "Kel'Thuzad", "Arthas", "Onyxia", "Fyrrak", "Kil'jaeden"};
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        var randomLength = random.nextInt((300 - 10) + 1) + 10;

        while (result.length() < randomLength) {
            if (!result.isEmpty()) {
                result.append(" ");
            }

            String randomName = names[random.nextInt(names.length)];
            result.append(randomName);
        }
        return result.substring(0, Math.min(result.length(), randomLength));
    }
    private List<DungeonModel> generateRandomDungeons(){
        var randomDungeonCount = new Random().nextInt(16);
        var dungeons = new ArrayList<DungeonModel>();
        for (int i = 0; i <= randomDungeonCount; i++) {
            dungeons.add(DungeonModel.builder().name("FakeDungeon").shortName("FD").finishedKeyLevel(new Random().nextInt(21)).build());
        }
        return dungeons;
    }
}
